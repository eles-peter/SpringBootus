package creator.backend.dto;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static creator.utils.ClassBuherator.createFieldTypeWithNameAsList;
import static creator.utils.ClassBuherator.makeGetterAsList;
import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateFormDatas {

    private CreateFormDatas() {
    }

    public static void createFormDatas(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            boolean isContainsEnumOrOtherObject = false;
            for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
                if (dbClassField.getType().equals("Enum") || dbClassField.getType().equals("Other Class")) {
                    isContainsEnumOrOtherObject = true;
                }
            }
            if (isContainsEnumOrOtherObject) {
                createFormData(dbClass, databaseService);
            }
        }
    }

    private static void createFormData(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "FormData";
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\dto\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".dto";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService));
            writer.write("public class " + classFileName + " {\n\n");

//            List<DBClassField> formDataFields=  dbClass.getCreateFieldList().stream()
//                    .filter(field -> field.getType().equals("Enum") || field.getType().equals("Other Class"))
//                    .collect(Collectors.toList());

            List<DBClassField> formDataFields = new ArrayList<>();
            for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
                if (dbClassField.getType().equals("Enum")) {
                    String enumOptionClassName = makeCapital(dbClassField.getEnumName()) + "Option";
                    String  fieldName = dbClassField.getName();
                    formDataFields.add(new DBClassField(fieldName, enumOptionClassName));
                } else if (dbClassField.getType().equals("Other Class")) {
                    String shortListItemClassName = dbClassField.getOtherClassName() + "ShortListItem";
                    String fieldName = makeUncapital(dbClassField.getOtherClassName());
                    formDataFields.add(new DBClassField(fieldName, shortListItemClassName));
                }
            }

            for (DBClassField formDataField : formDataFields) {
                writer.write("\tprivate " + createFieldTypeWithNameAsList(formDataField));
            }

            writer.write("\n");
            //TODO kell constructor???
            for (DBClassField formDataField : formDataFields) {
                writer.write(makeGetterAsList(formDataField) + "\n");
            }
            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import java.util.List;\n");
        result.append("\n");
        return result.toString();
    }

}
