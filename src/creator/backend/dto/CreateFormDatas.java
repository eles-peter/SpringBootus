package creator.backend.dto;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            List<DBClassField> formDataFields = getFormDataClassFields(dbClass);

            writer.write(createFormDataFieldLines(formDataFields) + "\n");

            writer.write(createFormDataConstructor(dbClass, formDataFields) + "\n");

            writer.write(createFormDataGetters(formDataFields) + "\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createFormDataGetters(List<DBClassField> formDataFields) {
        StringBuilder result = new StringBuilder();
        for (DBClassField formDataField : formDataFields) {
            result.append(makeGetterAsList(formDataField) + "\n");
        }
        return result.toString();
    }

    public static String createFormDataFieldLines(List<DBClassField> formDataFields) {
        StringBuilder result = new StringBuilder();
        for (DBClassField formDataField : formDataFields) {
            result.append("\tprivate " + createFieldTypeWithNameAsList(formDataField));
        }
        return result.toString();
    }

    public static List<DBClassField> getFormDataClassFields(DBClass dbClass) {
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
        return formDataFields;
    }

    public static String createFormDataConstructor(DBClass dbClass, List<DBClassField> formDataFields) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic " + dbClass.getName() + "FormData(");
        for (int i = 0; i < formDataFields.size(); i++) {
            DBClassField formDataField = formDataFields.get(i);
            result.append("List<" + formDataField.getType() + "> " + formDataField.getName() + "List");
            if (i != formDataFields.size() - 1) {
                result.append(", ");
            }
        }
        result.append(") {\n");
        for (DBClassField formDataField : formDataFields) {
            result.append("\t\tthis." + formDataField.getName() + "List = " + formDataField.getName() + "List;\n");
        }
        result.append("\t}\n");
        return result.toString();
    }

    private static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import java.util.List;\n");
        result.append("\n");
        return result.toString();
    }

}
