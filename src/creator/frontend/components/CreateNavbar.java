package creator.frontend.components;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateNavbar {

    private CreateNavbar() {
    }

    public static void createNavbar(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\navbar");
        file.mkdirs();

        createNavbarCss(databaseService);
        createNavbarHtml(databaseService);
        createNavbarTypeScript(databaseService);
    }

    private static void createNavbarTypeScript(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\navbar\\navbar.component.ts");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("import { Component } from '@angular/core';\n" +
                    "\n" +
                    "@Component({\n" +
                    "  selector: 'app-navbar',\n" +
                    "  templateUrl: './navbar.component.html',\n" +
                    "  styleUrls: ['./navbar.component.css']\n" +
                    "})\n" +
                    "export class NavbarComponent {\n" +
                    "\n" +
                    "}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createNavbarHtml(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\navbar\\navbar.component.html");

        String[] projectNameArray = databaseService.getProjectName().split("\\.");
        String mainTitle = projectNameArray[projectNameArray.length-1];

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<nav class=\"navbar fixed-top navbar-expand-lg navbar-dark bg-dark\">\n" +
                    "\t<a id=\"nav-index\" class=\"navbar-brand\" routerLink=\"/\">" + mainTitle + "</a>\n" +
                    "\t<button type=\"button\" class=\"navbar-toggler collapsed\" data-toggle=\"collapse\" data-target=\"#navbar\"\n" +
                    "\t\t\t\t\taria-expanded=\"false\" aria-controls=\"navbar\" aria-label=\"Toggle navigation\">\n" +
                    "\t\t<span class=\"navbar-toggler-icon\"></span>\n" +
                    "\t</button>\n" +
                    "\n" +
                    "\t\t<div id=\"navbar\" class=\"navbar-collapse collapse\">\n" +
                    "\t\t\t<ul class=\"navbar-nav mr-auto mt-2 mt-lg-0\">\n\n");

            writer.write(createMenuItems(databaseService) + "\n");

            writer.write("\t\t</ul>\n" +
                    "\t</div>\n" +
                    "</nav>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createNavbarCss(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\navbar\\navbar.component.css");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createMenuItems(DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            String className = dbClass.getName();
            result.append("\t\t\t<li class=\"nav-item\">\n" +
                    "\t\t\t\t<a class=\"nav-link\" id=\"" + makeUncapital(className) + "-form-link\" routerLink=\"/" + makeUncapital(className) + "-form\" routerLinkActive=\"active\">New " + className + "</a>\n" +
                    "\t\t\t</li>\n" +
                    "\t\t\t<li class=\"nav-item\">\n" +
                    "\t\t\t\t<a class=\"nav-link\" id=\"" + makeUncapital(className) + "-list-link\" routerLink=\"/" + makeUncapital(className) + "-list\" routerLinkActive=\"active\">" + className + " list</a>\n" +
                    "\t\t\t</li>");
        }
        return result.toString();
    }


}
