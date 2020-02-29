package creator.frontend.models;

import creator.DatabaseService;

import java.io.File;

import static creator.frontend.models.CreateCreateItemModels.createCreateItemModels;
import static creator.frontend.models.CreateDetailItemModels.createDetailItemModels;
import static creator.frontend.models.CreateEnumOptionModels.createEnumOptionModels;
import static creator.frontend.models.CreateFormDataModels.createFormDataModels;
import static creator.frontend.models.CreateListItemModels.createListItemModels;
import static creator.frontend.models.CreateShortListItemModels.createShortListItemModels;

public class CreateModels {

    private CreateModels() {
    }

    public static void createModels(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\models");
        file.mkdirs();

        createEnumOptionModels(databaseService);
        createFormDataModels(databaseService);
        createCreateItemModels(databaseService);
        createDetailItemModels(databaseService);
        createListItemModels(databaseService);
        createShortListItemModels(databaseService);

    }
}
