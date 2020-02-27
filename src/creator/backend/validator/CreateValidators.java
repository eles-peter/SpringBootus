package creator.backend.validator;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateValidators {

    private CreateValidators() {
    }

    public static void createValidators(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\validator");
        file.mkdirs();

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createValidator(dbClass, databaseService);
        }
    }

    private static void createValidator(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "CreateItemValidator";
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\validator\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".validator";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService) + "\n");
            writer.write("@Component\n");
            writer.write("public class " + classFileName + " implements Validator {\n\n");
            writer.write("\tprivate final " + dbClass.getName() + "Service validatorService;\n\n");
            writer.write("\tpublic " + classFileName + "(" + dbClass.getName() + "Service validatorService) {\n");
            writer.write("        this.validatorService = validatorService;\n" +
                    "    }\n\n");

            writer.write(createOverrideSupportsMethod(dbClass, databaseService) + "\n");

            writer.write(createOverrideValidateMethod(dbClass, databaseService) + "\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createOverrideValidateMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("\t@Override");
        result.append("    public void validate(Object obj, Errors e) {\n");

        result.append("\n\n\t\t//TODO write the validations!!!\n\n\n");

        result.append("\t}\n");
        return result.toString();
    }

    private static String createOverrideSupportsMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("\t@Override");
        result.append("    public boolean supports(Class clazz) {\n" +
                "        return " + dbClass.getName() + "Details.class.equals(clazz);\n" +
                "    }\n");
        return result.toString();
    }

    public static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import " + databaseService.getProjectName() + ".dto." + dbClass.getName() + "CreateItem;\n");
        result.append("import " + databaseService.getProjectName() + ".service." + dbClass.getName() + "Service;\n");
        result.append("import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "import org.springframework.validation.Errors;\n" +
                "import org.springframework.validation.Validator;\n");
        return result.toString();
    }


}
