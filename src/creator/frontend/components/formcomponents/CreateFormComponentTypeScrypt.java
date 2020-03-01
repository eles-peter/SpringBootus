package creator.frontend.components.formcomponents;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.StringBuherator.*;

public class CreateFormComponentTypeScrypt {

    private CreateFormComponentTypeScrypt() {
    }

    public static void createFormComponentTypeScrypt(DBClass dbClass, DatabaseService databaseService) {

        String dirName = makeUncapital(dbClass.getName()) + "-form";
        File file = new File(databaseService.getFrontendAppDirectory() + "\\components\\" + dirName + "\\" + dirName + ".component.ts");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(adddImports(dbClass) + "\n");

            writer.write(createClassLineWithAnnotations(dbClass) + "\n");

            writer.write(createFields(dbClass) + "\n");

            writer.write(createConstructor(dbClass) + "\n");

            writer.write(createOnInitMethod(dbClass) + "\n");

            writer.write(createCreateItemMethod(dbClass) + "\n");

            writer.write(createUpdateItemMethod(dbClass) + "\n");

            writer.write(createOnSubmitMethod(dbClass) + "\n");

            writer.write(createGetCreateDataMethod(dbClass) + "\n");

            for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
                if (dbClassField.getType().equals("Enum") && dbClassField.isList()) {
                    writer.write(createCreateCheckboxControlMethod(dbClassField, dbClass) + "\n");
                    writer.write(createCreateArrayToSendMethod(dbClassField, dbClass) + "\n");
                    writer.write(createCreateFormArrayMethod(dbClassField, dbClass) + "\n");
                }
            }

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createCreateFormArrayMethod(DBClassField dbClassField, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tcreate" + makeCapital(dbClassField.getName()) + "FormArray = (" + dbClassField.getName() + "Names: string[]) => {\n");
        result.append("\t\treturn this." + makeUncapital(dbClassField.getEnumName()) + "Option.map(" + dbClassField.getName() + " => {\n");
        result.append("\t\t\treturn " + dbClassField.getName() + "Names.includes(" + dbClassField.getName() + ".name);\n");
        result.append("\t\t\t}\n" +
                "\t\t);\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createCreateArrayToSendMethod(DBClassField dbClassField, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        result.append("\tprivate create" + makeCapital(dbClassField.getName()) + "ArrayToSend(): string[] {\n");
        result.append("\t\treturn this." + makeUncapital(className) + "Form.value." + dbClassField.getName() + "\n");
        result.append("\t\t\t.map((" + dbClassField.getName() + ", index) => " + dbClassField.getName() + " ? this." + makeUncapital(dbClassField.getEnumName()) + "Option[index].name : null)\n");
        result.append("\t\t\t.filter(" + dbClassField.getName() + " => " + dbClassField.getName() + " !== null);\n");
        result.append("\t\t}\n");

        return result.toString();
    }

    private static String createCreateCheckboxControlMethod(DBClassField dbClassField, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);        result.append("\tprivate create" + makeCapital(dbClassField.getName()) + "CheckboxControl() {\n");
        result.append("\t\tthis." + makeUncapital(dbClassField.getEnumName()) + "Option.forEach(() => {\n");
        result.append("\t\t\t\tconst control = new FormControl(false);\n");
        result.append("\t\t\t\t(this." + className + "Form.controls." + dbClassField.getName() + " as FormArray).push(control);\n");
        result.append("\t\t\t}\n" +
                "\t\t);\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createGetCreateDataMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tget" + className + "CreateData = (id: string) => {\n");
        result.append("\t\tthis." + instanceName + "Service." + instanceName + "ForUpdate(id).subscribe(\n");
        result.append("\t\t\t(response: " + className + "CreateItemModel) => {\n");
        result.append("\t\t\t\tthis.orcForm.patchValue({\n");
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (!(dbClassField.getType().equals("Enum") && dbClassField.isList())) {
                result.append("\t\t\t\t\t" + dbClassField.getName() + ": response." + dbClassField.getName() + ",\n");
            } else {
                result.append("\t\t\t\t\t" + dbClassField.getName() + ": this.create" + makeCapital(dbClassField.getName()) + "FormArray(response." + dbClassField.getName() + "),\n");
            }
            //TODO megírni, Other Class listára is!!!!
        }
        result.append("\t\t\t\t});\n" +
                "\t\t\t},\n" +
                "\t\t);\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createOnSubmitMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tonSubmit() {\n");
        result.append("\t\tconst data = {...this." + instanceName + "Form.value};\n");

        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.getType().equals("Enum") && dbClassField.isList()) {
                result.append("\t\tdata." + dbClassField.getName() + " = this.create" + makeCapital(dbClassField.getName()) + "ArrayToSend();");
            }
            //TODO megírni otherClass List-re is!!!
        }
        result.append("\t\tthis.id ? this.update" + className + "(data) : this.create" + className + "(data);\n");
        result.append("\t}\n");
        return result.toString();
    }

    private static String createUpdateItemMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tprivate update" + className + "(data: " + className + "CreateItemModel) {\n");
        result.append("\t\tthis." + instanceName + "Service.update" + className + "(data, this.id).subscribe(\n");
        result.append("\t\t\t() => {\n" +
                "\t\t\t\tthis.router.navigate(['/" + instanceName + "-list']);\n" +
                "\t\t\t},");
        result.append("\t\t\terror => console.warn(error),\n");
        result.append("\t\t);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createCreateItemMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tprivate create" + className + "(data: " + className + "CreateItemModel) {\n");
        result.append("\t\tthis." + instanceName + "Service.create" + className + "(data).subscribe(\n");
        result.append("\t\t\t() => {\n" +
                "\t\t\t\tthis.router.navigate(['/" + instanceName + "-list']);\n" +
                "\t\t\t},");
        result.append("\t\t\terror => console.warn(error),\n");
        result.append("\t\t);\n" +
                "\t}\n");
        return result.toString();
    }

    private static String createOnInitMethod(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tngOnInit() {\n");
        result.append("\t\tthis." + instanceName + "Service.get" + className + "FormData().subscribe(\n");
        result.append("\t\t\t(" + instanceName + "FormData: " + className + "FormDataModel) => {\n");

        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (dbClassField.getType().equals("Enum")) {
                result.append("\t\t\t\tthis." + makeUncapital(dbClassField.getEnumName()) + "Option = "
                        + instanceName + "FormData." + dbClassField.getName() + ";\n");
                if (dbClassField.isList()) {
                    result.append("\t\t\t\tthis.create" + makeCapital(dbClassField.getName()) + "CheckboxControl();\n");
                }
            } else if (dbClassField.getType().equals("Other Class")) {
                String otherInstanceName = makeUncapital(dbClassField.getOtherClassName());
                result.append("\t\t\t\tthis." + otherInstanceName + "Option = " + instanceName + "FormData." + otherInstanceName + ";\n");
                //TODO megvizsgáni a nevet!!! Miért nem a fieldname van az OrcFormData-ban!!!
                //TODO Meg Írni otherclass listre is!!!
            }
        }
        result.append("\n");
        result.append("\t\t\t\tthis.route.paramMap.subscribe(\n" +
                "\t\t\t\t\tparamMap => {\n" +
                "\t\t\t\t\t\tconst editableId = paramMap.get('id');\n" +
                "\t\t\t\t\t\tif (editableId) {\n" +
                "\t\t\t\t\t\t\tthis.id = +editableId;\n" +
                "\t\t\t\t\t\t\tthis.get" + className + "CreateData(editableId);\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\terror => console.warn(error),\n" +
                "\t\t\t\t);\n");

        result.append("\t\t\t}\n" +
                "\t\t);\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\tconstructor(private " + instanceName + "Service: " + className + "Service, private route: ActivatedRoute, private router: Router) {\n");
        result.append("\t\tthis." + instanceName + "Form = new FormGroup({\n");
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (!dbClassField.isList()) {
                switch (dbClassField.getType()) {
                    case "String":
                    case "Enum":
                    case "Image URL":
                    case "Text Area":
                        result.append("\t\t\t\t'" + dbClassField.getName() + "': new FormControl(''),\n");
                        break;
                    case "Integer":
                    case "Long":
                    case "Double":
                        result.append("\t\t\t\t'" + dbClassField.getName() + "': new FormControl(null),\n");
                        break;
                    case "Other Class":
                        result.append("\t\t\t\t'" + dbClassField.getName() + "Id': new FormControl(null),\n");
                        break;
                    case "Boolean":
                        result.append("\t\t\t\t'" + dbClassField.getName() + "': new FormControl(false),\n");
                        break;
                    case "Date":
                        result.append("\t\t\t\t'" + dbClassField.getName() + "': new FormControl(Date()),\n");
                        break;
                    default:
                        result.append("\t\t\t\t'" + dbClassField.getName() + "': new FormControl(''),\n");
                }
            } else {
                if (!dbClassField.getType().equals("Other Class")) {
                    result.append("\t\t\t\t'" + dbClassField.getName() + "': new FormArray([]),\n");
                } else {
                    result.append("\t\t\t\t'" + dbClassField.getName() + "Id': new FormArray([]),\n");
                }

            }
        }
        result.append("\t\t\t}\n" +
                "\t\t);\n" +
                "\t}\n");

        return result.toString();
    }

    private static String createFields(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("\t" + instanceName + "Form: FormGroup;\n");
        for (String enumName : dbClass.getEnumNameSet()) {
            result.append("\t" + makeUncapital(enumName) + "Option: " + enumName + "OptionModel[];\n");
        }
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append("\t" + makeUncapital(otherClassName) + "Option: " + otherClassName + "ShortListItemModel[];\n");
        }
        result.append("\tprivate id: number;\n");

        return result.toString();
    }

    private static String createClassLineWithAnnotations(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("@Component({\n" +
                "\tselector: 'app-" + instanceName + "-form',\n" +
                "\ttemplateUrl: './" + instanceName + "-form.component.html',\n" +
                "\tstyleUrls: ['./" + instanceName + "-form.component.css']\n" +
                "})\n");
        result.append("export class " + className + "FormComponent implements OnInit {\n");

        return result.toString();
    }

    private static String adddImports(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String className = dbClass.getName();
        String instanceName = makeUncapital(className);
        result.append("import {Component, OnInit} from '@angular/core';\n" +
                "import {FormArray, FormControl, FormGroup} from \"@angular/forms\";\n" +
                "import {ActivatedRoute, Router} from \"@angular/router\";\n");
        result.append("import {" + className + "Service} from \"../../services/" + instanceName + ".service\";\n");
        boolean isFormDataModel = false;
        for (String enumName : dbClass.getEnumNameSet()) {
            result.append("import {" + enumName + "OptionModel} from \"../../models/" + enumName + "Option.model\";\n");
            isFormDataModel = true;
        }
        for (String otherClassName : dbClass.getOtherClassNameSet()) {
            result.append("import {" + otherClassName + "ShortListItemModel} from \"../../models/" + otherClassName + "ShortListItem.model\";\n");
            isFormDataModel = true;
        }
        if (isFormDataModel) {
            result.append("import {" + className + "FormDataModel} from \"../../models/" + className + "FormData.model\";\n");
        }
        result.append("import {" + className + "CreateItemModel} from \"../../models/" + className + "CreateItem.model\";\n");

        return result.toString();
    }
}
