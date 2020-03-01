package creator.frontend.components;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;

import static creator.frontend.components.detailcomponents.CreateDetailComponentCss.createDetailComponentCss;
import static creator.frontend.components.detailcomponents.CreateDetailComponentHtml.createDetailComponentHtml;
import static creator.frontend.components.detailcomponents.CreateDetailComponentTypeScrypt.createDetailComponentTypeScrypt;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateDetailComponents {

    private CreateDetailComponents() {
    }

    public static void createDetailComponents(DatabaseService databaseService) {

        for (DBClass dbClass : databaseService.getDbclasslist()) {

            if (!dbClass.getDetailFieldList().isEmpty()) {

                String dirName = makeUncapital(dbClass.getName()) + "-detail";
                File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName);
                file.mkdirs();

                createDetailComponentCss(dbClass, databaseService);
                createDetailComponentHtml(dbClass, databaseService);
                createDetailComponentTypeScrypt(dbClass, databaseService);
            }
        }
    }
}
