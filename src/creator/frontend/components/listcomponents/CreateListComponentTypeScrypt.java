package creator.frontend.components.listcomponents;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateListComponentTypeScrypt {

    private CreateListComponentTypeScrypt() {
    }

    public static void createListComponentTypeScrypt(DBClass dbClass, DatabaseService databaseService) {

        String dirName = makeUncapital(dbClass.getName()) + "-list";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(adddImports(dbClass) + "\n");

            writer.write(createClassLineWithAnnotations(dbClass) + "\n");

            writer.write(createFields(dbClass) + "\n");

            writer.write(createConstructor(dbClass) + "\n");

            writer.write(createOnInitMethod(dbClass) + "\n");

            writer.write(createListMethod(dbClass) + "\n");

            writer.write(creaateDeleteMethod(dbClass) + "\n");

            writer.write(createUpdateMethod(dbClass) + "\n");

            writer.write(createDetailMethod(dbClass) + "\n");

            writer.write(createGoToOtherClassMethod(dbClass, databaseService));

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createGoToOtherClassMethod(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            for (DBClass otherClass : databaseService.getDbclasslist()) {
                if (otherClass.getName().equals(otherClassName)) {
                    if (!otherClass.getDetailFieldList().isEmpty()) {
                        result.append(
                                createGoToOtherClassDetailMethod(otherClassName) + "\n"
                        );
                    } else {
                        result.append(
                                createGoToOtherClassListMethod(otherClassName) + "\n"
                        );
                    }
                }
            }
        }
        return result.toString();
    }

    private static String createGoToOtherClassListMethod(String otherClassName) {
        StringBuilder result = new StringBuilder();
        result.append("\tgoTo" + otherClassName + "List() {\n" +
                "\t\tthis.router.navigate(['/" +  makeUncapital(otherClassName) + "-list])\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createGoToOtherClassDetailMethod(String otherClassName) {
        StringBuilder result = new StringBuilder();
        result.append("\tgoTo" + otherClassName + "Detail(id: number) {\n" +
                "\t\tthis.router.navigate(['/" +  makeUncapital(otherClassName) + "-detail/', id])\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createDetailMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\t" + instanceName + "Detail(id: number): void {\n" +
                "\t\tthis.router.navigate(['/" + instanceName + "-detail/', id])\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createUpdateMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tupdate" + className + "(id: number): void {\n" +
                "\t\tthis.router.navigate(['/" + instanceName + "-form/', id])\n" +
                "\t}\n");

        return result.toString();
    }

    private static String creaateDeleteMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tdelete" + className + "(id: number): void {\n" +
                "\t\tthis." + instanceName + "Service.delete" + instanceName + "(id).subscribe(\n" +
                "\t\t\t(response: " + className + "ListItemModel[]) => {\n" +
                "\t\t\t\tthis." + instanceName + "List = response;\n" +
                "\t\t\t},\n" +
                "\t\t\terror => console.warn(error),\n" +
                "\t\t);\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createListMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tlist" + className + " = () => {\n" +
                "\t\tthis." + instanceName + "Service.list" + className + "().subscribe(\n" +
                "\t\t\t(orcList: OrcListItemModel[]) => {\n" +
                "\t\t\t\tthis." + instanceName + "List = " + instanceName + "List;\n" +
                "\t\t\t}\n" +
                "\t\t);\n" +
                "\t};\n");

        return result.toString();
    }

    private static String createOnInitMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tngOnInit(): void {\n" +
                "\t\tthis.list" + className + "();\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tconstructor(private " + instanceName + "Service: " + className + "Service, private router: Router) { }\n");

        return result.toString();
    }

    private static String createFields(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\t" + instanceName + "List: " + className + "ListItemModel[] = [];\n");

        return result.toString();
    }

    private static String createClassLineWithAnnotations(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("@Component({\n" +
                "  selector: 'app-" + instanceName + "-list',\n" +
                "  templateUrl: './" + instanceName + "-list.component.html',\n" +
                "  styleUrls: ['./" + instanceName + "-list.component.css']\n" +
                "})\n" +
                "export class " + className + "ListComponent implements OnInit {");

        return result.toString();
    }

    private static String adddImports(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("import { Component, OnInit } from '@angular/core';\n" +
                "import {Router} from \"@angular/router\";\n" +
                "import {" + className + "Service} from \"../../services/" + instanceName + ".service\";\n" +
                "import {" + className + "ListItemModel} from \"../../models/" + className + "ListItem.model\";\n");

        return result.toString();
    }
}
