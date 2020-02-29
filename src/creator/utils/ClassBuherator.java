package creator.utils;

import creator.DBClass;
import creator.DBClassField;

import static creator.utils.StringBuherator.makeCapital;

public class ClassBuherator {

    private ClassBuherator() {
    }

    public static String makeFieldLine(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tprivate ");
        result.append(createFieldTypeWithName(dbClassField));
        if (dbClassField.isList()) {
            result.append(" = new ArrayList<>()");
        }
        result.append(";");
        return result.toString();
    }

    public static String makeFieldLineTypeAsString(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tprivate ");
        result.append(createFieldTypeAsStringWithName(dbClassField));
        if (dbClassField.isList()) {
            result.append(" = new ArrayList<>()");
        }
        result.append(";\n");
        return result.toString();
    }

    public static String makeFieldLineTypeAsShortListItem(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tprivate ");
        result.append(createFieldTypeAsShortListItemWithName(dbClassField));
        if (dbClassField.isList()) {
            result.append(" = new ArrayList<>()");
        }
        result.append(";\n");
        return result.toString();
    }


    public static String makeFieldLine(String fieldType, String fieldName, boolean isList) {
        StringBuilder result = new StringBuilder();
        result.append("\tprivate ");
        if (isList) {
            result.append("List<");
        }
        result.append(fieldType);
        if (isList) {
            result.append(">");
        }
        result.append(" " + fieldName);
        if (isList) {
            result.append(" = new ArrayList<>()");
        }
        result.append(";");
        return result.toString();
    }

    public static String makeGetter(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic ");
        if (dbClassField.isList()) {
            result.append("List<");
        }
        result.append(dbClassField.getRealType());
        if (dbClassField.isList()) {
            result.append(">");
        }
        result.append(" get");
        result.append(makeCapital(dbClassField.getName()));
        result.append("() {\n");

        result.append("\t\treturn " + dbClassField.getName() + ";\n");
        result.append("\t}\n");
        return result.toString();
    }


    public static String makeGetterAsString(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic ");
        if (dbClassField.isList()) {
            result.append("List<");
        }
        result.append("String");
        if (dbClassField.isList()) {
            result.append(">");
        }
        result.append(" get");
        result.append(makeCapital(dbClassField.getName()));
        result.append("() {\n");

        result.append("\t\treturn " + dbClassField.getName() + ";\n");
        result.append("\t}\n");
        return result.toString();
    }

    public static String makeGetterOtherClassAsShortListItem(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic ");
        if (dbClassField.isList()) {
            result.append("List<");
        }
        result.append(dbClassField.getOtherClassName() + "ShortListItem");
        if (dbClassField.isList()) {
            result.append(">");
        }
        result.append(" get");
        result.append(makeCapital(dbClassField.getName()));
        result.append("() {\n");

        result.append("\t\treturn " + dbClassField.getName() + ";\n");
        result.append("\t}\n");
        return result.toString();
    }

    public static String makeGetterAsList(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic ");
        result.append("List<");
        result.append(dbClassField.getRealType());
        result.append("> get");
        result.append(makeCapital(dbClassField.getName()));
        result.append("() {\n");
        result.append("\t\treturn " + dbClassField.getName() + "List;\n");
        result.append("\t}\n");
        return result.toString();
    }

    public static String makeAllGetterIncludeIdGetter(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("    public Long getId() {\n" +
                "        return id;\n" +
                "    }\n\n");
        for (DBClassField dbClassField : dbClass.getFieldList()) {
            result.append(makeGetter(dbClassField));
            result.append("\n");
        }
        return result.toString();
    }

    public static String makeSetter(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic void set");
        result.append(makeCapital(dbClassField.getName()));
        result.append("(");
        result.append(createFieldTypeWithName(dbClassField));
        result.append(") {\n");

        result.append("\t\tthis.");
        result.append(dbClassField.getName());
        result.append(" = ");
        result.append(dbClassField.getName());
        result.append(";\n");

        result.append("\t}\n");
        return result.toString();
    }

    public static String makeSetterOtherClassAsShortListItem(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic void set");
        result.append(makeCapital(dbClassField.getName()));
        result.append("(");
        result.append(createFieldTypeAsShortListItemWithName(dbClassField));
        result.append(") {\n");

        result.append("\t\tthis.");
        result.append(dbClassField.getName());
        result.append(" = ");
        result.append(dbClassField.getName());
        result.append(";\n");

        result.append("\t}\n");
        return result.toString();
    }

    public static String makeSetterAsString(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic void set");
        result.append(makeCapital(dbClassField.getName()));
        result.append("(");
        result.append(createFieldTypeAsStringWithName(dbClassField));
        result.append(") {\n");

        result.append("\t\tthis.");
        result.append(dbClassField.getName());
        result.append(" = ");
        result.append(dbClassField.getName());
        result.append(";\n");

        result.append("\t}\n");
        return result.toString();
    }

    public static String makeAllSetterIncludeIdSetter(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("    public void setId(Long id) {\n" +
                "        this.id = id;\n" +
                "    }\n\n");
        for (DBClassField dbClassField : dbClass.getFieldList()) {
            result.append(makeSetter(dbClassField));
            result.append("\n");
        }
        return result.toString();
    }

    public static String createFieldTypeWithName(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        if (dbClassField.isList()) {
            result.append("List<");
        }
        result.append(dbClassField.getRealType());
        if (dbClassField.isList()) {
            result.append(">");
        }
        result.append(" ");
        result.append(dbClassField.getName());
        return result.toString();
    }

    public static String createFieldTypeAsShortListItemWithName(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        if (dbClassField.isList()) {
            result.append("List<");
        }
        result.append(dbClassField.getOtherClassName() + "ShortListItem");
        if (dbClassField.isList()) {
            result.append(">");
        }
        result.append(" ");
        result.append(dbClassField.getName());
        return result.toString();
    }

    public static String createFieldTypeAsStringWithName(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        if (dbClassField.isList()) {
            result.append("List<");
        }
        result.append("String");
        if (dbClassField.isList()) {
            result.append(">");
        }
        result.append(" ");
        result.append(dbClassField.getName());
        return result.toString();
    }

    public static String createFieldTypeWithNameAsList(DBClassField dbClassField) {
        StringBuilder result = new StringBuilder();
        result.append("List<");
        result.append(dbClassField.getRealType());
        result.append("> ");
        result.append(dbClassField.getName());
        result.append("List;\n");
        return result.toString();
    }

    public static String createEmptyConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\t" + dbClass.getName() + "() {\n");
        result.append("\t}\n");
        return result.toString();
    }

    public static String createFullConstructor(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic " + dbClass.getName() + "(");
        for (int i = 0; i < dbClass.getFieldList().size(); i++) {
            DBClassField actualFiled = dbClass.getFieldList().get(i);
            result.append(createFieldTypeWithName(actualFiled));
            if (i != dbClass.getFieldList().size() - 1) {
                result.append(", ");
            }
        }
        result.append(") {\n");

        for (DBClassField dbClassField : dbClass.getFieldList()) {
            result.append("\tthis.");
            result.append(dbClassField.getName() + " = " + dbClassField.getName() + ";\n");
        }

        result.append("\t}\n");
        return result.toString();
    }

    public static String createFullConstructorIncludeId(DBClass dbClass) {
        StringBuilder result = new StringBuilder();
        result.append("\tpublic " + dbClass.getName() + "(Long id,");
        for (int i = 0; i < dbClass.getFieldList().size(); i++) {
            DBClassField actualFiled = dbClass.getFieldList().get(i);
            result.append(createFieldTypeWithName(actualFiled));
            if (i != dbClass.getFieldList().size() - 1) {
                result.append(", ");
            }
        }
        result.append(") {\n");

        result.append("\tthis.id = id;\n");
        for (DBClassField dbClassField : dbClass.getFieldList()) {
            result.append("\tthis.");
            result.append(dbClassField.getName() + " = " + dbClassField.getName() + ";\n");
        }

        result.append("\t}\n");
        return result.toString();
    }




}
