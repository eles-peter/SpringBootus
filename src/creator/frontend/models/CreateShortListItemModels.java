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

public class CreateShortListItemModels {

    private CreateShortListItemModels() {
    }

    public static void createShortListItemModels(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createShortListItemModel(dbClass, databaseService);
        }
    }

    private static void createShortListItemModel(DBClass dbClass, DatabaseService databaseService) {
        String fileName = makeCapital(dbClass.getName()) + "ShortListItem";
        String createModelName = fileName + "Model";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\models\\" + fileName + ".model.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(addImports(dbClass, databaseService) + "\n");

            writer.write("export interface " + createModelName + " {\n");

            writer.write(createShortListItemFields(dbClass, databaseService));

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        Set<String> shortListItemOtherClassName = new HashSet<>();
        for (DBClassField dbClassField : dbClass.getShortListFieldList()) {
            if (dbClassField.getType().equals("Other Class")) {
                shortListItemOtherClassName.add(dbClassField.getOtherClassName());
            }
        }
        for (String otherClassName : shortListItemOtherClassName) {
            result.append("import {" + otherClassName +
                    "ShortListItemModel} from \"./" + otherClassName + "ShortListItem.model\";\n");
        }
        return result.toString();
    }

    private static String createShortListItemFields(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("\tid: number;\n");
        for (DBClassField dbClassField : dbClass.getShortListFieldList()) {
            result.append("\t" + dbClassField.getName());
            result.append(": ");
            switch (dbClassField.getType()) {
                case "String":
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
                case "Date Time":
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
