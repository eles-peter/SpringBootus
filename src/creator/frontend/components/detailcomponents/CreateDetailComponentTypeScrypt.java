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

        String dirName = makeUncapital(dbClass.getName()) + "-detail";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.ts");

        try (FileWriter writer = new FileWriter(file)) {

            String instanceName = makeUncapital(dbClass.getName());

            //TODO megírni rendesen

            writer.write("import { Component, OnInit } from '@angular/core';\n" +
                    "import {ActivatedRoute, Router} from \"@angular/router\";\n" +
                    "import {" + dbClass.getName() + "Service} from \"../../services/" + makeUncapital(dbClass.getName()) + ".service\";\n" +
                    "import {" + dbClass.getName() + "DetailItemModel} from \"../../models/" + dbClass.getName() + "DetailItem.model\";\n" +
                    "\n" +
                    "@Component({\n" +
                    "  selector: 'app-" + instanceName + "-detail',\n" +
                    "  templateUrl: './" + instanceName + "-detail.component.html',\n" +
                    "  styleUrls: ['./" + instanceName + "-detail.component.css']\n" +
                    "})\n" +
                    "export class " + dbClass.getName() + "DetailComponent implements OnInit {\n" +
                    "\n");

            writer.write("\t" + makeUncapital(dbClass.getName()) + ": " + dbClass.getName() + "DetailItemModel;\n\n");

            writer.write("\tconstructor(private  " + makeUncapital(dbClass.getName()) + "Service: " + dbClass.getName() + "Service, private route: ActivatedRoute, private router: Router) {}\n\n");

            writer.write("  ngOnInit(): void {\n" +
                    "    this.route.paramMap.subscribe(\n" +
                    "      paramMap => {\n" +
                    "        const itemId = paramMap.get('id');\n" +
                    "        if (itemId) {\n" +
                    "          this.get" + dbClass.getName() + "Detail(itemId);\n" +
                    "        }\n" +
                    "      },\n" +
                    "      error => console.warn(error),\n" +
                    "    );\n" +
                    "  }\n\n");

            writer.write("  get" + dbClass.getName() + "Detail = (itemId: string) => {\n" +
                    "    this." + makeUncapital(dbClass.getName()) + "Service." + makeUncapital(dbClass.getName()) + "Detail(itemId).subscribe(\n" +
                    "      (response: " + dbClass.getName() + "DetailItemModel) => {\n" +
                    "        this." + makeUncapital(dbClass.getName()) + " = response;\n" +
                    "      }\n" +
                    "    );\n" +
                    "  }\n\n");

            writer.write("  backToList() {\n" +
                    "    this.router.navigate(['/" + makeUncapital(dbClass.getName()) + "-list/'])\n" +
                    "  }\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
