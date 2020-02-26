package creator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static creator.utils.StringBuherator.makeSQLName;

public class DBClass {

    private String name;
    private List<DBClassField> fieldList = new ArrayList<>();
    private List<DBClassField> createFieldList = new ArrayList<>();
    private List<DBClassField> detailFieldList = new ArrayList<>();
    private List<DBClassField> listFieldList = new ArrayList<>();
    private DBClassField listSortByField;
    private List<DBClassField> shortListFieldList = new ArrayList<>();
    private Set<String> enumNameSet = new HashSet<>();
    private Set<String> otherClassNameSet = new HashSet<>();


    public DBClass(String name) {
        this.name = name;
    }

    public void add(DBClassField classField) {
        this.fieldList.add(classField);
        if (classField.isCreateItem()) {
            this.createFieldList.add(classField);
        }
        if (classField.isDetailItem()) {
            this.detailFieldList.add(classField);
        }
        if (classField.isListItem()) {
            this.listFieldList.add(classField);
        }
        if (classField.isShortListItem()) {
            this.shortListFieldList.add(classField);
        }
        if (classField.getType().equals("Enum")) {
            this.enumNameSet.add(classField.getEnumName());
        }
        if (classField.getType().equals("Other Class")) {
            this.otherClassNameSet.add(classField.getOtherClassName());
        }
    }

    public String getName() {
        return name;
    }

    public String getSQLName() {
        return makeSQLName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DBClassField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DBClassField> fieldList) {
        for (DBClassField dbClassField : fieldList) {
            this.add(dbClassField);
        }
    }

    public List<DBClassField> getCreateFieldList() {
        return createFieldList;
    }

    public List<DBClassField> getDetailFieldList() {
        return detailFieldList;
    }

    public List<DBClassField> getListFieldList() {
        return listFieldList;
    }

    public List<DBClassField> getShortListFieldList() {
        return shortListFieldList;
    }

    public Set<String> getEnumNameSet() {
        return enumNameSet;
    }

    public Set<String> getOtherClassNameSet() {
        return otherClassNameSet;
    }

    @Override
    public String toString() {
        return "DBClass{" +
                "name='" + name + '\'' +
                ", fieldList=" + fieldList +
                '}';
    }
}
