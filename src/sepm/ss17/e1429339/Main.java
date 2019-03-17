package sepm.ss17.e1429339;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.dao.impl.BoxDAOJDBC;
import sepm.ss17.e1429339.dao.impl.ReservationDAOJDBC;
import sepm.ss17.e1429339.gui.MainController;
import sepm.ss17.e1429339.help.Alerts;
import sepm.ss17.e1429339.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sepm.ss17.e1429339.util.DBUtil;

public class Main extends Application {

    final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/MainWindow.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("Wendys Pferdepension");
            primaryStage.setScene(new Scene(root));

            MainController c = fxmlLoader.getController();
            BoxService boxService = new BoxServiceImpl();
            BoxDAO boxDAO = new BoxDAOJDBC();

            ReservationService reservationService = new ReservationServiceImpl();
            ReservationDAO reservationDAO = new ReservationDAOJDBC();

            StatisticService statisticService = new StatisticServiceImpl();

            boxService.setBoxDAO(boxDAO);
            c.setBoxService(boxService);

            reservationService.setReservationDAO(reservationDAO);
            reservationService.setBoxDAO(boxDAO);
            c.setBoxService(boxService);
            c.setReservationService(reservationService);

            statisticService.setBoxDAO(boxDAO);
            statisticService.setReservationDAO(reservationDAO);
            c.setStatisticService(statisticService);

            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                LOGGER.info("Shutting down application");
                DBUtil.closeConnection();
                primaryStage.close();});

        }catch (Exception e){
            LOGGER.error("Database Error:",e);
            Alerts.errorDialog("Error Dialog","Keine Verbindung zur Datenbank","Bitte Datenbank starten");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
