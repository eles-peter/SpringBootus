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

public class CreateFormDataModels {

    private CreateFormDataModels() {
    }

    public static void createFormDataModels(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            boolean isContainsEnumOrOtherObject = false;
            for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
                if (dbClassField.getType().equals("Enum") || dbClassField.getType().equals("Other Class")) {
                    isContainsEnumOrOtherObject = true;
                }
            }
            if (isContainsEnumOrOtherObject) {
                createFormDataModel(dbClass, databaseService);
            }
        }
    }

    private static void createFormDataModel(DBClass dbClass, DatabaseService databaseService) {
        String fileName = makeCapital(dbClass.getName()) + "FormData";
        String formDataModelName = fileName + "Model";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\models\\" + fileName + ".model.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(addImports(dbClass, databaseService) + "\n");

            writer.write("export interface " + formDataModelName + " {\n");

            writer.write(createFormDataFields(dbClass, databaseService));

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createFormDataFields(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.getType().equals("Enum")) {
                result.append("\t" + dbClassField.getName() + ": ");
                result.append(dbClassField.getEnumName() + "OptionModel[];\n");
            } else if (dbClassField.getType().equals("Other Class")) {
                result.append("\t" + dbClassField.getName() + ": ");
                result.append(dbClassField.getOtherClassName() + "ShortListItemModel[];\n");
            }
        }
        return result.toString();
    }

    private static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();

        for (String enumName : dbClass.getEnumNameSet()) {
            result.append("import {" + enumName + "OptionModel} from \"./" +
                    enumName + "Option.model\";\n");
        }
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append("import {" + otherClassName + "ShortListItemModel} from \"./" +
                    otherClassName + "ShortListItem.model\";\n");
        }
        return result.toString();
    }


}
