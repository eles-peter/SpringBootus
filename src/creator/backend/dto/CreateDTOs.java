package creator.backend.dto;

import creator.DatabaseService;

import java.io.File;

import static creator.backend.dto.CreateCreateItems.createCreateItems;
import static creator.backend.dto.CreateEnumOptions.createEnumOptions;
import static creator.backend.dto.CreateFormDatas.createFormDatas;
import static creator.backend.dto.CreateShortListItems.createShortListItems;

public class CreateDTOs {

    private CreateDTOs() {
    }

    public static void createDTOs(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\dto");
        file.mkdirs();

        createEnumOptions(databaseService);
        createCreateItems(databaseService);
        createFormDatas(databaseService);
        createShortListItems(databaseService);


    }
}
