package creator.backend.config;

import creator.DatabaseService;

import java.io.File;

import static creator.backend.config.CreateSpringWebConfig.createSpringWebConfig;

public class CreateConfig {

    private CreateConfig() {
    }

    public static void createConfig(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\config");
        file.mkdirs();

        createSpringWebConfig(databaseService);

    }
}
