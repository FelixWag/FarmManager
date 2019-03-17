package sepm.ss17.e1429339.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.dao.impl.BoxDAOJDBC;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.BoxServiceException;
import sepm.ss17.e1429339.exceptions.BoxServiceValidationException;
import sepm.ss17.e1429339.help.Alerts;
import sepm.ss17.e1429339.help.area;
import sepm.ss17.e1429339.help.litter;
import sepm.ss17.e1429339.service.BoxService;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BoxTabController {

    final static Logger LOGGER = LoggerFactory.getLogger(BoxDAOJDBC.class);

    private Stage primaryStage = new Stage();
    private BoxService boxService;
    private Box b;




    @FXML
    private TableView<Box> tableView;
    @FXML
    private TableColumn<Box, String> box_idCol;
    @FXML
    private TableColumn<Box, String> windowCol;
    @FXML
    private TableColumn<Box, String> litterCol;
    @FXML
    private TableColumn<Box, String> insideCol;
    @FXML
    private TableColumn<Box, String> box_sizeCol;
    @FXML
    private TableColumn<Box, String> areaCol;
    @FXML
    private TableColumn<Box, String> daily_rateCol;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
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
    private ChoiceBox<Integer> idChoiceBox;
    @FXML
    private Label idLabel;
    @FXML
    private Label windowLabel;
    @FXML
    private Label litterLabel;
    @FXML
    private Label box_sizeLabel;
    @FXML
    private Label areaLabel;
    @FXML
    private Label daily_rateLabel;
    @FXML
    private Label insideLabel;
    @FXML
    private ImageView photoImageView;


    public void setBoxService(BoxService boxService) {
        try {
            this.boxService = boxService;
            updateTableview();

            List<Box> boxList = new LinkedList<Box>();
            boxList = boxService.searchall();

            //List for id
            List<Integer> boxIdList = new LinkedList<Integer>();

            for (Box b : boxList) {
                boxIdList.add(b.getBox_id());
            }

            idChoiceBox.getItems().setAll(boxIdList);
        }catch (BoxServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onDeleteButtonClicked(ActionEvent actionEvent) {

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigungs Dialog");
            alert.setHeaderText("Diese Box wird gelöscht, ausstehende Reservierungen laufen noch aus!");
            alert.setContentText("Wollen Sie wirklich die Box löschen?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                int index = tableView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {

                    Box b = tableView.getSelectionModel().getSelectedItem();
                    boxService.deleteBox(b);

                    updateTableview();
                } else {
                    Alerts.informationDialog("Information Dialog", "Keine Box ausgewählt", "Bitte Box auswählen");
                }
            }
        }catch (BoxServiceValidationException |BoxServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

    public void onEditButtonClicked(ActionEvent actionEvent) {

        int index=tableView.getSelectionModel().getSelectedIndex();
        if(index>=0) {
            try {
                // Load the fxml file and create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("EditBoxWindow.fxml"));
                AnchorPane page = (AnchorPane) loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Bearbeite Box");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                EditController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setBoxService(boxService);
                controller.setBox(tableView.getSelectionModel().getSelectedItem());

                // Show the dialog and wait
                dialogStage.showAndWait();
                updateTableview();
            } catch (IOException e) {
                LOGGER.error("IOError", e);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Keine Box ausgewählt");
            alert.setContentText("Bitte Box auswählen");

            alert.showAndWait();
        }

    }


    public void onDetailsButtonClicked(ActionEvent actionEvent){

        int index =tableView.getSelectionModel().getSelectedIndex();
        if(index>=0) {
            Box b = tableView.getSelectionModel().getSelectedItem();
            idLabel.setText(b.getBox_id().toString());
            windowLabel.setText(b.isWindow().toString());
            litterLabel.setText(b.getLitter());
            insideLabel.setText(b.isInside().toString());
            box_sizeLabel.setText(Float.toString(b.getBox_size()));
            areaLabel.setText(b.getArea());
            daily_rateLabel.setText(Float.toString(b.getDaily_rate()));

            if(b.getPhoto()!=null){
                Image image = new Image("file:"+b.getPhoto());
                File f = new File(b.getPhoto());
                if(f.exists() && !f.isDirectory()) {
                    photoImageView.setImage(image);
                }else{
                    photoImageView.setImage(null);
                    Alerts.informationDialog("Informations Dialog","Bild wurde verschoben oder gelöscht","Bitte einen Image Ordner anlegen und nur aus diesem die Bilder verwenden. Dieser sollte nicht verschoben werden");
                }
            }else{
                photoImageView.setImage(null);
            }

        }else{
            Alerts.informationDialog("Informations Dialog","Keine Box ausgewählt","Bitte Box auswählen");
        }

    }

    public void onAddTableButtonClicked(ActionEvent actionEvent){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddWindow.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Box");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBoxService(boxService);

            // Show the dialog and wait
            dialogStage.showAndWait();
            updateTableview();
        } catch (IOException e) {
            LOGGER.error("IOException",e);
        }
    }


    public void onFilterButtonClicked(ActionEvent actionEvent){

        try {

            /**To check if the input is valid*/
            boolean succcess = false;
            b = new Box();
            if (!windowCheck.isIndeterminate()) {
                b.setWindow(windowCheck.isSelected());
                succcess=true;
            }
            if (litterChoice.getSelectionModel().getSelectedItem() != null) {
                b.setLitter(litterChoice.getValue().toString());
                succcess=true;
            }
            if (!insideCheck.isIndeterminate()) {
                b.setInside(insideCheck.isSelected());
                succcess=true;
            }
            if (!box_sizeField.getText().equals("")) {
                if (box_sizeField.getText().matches("\\d*\\.?\\d{1,2}")) {
                    b.setBox_size(Float.valueOf(box_sizeField.getText()));
                    succcess=true;
                } else {
                    Alerts.informationDialog("Information Dialog", "Keine gültige Zahl in Box Größe Feld", "Bitte gültige Zahl eingeben");
                    succcess = false;
                }
            }
            if (areaChoice.getSelectionModel().getSelectedItem() != null) {
                b.setArea(areaChoice.getValue().toString());
                succcess=true;
            }
            if (!daily_rateField.getText().equals("")) {
                if (daily_rateField.getText().matches("\\d*\\.?\\d{1,2}")) {
                    b.setDaily_rate(Float.valueOf(daily_rateField.getText()));
                    succcess=true;
                } else {
                    Alerts.informationDialog("Information Dialog", "Keine gültige Zahl im Tagessatz Feld", "Bitte gültige Zahl eingeben");
                    succcess = false;
                }
            }
            if (idChoiceBox.getSelectionModel().getSelectedItem() != null) {
                b.setBox_id(idChoiceBox.getValue());
                succcess=true;
            }

            if (succcess) {
                tableView.getItems().setAll(boxService.search(b));
            }
            /**Clear the inputs*/
            litterChoice.setValue(null);
            areaChoice.setValue(null);
            idChoiceBox.setValue(null);
            insideCheck.setIndeterminate(true);
            windowCheck.setIndeterminate(true);
            daily_rateField.setText("");
            box_sizeField.setText("");
        }catch (BoxServiceValidationException | BoxServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

    private void updateTableview(){

        try {
            tableView.getItems().setAll(boxService.searchall());
        } catch (BoxServiceException e) {
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }


    public void initialize(){

        box_idCol.setCellValueFactory(new PropertyValueFactory<Box, String>("box_id"));
        windowCol.setCellValueFactory(new PropertyValueFactory<Box, String>("window"));
        litterCol.setCellValueFactory(new PropertyValueFactory<Box, String>("litter"));
        insideCol.setCellValueFactory(new PropertyValueFactory<Box, String>("inside"));
        box_sizeCol.setCellValueFactory(new PropertyValueFactory<Box, String>("box_size"));
        areaCol.setCellValueFactory(new PropertyValueFactory<Box, String>("area"));
        daily_rateCol.setCellValueFactory(new PropertyValueFactory<Box, String>("daily_rate"));


        litterChoice.getItems().setAll(litter.values());
        areaChoice.getItems().setAll(area.values());

    }

    public void onShowAllButtonClicked(ActionEvent actionEvent){updateTableview();}


}
