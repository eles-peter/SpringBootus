package creator.frontend.components.detailcomponents;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateDetailComponentHtml {

    private CreateDetailComponentHtml() {
    }

    public static void createDetailComponentHtml(DBClass dbClass, DatabaseService databaseService) {
        String dirName = makeUncapital(dbClass.getName()) + "-detail";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.html");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write("<h2>" + dbClass.getName() + "-detail</h2>\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
