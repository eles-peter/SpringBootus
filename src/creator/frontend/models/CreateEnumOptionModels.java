package creator.frontend.models;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeCapital;

public class CreateEnumOptionModels {

    private CreateEnumOptionModels() {
    }

    static public void createEnumOptionModels(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            for (DBClassField dbClassField : dbClass.getFieldList()) {
                if (dbClassField.getType().equals("Enum")) {
                    createEnumOptionModel(dbClassField, databaseService);
                }
            }
        }
    }

    private static void createEnumOptionModel(DBClassField dbClassField, DatabaseService databaseService) {
        String fileName = makeCapital(dbClassField.getEnumName()) + "Option";
        String enumModelName = fileName + "Model";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\models\\" + fileName + ".model.ts");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("export interface " + enumModelName + " {\n");

            //TODO ezen még gondolkodni egy kicsit!!!!
            writer.write("\tname: string;\n" +
                    "\tdisplayName: string;\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
