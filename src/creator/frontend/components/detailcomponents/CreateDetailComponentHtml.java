package creator.frontend.components.detailcomponents;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;

import static creator.utils.StringBuherator.makeSentence;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateDetailComponentHtml {

    private CreateDetailComponentHtml() {
    }

    public static void createDetailComponentHtml(DBClass dbClass, DatabaseService databaseService) {
        String dirName = makeUncapital(dbClass.getName()) + "-detail";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.html");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write("<h2>" + dbClass.getName() + "-detail</h2>\n" +
                    "<br>\n");


            for (DBClassField dbClassField : dbClass.getDetailFieldList()) {
                if (!dbClassField.isList()) {
                    switch (dbClassField.getType()) {
                        case "String":
                        case "Text Area":
                        case "Integer":
                        case "Long":
                        case "Double":
                        case "Enum":
                        case "Date Time":
                            writer.write(createTextOutput(dbClassField, dbClass) + "\n");
                            break;
                        case "Image URL":
                            writer.write(createImageOutput(dbClassField, dbClass) + "\n");
                            break;
                        case "Other Class":
                            writer.write(createOtherClassOutput(dbClassField, dbClass, databaseService) + "\n");
                            break;
                        case "Boolean":
                            //TODO megírni!!!
                            break;
                        default:
                            writer.write(createTextOutput(dbClassField, dbClass) + "\n");
                    }
                } else {
                    if (!dbClassField.getType().equals("Other Class")) {
                        writer.write(createTextOutput(dbClassField, dbClass) + "\n");
                    } else {
                        writer.write(createOtherClassListOutput(dbClassField, dbClass, databaseService) + "\n");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createOtherClassListOutput(DBClassField dbClassField, DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("<h5>\n" +
                "\t" + makeSentence(dbClassField.getName()) + ":\n" +
                "</h5>\n");

        result.append("<table id=\"" + dbClassField.getName() + "-list-table\" class=\"table table-striped\">\n");
        result.append("  <thead class=\"thead-dark\">\n" +
                "  <tr>\n");
        System.out.println("Classfield otherClass Name: " + dbClassField.getOtherClassName());
        for (DBClass aClass : databaseService.getDbclasslist()) {
            System.out.println("ClassNames in the DB: " + aClass.getName());
            if (aClass.getName().equals(dbClassField.getOtherClassName())) {
                System.out.println("It was match, choosed class: " + aClass.getName());
                DBClass otherClass = aClass;

                //TODO it valami valamiért elbaszódik (NUllpointer exeption)

                for (DBClassField otherClassField : otherClass.getShortListFieldList()) {
                    result.append("\t\t<th>" + makeSentence(otherClassField.getName()) + "</th>\n");
                }
                result.append("  </tr>\n" +
                        "  </thead>\n" +
                        "\n" +
                        "  <tbody id=\"" + dbClassField.getName() + "-list-table-body\">\n");
                result.append("  <tr *ngFor=\"let " + makeUncapital(otherClass.getName()) + " of " + makeUncapital(dbClass.getName()) + "?." + dbClassField.getName() + "\">\n");
                for (DBClassField otherClassField : otherClass.getShortListFieldList()) {
                    result.append("    <td>{{ " + makeUncapital(otherClass.getName()) + "." + otherClassField.getName() + " }}</td>\n");
                }

            } else {
                System.out.println("it was NOT match !!!!!??????");
            }
        }

        result.append("  </tr>\n" +
                "  </tbody>\n" +
                "</table>\n");

        return result.toString();
    }

    private static String createOtherClassOutput(DBClassField dbClassField, DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("<h5>\n" +
                "\t" + makeSentence(dbClassField.getName()) + ":\n" +
                "</h5>\n");
        for (DBClass otherClass : databaseService.getDbclasslist()) {
            if (otherClass.getName().equals(dbClassField.getOtherClassName())) {
                result.append("<p style=\" white-space: pre-wrap;\">\n");
                for (DBClassField otherClassField : otherClass.getShortListFieldList()) {
                    result.append(" {{ " + makeUncapital(dbClass.getName()) + "." + dbClassField.getName() + "." + otherClassField.getName() + " }} ");
                }
            }
        }
        return result.toString();
    }


    private static String createImageOutput(DBClassField dbClassField, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("<img [src]=\"" + makeUncapital(dbClass.getName()) + "." + dbClassField.getName() + "\" *ngIf=\"" + makeUncapital(dbClass.getName()) + "?." + dbClassField.getName() + "\" alt=\"" + dbClassField.getName() + "\" style=\"width: 100%\" >\n" +
                "<br>\n");
        return result.toString();
    }

    private static String createTextOutput(DBClassField dbClassField, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("<h5>\n" +
                "\t" + makeSentence(dbClassField.getName()) + ":\n" +
                "</h5>");
        result.append("<p style=\" white-space: pre-wrap;\">\n" +
                "  {{" + makeUncapital(dbClass.getName()) + "?." + dbClassField.getName() + "}}\n" +
                "</p>\n" +
                "<br>\n");
        return result.toString();
    }


}
