package creator.frontend.app;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateAppModules {

    private CreateAppModules() {
    }

    public static void createAppModules(DatabaseService databaseService) {

        createAppModule(databaseService);
        createAppRoutingModule(databaseService);
    }

    private static void createAppRoutingModule(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\app-routing.module.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(addImportsToRoutingModule(databaseService) + "\n");

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

    private static void createAppModule(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\app.module.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(addImportsToAppModule(databaseService) + "\n");

            writer.write("@NgModule({\n");

            writer.write(addDeclarations(databaseService));

            writer.write("\timports: [\n" +
                    "\t\tBrowserModule,\n" +
                    "\t\tAppRoutingModule,\n" +
                    "\t\tHttpClientModule,\n" +
                    "\t\tReactiveFormsModule\n" +
                    "\t],\n" +
                    "\tproviders: [],\n" +
                    "\tbootstrap: [AppComponent]\n" +
                    "})\n" +
                    "export class AppModule { }\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addDeclarations(DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("\tdeclarations: [\n" +
                "\t\tAppComponent,\n" +
                "\t\tNavbarComponent,\n");
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            result.append("\t\t" + dbClass.getName() + "FormComponent,\n");
            result.append("\t\t" + dbClass.getName() + "ListComponent,\n");
            if (!dbClass.getDetailFieldList().isEmpty()) {
                result.append("\t\t" + dbClass.getName() + "DetailComponent,");
            }
        }
        result.append("\t],\n");
        return result.toString();
    }

    private static String addImportsToAppModule(DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import { BrowserModule } from '@angular/platform-browser';\n" +
                "import { NgModule } from '@angular/core';\n");
        result.append("import { AppRoutingModule } from './app-routing.module';\n" +
                "import { AppComponent } from './app.component';\n" +
                "import {HttpClientModule} from \"@angular/common/http\";\n" +
                "import {ReactiveFormsModule} from \"@angular/forms\";\n" +
                "import { NavbarComponent } from './components/navbar/navbar.component';\n");

        result.append(addComponentImports(databaseService));

        return result.toString();
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
                result.append("\t{path: '" + instanceName + "-detail/:id', component: " + className + "DetailComponent},\n");
            }
        }
        String firstClassNameUncapital = makeUncapital(databaseService.getDbclasslist().get(0).getName());
        result.append("\t{path: '', redirectTo: '" + firstClassNameUncapital + "-list', pathMatch: 'full'},\n");
        result.append("];\n");

        return result.toString();
    }

    private static String addImportsToRoutingModule(DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import { NgModule } from '@angular/core';\n" +
                "import { Routes, RouterModule } from '@angular/router';\n");

        result.append(addComponentImports(databaseService));

        return result.toString();
    }

    private static String addComponentImports(DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            String className = dbClass.getName();
            String instanceName = makeUncapital(className);
            result.append("import {" +className + "ListComponent} " +
                    "from \"./components/" + instanceName + "-list/" + instanceName + "-list.component\";\n");
            result.append("import {" + className + "FormComponent} from " +
                    "\"./components/" + instanceName + "-form/" + instanceName + "-form.component\";\n");
            if (!dbClass.getDetailFieldList().isEmpty()) {
                result.append("import {" + className + "DetailComponent} " +
                        "from \"./components/" + instanceName + "-detail/" + instanceName + "-detail.component\";\n");
            }
        }
        return result.toString();
    }


}
