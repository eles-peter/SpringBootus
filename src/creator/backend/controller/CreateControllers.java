package creator.backend.controller;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateControllers {

    private CreateControllers() {
    }

    public static void createControllers(DatabaseService databaseService) {
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\controller");
        file.mkdirs();

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createController(dbClass, databaseService);
        }
    }

    public static void createController(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName() + "Controller";
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\controller\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".controller";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService));
            writer.write("@RestController\n" +
                    "@RequestMapping(\"/api/" + makeUncapital(dbClass.getName())+ "\")\n" +
                    "public class " + classFileName + " {\n\n");
            writer.write(createControllerFields(dbClass) + "\n");
            writer.write(createControllerConstructor(dbClass) + "\n");
            writer.write(createDataBinder(dbClass) + "\n");

            List<DBClassField> formDataFields = getFormDataClassFields(dbClass);

            if (!formDataFields.isEmpty()) {
                writer.write(createFormDataMethod(dbClass, formDataFields) + "\n");
            }

            writer.write(createSaveMethod(dbClass) + "\n");

            writer.write(createUpdateMethod(dbClass) + "\n");

            writer.write(createDeleteMethod(dbClass) + "\n");

            writer.write(createGetListMethod(dbClass) + "\n");

            if (!dbClass.getDetailFieldList().isEmpty()) {
                writer.write(createGetDeatailItemMethod(dbClass) + "\n");
            } else {
                writer.write(createGetListItemMethod(dbClass) + "\n");
            }

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createGetListItemMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\t@GetMapping(\"/{id}\")\n" +
                "\tpublic ResponseEntity<" + className + "ListItem> get" + className + "(@PathVariable Long id) {\n" +
                "\t\treturn new ResponseEntity<>(" + makeUncapital(className) + "Service.get" + className + "ListItem(id), HttpStatus.OK);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createGetDeatailItemMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\t@GetMapping(\"/{id}\")\n" +
                "\tpublic ResponseEntity<" + className + "DetailItem> get" + className + "(@PathVariable Long id) {\n" +
                "\t\treturn new ResponseEntity<>(" + makeUncapital(className) + "Service.get" + className + "DetailItem(id), HttpStatus.OK);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createGetListMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\t@GetMapping\n" +
                "\tpublic ResponseEntity<List<" + className + "ListItem>> get" + className + "List() {\n" +
                "\t\treturn new ResponseEntity<>(" + makeUncapital(className) + "Service.get" + className + "ListItemList(), HttpStatus.OK);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createDeleteMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\t@DeleteMapping(\"/{id}\")\n" +
                "\tpublic ResponseEntity<List<" + className + "ListItem>> delete" + className + "(@PathVariable Long id) {\n" +
                "\t\tboolean isDeleteSuccessful = " + makeUncapital(className) + "Service.delete" + className + "(id);\n" +
                " \t\tResponseEntity<List<" + className + "ListItem>> result;\n" +
                "\t\tif (isDeleteSuccessful) {\n" +
                "\t\t\tresult = new ResponseEntity<>(" + makeUncapital(className) + "Service.get" + className + "ListItemList(), HttpStatus.OK);\n" +
                "\t\t} else {\n" +
                "\t\t\tresult = new ResponseEntity<>(HttpStatus.NOT_FOUND);\n" +
                "\t\t}\n" +
                " \t\treturn result;\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createUpdateMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\t@PutMapping(\"/{id}\")\n" +
                "\tpublic ResponseEntity<" + className + "CreateItem> update" + className +
                "(@Valid @RequestBody " + className + "CreateItem " + makeUncapital(className) + "CreateItem, @PathVariable Long id) {\n" +
                "\t\tBoolean " + makeUncapital(className) + "IsUpdated = " + makeUncapital(className) +"Service.update" + className + "(" + makeUncapital(className) + "CreateItem, id);\n" +
                "\t\treturn " + makeUncapital(className) + "IsUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String  createSaveMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\t@PostMapping\n" +
                "\tpublic ResponseEntity<Void> save" + className + "(@Valid @RequestBody " + className + "CreateItem " + makeUncapital(className) + "CreateItem) {\n" +
                "\t\t" + makeUncapital(className) + "Service.save" + className + "(" + makeUncapital(className) + "CreateItem);\n" +
                "        return new ResponseEntity<>(HttpStatus.CREATED);\n" +
                "    }\n");
        return result.toString();
    }


    private static String createFormDataMethod(DBClass dbClass, List<DBClassField> formDataFields) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\t@GetMapping(\"/formData\")\n" +
                "\tpublic ResponseEntity<" + className + "FormData> get" + className + "FormData() {\n");
        for (DBClassField formDataField : formDataFields) {
            result.append("\t\tList<" + formDataField.getType() + "> " + makeUncapital(formDataField.getType()) + "List = this.");
            if (formDataField.getOtherClassName() == null) {
                result.append(makeUncapital(dbClass.getName()));
            } else {
                result.append(makeUncapital(formDataField.getOtherClassName()));
            }
            result.append("Service.get" + formDataField.getType() + "List();\n");
        }
        result.append("\t\t" + className + "FormData " + makeUncapital(className) + "FormData = new " + className + "FormData(");
        for (int i = 0; i < formDataFields.size(); i++) {
            DBClassField formDataField = formDataFields.get(i);
            result.append(makeUncapital(formDataField.getType()) + "List");
            if (i != formDataFields.size() - 1) {
                result.append(", ");
            }
        }
        result.append(");\n");
        result.append("\t\treturn new ResponseEntity<>(" + makeUncapital(className) + "FormData, HttpStatus.OK);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createDataBinder(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("    @InitBinder(\"" + dbClass.getName() + "CreateItem\")\n" +
                "    protected void initBinder(WebDataBinder binder) {\n" +
                "        binder.addValidators(validator);\n" +
                "    }\n");
        return result.toString();
    }

    private static String createControllerConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("    @Autowired\n" +
                "    public " + dbClass.getName() + "Controller(" +
                dbClass.getName() + "Service " + makeUncapital(dbClass.getName()) + "Service, ");
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append(otherClassName + "Service " + makeUncapital(otherClassName) + "Service, ");
        }
        result.append(dbClass.getName() + "CreateItemValidator validator) {\n");

        result.append("\t\tthis." + makeUncapital(dbClass.getName()) + "Service = " +  makeUncapital(dbClass.getName()) + "Service;\n");
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append("\t\tthis." + makeUncapital(otherClassName) + "Service = " +  makeUncapital(otherClassName) + "Service;\n");
        }
        result.append("        this.validator = validator;\n" +
                "    }\n");
        return result.toString();
    }

    private static String createControllerFields(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\tprivate " + dbClass.getName() + "Service " + makeUncapital(dbClass.getName()) + "Service;\n");
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append("\tprivate " + otherClassName + "Service " + makeUncapital(otherClassName) + "Service;\n");
        }
        result.append(("\tprivate " + dbClass.getName() + "CreateItemValidator validator;\n"));
        return result.toString();
    }

    public static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();

        result.append("import javax.validation.Valid;\n\n" +
                "import " + databaseService.getProjectName() + ".validator." + dbClass.getName() + "CreateItemValidator;\n");

        result.append("import " + databaseService.getProjectName() + ".dto.*;\n" +
                "import " + databaseService.getProjectName() + ".service." + dbClass.getName() + "Service;\n");

        for (String OtherClassName : dbClass.getOtherClassNameSet()) {
            result.append("import " + databaseService.getProjectName() + ".service." + OtherClassName + "Service;\n");
        }
        result.append("\n");
        result.append("import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.web.bind.WebDataBinder;\n" +
                "import org.springframework.web.bind.annotation.*;\n\n" +
                "import java.util.*;\n\n");

        return result.toString();
    }

    public static List<DBClassField> getFormDataClassFields(DBClass dbClass) {
        List<DBClassField> formDataFields = new ArrayList<>();
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.getType().equals("Enum")) {
                String enumOptionClassName = makeCapital(dbClassField.getEnumName()) + "Option";
                String fieldName = dbClassField.getName();
                formDataFields.add(new DBClassField(fieldName, enumOptionClassName));
            } else if (dbClassField.getType().equals("Other Class")) {
                String shortListItemClassName = dbClassField.getOtherClassName() + "ShortListItem";
                String fieldName = makeUncapital(dbClassField.getOtherClassName());
                DBClassField otherClassFormDataField = new DBClassField(fieldName, shortListItemClassName);
                otherClassFormDataField.setOtherClassName(dbClassField.getOtherClassName());
                formDataFields.add(otherClassFormDataField);
            }
        }
        return formDataFields;
    }
}
