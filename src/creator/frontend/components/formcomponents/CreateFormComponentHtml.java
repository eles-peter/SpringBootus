package creator.frontend.components.formcomponents;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeSentence;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateFormComponentHtml {

    private CreateFormComponentHtml() {
    }

    public static void createFormComponentHtml(DBClass dbClass, DatabaseService databaseService) {

        String dirName = makeUncapital(dbClass.getName()) + "-form";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.html");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<h2>" + dbClass.getName() + " form</h2>\n" +
                    "<hr/>\n" +
                    "<form [formGroup]=\"" + makeUncapital(dbClass.getName()) + "Form\">");

            for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
                if (!dbClassField.isList()) {
                    switch (dbClassField.getType()) {
                        case "String":
                        case "Image URL":
                            writer.write(createTextInput(dbClassField) + "\n");
                            break;
                        case "Text Area":
                            //TODO megírni!!!
                            break;
                        case "Integer":
                        case "Long":
                        case "Double":
                            writer.write(createNumberInput(dbClassField) + "\n");
                            break;
                        case "Enum":
                            writer.write(createEnumInput(dbClassField) + "\n");
                            break;
                        case "Other Class":
                            writer.write(createOtherClassInput(dbClassField, databaseService) + "\n");
                            break;
                        case "Boolean":
                            //TODO megírni!!!
                            break;
                        case "Date":
                            //TODO megírni Date-re!!!
                            writer.write(createDateTimeInput(dbClassField) + "\n");
                            break;
                        default:
                            writer.write(createTextInput(dbClassField) + "\n");
                    }
                } else {
                    if (!dbClassField.getType().equals("Other Class")) {
                        writer.write(createCheckBoxInput(dbClassField) + "\n");
                    } else {
                        writer.write(createTextInput(dbClassField) + "\n");
                    }
                }
            }

            writer.write(createSubmitButton() + "\n");

            writer.write("</form>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createDateTimeInput(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        String fieldName = dbClassField.getName();
        result.append("  <div class=\"form-group\">\n" +
                "    <label for=\"" + fieldName + "\" class=\"form-control-label\">\n" +
                "      " + makeSentence(fieldName) + ":\n" +
                "    </label>\n" +
                "    <input\n" +
                "      type=\"datetime-local\"\n" +
                "      id=\"" + fieldName + "\"\n" +
                "      formControlName=\"" + fieldName + "\"\n" +
                "      name=\"" + fieldName + "\"\n" +
                "      placeholder=\"" + makeSentence(fieldName) + "\"\n" +
                "      class=\"form-control\"\n" +
                "    />\n" +
                "  </div>\n");

        return result.toString();
    }

    private static String createCheckBoxInput(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        String fieldName = dbClassField.getName();
        result.append("  <div class=\"form-group\">\n" +
                "    <label class=\"form-control-label\">\n" +
                "      " + makeSentence(fieldName) + ":\n" +
                "    </label>\n" +
                "    <div formArrayName=\"" + fieldName + "\" *ngFor=\"let " + fieldName + " of " + makeUncapital(dbClassField.getEnumName()) + "Option; let i = index\">\n" +
                "      <label>\n" +
                "        <input type=\"checkbox\" [formControlName]=\"i\">\n" +
                "        {{ " + fieldName + ".displayName }}\n" +
                "      </label>\n" +
                "    </div>\n" +
                "  </div>\n");

        return result.toString();
    }

    private static String createEnumInput(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        String fieldName = dbClassField.getName();
        result.append("  <div class=\"form-group\">\n" +
                "    <label for=\"" + fieldName + "\" class=\"form-control-label\">\n" +
                "      " + makeSentence(fieldName) + ":\n" +
                "    </label>\n" +
                "    <select\n" +
                "      id=\"" + fieldName + "\"\n" +
                "      formControlName=\"" + fieldName + "\"\n" +
                "      name=\"" + fieldName + "\"\n" +
                "      class=\"form-control\"\n" +
                "    >\n" +
                "      <option value=\"\" disabled>&#45;&#45; Please choose a(n) " + makeUncapital(makeSentence(fieldName)) + " &#45;&#45;</option>\n" +
                "      <option *ngFor=\"let " + fieldName + " of " + makeUncapital(dbClassField.getEnumName()) + "Option, let i = index\"\n" +
                "              value=\"{{" + fieldName + ".name}}\"> {{" + fieldName + ".displayName}} </option>\n" +
                "    </select>\n" +
                "  </div>\n");


        return result.toString();
    }

    private static String createOtherClassInput(DBClassField dbClassField, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String fieldName = dbClassField.getName() + "Id";
        for (DBClass otherClass : databaseService.getDbclasslist()) {
            if (dbClassField.getOtherClassName().equals(otherClass.getName())) {
                result.append("  <div class=\"form-group\">\n" +
                        "    <label for=\"" + fieldName + "\" class=\"form-control-label\">\n" +
                        "      " + makeSentence(dbClassField.getName()) + ":\n" +
                        "    </label>\n" +
                        "    <select\n" +
                        "      id=\"" + fieldName + "\"\n" +
                        "      name=\"" + fieldName + "\"\n" +
                        "      formControlName=\"" + fieldName + "\"\n" +
                        "      class=\"form-control\"\n" +
                        "    >\n" +
                        "      <option [value]=\"null\">&#45;&#45; Please choose a(n) "
                        + makeUncapital(makeSentence(dbClassField.getName())) + " &#45;&#45;</option>\n" +
                        "      <option *ngFor=\"let " + dbClassField.getName() + " of " + makeUncapital(otherClass.getName()) + "Option\"\n" +
                        "              value=\"{{" + dbClassField.getName() + ".id}}\">");
                for (DBClassField otherClassField : otherClass.getShortListFieldList()) {
                    result.append(" {{" + dbClassField.getName() + "." + otherClassField.getName() + "}} ");
                } //TODO megcsinálni rendesen kötőjellel leválasztva vagy mi...
                result.append("</option>\n" +
                        "    </select>\n" +
                        "  </div>\n");
            }
        }
        return result.toString();
    }

    private static String createNumberInput(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        String fieldName = dbClassField.getName();
        result.append("  <div class=\"form-group\">\n" +
                "    <label for=\"" + fieldName + "\" class=\"form-control-label\">\n" +
                "      " + makeSentence(fieldName) + ":\n" +
                "    </label>\n" +
                "    <input\n" +
                "      type=\"number\"\n" +
                "      id=\"" + fieldName + "\"\n" +
                "      formControlName=\"" + fieldName + "\"\n" +
                "      name=\"" + fieldName + "\"\n" +
                "      class=\"form-control\"\n" +
                "    />\n" +
                "  </div>\n");

        return result.toString();
    }

    private static String createTextInput(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        String fieldName = dbClassField.getName();
        result.append("  <div class=\"form-group\">\n" +
                "    <label for=\"" + fieldName + "\" class=\"form-control-label\">\n" +
                "      " + makeSentence(fieldName) + ":\n" +
                "    </label>\n" +
                "    <input\n" +
                "      type=\"text\"\n" +
                "      id=\"" + fieldName + "\"\n" +
                "      formControlName=\"" + fieldName + "\"\n" +
                "      name=\"" + fieldName + "\"\n" +
                "      placeholder=\"" + makeSentence(fieldName) + "\"\n" +
                "      class=\"form-control\"\n" +
                "    />\n" +
                "  </div>\n");

        return result.toString();
    }

    private static String createSubmitButton() {
        StringBuilder result = new StringBuilder();
        result.append("\t<button type=\"submit\" class=\"btn btn-success\" (click)=\"onSubmit()\">Submit</button>\n");
        return result.toString();
    }
}
