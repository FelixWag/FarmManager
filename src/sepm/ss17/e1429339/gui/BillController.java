package sepm.ss17.e1429339.gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.ReservationServiceException;
import sepm.ss17.e1429339.exceptions.ReservationServiceValidationException;
import sepm.ss17.e1429339.help.Alerts;
import sepm.ss17.e1429339.service.ReservationService;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class BillController {

    final static Logger LOGGER = LoggerFactory.getLogger(BillController.class);

    private ReservationService reservationService;

    private Stage dialogeStage;
    private Reservation r;
    /**To generate unique bill number*/
    private SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");

    @FXML
    private Label billNumberLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label boxAndRateLabel;
    @FXML
    private Label totalLabel;

    public void setDialogStage(Stage dialogStage){
        this.dialogeStage=dialogStage;
    }

    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void initialize(){

    }

    public void setReservation(Reservation r){

        float total=0.0f;
        /**Generate unique number*/
        Date dNow = new Date();
        String datetime = ft.format(dNow);
        billNumberLabel.setText(datetime);

        customerLabel.setText(r.getCustomer());

        try {
            List<BoxRes> boxResList = reservationService.getBoxReservation(r);
            String s = "";
            for (BoxRes br : boxResList) {
                long days = DAYS.between(r.getRes_from().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), r.getRes_to().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
                float daily_rate = br.getPrice() / days;

                total += br.getPrice();
                s += (br.getBox_id().toString() + "-" + Float.toString(daily_rate) + "€, ");

            }
            boxAndRateLabel.setText(s);
            totalLabel.setText(Float.toString(total) + "€");
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

        this.r=r;
    }

    public void onConfirmButtonClicked(ActionEvent actionEvent){
        try {
            reservationService.pickUpHorse(r);
            dialogeStage.close();
        }catch (ReservationServiceValidationException | ReservationServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onCancelButtonClicked(ActionEvent actionEvent){
        dialogeStage.close();
    }

}
