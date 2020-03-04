package creator.backend.dto;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static creator.utils.ClassBuherator.*;
import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateCreateItems {

    private CreateCreateItems() {
    }

    public static void createCreateItems(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createCreateItem(dbClass, databaseService);
        }
    }

    private static void createCreateItem(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "CreateItem";
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\dto\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".dto";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService));
            writer.write("public class " + classFileName + " {\n\n");

            //TODO átgondolni, enum vagy lista, vagy más objekt miatt kell-e valami!!!!

            List<DBClassField> modifiedCreateFieldList = new ArrayList<>();
            for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
                DBClassField actualField = new DBClassField(dbClassField);
                if (dbClassField.getType().equals("Enum")) {
                    actualField.setEnumName("String");
                } else if (dbClassField.getType().equals("Other Class")) {
                    actualField.setOtherClassName("Long");
                    actualField.setName(dbClassField.getName() + "Id");
                }
                modifiedCreateFieldList.add(actualField);
            }

            writer.write(createFields(modifiedCreateFieldList) + "\n");

            writer.write(createEmptyConstructor(dbClass) + "\n");

            writer.write(createConstructorFromDomain(dbClass) + "\n");

            writer.write(createGetters(modifiedCreateFieldList) + "\n");

            writer.write(createSetters(modifiedCreateFieldList) + "\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createEmptyConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\t" + dbClass.getName() + "CreateItem() {\n");
        result.append("\t}\n");
        return result.toString();
    }

    private static String createConstructorFromDomain(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String createItemClassName = dbClass.getName() + "CreateItem";
        result.append("\tpublic " + createItemClassName + "(" + dbClass.getName() + " " + makeUncapital(dbClass.getName()) + ") {\n");
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (!dbClassField.getType().equals("Other Class")) {
                result.append("\t\tthis." + dbClassField.getName() + " = ");

                if (dbClassField.getType().equals("Enum") && !dbClassField.isList()) {
                    result.append(makeUncapital(dbClass.getName()) + ".get" + makeCapital(dbClassField.getName()) + "().name();\n");
                } else if (dbClassField.getType().equals("Enum") && dbClassField.isList()) {
                    result.append(makeUncapital(dbClass.getName()) + ".get" + makeCapital(dbClassField.getName()) + "().stream()" +
                            ".map(Enum::name)" +
                            ".collect(Collectors.toList());\n");
                } else {
                    result.append(makeUncapital(dbClass.getName()) + ".get" + makeCapital(dbClassField.getName()) + "();\n");
                }
            } else {
                result.append("\t\tthis." + dbClassField.getName() + "Id = ");
                result.append(makeUncapital(dbClass.getName() + ".get" + makeCapital(dbClassField.getName()) + "().getId();\n"));

                //TODO megírni, ha otherclass és list!!!!
            }
        }
        result.append("\t}\n");
        return result.toString();
    }

    private static String createFields(List<DBClassField> modifiedCreateFieldList) {
        StringBuilder result = new StringBuilder();
        for (DBClassField dbClassField : modifiedCreateFieldList) {
            if (dbClassField.getType().equals("Date Time")) {
                result.append("\t@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)\n");
            }
            result.append(makeFieldLine(dbClassField) + "\n");
        }
        return result.toString();
    }

    private static String createGetters(List<DBClassField> modifiedCreateFieldList) {
        StringBuilder result = new StringBuilder();
        for (DBClassField dbClassField : modifiedCreateFieldList) {
            result.append(makeGetter(dbClassField) + "\n");
        }
        return result.toString();
    }

    private static String createSetters(List<DBClassField> modifiedCreateFieldList) {
        StringBuilder result = new StringBuilder();
        for (DBClassField dbClassField : modifiedCreateFieldList) {
            result.append(makeSetter(dbClassField) + "\n");
        }
        return result.toString();
    }


    private static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();

        result.append("import " + databaseService.getProjectName() + ".domain." + dbClass.getName() + ";\n");

        boolean isContainList = false;
        boolean isContainDate = false;
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.isList()) {
                isContainList = true;
            }
            if (dbClassField.getType().equals("Date Time")) {
                isContainDate = true;
            }
        }
        if (isContainList) {
            result.append("import java.util.ArrayList;\n" +
                    "import java.util.List;\n" +
                    "import java.util.stream.Collectors;\n");
        }
        if (isContainDate) {
            result.append("import java.time.LocalDateTime;\n" +
                    "import org.springframework.format.annotation.DateTimeFormat;\n");
        }
        result.append("\n");
        return result.toString();
    }


}
