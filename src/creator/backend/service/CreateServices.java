package creator.backend.service;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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




            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        result.append("\tprivate " + repositoryClassName + " " + repositoryInstanceName + ";\n\n");
        result.append("\t@Autowired\n" +
                "\tpublic " + dbClass.getName() + "Service(" + repositoryClassName + " " + repositoryInstanceName + ") {\n" +
                "\t\tthis." + repositoryInstanceName + " = " + repositoryInstanceName + ";\n" +
                "\t}\n");
        return result.toString();
    }

    public static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import " + databaseService.getProjectName() + ".domain." + dbClass.getName() + ";\n");
        for (String otherClassname : dbClass.getOtherClassNameSet()) {
            result.append("import " + databaseService.getProjectName() + ".domain." + otherClassname + ";\n");
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
