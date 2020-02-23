package creator;

import java.util.ArrayList;
import java.util.List;

import static creator.utils.StringBuherator.makeCapital;
import static creator.utils.StringBuherator.makeUncapital;

public class DBEnum {

    private String name;
    private List<String> displayNameList = new ArrayList<>();

    public DBEnum() {
    }

    public DBEnum(String name) {
        this.name = name;
    }

    public DBEnum(String name, List<String> displayNameList) {
        this.name = name;
        this.displayNameList = displayNameList;
    }

    public void addValue(String value) {
        this.displayNameList.add(value);
    }

    public String getName() {
        return name;
    }

    public String getCapitalName() {
        return makeCapital(name);
    }

    public String getUncapitalName() {
        return makeUncapital(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDisplayNameList() {
        return displayNameList;
    }

    public void setDisplayNameList(List<String> displayNameList) {
        this.displayNameList = displayNameList;
    }

    @Override
    public String toString() {
        return "DBEnum{" +
                "name='" + name + '\'' +
                ", displayNameList=" + displayNameList +
                '}';
    }
}
