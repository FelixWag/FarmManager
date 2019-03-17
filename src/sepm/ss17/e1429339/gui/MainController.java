package sepm.ss17.e1429339.gui;


import javafx.fxml.FXML;
import sepm.ss17.e1429339.gui.ReservationTabController;
import sepm.ss17.e1429339.gui.StatisticTabController;
import sepm.ss17.e1429339.service.BoxService;
import sepm.ss17.e1429339.service.ReservationService;
import sepm.ss17.e1429339.service.StatisticService;

public class MainController {

    private BoxService boxService;
    private ReservationService reservationService;
    private StatisticService statisticService;

    @FXML
    private BoxTabController boxTabController;

    @FXML
    private ReservationTabController reservationTabController;

    @FXML
    private StatisticTabController statisticTabController;


    public void setBoxService(BoxService boxService) {
        this.boxService = boxService;
        boxTabController.setBoxService(boxService);
    }

    public void setReservationService(ReservationService reservationService){
        this.reservationService = reservationService;
        reservationTabController.setReservationService(reservationService);
        reservationTabController.setBoxService(boxService);
    }

    public void setStatisticService(StatisticService statisticService){
        this.statisticService = statisticService;
        statisticTabController.setStatisticService(statisticService);
    }



}
