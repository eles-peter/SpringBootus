package creator.frontend.components.listcomponents;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateListComponentCss {

    private CreateListComponentCss() {
    }

    public static void createListComponentCss(DBClass dbClass, DatabaseService databaseService) {

        String dirName = makeUncapital(dbClass.getName()) + "-list";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.css");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
