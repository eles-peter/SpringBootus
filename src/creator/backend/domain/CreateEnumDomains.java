package creator.backend.domain;

import creator.DBEnum;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeEnumName;

public class CreateEnumDomains {

    public CreateEnumDomains() {}

    public static void createEnumDomains(DatabaseService databaseService) {
        for (DBEnum dbEnum : databaseService.getDbEnumList()) {
            String enumPackageName = databaseService.getProjectName() + ".domain";
            String enumFileName = dbEnum.getCapitalName();
            File file= new File(databaseService.getBackendApplicationDirectory() + "\\domain\\" + enumFileName + ".java");
            createEnumDomain(dbEnum, file, enumPackageName);
        }
    }

    private static void createEnumDomain(DBEnum dbEnum, File file, String enumPackageName) {
        try (FileWriter writer = new FileWriter(file)) {
            String enumClassName = dbEnum.getCapitalName();
            writer.write("package " + enumPackageName + ";\n\n");
            writer.write("public enum " + enumClassName + " {\n\n");
            for (String displayName : dbEnum.getDisplayNameList()) {
                writer.write("\t" + makeEnumName(displayName) + "(\"" + displayName + "\"),\n");
            }
            writer.write("    ;\n" +
                    "\n" +
                    "    private String displayName;\n" +
                    "\n" +
                    "    private " + enumClassName + "(String displayName) {\n" +
                    "        this.displayName = displayName;\n" +
                    "    }\n" +
                    "\n" +
                    "    public String getDisplayName() {\n" +
                    "        return displayName;\n" +
                    "    }\n" +
                    "}");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
