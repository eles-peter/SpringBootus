package creator.backend.repository;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateRepositories {

    private CreateRepositories() {
    }

    public static void createRepositories(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\repository");
        file.mkdirs();

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createRepository(dbClass, databaseService);
        }
    }

    private static void createRepository(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "Repository";
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\repository\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".repository";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService));
            writer.write("@Repository\n");
            writer.write("public interface " + classFileName + " extends JpaRepository<" + dbClass.getName() + ", Long> {\n\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import " + databaseService.getProjectName() + ".domain." + dbClass.getName() + ";\n");
        result.append("import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import org.springframework.stereotype.Repository;\n\n");
        return result.toString();
    }
}
