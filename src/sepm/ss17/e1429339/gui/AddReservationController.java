package sepm.ss17.e1429339.gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class AddReservationController {

    final static Logger LOGGER = LoggerFactory.getLogger(AddReservationController.class);

    private ReservationService reservationService;
    private Stage dialogeStage;
    private BoxService boxService;

    private Reservation r;

    private Date from;
    private Date to;
    private String customer;


    /**List to store temp boxRes*/
    private List<BoxRes> boxResList= new LinkedList<BoxRes>();

    @FXML
    private ChoiceBox<Integer> boxChoiceBox;
    @FXML
    private TextField horseTextField;
    @FXML
    private Label addedLabel;

    private String addedHorses="";


    public void setDialogStage(Stage dialogStage){
        this.dialogeStage=dialogStage;
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

    public void setDateandName(Date from, Date to, String customer){
        this.from=from;
        this.to=to;
        this.customer=customer;
    }


    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;

    }

    public void onAddButtonClicked(ActionEvent actionEvent){

        try {

            if (boxChoiceBox.getValue() != null && !horseTextField.getText().equals("")) {
                if (reservationService.boxResCheckAvailable(boxChoiceBox.getValue(), from, to) == 0) {
                    //BoxRes boxRes = new BoxRes(0.0f,horseTextField.getText());
                    BoxRes boxRes = new BoxRes();
                    boxRes.setBox_id(boxChoiceBox.getValue());
                    boxRes.setHorse(horseTextField.getText());
                    boxRes.setPrice(reservationService.computePrice(boxChoiceBox.getValue(), from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                    boxResList.add(boxRes);
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

    public void onResConfirmButtonClicked(ActionEvent actionEvent){

        try {

            if (boxResList.size() > 0) {
                r = new Reservation(customer, from, to, false);
                Reservation result;
                result = reservationService.addReservation(r);
                for (BoxRes br : boxResList) {
                    br.setRes_id(result.getRes_id());
                    reservationService.addBoxRes(br);
                }

                dialogeStage.close();
            } else {
                Alerts.informationDialog("Information Dialog", "Keine Pferd hinzugefügt", "Bitte Pferd auswählen");
            }
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

    public void onCloseButtonClicked(){
        dialogeStage.close();
    }

    public void initialize() {
        /**To reset the list*/
        boxResList = new LinkedList<BoxRes>();
    }

}
