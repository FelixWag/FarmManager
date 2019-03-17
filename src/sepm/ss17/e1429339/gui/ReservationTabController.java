package sepm.ss17.e1429339.gui;


import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.ReservationServiceException;
import sepm.ss17.e1429339.exceptions.ReservationServiceValidationException;
import sepm.ss17.e1429339.help.Alerts;
import sepm.ss17.e1429339.service.BoxService;
import sepm.ss17.e1429339.service.ReservationService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ReservationTabController {

    final static Logger LOGGER = LoggerFactory.getLogger(ReservationTabController.class);

    private ReservationService reservationService;
    private Reservation r;
    private SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Stage primaryStage = new Stage();

    private BoxService boxService;

    /**Todays date*/
    Date today = new Date();

    LocalDate localToday = LocalDate.now();

    @FXML
    private TableView<Reservation> resTableView;
    @FXML
    private TableColumn<Reservation, String> res_idCol;
    @FXML
    private TableColumn<Reservation, String> customerCol;
    @FXML
    private TableColumn<Reservation, String> fromCol;
    @FXML
    private TableColumn<Reservation, String> toCol;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Label idLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label boxAndHorseLabel;
    @FXML
    private DatePicker addFromDatePicker;
    @FXML
    private DatePicker addToDatePicker;
    @FXML
    private TextField customerTextField;


    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;

        updateTableview();
    }

    public void setBoxService(BoxService boxService) {
        this.boxService = boxService;
    }

    public void initialize(){
        res_idCol.setCellValueFactory(reservation -> new SimpleStringProperty(Long.toString(reservation.getValue().getRes_id())));
        customerCol.setCellValueFactory(reservation -> new SimpleStringProperty(reservation.getValue().getCustomer()));
        fromCol.setCellValueFactory(reservation -> new SimpleStringProperty(dmyFormat.format(reservation.getValue().getRes_from())));
        toCol.setCellValueFactory(reservation -> new SimpleStringProperty(dmyFormat.format(reservation.getValue().getRes_to())));
    }

    private void updateTableview(){
        try {
            resTableView.getItems().setAll(reservationService.searchall());
        } catch (ReservationServiceException e) {
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onFilterButtonClicked(ActionEvent actionEvent){

        try {
            if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null && fromDatePicker.getValue().isBefore(toDatePicker.getValue())) {
                r = new Reservation();

                r.setRes_from(Date.from(fromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                r.setRes_to(Date.from(toDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

                resTableView.getItems().setAll(reservationService.search(r));
            } else {
                Alerts.informationDialog("Information Dialog", "Im Kalendar sind ungültige Werte ausgewählt", "Bitte beide Kalendardaten überprüfen");
            }
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onSearchAllClicked(ActionEvent actionEvent){
        updateTableview();
    }

    public void onDetailsButtonClicked(ActionEvent actionEvent){

        try {
            int index = resTableView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                List<BoxRes> listBoxRes = new LinkedList<BoxRes>();
                Reservation r = resTableView.getSelectionModel().getSelectedItem();
                idLabel.setText(Long.toString(r.getRes_id()));
                customerLabel.setText(r.getCustomer());
                toLabel.setText(dmyFormat.format(r.getRes_to()));
                fromLabel.setText(dmyFormat.format(r.getRes_from()));

                listBoxRes = reservationService.getBoxReservation(r);
                String boxAndHorse = "";

                for (BoxRes boxRes : listBoxRes) {
                    boxAndHorse += boxRes.getBox_id() + "->" + boxRes.getHorse() + " , ";
                }

                boxAndHorseLabel.setText(boxAndHorse);

            } else {
                Alerts.informationDialog("Information Dialog", "Keine Reservierung ausgewählt", "Bitte Reservierung auswählen");
            }
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onEditButtonClicked(ActionEvent actionEvent){

        int index=resTableView.getSelectionModel().getSelectedIndex();
        if(index>=0) {
            Reservation r = resTableView.getSelectionModel().getSelectedItem();
            if(r.getRes_from().after(today)) {
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("EditReservationWindow.fxml"));
                    AnchorPane page = (AnchorPane) loader.load();

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Bearbeite Reservierung");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(primaryStage);
                    Scene scene = new Scene(page);
                    dialogStage.setScene(scene);

                    EditReservationController controller = loader.getController();
                    controller.setDialogStage(dialogStage);
                    controller.setReservationService(reservationService);
                    controller.setBoxService(boxService);
                    controller.setReservation(resTableView.getSelectionModel().getSelectedItem());

                    // Show the dialog and wait
                    dialogStage.showAndWait();
                    updateTableview();
                } catch (IOException e) {
                    LOGGER.error("IOException",e);
                }
            }else{
                Alerts.informationDialog("Information Dialog","Stichtag ist schon vorbei!","Reservierung kann nicht mehr geändert werden");
            }
        }else{
            Alerts.informationDialog("Information Dialog","Keine Reservierung ausgewählt","Bitte Reservierung auswählen");
        }

    }

    public void onAddButtonClicked(ActionEvent actionEvent){

        if(addFromDatePicker.getValue() != null && addToDatePicker.getValue() !=null && !customerTextField.getText().equals("") && addFromDatePicker.getValue().isBefore(addToDatePicker.getValue()) && addFromDatePicker.getValue().isAfter(localToday)) {
            try {
                // Load the fxml file and create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AddReservationWindow.fxml"));
                AnchorPane page = (AnchorPane) loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Reservierung hinzufügen");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                AddReservationController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setReservationService(reservationService);
                controller.setBoxService(boxService);
                controller.setDateandName(Date.from(addFromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(addToDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), customerTextField.getText());

                // Show the dialog and wait
                dialogStage.showAndWait();
                updateTableview();
            } catch (IOException e) {
                LOGGER.error("ERROR",e);
            }
        }else{
            Alerts.informationDialog("Information Dialog","Im Kalendar und Kundennamenfeld sind ungültige Werte ausgewählt","Bitte beide Kalendardaten und Kundennamenfeld überprüfen");
        }

    }

    public void onDeleteButtonClicked(ActionEvent actionEvent){

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigungs Dialog");
            alert.setHeaderText("Diese Reservierung wird gelöscht!");
            alert.setContentText("Wollen Sie wirklich die Box löschen?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK ) {
                int index = resTableView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    if(resTableView.getSelectionModel().getSelectedItem().getRes_from().after(today)) {

                        Reservation r = resTableView.getSelectionModel().getSelectedItem();
                        reservationService.deleteReservation(r);

                        updateTableview();
                    }else{
                        Alerts.informationDialog("Information Dialog", "Reservierung schon abgelaufen", "Nur nicht begonnene Reservierungen können gelöscht werden");
                    }
                } else {
                    Alerts.informationDialog("Information Dialog", "Keine Reservierung ausgewählt", "Bitte Reservierung auswählen");
                }
            }
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

    public void onPickUpButtonClicked(ActionEvent actionEvent){

        int index=resTableView.getSelectionModel().getSelectedIndex();
        if(index>=0 && resTableView.getSelectionModel().getSelectedItem().getRes_from().before(today)) {
            try {
                // Load the fxml file and create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("billWindow.fxml"));
                AnchorPane page = (AnchorPane) loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Rechnung");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                BillController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setReservationService(reservationService);
                controller.setReservation(resTableView.getSelectionModel().getSelectedItem());

                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
                updateTableview();
            } catch (IOException e) {
                LOGGER.error("ERROR",e);
            }
        }else{
            Alerts.informationDialog("Infortmation Dialog", "Keine Reservierung ausgewählt oder Reservierung hat noch nicht begonnen", "Bitte  gültige Reservierung auswählen");
        }

    }



}
