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

            writer.write("import { Component } from '@angular/core';\n" +
                    "\n" +
                    "@Component({\n" +
                    "\tselector: 'app-root',\n" +
                    "\ttemplateUrl: './app.component.html',\n" +
                    "\tstyleUrls: ['./app.component.css']\n" +
                    "})\n" +
                    "export class AppComponent {\n" +
                    "\ttitle = 'angular-frontend';\n" +
                    "}\n"); //TODO Mi az a title????

        } catch (IOException e) {
            e.printStackTrace();
        }
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
