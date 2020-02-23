package creator;

import java.util.ArrayList;
import java.util.List;

import static creator.utils.StringBuherator.makeSQLName;

public class DBClass {

    private String name;
    private List<DBClassField> fieldList = new ArrayList<>();
    private List<DBClassField> createFieldList = new ArrayList<>();
    private List<DBClassField> detailFieldList = new ArrayList<>();
    private List<DBClassField> listFieldList = new ArrayList<>();
    private List<DBClassField> shortListFieldList = new ArrayList<>();


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

    @Override
    public String toString() {
        return "DBClass{" +
                "name='" + name + '\'' +
                ", fieldList=" + fieldList +
                '}';
    }
}
