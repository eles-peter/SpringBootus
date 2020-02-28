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

            //TODO átgondolni "other Class"-t passzolgatni, vagy otherClass Id-jét
            //TODO Enum-nak meg csak a nevét Strinben....

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

            for (DBClassField dbClassField : modifiedCreateFieldList) {
                writer.write(makeFieldLine(dbClassField) + "\n");
            }
            writer.write("\n");

            for (DBClassField dbClassField : modifiedCreateFieldList) {
                writer.write(makeGetter(dbClassField) + "\n");
            }
            writer.write("\n");

            for (DBClassField dbClassField : modifiedCreateFieldList) {
                writer.write(makeSetter(dbClassField) + "\n");
            }
            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();

        boolean isContainList = false;
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.isList()) {
                isContainList = true;
            }
        }
        if (isContainList) {
            result.append("import java.util.ArrayList;\n" +
                    "import java.util.List;\n");
        }
        result.append("\n");
        return result.toString();
    }


}
