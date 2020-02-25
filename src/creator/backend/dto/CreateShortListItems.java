package creator.backend.dto;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.ClassBuherator.*;
import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateShortListItems {

    private CreateShortListItems() {
    }

    public static void createShortListItems(DatabaseService databaseService) {

        //TODO lehet, hogy kell egy szűrő, hogy csak azokat az osztályokat készítse el amik benne vannak másik osztály createList-ében...

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createShortListItem(dbClass, databaseService);
        }
    }

    private static void createShortListItem(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "ShortListItem";
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\dto\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".dto";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write("public class " + classFileName + " {\n\n");

            writer.write("    private long id;\n");
            for (DBClassField dbClassField : dbClass.getShortListFieldList()) {
                writer.write(makeFieldLine(dbClassField)+ "\n");
            }

            writer.write("\t" + dbClass.getName() + "ShortListItem() {\n");
            writer.write("\t}\n");
            writer.write(createConstructorFromListItem(dbClass));

            writer.write("    public Long getId() {\n" +
                    "        return id;\n" +
                    "    }\n\n");
            for (DBClassField dbClassField : dbClass.getShortListFieldList()) {
                writer.write(makeGetter(dbClassField));
            }

            writer.write("    public void setId(Long id) {\n" +
                    "        this.id = id;\n" +
                    "    }\n\n");
            for (DBClassField dbClassField : dbClass.getShortListFieldList()) {
                writer.write(makeSetter(dbClassField));
            }
            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String createConstructorFromListItem(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic " + dbClass.getName() + "ShortListItem(");
        String ListItemInstanceName = makeUncapital(dbClass.getName()) + "ListItem";
        result.append(dbClass.getName() + "ListItem " + ListItemInstanceName);
        result.append(") {\n");

        //TODO megvizsgálni, hogy lehet-e itt ENUM (vagy List...)

        result.append("\tthis.id = " + ListItemInstanceName + ".getId;\n");
        for (DBClassField dbClassField : dbClass.getShortListFieldList()) {
            result.append("\tthis.");
            result.append(dbClassField.getName() + " = " + ListItemInstanceName + ".get" + makeCapital(dbClassField.getName()) + ";\n");
        }

        result.append("\t}\n");
        return result.toString();
    }

}
