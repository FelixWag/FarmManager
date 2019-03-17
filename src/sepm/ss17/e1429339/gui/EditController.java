package sepm.ss17.e1429339.gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.BoxServiceException;
import sepm.ss17.e1429339.exceptions.BoxServiceValidationException;
import sepm.ss17.e1429339.help.Alerts;
import sepm.ss17.e1429339.help.area;
import sepm.ss17.e1429339.help.litter;
import sepm.ss17.e1429339.service.BoxService;

import java.io.File;

public class EditController {

    final static Logger LOGGER = LoggerFactory.getLogger(EditController.class);

    @FXML
    private TextField box_sizeField;
    @FXML
    private TextField daily_rateField;
    @FXML
    private CheckBox windowCheck;
    @FXML
    private CheckBox insideCheck;
    @FXML
    private ChoiceBox<litter> litterChoice;
    @FXML
    private ChoiceBox<area> areaChoice;
    @FXML
    private Button buttonClose;
    @FXML
    private ImageView photoImageView;



    private BoxService boxService;
    private Stage dialogeStage;

    private Box b;


    public void setDialogStage(Stage dialogStage){
        this.dialogeStage=dialogStage;
    }



    public void setBoxService(BoxService boxService) {
        this.boxService = boxService;

        litterChoice.getItems().setAll(litter.values());
        areaChoice.getItems().setAll(area.values());
    }

    public void setBox(Box b){
        windowCheck.setSelected(b.isWindow());
        litterChoice.setValue(litter.valueOf(b.getLitter()));
        insideCheck.setSelected(b.isInside());
        box_sizeField.setText(b.getBox_size().toString());
        areaChoice.setValue(area.valueOf(b.getArea()));
        daily_rateField.setText(b.getDaily_rate().toString());
        this.b=b;
    }

    public void onUpdateButtonClicked(ActionEvent actionEvent){

        try {
            if (box_sizeField.getText().matches("\\d*\\.?\\d{1,2}") && daily_rateField.getText().matches("\\d*\\.?\\d{1,2}")) {
                Float.parseFloat(box_sizeField.getText());
                Float.parseFloat(daily_rateField.getText());
                b.setWindow(windowCheck.isSelected());
                b.setLitter(litterChoice.getValue().toString());
                b.setInside(insideCheck.isSelected());
                b.setBox_size(Float.valueOf(box_sizeField.getText()));
                b.setArea(areaChoice.getValue().toString());
                b.setDaily_rate(Float.valueOf(daily_rateField.getText()));
                boxService.updateBox(b);
                dialogeStage.close();
            } else {
                Alerts.informationDialog("Information Dialog", "Keine gültige Zahl", "Bitte gültige Zahl eingeben");
            }
        }catch (BoxServiceValidationException | BoxServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

    public void onSelectButtonClicked(ActionEvent actionEvent){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Bild öffnen");

        //Setting filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );


        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            b.setPhoto(file.getAbsolutePath());
            javafx.scene.image.Image image = new javafx.scene.image.Image("file:"+file.getAbsolutePath());
            photoImageView.setImage(image);
        }
    }

    public void onCloseButtonClicked(ActionEvent actionEvent){
        dialogeStage.close();
    }

}
