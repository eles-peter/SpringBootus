package creator.frontend.components.detailcomponents;

import creator.DBClass;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeUncapital;

public class CreateDetailComponentTypeScrypt {

    private CreateDetailComponentTypeScrypt() {
    }

    public static void createDetailComponentTypeScrypt(DBClass dbClass, DatabaseService databaseService) {

        String dirName = makeUncapital(dbClass.getName()) + "-form";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.ts");

        try (FileWriter writer = new FileWriter(file)) {

            String instanceName = makeUncapital(dbClass.getName());

            writer.write("import { Component, OnInit } from '@angular/core';\n" +
                    "\n" +
                    "@Component({\n" +
                    "  selector: 'app-" + instanceName + "-detail',\n" +
                    "  templateUrl: './" + instanceName + "-detail.component.html',\n" +
                    "  styleUrls: ['./" + instanceName + "-detail.component.css']\n" +
                    "})\n" +
                    "export class " + dbClass.getName() + "DetailComponent implements OnInit {\n" +
                    "\n" +
                    "  constructor() { }\n" +
                    "\n" +
                    "  ngOnInit(): void {\n" +
                    "  }\n" +
                    "\n" +
                    "}\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
