package creator.frontend.components.formcomponents;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateFormComponentCss {

    private CreateFormComponentCss() {
    }

    public static void createFormComponentCss(DBClass dbClass, DatabaseService databaseService) {

        String dirName = makeUncapital(dbClass.getName()) + "-form";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.css");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
