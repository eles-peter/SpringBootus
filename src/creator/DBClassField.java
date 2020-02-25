package creator;

import static creator.utils.StringBuherator.makeSQLName;

public class DBClassField {

    private String name;
    private String type;
    private String otherClassName;
    private String enumName;
    private boolean isList;
    private boolean isMany;
    private String isManyField;
    private boolean isCreateItem;
    private boolean isDetailItem;
    private boolean isListItem;
    private boolean isShortListItem;

    public DBClassField() {
    }

    public DBClassField(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public DBClassField(String name, String type, String otherClassName, String enumName, boolean isList, boolean isMany, String isManyField, boolean isCreateItem, boolean isDetailItem, boolean isListItem, boolean isShortListItem) {
        this.name = name;
        this.type = type;
        this.otherClassName = otherClassName;
        this.enumName = enumName;
        this.isList = isList;
        this.isMany = isMany;
        this.isManyField = isManyField;
        this.isCreateItem = isCreateItem;
        this.isDetailItem = isDetailItem;
        this.isListItem = isListItem;
        this.isShortListItem = isShortListItem;
    }

    public DBClassField(DBClassField dbClassField) {
        this.name = dbClassField.getName();
        this.type = dbClassField.getType();
        this.otherClassName = dbClassField.getOtherClassName();
        this.enumName = dbClassField.getEnumName();
        this.isList = dbClassField.isList();
        this.isMany = dbClassField.isMany();
        this.isManyField = dbClassField.getIsManyField();
        this.isCreateItem = dbClassField.isCreateItem();
        this.isDetailItem = dbClassField.isDetailItem();
        this.isListItem = dbClassField.isListItem();
        this.isShortListItem = dbClassField.isShortListItem();
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

    public String getType() {
        return type;
    }

    public String getRealType() {
        String result = "";
        if (this.getType().equals("Enum")) {
            result = this.getEnumName();
        } else if (this.getType().equals("Other Class")) {
            result = this.getOtherClassName();
        } else if (this.getType().equals("Image URL") || this.getType().equals("Text URL")) {
            result = "String";
        } else {
            result = this.getType();
        }
        return result;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOtherClassName() {
        return otherClassName;
    }

    public void setOtherClassName(String otherClassName) {
        this.otherClassName = otherClassName;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public boolean isMany() {
        return isMany;
    }

    public void setMany(boolean many) {
        isMany = many;
    }

    public String getIsManyField() {
        return isManyField;
    }

    public void setIsManyField(String isManyField) {
        this.isManyField = isManyField;
    }

    public boolean isCreateItem() {
        return isCreateItem;
    }

    public void setCreateItem(boolean createItem) {
        isCreateItem = createItem;
    }

    public boolean isDetailItem() {
        return isDetailItem;
    }

    public void setDetailItem(boolean detailItem) {
        isDetailItem = detailItem;
    }

    public boolean isListItem() {
        return isListItem;
    }

    public void setListItem(boolean listItem) {
        isListItem = listItem;
    }

    public boolean isShortListItem() {
        return isShortListItem;
    }

    public void setShortListItem(boolean shortListItem) {
        isShortListItem = shortListItem;
    }

    @Override
    public String toString() {
        return "DBClassField{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", otherClassName='" + otherClassName + '\'' +
                ", enumName='" + enumName + '\'' +
                ", isList=" + isList +
                ", isMany=" + isMany +
                ", isManyField='" + isManyField + '\'' +
                ", isCreateItem=" + isCreateItem +
                ", isDetailItem=" + isDetailItem +
                ", isListItem=" + isListItem +
                ", isShortListItem=" + isShortListItem +
                '}';
    }
}
