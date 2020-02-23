package creator;

import creator.utils.FieldHBox;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class CreateClassStageController {

    private DatabaseService databaseService;

    @FXML
    private AnchorPane createClassMainPane;
    @FXML
    private TextField classNameInput;
    @FXML
    private FlowPane fieldsFlowPane;

    @FXML
    public void addNewFieldLine() {
        fieldsFlowPane.getChildren().add(new FieldHBox());
    }

    @FXML
    public void okAdd() {
        String className = classNameInput.getText();
        if (!className.isEmpty()) {
            DBClass dbClass = new DBClass(className);
            for (Node child : fieldsFlowPane.getChildren()) {
                FieldHBox actualField = (FieldHBox) child;
                String fieldName = actualField.getFieldName();
                if (!fieldName.isEmpty()) {
                    DBClassField dbClassField = new DBClassField();
                    dbClassField.setName(fieldName);
                    dbClassField.setList(actualField.getIsList());
                    String fieldType = actualField.getFieldType();
                    dbClassField.setType(fieldType);
                    if (fieldType.equals("Enum")) {
                        String enumName = actualField.getOtherClassName();
                        dbClassField.setEnumName(actualField.getOtherClassName());
                        createDBEnumStage(enumName);
                    } else if (fieldType.equals("Other Class")) {
                        dbClassField.setOtherClassName(actualField.getOtherClassName());
                    }
                    dbClassField.setMany(actualField.getIsMany());
                    dbClassField.setIsManyField(actualField.getIsManyField());
                    dbClassField.setCreateItem(actualField.getIsCreateItem());
                    dbClassField.setDetailItem(actualField.getIsDetailItem());
                    dbClassField.setListItem(actualField.getIsListItem());
                    dbClassField.setShortListItem(actualField.getIsShortListItem());
                    dbClass.add(dbClassField);
                }
            }
            this.databaseService.addClass(dbClass);
            databaseService.setIsChanged(true);
        }
        closeWindow();
    }

    private void createDBEnumStage(String enumName) {
        Label label = new Label("Enter the display names of the " + enumName + " enum\n" +
                "Separate lines, separated by \"enter\"");
        label.setTextAlignment(TextAlignment.CENTER);
        TextArea enumValues = new TextArea();
        enumValues.setStyle("-fx-pref-height: 390.0; -fx-pref-width: 280.0;");
        Button okButton = new Button("OK");
        VBox vbox = new VBox(label, enumValues, okButton);
        vbox.setStyle("-fx-alignment: TOP_CENTER; -fx-pref-height: 500.0; -fx-pref-width: 300.0; -fx-spacing: 10.0; -fx-padding: 15.0");
        AnchorPane enumPane = new AnchorPane(vbox);
        final Stage createDBEnumStage = new Stage();
        createDBEnumStage.initModality(Modality.APPLICATION_MODAL);
        createDBEnumStage.setTitle("Create new Enum");
        createDBEnumStage.setResizable(false);
        createDBEnumStage.setScene(new Scene(enumPane, 300, 500));
        okButton.setOnAction(event -> {
            String[] enumValueArray = enumValues.getText().split("\n");
            List<String> enumValueList = Arrays.asList(enumValueArray);
            this.databaseService.addEnum(new DBEnum(enumName, enumValueList));
            databaseService.setIsChanged(true);
            createDBEnumStage.close();
        });
        createDBEnumStage.show();
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) createClassMainPane.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        databaseService = DatabaseService.getInstance();

        for (int i = 0; i < 10; i++) {
            fieldsFlowPane.getChildren().add(new FieldHBox());
        }
    }

}
