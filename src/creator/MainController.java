package creator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static creator.backend.config.CreateConfig.createConfig;
import static creator.backend.controller.CreateControllers.createControllers;
import static creator.backend.domain.CreateDomains.createDomains;
import static creator.backend.dto.CreateDTOs.createDTOs;
import static creator.backend.exception.CreateExceptions.createExceptions;
import static creator.backend.repository.CreateRepositories.createRepositories;
import static creator.backend.service.CreateServices.createServices;
import static creator.backend.validator.CreateValidators.createValidators;

public class MainController {

    private DatabaseService databaseService;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField projectNameInput;
    @FXML
    private TextField backendApplicationDirectoryInput;
    @FXML
    private TextField frontendAppDirectoryInput;
    @FXML
    private TextArea outputTextArea;

    @FXML
    public void createNewClass() throws IOException {
        final Stage createClassStage = new Stage();
        createClassStage.initModality(Modality.APPLICATION_MODAL);
        createClassStage.setTitle("Create new Class");
        createClassStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("createclassstage.fxml"));
        createClassStage.setScene(new Scene(root, 900, 500));
        createClassStage.show();
    }

    @FXML
    public void okCreate() {
        databaseService.setProjectName(projectNameInput.getText());
        databaseService.setBackendApplicationDirectory(backendApplicationDirectoryInput.getText());
        databaseService.setFrontendAppDirectory(frontendAppDirectoryInput.getText());

        createConfig(databaseService);
        createRepositories(databaseService);
        createControllers(databaseService);
        createServices(databaseService);
        createDomains(databaseService);
        createDTOs(databaseService);
        createExceptions(databaseService);
        createValidators(databaseService);


        closeWindow();
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        databaseService = DatabaseService.getInstance();

        databaseService.isChangedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                printClassesAndEnums();
                databaseService.setIsChanged(false);
            }
        });
    }

    private void printClassesAndEnums() {
        StringBuilder stringBuilder = printClasses();
        stringBuilder.append(printEnums());
        outputTextArea.setText(stringBuilder.toString());
        databaseService.setIsChanged(false);
    }

    private StringBuilder printEnums() {
        StringBuilder stringBuilder = new StringBuilder();
        List<DBEnum> dbEnumList = databaseService.getDbEnumList();
        for (DBEnum dbEnum : dbEnumList) {
            stringBuilder.append("Enum: " + dbEnum.getName() + "\n");
            for (String value : dbEnum.getDisplayNameList()) {
                stringBuilder.append("    -" + value + "\n");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }

    private StringBuilder printClasses() {
        StringBuilder stringBuilder = new StringBuilder();
        List<DBClass> dbclasslist = databaseService.getDbclasslist();
        for (DBClass dbClass : dbclasslist) {
            stringBuilder.append("Class: " + dbClass.getName() + "\n");
            for (DBClassField dbClassField : dbClass.getFieldList()) {
                stringBuilder.append("    -" + dbClassField.getName() + " : ");
                if (dbClassField.isList()) {
                    stringBuilder.append("List<");
                }
                stringBuilder.append(dbClassField.getRealType());
                if (dbClassField.isList()) {
                    stringBuilder.append(">");
                }
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }


}
