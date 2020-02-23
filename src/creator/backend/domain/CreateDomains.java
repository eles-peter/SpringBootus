package creator.backend.domain;

import java.io.File;

import creator.DatabaseService;

import static creator.backend.domain.CreateClassDomains.createClassDomains;
import static creator.backend.domain.CreateEnumDomains.createEnumDomains;

public class CreateDomains {

    private CreateDomains() {
    }

    public static void createDomains(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\domain");
        file.mkdirs();

        createClassDomains(databaseService);
        createEnumDomains(databaseService);

    }

}
