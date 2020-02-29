package creator.frontend.models;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeCapital;

public class CreateCreateItemModels {

    private CreateCreateItemModels() {
    }

    public static void createCreateItemModels(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createCreateItemModel(dbClass, databaseService);
        }
    }

    private static void createCreateItemModel(DBClass dbClass, DatabaseService databaseService) {
    String fileName = makeCapital(dbClass.getName()) + "CreateItem";
        String createModelName = fileName + "Model";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\models\\" + fileName + ".model.ts");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("export interface " + createModelName + " {\n");

            writer.write(createCreateItemFields(dbClass, databaseService));

            writer.write("}\n");

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    private static String createCreateItemFields(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("\tid?: number;\n");
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            result.append("\t" + dbClassField.getName());
            if (dbClassField.getType().equals("Other Class")) {
                result.append("Id");
            }
            result.append(": ");
            switch (dbClassField.getType()) {
                case "string":
                case "Enum":
                case "Image URL":
                case "Text Area":
                    result.append("string");
                    break;
                case "Integer":
                case "Long":
                case "Double":
                case "Other Class":
                    result.append("number");
                    break;
                case "Boolean":
                    result.append("boolean");
                    break;
                case "Date":
                    result.append("Date");
                    break;
                default:
                    result.append("string");
            }
            if (dbClassField.isList()) {
                result.append("[]");
            }
            result.append(";\n");
        }

        return result.toString();
    }
}
