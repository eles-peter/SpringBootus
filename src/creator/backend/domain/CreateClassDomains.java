package creator.backend.domain;

import creator.DBClass;
import creator.DBClassField;
import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static creator.utils.ClassBuherator.*;
import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class CreateClassDomains {

    public CreateClassDomains() {
    }

    public static void createClassDomains(DatabaseService databaseService) {
        for (DBClass dbClass : databaseService.getDbclasslist()) {
            createClassDomain(dbClass, databaseService);
        }
    }

    private static void createClassDomain(DBClass dbClass, DatabaseService databaseService) {
        String classFileName = dbClass.getName();
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\domain\\" + classFileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".domain";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n\n");
            writer.write(addImports(dbClass, databaseService));
            writer.write("@Entity\n");
            writer.write("@Table(name = \"" + dbClass.getSQLName() + "\")\n");
            writer.write("public class " + dbClass.getName() + " {\n\n");
            writer.write("    @Id\n" +
                    "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                    "    @Column(name = \"id\")\n" +
                    "    private Long id;\n\n");

            for (DBClassField dbClassField : dbClass.getFieldList()) {
                writer.write(addFieldAnnotation(dbClassField, dbClass));
                writer.write(makeFieldLine(dbClassField) + "\n\n");
            }
            writer.write(createEmptyConstructor(dbClass) + "\n");
            writer.write(createFullConstructor(dbClass) + "\n");
            writer.write(createCreateItemConstructor(dbClass) + "\n");

            writer.write(makeAllGetterIncludeIdGetter(dbClass) + "\n");

            writer.write(makeAllSetterIncludeIdSetter(dbClass) + "\n");

            //TODO write override methods (toString, equals, hashCode)

            writer.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String addImports(DBClass dbClass, DatabaseService databaseService) {
        StringBuilder result = new StringBuilder();
        result.append("import " + databaseService.getProjectName() + ".dto." + dbClass.getName() + "CreateItem;\n\n");

        result.append("import javax.persistence.*;\n");

        boolean isContainsList = false;
        boolean isContainsEnumList = false;
        for (DBClassField dbClassField : dbClass.getFieldList()) {
            if (dbClassField.isList()) {
                isContainsList = true;
                if (dbClassField.getType().equals("Enum")) {
                    isContainsEnumList = true;
                }
            }
        }
        if (isContainsList) {
            result.append("import java.util.ArrayList;\n" +
                    "import java.util.List;\n");
        }
        if (isContainsEnumList) {
            result.append("import java.util.stream.Collectors;\n");
        }
        result.append("\n");
        return result.toString();
    }

    public static String addFieldAnnotation(DBClassField dbClassField, DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        if (dbClassField.getType().equals("Other Class") && !dbClassField.isMany()) {
            result.append("    @OneToOne\n" +
                    "    @JoinColumn(name = \"" + dbClassField.getSQLName() + "_id\")\n");
        } else if (dbClassField.getType().equals("Other Class") && dbClassField.isMany() && dbClassField.getIsManyField().isBlank()) {
            result.append("    @ManyToOne\n" +
                    "    @JoinColumn(name = \"" + dbClassField.getSQLName() + "_id\")\n");
        } else if (dbClassField.getType().equals("Other Class") && dbClassField.isMany() && !dbClassField.getIsManyField().isBlank()) {
            result.append("    @OneToMany(mappedBy = \"" + dbClassField.getIsManyField() + "\", fetch = FetchType.EAGER)\n");
        } else if (dbClassField.getType().equals("Enum") && !dbClassField.isList()) {
            result.append("    @Enumerated(EnumType.STRING)\n" +
                    "    @Column(name = \"" + dbClassField.getSQLName() + "\")\n");
        } else if (dbClassField.getType().equals("Enum") && dbClassField.isList()) {
            result.append("    @Enumerated(EnumType.STRING)\n" +
                    "    @ElementCollection(targetClass = " + dbClassField.getEnumName() + ".class, fetch = FetchType.EAGER)\n" +
                    "    @CollectionTable(name = \"" + dbClass.getSQLName() + "_" + dbClassField.getSQLName() + "\")\n" +
                    "    @Column(name = \"" + dbClass.getSQLName() + "_" + dbClassField.getSQLName() + "\")\n");
        } else {
            result.append("    @Column(name = \"" + dbClassField.getSQLName() + "\")\n");
        }
        return result.toString();
    }

    private static String createCreateItemConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        String createItemClassName = dbClass.getName() + "CreateItem";
        String createItemName = makeUncapital(dbClass.getName()) + "CreateItem";
        result.append("\tpublic " + dbClass.getName() + "(" + createItemClassName + " " + createItemName + ") {\n");
        for (DBClassField dbClassField : dbClass.getCreateFieldList()) {
            if (!dbClassField.getType().equals("Other Class")) {
                result.append("\t\tthis." + dbClassField.getName() + " = ");

                if (dbClassField.getType().equals("Enum") && !dbClassField.isList()) {
                    result.append(dbClassField.getEnumName() + ".valueOf(" + createItemName + ".get" + makeCapital(dbClassField.getName()) + "());\n");
                } else if (dbClassField.getType().equals("Enum") && dbClassField.isList()) {
                    result.append(createItemName + ".get" + makeCapital(dbClassField.getName()) + "().stream()" +
                            ".map(" + dbClassField.getEnumName() + "::valueOf)" +
                            ".collect(Collectors.toList());\n");
                } else {
                    result.append(createItemName + ".get" + makeCapital(dbClassField.getName()) + "();\n");
                }
            }
        }
        result.append("\t}\n");
        return result.toString();
    }


}
