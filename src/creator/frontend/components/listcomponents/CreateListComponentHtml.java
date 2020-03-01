package creator.frontend.components.listcomponents;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeSentence;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateListComponentHtml {

    private CreateListComponentHtml() {
    }

    public static void createListComponentHtml(DBClass dbClass, DatabaseService databaseService) {

        String dirName = makeUncapital(dbClass.getName()) + "-list";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.html");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<h2>" + dbClass.getName() + " list</h2>\n" +
                    "\n" +
                    "<table id=\"list-table\" class=\"table table-striped\">\n\n");

            writer.write(createTableHead(dbClass) + "\n");

            writer.write(createTableBody(dbClass, databaseService) + "\n");

            writer.write("</table>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createTableBody(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\t<tbody id=\"" + instanceName + "-list-table-body\">\n" +
                "\t<tr *ngFor=\"let " + instanceName + " of " + instanceName + "List\">\n");
        for (DBClassField dbClassField : dbClass.getListFieldList()) {
            if (dbClassField.getType().equals("Other Class")) {
                for (DBClass otherClass : databaseService.getDbclasslist()) {
                    if (otherClass.getName().equals(dbClassField.getOtherClassName())) {
                        if (!otherClass.getDetailFieldList().isEmpty()) {
                            result.append(
                                    createGoToOtherClassDetailLink(instanceName, dbClassField, otherClass));
                        } else {
                            result.append(
                                    createGoToOtherClassListLink(instanceName, dbClassField, otherClass));
                        }
                    }
                }
            } else if (dbClassField.getType().equals("Other Class")) {
                result.append(
                        createImageItem(instanceName, dbClassField, dbClass));
            } else {
                result.append("\t\t<td>");
                result.append("{{ " + instanceName + "." + dbClassField.getName() + " }}");
                result.append("</td>\n");
            }
        }
        return result.toString();
    }

    private static String createImageItem(String instanceName, DBClassField dbClassField, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\t\t<td>");
        result.append("<img [src]=\"" + instanceName + "." + dbClassField.getName() + "\" alt=\"\" height=100 width=100></img>");
        result.append("</td>\n");
        return result.toString();
    }

    private static String createGoToOtherClassListLink(String instanceName, DBClassField dbClassField, DBClass otherClass) {
        StringBuilder result = new StringBuilder();
        result.append("\t\t<td class=\"btn btn-link\" ");
        result.append("(click)=\"goTo" + otherClass.getName() + "List()\">");
        for (int i = 0; i < otherClass.getShortListFieldList().size(); i++) {
            DBClassField otherClassShortListField = otherClass.getShortListFieldList().get(i);
            result.append("{{ " + instanceName + "." + dbClassField.getName() + "." + otherClassShortListField.getName() + " }}");
            if (i < otherClass.getShortListFieldList().size() - 1) {
                result.append(" - ");
            }
        }
        result.append("</td>\n");

        return result.toString();
    }

    private static String createGoToOtherClassDetailLink(String instanceName, DBClassField dbClassField, DBClass otherClass) {
        StringBuilder result = new StringBuilder();
        result.append("\t\t<td class=\"btn btn-link\" ");
        result.append("(click)=\"goTo" + otherClass.getName() + "Detail(" + instanceName + "." + dbClassField.getName() + ".id)\">");
        for (int i = 0; i < otherClass.getShortListFieldList().size(); i++) {
            DBClassField otherClassShortListField = otherClass.getShortListFieldList().get(i);
            result.append("{{ " + instanceName + "." + dbClassField.getName() + "." + otherClassShortListField.getName() + " }}");
            if (i < otherClass.getShortListFieldList().size() - 1) {
                result.append(" - ");
            }
        }
        result.append("</td>\n");

        return result.toString();
    }

    private static String createTableHead(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\t<thead class=\"thead-dark\">\n" +
                "\t<tr>\n");
        for (DBClassField dbClassField : dbClass.getListFieldList()) {
            result.append("\t\t<th>");
            result.append(makeSentence(dbClassField.getName()));
            result.append("</th>\n");
        }
        result.append("\t</tr>\n" +
                "\t</thead>");

        return result.toString();
    }
}
