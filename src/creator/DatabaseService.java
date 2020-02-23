package creator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private String backendApplicationDirectory;
    private String frontendAppDirectory;
    private String projectName;

    private BooleanProperty isChanged = new SimpleBooleanProperty(false);

    private List<DBClass> dbclasslist = new ArrayList<>(); //TODO megírni Set-re
    private List<DBEnum> dbEnumList = new ArrayList<>(); //TODO megírni Set-re

    private static final DatabaseService INSTANCE = new DatabaseService();

    private DatabaseService() {
    }

    public static DatabaseService getInstance() {
        return INSTANCE;
    }

    public void set(String springbootSrcRoute, String angularFrontendRoute, String projectName) {
        this.backendApplicationDirectory = springbootSrcRoute;
        this.frontendAppDirectory = angularFrontendRoute;
        this.projectName = projectName;
    }

    public void addClass(DBClass dbClass) {
        this.dbclasslist.add(dbClass);
    }

    public void addEnum(DBEnum dbEnum) {
        this.dbEnumList.add(dbEnum);
    }

    public List<DBClass> getDbclasslist() {
        return dbclasslist;
    }

    public List<DBEnum> getDbEnumList() {
        return dbEnumList;
    }

    public boolean isChanged() {
        return isChanged.get();
    }

    public BooleanProperty isChangedProperty() {
        return isChanged;
    }

    public void setIsChanged(boolean isChanged) {
        this.isChanged.set(isChanged);
    }

    public String getBackendApplicationDirectory() {
        return backendApplicationDirectory;
    }

    public void setBackendApplicationDirectory(String backendApplicationDirectory) {
        this.backendApplicationDirectory = backendApplicationDirectory;
    }

    public String getFrontendAppDirectory() {
        return frontendAppDirectory;
    }

    public void setFrontendAppDirectory(String frontendAppDirectory) {
        this.frontendAppDirectory = frontendAppDirectory;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "DatabaseService{" +
                "backendApplicationDirectory='" + backendApplicationDirectory + '\'' +
                ", frontendAppDirectory='" + frontendAppDirectory + '\'' +
                ", projectName='" + projectName + '\'' +
                ", dbclasslist=" + dbclasslist +
                ", dbEnumList=" + dbEnumList +
                '}';
    }
}
