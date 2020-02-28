package creator.backend.service;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateServices {

    private CreateServices() {
    }

    public static void createServices(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\service");
        file.mkdirs();

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createService(dbClass, databaseService);
        }
    }

    private static void createService(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "Service";
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\service\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".service";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService) + "\n");
            writer.write("@Service\n" +
                    "@Transactional\n" +
                    "public class " + classFileName + " {\n\n");
            writer.write(createFieldAndConstructor(dbClass, databaseService) + "\n");

            writer.write(createFindAllMethod(dbClass, databaseService) + "\n");

            writer.write(createGetListItemListMethod(dbClass, databaseService) + "\n");

            writer.write(createGetShortListItemListMethod(dbClass, databaseService) + "\n");

            writer.write(createGetEnumOptionListMethod(dbClass, databaseService) + "\n");

            writer.write(createSaveMethod(dbClass, databaseService) + "\n");

            if (!dbClass.getDetailFieldList().isEmpty()) {
                writer.write(createGetDetailItem(dbClass, databaseService) + "\n");
            } else {
                writer.write(createGetListItem(dbClass, databaseService) + "\n");
            }

            writer.write(createUpdateMethod(dbClass, databaseService) + "\n");

            writer.write(createDeleteMethod(dbClass, databaseService) + "\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createDeleteMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tpublic boolean delete" + className + "(Long id) {\n");
        result.append("\t\tOptional<" + className + "> " + instanceName + "Optional = " + instanceName + "Repository.findById(id);\n");
        result.append("\t\tif (" + instanceName + "Optional.isPresent()) {\n");
        result.append("\t\t\t" + className + " " + instanceName + " = " + instanceName + "Optional.get();\n");
        result.append("            //TODO megnézni máshol van-e...\n");
        result.append("\t\t\t" + instanceName + "Repository.delete(" + instanceName + ");\n");
        result.append("\t\t\treturn true;\n");
        result.append("\t\t} else {\n");
        result.append("\t\t\treturn false;\n");
        result.append("\t\t}\n");
        result.append("\t}\n");

        return result.toString();
    }

    private static String createGetListItem(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tpublic " + className + "ListItem get" + className + "ListItem (Long id) {\n");
        String instanceName = makeUncapital(className);
        result.append("\t\t" + className + "ListItem " + instanceName + "ListItem = null;\n");
        result.append("\t\t Optional<" + className + "> " + instanceName + "Optional = " + instanceName + "Repository.findById(id);\n");
        result.append("\t\tif (" + instanceName + "Optional.isPresent()) {\n");
        result.append("\t\t\t" + instanceName + "ListItem = new " + className + "ListItem(" + instanceName + "Optional.get());\n");
        result.append("\t\t}\n");
        result.append("\t\treturn " + instanceName + "ListItem;\n");

        result.append("\t}\n");

        return result.toString();
    }

    private static String createGetDetailItem(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tpublic " + className + "DetailItem get" + className + "DetailItem (Long id) {\n");
        String instanceName = makeUncapital(className);
        result.append("\t\t" + className + "DetailItem " + instanceName + "DetailItem = null;\n");
        result.append("\t\t Optional<" + className + "> " + instanceName + "Optional = " + instanceName + "Repository.findById(id);\n");
        result.append("\t\tif (" + instanceName + "Optional.isPresent()) {\n");
        result.append("\t\t\t" + instanceName + "DetailItem = new " + className + "DetailItem(" + instanceName + "Optional.get());\n");
        result.append("\t\t}\n");
        result.append("\t\treturn " + instanceName + "DetailItem;\n");

        result.append("\t}\n");

        return result.toString();
    }

    private static String createUpdateMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tpublic Boolean update" + className + "(" + className + "CreateItem " + makeUncapital(className) + "CreateItem, Long id) {\n");
        result.append("\t\tOptional<" + className + "> " + makeUncapital(className) + "Optional = " + makeUncapital(className) + "Repository.findById(id);\n");
        result.append("\t\tif (" + makeUncapital(className) + "Optional.isPresent()) {\n");
        result.append("\t\t\t" + className + " " + makeUncapital(className));
        result.append(" = new " + className + "(" + makeUncapital(className + "CreateItem);\n\n"));
        result.append("\t\t\t" + makeUncapital(className) + ".setId(id);\n");

        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.getType().equals("Other Class")) {
                String fieldName = dbClassField.getName();
                String otherClassName = dbClassField.getOtherClassName();
                result.append("\t\t\tLong " + fieldName + "Id = " + makeUncapital(className) + "CreateItem.get" + makeCapital(fieldName) + "Id();\n");
                result.append("\t\t\tOptional<" + otherClassName + ">" + fieldName + "Optional = this." + makeUncapital(otherClassName) + "Repository.findById(" + fieldName + "Id);\n");
                result.append("\t\t\tif (" + fieldName + "Optional.isPresent()) {\n");
                result.append("\t\t\t\t" + otherClassName + " " + fieldName + " = " + fieldName + "Optional.get();\n");
                result.append("\t\t\t\t" + makeUncapital(className) + ".set" + makeCapital(fieldName) + "(" + fieldName + ");\n");
                result.append("\t\t\t}\n\n");
            }
        }
        result.append("\t\t\tthis." + makeUncapital(className) + "Repository.save(" + makeUncapital(className) + ");\n");
        result.append("\t\t\treturn true;\n");
        result.append("\t\t} else {\n");
        result.append("\t\t\treturn false;\n");
        result.append("\t\t}\n");
        result.append("\t}\n");

        return result.toString();
    }

    private static String createSaveMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tpublic void save" + className + "(" + className + "CreateItem " + makeUncapital(className) + "CreateItem) {\n");
        result.append("\t\t" + className + " " + makeUncapital(className));
        result.append(" = new " + className + "(" + makeUncapital(className + "CreateItem);\n\n"));

        //TODO megírni, mi van, ha ez lista!!!!!!!
        //TODO UPDATE-ben is módosítani!!!!

        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.getType().equals("Other Class")) {
                String fieldName = dbClassField.getName();
                String otherClassName = dbClassField.getOtherClassName();
                result.append("\t\tLong " + fieldName + "Id = " + makeUncapital(className) + "CreateItem.get" + makeCapital(fieldName) + "Id();\n");
                result.append("\t\tOptional<" + otherClassName + ">" + fieldName + "Optional = this." + makeUncapital(otherClassName) + "Repository.findById(" + fieldName + "Id);\n");
                result.append("\t\tif (" + fieldName + "Optional.isPresent()) {\n");
                result.append("\t\t\t" + otherClassName + " " + fieldName + " = " + fieldName + "Optional.get();\n");
                result.append("\t\t\t" + makeUncapital(className) + ".set" + makeCapital(fieldName) + "(" + fieldName + ");\n");
                result.append("\t\t}\n\n");
            }
        }
        result.append("\t\tthis." + makeUncapital(className) + "Repository.save(" + makeUncapital(className) + ");\n");
        result.append("\t}\n");

        return result.toString();
    }

    private static String createGetEnumOptionListMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        for (String enumName : dbClass.getEnumNameSet()) {
            result.append("\tpublic List<" + enumName + "Option> get" + enumName + "OptionList() {\n" +
                    "\t\treturn Arrays.stream(" + enumName + ".values()).map(" + enumName + "Option::new).collect(Collectors.toList());\n" +
                    "\t}\n");
        }
        return result.toString();
    }

    private static String createGetShortListItemListMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tpublic List<" + className + "ShortListItem> get" + className + "ShortListItemList() {\n" +
                "\t\treturn findAll" + className + "().stream().map(" + className + "ShortListItem::new).collect(Collectors.toList());\n" +
                "\t}");
        return result.toString();
    }

    private static String createGetListItemListMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tpublic List<" + className + "ListItem> get" + className + "ListItemList() {\n" +
                "\t\treturn findAll" + className + "().stream().map(" + className + "ListItem::new).collect(Collectors.toList());\n" +
                "\t}");
        return result.toString();
    }

    private static String createFindAllMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tpublic List<" + className + "> findAll" + className + "() {\n" +
                "\t\treturn " + makeUncapital(className) + "Repository.findAll();\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createFieldAndConstructor(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String repositoryInstanceName = makeUncapital(dbClass.getName()) + "Repository";
        String repositoryClassName = dbClass.getName() + "Repository";
        result.append("\tprivate " + repositoryClassName + " " + repositoryInstanceName + ";\n");
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append("\tprivate " + otherClassName + "Repository " + makeUncapital(otherClassName) + "Repository;\n");
        }
        result.append("\n\t@Autowired\n" +
                "\tpublic " + dbClass.getName() + "Service(");
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append(otherClassName + "Repository " + makeUncapital(otherClassName) + "Repository, ");
        }
        result.append(repositoryClassName + " " + repositoryInstanceName+ ") {\n");
        result.append("\t\tthis." + repositoryInstanceName + " = " + repositoryInstanceName + ";\n");
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append("\t\tthis." + makeUncapital(otherClassName) + "Repository = " + makeUncapital(otherClassName) + "Repository;\n");
        }

        result.append("\t}\n");
        return result.toString();
    }

    public static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import " + databaseService.getProjectName() + ".domain." + dbClass.getName() + ";\n");
        for (String otherClassname : dbClass.getOtherClassNameSet()) {
            result.append("import " + databaseService.getProjectName() + ".domain." + otherClassname + ";\n");
            result.append("import " + databaseService.getProjectName() + ".repository." + otherClassname + "Repository;\n");
        }
        for (String enumName : dbClass.getEnumNameSet()) {
            result.append("import " + databaseService.getProjectName() + ".domain." + enumName + ";\n");
        }
        result.append("import " + databaseService.getProjectName() + ".dto.*;\n");
        result.append("import " + databaseService.getProjectName() + ".repository." + dbClass.getName() + "Repository;\n\n");

        result.append("import org.springframework.stereotype.Service;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n\n");

        result.append("import java.util.List;\n" +
                "import java.util.Optional;\n" +
                "import java.util.stream.Collectors;\n");
        if (!dbClass.getEnumNameSet().isEmpty()) {
            result.append("import java.util.Arrays;\n");
        }

        return result.toString();
    }

}
