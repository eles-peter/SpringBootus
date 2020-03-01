package creator.frontend.components;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;

import static creator.frontend.components.formcomponents.CreateFormComponentCss.createFormComponentCss;
import static creator.frontend.components.formcomponents.CreateFormComponentHtml.createFormComponentHtml;
import static creator.frontend.components.formcomponents.CreateFormComponentTypeScrypt.createFormComponentTypeScrypt;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateFormComponents {

    private CreateFormComponents() {
    }

    public static void createFormComponents(DatabaseService databaseService) {

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            String dirName = makeUncapital(dbClass.getName()) + "-form";
            File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName);
            file.mkdirs();

            createFormComponentCss(dbClass, databaseService);
            createFormComponentHtml(dbClass, databaseService);
            createFormComponentTypeScrypt(dbClass, databaseService);
        }

    }

}
