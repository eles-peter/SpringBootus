package creator.frontend.services;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateFrontendServices {

    private CreateFrontendServices() {
    }

    public static void createFrontendServices(DatabaseService databaseService) {
        File file = new File(databaseService.getFrontendAppDirectory() + "\\services");
        file.mkdirs();

        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createFrontendService(dbClass, databaseService);
        }
    }

    private static void createFrontendService(DBClass dbClass, DatabaseService databaseService) {
        String fileName = makeCapital(dbClass.getName()) + ".service";
        String serviceName = makeCapital(dbClass.getName()) + "Service";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\services\\" + fileName + ".ts");

        try (FileWriter writer = new FileWriter(file)) {

            String className = dbClass.getName();

            writer.write(addImports(dbClass) + "\n");

            writer.write("const BASE_URL = 'http://localhost:8080/api/" + makeUncapital(dbClass.getName()) + "';\n\n");
            writer.write("@Injectable({\n" +
                    "\tprovidedIn: 'root'\n" +
                    "})\n" +
                    "export class " + serviceName + " {\n\n");
            writer.write("\tconstructor(private http: HttpClient) { }\n\n");

            writer.write(createGetListMethod(className) + "\n");

            writer.write(createDeleteMethod(className) + "\n");

            writer.write(createCreateMethod(className) + "\n");

            if (isFormData(dbClass)) {
                writer.write(createGetFormDataMethod(className) + "\n");
            }

            writer.write(createUpdateMethod(className) + "\n");

            writer.write(createGetForUpdateMethod(className) + "\n");

            writer.write(createGetMethod(className, dbClass) + "\n");

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createGetMethod(String className, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        if (!dbClass.getDetailFieldList().isEmpty()) {
            result.append("\t" + makeUncapital(className) + "Detail(id: string): Observable<" + className + "DetailItemModel> {\n" +
                    "\treturn this.http.get<" + className + "DetailItemModel>(BASE_URL + '/' + id);\n" +
                    "\t}\n");
        } else {
            result.append("\t" + makeUncapital(className) + "Detail(id: string): Observable<" + className + "ListItemModel> {\n" +
                    "\treturn this.http.get<" + className + "ListItemModel>(BASE_URL + '/' + id);\n" +
                    "\t}\n");
        }
        return result.toString();
    }

    private static String createGetForUpdateMethod(String className) {
        StringBuilder result = new StringBuilder();
        result.append("\t" + makeUncapital(className) + "ForUpdate(id: string): Observable<" + className + "CreateItemModel> {\n" +
                "\t\treturn this.http.get<" + className + "CreateItemModel>(BASE_URL + '/formData/' + id);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createUpdateMethod(String className) {
        StringBuilder result = new StringBuilder();
        result.append("\tupdate" + className + "(data: " + className + "CreateItemModel, id: number): Observable<any> {\n" +
                "\t\tdata.id = id;\n" +
                "\t\treturn this.http.put(BASE_URL + '/' + id, data);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createGetFormDataMethod(String className) {
        StringBuilder result = new StringBuilder();
        result.append("\tget" + className + "FormData(): Observable<" + className + "FormDataModel> {\n" +
                "\t\treturn this.http.get<" + className + "FormDataModel>(BASE_URL + '/formData');\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createCreateMethod(String className) {
        StringBuilder result = new StringBuilder();
        result.append("\tcreate" + className + "(data: " + className + "CreateItemModel): Observable<any> {\n" +
                "\t\treturn this.http.post(BASE_URL, data);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createDeleteMethod(String className) {
        StringBuilder result = new StringBuilder();
        result.append("\tdelete" + className + "(id: number): Observable<Array<" + className + "ListItemModel>> {\n" +
                "\t\treturn this.http.delete<Array<" + className + "ListItemModel>>(BASE_URL + '/' + id);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createGetListMethod(String className) {
        StringBuilder result = new StringBuilder();
        result.append("\tlist" + className + "(): Observable<Array<" + className + "ListItemModel>> {\n" +
                "\t\treturn this.http.get<Array<" + className + "ListItemModel>>(BASE_URL);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String addImports(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("import { Injectable } from '@angular/core';\n" +
                "import {HttpClient} from \"@angular/common/http\";\n" +
                "import {Observable} from \"rxjs\";\n");
        String className = dbClass.getName();
        result.append("import {" + className +"ListItemModel} from \"../models/" + className + "ListItem.model\";\n" +
                "import {" + className + "CreateItemModel} from \"../models/" + className + "CreateItem.model\";\n");

        if (isFormData(dbClass)) {
            result.append("import {" + className + "FormDataModel} from \"../models/" + className + "FormData.model\";\n");
        }

        if (!dbClass.getDetailFieldList().isEmpty()) {
            result.append("import {" + className + "DetailItemModel} from \"../models/" + className + "DetailItem.model\";\n");
        }
        return result.toString();
    }

    public static boolean isFormData(DBClass dbClass) {
        boolean result = false;
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.getType().equals("Enum") || dbClassField.getType().equals("Other Class")) {
                result = true;
            }
        }
        return result;
    }




}
