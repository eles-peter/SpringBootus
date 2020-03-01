package creator.frontend.components;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;

import static creator.frontend.components.listcomponents.CreateListComponentCss.createListComponentCss;
import static creator.frontend.components.listcomponents.CreateListComponentHtml.createListComponentHtml;
import static creator.frontend.components.listcomponents.CreateListComponentTypeScrypt.createListComponentTypeScrypt;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateListComponents {

    private CreateListComponents() {
    }

    public static void createListComponents(DatabaseService databaseService) {

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            String dirName = makeUncapital(dbClass.getName()) + "-list";
            File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName);
            file.mkdirs();

            createListComponentCss(dbClass, databaseService);
            createListComponentHtml(dbClass, databaseService);
            createListComponentTypeScrypt(dbClass, databaseService);
        }



    }
}
