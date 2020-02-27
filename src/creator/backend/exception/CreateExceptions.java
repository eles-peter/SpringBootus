package creator.backend.exception;

import creator.DatabaseService;

import java.io.File;

import static creator.backend.exception.CreateApiError.createApiError;
import static creator.backend.exception.CreateGlobalExceptionHandler.createGlobalExceptionHandler;
import static creator.backend.exception.CreateValidationError.createValidationError;

public class CreateExceptions {

    private CreateExceptions() {
    }

    public static void createExceptions(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\exception");
        file.mkdirs();

        createApiError(databaseService);
        createGlobalExceptionHandler(databaseService);
        createValidationError(databaseService);

    }
}
