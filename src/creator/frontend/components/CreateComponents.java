package creator.frontend.components;

import creator.DatabaseService;

import java.io.File;

import static creator.frontend.components.CreateDetailComponents.createDetailComponents;
import static creator.frontend.components.CreateFormComponents.createFormComponents;
import static creator.frontend.components.CreateListComponents.createListComponents;
import static creator.frontend.components.CreateNavbar.createNavbar;

public class CreateComponents {

    private CreateComponents() {
    }

    public static void createComponents(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components");
        file.mkdirs();

        createNavbar(databaseService);
        createListComponents(databaseService);
        createFormComponents(databaseService);
        createDetailComponents(databaseService);

    }
}
