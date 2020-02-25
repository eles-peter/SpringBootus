package creator.utils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldHBox extends HBox {

    private TextField fieldNameInput = new TextField();
    private CheckBox isListCheckBox = new CheckBox();
    private ChoiceBox fieldTypeInput = new ChoiceBox();
    private TextField otherClassNameInput = new TextField();
    private CheckBox isManyCheckBox = new CheckBox();
    private TextField isManyFieldInput = new TextField();
    private CheckBox isCreateItemCheckBox = new CheckBox();
    private CheckBox isDetailItemCheckBox = new CheckBox();
    private CheckBox isListItemCheckBox = new CheckBox();
    private CheckBox isShortListItemCheckBox = new CheckBox();


    public FieldHBox() {
        this.setStyle("-fx-alignment: CENTER_LEFT; -fx-pref-height: 35.0; -fx-pref-width: 860.0; -fx-spacing: 8.0; -fx-padding: 5.0");
        fieldNameInput.setPrefWidth(180.0);
        fieldNameInput.setPromptText("Field Name");
        setTypeChoiceBox(fieldTypeInput);
        fieldTypeInput.setPrefWidth(100.0);
        otherClassNameInput.setPrefWidth(180.0);
        otherClassNameInput.setPromptText("other Class / Enum Name");
        otherClassNameInput.setDisable(true);
        isManyCheckBox.setDisable(true);
        isManyFieldInput.setPrefWidth(180.0);
        isManyFieldInput.setPromptText("Field Name in other Class");
        isManyFieldInput.setDisable(true);
        Separator separator1 = new Separator();
        separator1.setStyle(" -fx-orientation: VERTICAL; -fx-pref-height: 50.0;");
        Separator separator2 = new Separator();
        separator2.setStyle(" -fx-orientation: VERTICAL; -fx-pref-height: 50.0;");
        Separator separator3 = new Separator();
        separator3.setStyle(" -fx-orientation: VERTICAL; -fx-pref-height: 50.0;");
        setFieldTypeInputListener();
        setIsManyCheckBoxListener();
        this.getChildren().addAll(fieldNameInput, isListCheckBox, fieldTypeInput, otherClassNameInput, isManyCheckBox, isManyFieldInput,
                isCreateItemCheckBox, separator1, isDetailItemCheckBox, separator2, isListItemCheckBox, separator3, isShortListItemCheckBox);
    }

    private void setIsManyCheckBoxListener() {
        isManyCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                boolean isMany = newValue;
                if (isMany) {
                    isManyFieldInput.setDisable(false);
                } else {
                    isManyFieldInput.setText("");
                    isManyFieldInput.setDisable(true);
                }
            }
        });
    }

    private void setFieldTypeInputListener() {
        fieldTypeInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                String actualType = (String) newValue;
                if (actualType.equals("Other Class")) {
                    otherClassNameInput.setDisable(false);
                    isManyCheckBox.setDisable(false);
                } else if(actualType.equals("Enum")){
                    otherClassNameInput.setDisable(false);
                    isManyCheckBox.setSelected(false);
                    isManyCheckBox.setDisable(true);
                } else {
                    otherClassNameInput.setText("");
                    otherClassNameInput.setDisable(true);
                    isManyCheckBox.setSelected(false);
                    isManyCheckBox.setDisable(true);
                }
            }
        });
    }

    private void setTypeChoiceBox(ChoiceBox choiceBox) {
        //TODO ezt áttenni valahova (a lehetséges elem típusokat) most nagyon rossz helyen van
        List<String> typeOption = new ArrayList<>(Arrays.asList("String", "Integer", "Long", "Double", "Boolean", "Date", "Enum", "Other Class", "Image URL", "Text URL"));
        choiceBox.getItems().addAll(typeOption);
        choiceBox.setValue(typeOption.get(0));
    }

    public String getFieldName() {
        return fieldNameInput.getText();
    }

    public boolean getIsList() {
        return isListCheckBox.isSelected();
    }

    public String getFieldType() {
        return (String) fieldTypeInput.getValue();
    }

    public String getOtherClassName() {
        return otherClassNameInput.getText();
    }

    public boolean getIsMany() {
        return isManyCheckBox.isSelected();
    }

    public String getIsManyField() {
        return isManyFieldInput.getText();
    }

    public boolean getIsCreateItem() {
        return isCreateItemCheckBox.isSelected();
    }

    public boolean getIsDetailItem() {
        return isDetailItemCheckBox.isSelected();
    }

    public boolean getIsListItem() {
        return isListItemCheckBox.isSelected();
    }

    public boolean getIsShortListItem() {
        return isShortListItemCheckBox.isSelected();
    }
}


