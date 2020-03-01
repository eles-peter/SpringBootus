package creator.backend.dto;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static creator.utils.ClassBuherator.*;
import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateDetailItems {

    private CreateDetailItems() {
    }

    public static void createDetailItems(DatabaseService databaseService) {

        for (DBClass dbClass : databaseService.getDbclasslist()) {

            if (!dbClass.getDetailFieldList().isEmpty()) {
                createDetailItem(dbClass, databaseService);
            }
        }

    }

    private static void createDetailItem(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "DetailItem";
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\dto\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".dto";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService) + "\n");
            writer.write("public class " + classFileName + " {\n\n");

            writer.write(createFields(dbClass, databaseService) + "\n");

            writer.write(createEmptyConstructor(dbClass) + "\n");

            writer.write(createConstructorFromDomain(dbClass) + "\n");

            writer.write(createGetters(dbClass) + "\n");

            writer.write(createSetters(dbClass) + "\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String createSetters(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("    public void setId(Long id) {\n" +
                "        this.id = id;\n" +
                "    }\n\n");
        for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
            if (dbClassField.getType().equals("Enum")) {
                result.append(makeSetterAsString(dbClassField) + "\n");
            } else if (dbClassField.getType().equals("Other Class")) {
                result.append(makeSetterOtherClassAsShortListItem(dbClassField) + "\n");
            } else {
                result.append(makeSetter(dbClassField) + "\n");
            }
        }
        return result.toString();
    }


    private static String createGetters(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("    public Long getId() {\n" +
                "        return id;\n" +
                "    }\n\n");
        for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
            if (dbClassField.getType().equals("Enum")) {
                result.append(makeGetterAsString(dbClassField) + "\n");
            } else if (dbClassField.getType().equals("Other Class")) {
                result.append(makeGetterOtherClassAsShortListItem(dbClassField) + "\n");
            } else {
                result.append(makeGetter(dbClassField) + "\n");
            }
        }
        return result.toString();
    }

    private static String createConstructorFromDomain(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String domainClassname = dbClass.getName();
        String domainInstanceName = makeUncapital(domainClassname);
        result.append("\tpublic " + domainClassname + "DetailItem(" + domainClassname + " " + domainInstanceName + ") {\n");

        result.append("\t\tthis.id = " + domainInstanceName + ".getId();\n");
        for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
            if (dbClassField.getType().equals("Enum") && !dbClassField.isList()) {
                result.append("\t\tthis." + dbClassField.getName() + " = ");
                result.append(domainInstanceName + ".get" + makeCapital(dbClassField.getName()) + "().getDisplayName();\n");
            } else if (dbClassField.getType().equals("Enum") && dbClassField.isList()) {
                result.append("\t\tfor (" + dbClassField.getEnumName() + " " + makeUncapital(dbClassField.getEnumName()) + " : " + domainInstanceName + ".get" + makeCapital(dbClassField.getName() + "()) {\n"));
                result.append("\t\t\tthis." + dbClassField.getName() + ".add(" + makeUncapital(dbClassField.getEnumName()) + ".getDisplayName());\n");
                result.append("\t\t}\n");
            } else if (dbClassField.getType().equals("Other Class") && !dbClassField.isList()) {
                result.append("\t\tthis." + dbClassField.getName() + " = ");
                result.append("new " + dbClassField.getOtherClassName() + "ShortListItem(" + domainInstanceName + ".get" + makeCapital(dbClassField.getName()) + "());\n");
            } else if (dbClassField.getType().equals("Other Class") && dbClassField.isList()) {
                result.append("\t\tfor (" + dbClassField.getOtherClassName() + " " + makeUncapital(dbClassField.getOtherClassName()) + " : " + domainInstanceName + ".get" + makeCapital(dbClassField.getName()) + "()) {\n");
                result.append("\t\t\tthis." + dbClassField.getName() + ".add(new " + dbClassField.getOtherClassName() + "ShortListItem(" + makeUncapital(dbClassField.getOtherClassName()) + "));\n");
                result.append("\t\t}\n");
            } else {
                result.append("\t\tthis." + dbClassField.getName() + " = ");
                result.append(domainInstanceName + ".get" + makeCapital(dbClassField.getName()) + "();\n");
            }
        }
        result.append("\t}\n");
        return result.toString();
    }

    private static String createEmptyConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\t" + dbClass.getName() + "DetailItem() {\n");
        result.append("\t}\n");
        return result.toString();
    }


    private static String createFields(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("    private Long id;\n");
        for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
            if (dbClassField.getType().equals("Enum")) {
                result.append(makeFieldLineTypeAsString(dbClassField));
            } else if (dbClassField.getType().equals("Other Class")) {
                result.append(makeFieldLineTypeAsShortListItem(dbClassField));
            } else {
                result.append(makeFieldLine(dbClassField) + "\n");
            }
        }
        return result.toString();
    }

    public static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import " + databaseService.getProjectName() + ".domain." + dbClass.getName() + ";\n");
        Set<String> listEnumSet = new HashSet<>();
        Set<String> listOtherClassNameSet = new HashSet<>();
        Boolean isListInList = false;
        boolean isContainDate = false;
        for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
            if (dbClassField.getType().equals("Enum")) {
                listEnumSet.add(dbClassField.getEnumName());
            } else if (dbClassField.getType().equals("Other Class")){
                listOtherClassNameSet.add(dbClassField.getOtherClassName());
            }
            if (dbClassField.isList()) {
                isListInList = true;
            }
            if (dbClassField.getType().equals("Date")) {
                isContainDate = true;
            }
        }
        for (String enumName : listEnumSet) {
            result.append("import " + databaseService.getProjectName() + ".domain." + enumName + ";\n");
        }
        for (String otherClassName : listOtherClassNameSet) {
            result.append("import " + databaseService.getProjectName() + ".domain." + otherClassName + ";\n");
        }
        if (isListInList) {
            result.append("import java.util.ArrayList;\n" +
                    "import java.util.List;\n");
        }
        if (isContainDate) {
            result.append("import java.time.LocalDateTime;\n");
        }
        return result.toString();
    }












}
