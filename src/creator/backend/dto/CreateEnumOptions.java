package creator.backend.dto;

import creator.DBClass;
import creator.DBClassField;
import creator.DBEnum;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateEnumOptions {

    private CreateEnumOptions() {
    }

    public static void createEnumOptions(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            for (DBClassField dbClassField : dbClass.getFieldList()) {
                if (dbClassField.getType().equals("Enum")) {
                    createEnumOption(dbClassField, dbClass, databaseService);
                }
            }
        }
    }

    private static void createEnumOption(DBClassField dbClassField, DBClass dbClass, DatabaseService databaseService) {
        String fileName = makeCapital(dbClassField.getEnumName()) + "Option";
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\dto\\" + fileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".dto";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClassField, databaseService));
            writer.write("public class " + fileName + " {\n\n");
            writer.write("\n" +
                    "    private String name;\n" +
                    "    private String displayName;\n\n");
            writer.write("    public " + fileName + "(" + dbClassField.getEnumName() + " " + makeUncapital(dbClassField.getEnumName()) + ") {\n");
            writer.write("        this.name = " + makeUncapital(dbClassField.getEnumName()) + ".toString();\n");
            writer.write("        this.displayName = " + makeUncapital(dbClassField.getEnumName()) + ".getDisplayName();\n");
            writer.write("    }\n" +
                    "\n" +
                    "    public String getName() {\n" +
                    "        return name;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setName(String name) {\n" +
                    "        this.name = name;\n" +
                    "    }\n" +
                    "\n" +
                    "    public String getDisplayName() {\n" +
                    "        return displayName;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setDisplayName(String displayName) {\n" +
                    "        this.displayName = displayName;\n" +
                    "    }\n" +
                    "}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addImports(DBClassField dbClassField, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import " + databaseService.getProjectName() + ".domain." + dbClassField.getEnumName() + ";\n");
        result.append("\n");
        return result.toString();
    }


}
