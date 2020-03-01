package creator.frontend.app;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateAppComponent {

    private CreateAppComponent() {
    }

    public static void createAppComponent(DatabaseService databaseService) {
        createAppComponentCss(databaseService);
        createAppComponentHtml(databaseService);
        createAppComponentTypeScrypt(databaseService);
    }

    private static void createAppComponentTypeScrypt(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "app.component.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(addImports(databaseService) + "\n");

            writer.write(addRoutes(databaseService) + "\n");

            writer.write("@NgModule({\n" +
                    "  imports: [RouterModule.forRoot(routes)],\n" +
                    "  exports: [RouterModule]\n" +
                    "})\n" +
                    "export class AppRoutingModule { }\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addRoutes(DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("const routes: Routes = [\n");

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            String className = dbClass.getName();
            String instanceName = makeUncapital(className);
            result.append("\t{path: '" + instanceName + "-list', component: " + className + "ListComponent},\n");
            result.append("\t{path: '" + instanceName + "-form', component: " + className + "FormComponent},\n");
            result.append("\t{path: '" + instanceName + "-form/:id', component: " + className + "FormComponent},\n");
            if (!dbClass.getDetailFieldList().isEmpty()) {
                result.append("\t{path: '" + instanceName + "-detail/:id', component: " + className + "DetailComponent},\t");
            }
        }
        String firstClassNameUncapital = makeUncapital(databaseService.getDbclasslist().get(0).getName());
        result.append("\t{path: '', redirectTo: '" + firstClassNameUncapital + "-list', pathMatch: 'full'},\n");
        result.append("];\n");

        return result.toString();
    }

    private static String addImports(DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import { NgModule } from '@angular/core';\n" +
                "import { Routes, RouterModule } from '@angular/router';\n");

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            String className = dbClass.getName();
            String instanceName = makeUncapital(className);
            result.append("import {" +className + "ListComponent} " +
                    "from \"./components/" + instanceName + "-list/" + instanceName + "-list.component\";\n");
            result.append("import {" + className + "FormComponent} from " +
                    "\"./components/" + instanceName + "-form/" + instanceName + "-fomm.component\";");
            if (!dbClass.getDetailFieldList().isEmpty()) {
                result.append("import {" + className + "DetailComponent} " +
                        "from \"./components/" + instanceName + "-detail/" + instanceName + "-detail.component\";\n");
            }
        }
        return result.toString();
    }

    private static void createAppComponentHtml(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "app.component.html");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write("<div class=\"container app__container\">\n" +
                    "  <app-navbar></app-navbar>\n" +
                    "  <router-outlet></router-outlet>\n" +
                    "</div>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAppComponentCss(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "app.component.css");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(".app__container {\n" +
                    "  margin-top: 56px;\n" +
                    "  padding: 24px;\n" +
                    "}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
