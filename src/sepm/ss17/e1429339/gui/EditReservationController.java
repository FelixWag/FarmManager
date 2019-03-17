package sepm.ss17.e1429339.gui;


import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.BoxServiceException;
import sepm.ss17.e1429339.exceptions.ReservationServiceException;
import sepm.ss17.e1429339.exceptions.ReservationServiceValidationException;
import sepm.ss17.e1429339.exceptions.ServiceException;
import sepm.ss17.e1429339.help.Alerts;
import sepm.ss17.e1429339.service.BoxService;
import sepm.ss17.e1429339.service.ReservationService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EditReservationController {

    final static Logger LOGGER = LoggerFactory.getLogger(EditReservationController.class);

    private ReservationService reservationService;
    private Stage dialogeStage;

    private BoxService boxService;

    private Reservation r;

    private String addedHorses="";

    /**Todays date*/
    LocalDate localToday = LocalDate.now();

    private List<BoxRes> boxResListAdd= new LinkedList<BoxRes>();

    @FXML
    private TextField customerTextField;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private TableView<BoxRes> tableView;
    @FXML
    private TableColumn<BoxRes, String> boxIdCol;
    @FXML
    private TableColumn<BoxRes, String> horseCol;
    @FXML
    private ChoiceBox<Integer> boxChoiceBox;
    @FXML
    private TextField horseTextField;
    @FXML
    private Label addedLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Button changeDateButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;

    public void setDialogStage(Stage dialogStage){
        this.dialogeStage=dialogStage;
    }



    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void setBoxService(BoxService boxService) {
        this.boxService = boxService;

        try {
            List<Box> boxList = new LinkedList<Box>();
            boxList = boxService.searchall();

            //List for id
            List<Integer> boxIdList = new LinkedList<Integer>();

            for (Box b : boxList) {
                boxIdList.add(b.getBox_id());
            }

            boxChoiceBox.getItems().setAll(boxIdList);
        }catch (BoxServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void setReservation(Reservation r){

        customerTextField.setText(r.getCustomer());

        fromDatePicker.setValue(r.getRes_from().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        toDatePicker.setValue(r.getRes_to().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        this.r=r;

        try {
            tableView.getItems().setAll(reservationService.getBoxReservation(this.r));
        } catch (ReservationServiceValidationException | ReservationServiceException e) {
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onDeleteButtonClicked(ActionEvent actionEvent){

        try {
            int index = tableView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                if (tableView.getItems().size() > 1) {
                    /**Give warning to user*/
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Bestätigungs Dialog");
                    alert.setHeaderText("Die Reservierung der Box wird gelöscht!");
                    alert.setContentText("Wollen Sie wirklich die Box löschen?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {

                        BoxRes br = new BoxRes();
                        br.setRes_id(r.getRes_id());
                        br.setBox_id(tableView.getSelectionModel().getSelectedItem().getBox_id());

                        reservationService.deleteBoxRes(br);
                        tableView.getItems().setAll(reservationService.getBoxReservation(this.r));
                    }
                } else {
                    Alerts.informationDialog("Information Dialog", "Dies ist die letzte Box. Bitte löschen Sie die komplette Reservierung", "Bitte komplette Reservierung löschen");
                }
            } else {
                Alerts.informationDialog("Information Dialog", "Keine Box ausgewählt", "Bitte Box auswählen");
            }
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }


    }

    public void onAddButtonClicked(ActionEvent actionEvent){

        try {
            if (boxChoiceBox.getValue() != null && !horseTextField.getText().equals("")) {
                if (reservationService.boxResCheckAvailable(boxChoiceBox.getValue(), r.getRes_from(), r.getRes_to()) == 0) {
                    BoxRes boxRes = new BoxRes();
                    boxRes.setBox_id(boxChoiceBox.getValue());
                    boxRes.setHorse(horseTextField.getText());
                    boxRes.setPrice(reservationService.computePrice(boxChoiceBox.getValue(), r.getRes_from().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), r.getRes_to().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                    boxResListAdd.add(boxRes);
                    boxChoiceBox.getItems().remove(boxRes.getBox_id());

                    addedHorses += boxChoiceBox.getValue().toString() + "->" + horseTextField.getText() + ", ";
                    addedLabel.setText(addedHorses);
                } else {
                    Alerts.informationDialog("Information Dialog", "Gewählte Box zu diesem Zeitraum schon gebucht", "Bitte andere Box auswählen");
                }
            } else {
                Alerts.informationDialog("Information Dialog", "Keine Werte ausgewählt", "Bitte Werte auswählen");
            }
        }catch (ReservationServiceValidationException | ServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

    public void onEditButtonClicked(ActionEvent actionEvent){

        try {
            /**Give warning to user*/
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigungs Dialog");
            alert.setHeaderText("Pferde werden jetzt zu der Reservierung hinzugefügt!");
            alert.setContentText("Wollen Sie wirklich diese Pferde zur Reservierung hinzufügen?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                for (BoxRes br : boxResListAdd) {
                    br.setRes_id(r.getRes_id());
                    reservationService.addBoxRes(br);
                }
                dialogeStage.close();
            }
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onCloseButtonClicked(ActionEvent actionEvent){
        dialogeStage.close();
    }

    public void onCheckButtonClicked(ActionEvent actionEvent){

        try {
            /**Give warning to user*/
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigungs Dialog");
            alert.setHeaderText("Diese Reservierung wird im Datum geändert!");
            alert.setContentText("Wollen Sie wirklich das Datum ändern?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null && !customerTextField.getText().equals("") && fromDatePicker.getValue().isBefore(toDatePicker.getValue()) && fromDatePicker.getValue().isAfter(localToday)) {
                    r.setCustomer(customerTextField.getText());
                    r.setRes_from(Date.from(fromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    r.setRes_to(Date.from(toDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

                    boolean success = reservationService.updateReservation(r);
                    /**To update all box_res entries*/
                    if (success) {
                        List<BoxRes> boxResList = reservationService.getBoxReservation(r);
                        for (BoxRes br : boxResList) {
                            BoxRes retBr=new BoxRes();
                            retBr.setRes_id(r.getRes_id());
                            retBr.setBox_id(br.getBox_id());
                            retBr.setPrice(reservationService.computePrice(br.getBox_id(), r.getRes_from().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                                    r.getRes_to().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                            retBr.setHorse(br.getHorse());
                            reservationService.updateBoxRes(retBr);
                        }

                        deleteButton.setDisable(true);
                        changeDateButton.setDisable(true);
                        customerTextField.setDisable(true);
                        fromDatePicker.setDisable(true);
                        toDatePicker.setDisable(true);
                        addButton.setDisable(false);
                        boxChoiceBox.setDisable(false);
                        horseTextField.setDisable(false);
                        editButton.setDisable(false);
                    } else {
                        Alerts.informationDialog("Informations Dialog", "Box/en zu diesem Zeitpunkt schon reserviert", "Bitte anderen Termin auswählen");
                    }
                } else {
                    Alerts.informationDialog("Information Dialog", "Im Kalendar und Kundennamenfeld sind ungültige Werte ausgewählt", "Bitte beide Kalendardaten und Kundennamenfeld überprüfen");
                }
            }
        }catch (ReservationServiceValidationException | ServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

    public void initialize(){

        boxIdCol.setCellValueFactory(boxRes -> new SimpleStringProperty(boxRes.getValue().getBox_id().toString()));
        horseCol.setCellValueFactory(boxRes -> new SimpleStringProperty(boxRes.getValue().getHorse()));

        /**To reset the list*/
        boxResListAdd = new LinkedList<BoxRes>();
    }

}
