package creator.frontend.models;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static creator.utils.StringBuherator.makeCapital;

public class CreateDetailItemModels {

    private CreateDetailItemModels() {
    }

    public static void createDetailItemModels(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            if (!dbClass.getDetailFieldList().isEmpty()) {
                createDetailItemModel(dbClass, databaseService);
            }
        }
    }

    private static void createDetailItemModel(DBClass dbClass, DatabaseService databaseService) {
        String fileName = makeCapital(dbClass.getName()) + "DetailItem";
        String createModelName = fileName + "Model";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\models\\" + fileName + ".model.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(addImports(dbClass, databaseService) + "\n");

            writer.write("export interface " + createModelName + " {\n");

            writer.write(createDetailItemFields(dbClass, databaseService));

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        Set<String> createItemOtherClassName = new HashSet<>();
        for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
            if (dbClassField.getType().equals("Other Class")) {
                createItemOtherClassName.add(dbClassField.getOtherClassName());
            }
        }
        for (String otherClassName : createItemOtherClassName) {
            result.append("import {" + otherClassName +
                    "ShortListItemModel} from \"./" + otherClassName + "ShortListItem.model\";\n");
        }
        return result.toString();
    }

    private static String createDetailItemFields(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("\tid: number;\n");
        for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
            result.append("\t" + dbClassField.getName());
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
                    result.append("number");
                    break;
                case "Other Class":
                    result.append(dbClassField.getOtherClassName() + "ShortListItemModel");
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
