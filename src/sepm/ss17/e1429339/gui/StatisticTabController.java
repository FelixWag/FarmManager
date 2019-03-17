package sepm.ss17.e1429339.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.ReservationServiceException;
import sepm.ss17.e1429339.exceptions.ServiceException;
import sepm.ss17.e1429339.exceptions.StatisticServiceValidationException;
import sepm.ss17.e1429339.help.Alerts;
import sepm.ss17.e1429339.help.days;
import sepm.ss17.e1429339.service.BoxService;
import sepm.ss17.e1429339.service.ReservationService;
import sepm.ss17.e1429339.service.StatisticService;

import javax.swing.text.TableView;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;


public class StatisticTabController {

    final static Logger LOGGER = LoggerFactory.getLogger(StatisticTabController.class);


    private StatisticService statisticService;

    @FXML
    private javafx.scene.control.TableView<Box> tableView;
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
    private BarChart<Integer, Integer> singleBoxBarChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private BarChart<Integer, Integer> singleBoxBarChartDays;
    @FXML
    private RadioButton radioWorst;
    @FXML
    private RadioButton radioBest;
    @FXML
    private RadioButton radioValue;
    @FXML
    private RadioButton radioPercentage;
    @FXML
    private RadioButton radioSpecificDay;
    @FXML
    private RadioButton radioDateRange;
    @FXML
    private DatePicker priceChangeFromDatePicker;
    @FXML
    private DatePicker priceChangeToDatePicker;
    @FXML
    private TextField valueTextField;
    @FXML
    private ChoiceBox<days> choiceDays;
    @FXML
    private CategoryAxis xAxisDays;
    @FXML
    private NumberAxis yAxisDays;
    @FXML
    private Label oldSumLabel;
    @FXML
    private Label newSumLabel;
    @FXML
    private Label bestBoxLabel;
    @FXML
    private Label worstBoxLabel;

    private Stage primaryStage = new Stage();


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date beginTime;
    private Date endTime;

    DecimalFormat df = new DecimalFormat("#.##");


    public void setStatisticService(StatisticService statisticService){
        this.statisticService=statisticService;
        updateTableview();
    }

    public void initialize(){

        box_idCol.setCellValueFactory(new PropertyValueFactory<Box, String>("box_id"));
        windowCol.setCellValueFactory(new PropertyValueFactory<Box, String>("window"));
        litterCol.setCellValueFactory(new PropertyValueFactory<Box, String>("litter"));
        insideCol.setCellValueFactory(new PropertyValueFactory<Box, String>("inside"));
        box_sizeCol.setCellValueFactory(new PropertyValueFactory<Box, String>("box_size"));
        areaCol.setCellValueFactory(new PropertyValueFactory<Box, String>("area"));
        daily_rateCol.setCellValueFactory(new PropertyValueFactory<Box, String>("daily_rate"));

        choiceDays.getItems().setAll(days.values());
        choiceDays.getSelectionModel().selectFirst();

        try{
            beginTime= sdf.parse("1995-01-01");
            endTime = sdf.parse("4000-12-31");

        }catch (ParseException e){
            LOGGER.error("ERROR",e);
        }

    }

    private void updateTableview(){
        try {
            tableView.getItems().setAll(statisticService.searchall());
        } catch (ServiceException e) {
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onFilterButtonClicked(ActionEvent actionEvent){

        try {
            int index = tableView.getSelectionModel().getSelectedIndex();

            if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null && fromDatePicker.getValue().isBefore(toDatePicker.getValue()) && index >= 0) {
                int count = statisticService.getNumberOfDays(tableView.getSelectionModel().getSelectedItem().getBox_id(),
                        Date.from(fromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(toDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

                /**Chart for the bookings*/
                XYChart.Series set1 = new XYChart.Series<>();

                set1.getData().add(new XYChart.Data("Box:" + tableView.getSelectionModel().getSelectedItem().getBox_id().toString(), count));
                singleBoxBarChart.getData().setAll(set1);
            } else {
                Alerts.informationDialog("Information Dialog", "Keine gültigen Datumseingaben oder keine Box ausgewählt", "Bitte Datumseingaben überprüfen oder Box auswählen");
            }
        }catch (StatisticServiceValidationException | ServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }


    public void onFilterAllButtonClicked(ActionEvent actionEvent) {

        try {

            if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null && fromDatePicker.getValue().isBefore(toDatePicker.getValue())) {

                int count = statisticService.getNumberOfAllDays(Date.from(fromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(toDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

                /**Chart for the bookings*/
                XYChart.Series set1 = new XYChart.Series<>();

                set1.getData().add(new XYChart.Data("Alle Boxen:", count));
                singleBoxBarChart.getData().setAll(set1);
            } else {
                Alerts.informationDialog("Information Dialog", "Keine gültigen Datumseingaben", "Bitte Datumseingaben überprüfen");
            }
        }catch (StatisticServiceValidationException | ServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }

    public void onComputeButtonClicked(ActionEvent actionEvent) {
        try {
            if (valueTextField.getText().matches("\\-?\\d*\\.?\\d{1,2}") && Float.parseFloat(valueTextField.getText())<=100.0f && Float.parseFloat(valueTextField.getText())>=-100.0f) {
                if (radioSpecificDay.isSelected()) {
                    /**Choose the right day*/
                    int day = 0;
                    if (choiceDays.getValue().toString().equals("Montag")) {day = 0;}
                    if (choiceDays.getValue().toString().equals("Dienstag")) {day = 1;}
                    if (choiceDays.getValue().toString().equals("Mittwoch")) {day = 2;}
                    if (choiceDays.getValue().toString().equals("Donnerstag")) {day = 3;}
                    if (choiceDays.getValue().toString().equals("Freitag")) {day = 4;}
                    if (choiceDays.getValue().toString().equals("Samstag")) {day = 5;}
                    if (choiceDays.getValue().toString().equals("Sonntag")) {day = 6;}

                    float[] oldAndNew = statisticService.priceAdjustmentWeekday(day, beginTime, endTime, Float.parseFloat(valueTextField.getText()), radioBest.isSelected(), radioPercentage.isSelected());

                    oldSumLabel.setText(Float.toString(oldAndNew[0]));
                    if(oldAndNew[1]<0.0f){
                        newSumLabel.setText("Zu großer Anpassung");
                    }else{
                        newSumLabel.setText(df.format(oldAndNew[1]));
                    }

                    if(radioBest.isSelected()){
                        bestBoxLabel.setText(Integer.toString(statisticService.getBestBoxWeekday(day,beginTime,endTime)));
                        worstBoxLabel.setText("-");
                    }else if(radioWorst.isSelected()){
                        worstBoxLabel.setText(Integer.toString(statisticService.getWorstBoxWeekday(day,beginTime,endTime)));
                        bestBoxLabel.setText("-");
                    }
                } else if (radioDateRange.isSelected()) {
                    /**Check the time ranges*/
                    if (priceChangeFromDatePicker.getValue() != null && priceChangeToDatePicker.getValue() != null && priceChangeFromDatePicker.getValue().isBefore(priceChangeToDatePicker.getValue())) {
                        Date from = Date.from(priceChangeFromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        Date to = Date.from(priceChangeToDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        float[] oldAndNew = statisticService.priceAdjustmentRange(from, to, Float.parseFloat(valueTextField.getText()), radioBest.isSelected(), radioPercentage.isSelected());

                        oldSumLabel.setText(Float.toString(oldAndNew[0]));
                        if(oldAndNew[1]<0.0f){
                            newSumLabel.setText("Zu große Anpassung");
                        }else{
                            newSumLabel.setText(df.format(oldAndNew[1]));
                        }
                        if(radioBest.isSelected()){
                            bestBoxLabel.setText(Integer.toString(statisticService.getBestBoxRange(from,to)));
                            worstBoxLabel.setText("-");
                        }else if(radioWorst.isSelected()){
                            worstBoxLabel.setText(Integer.toString(statisticService.getWorstBoxRange(from,to)));
                            bestBoxLabel.setText("-");
                        }
                    } else {
                        Alerts.informationDialog("Information Dialog", "Keine gültigen Datumseingaben", "Bitte Datumseingaben überprüfen");
                    }
                }
            } else {
                Alerts.informationDialog("Information Dialog", "Kein gültiger Zahlenwert", "Bitte gültigen Zahlenwert eingeben");
            }
        }catch (StatisticServiceValidationException | ServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }
    }


    public void onWeekDayAllButtonClicked(ActionEvent actionEvent) {

        try {
            XYChart.Series setDays = new XYChart.Series<>();

            setDays.getData().add(new XYChart.Data("Montag", statisticService.getAllFrequencys(beginTime, endTime)[0]));
            setDays.getData().add(new XYChart.Data("Dienstag", statisticService.getAllFrequencys(beginTime, endTime)[1]));
            setDays.getData().add(new XYChart.Data("Mittwoch", statisticService.getAllFrequencys(beginTime, endTime)[2]));
            setDays.getData().add(new XYChart.Data("Donnerstag", statisticService.getAllFrequencys(beginTime, endTime)[3]));
            setDays.getData().add(new XYChart.Data("Freitag", statisticService.getAllFrequencys(beginTime, endTime)[4]));
            setDays.getData().add(new XYChart.Data("Samstag", statisticService.getAllFrequencys(beginTime, endTime)[5]));
            setDays.getData().add(new XYChart.Data("Sonntag", statisticService.getAllFrequencys(beginTime, endTime)[6]));
            singleBoxBarChartDays.getData().setAll(setDays);
        }catch (StatisticServiceValidationException | ServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }


    }


    public void onWeekDayButtonClicked(ActionEvent actionEvent) {

        try {
            int index = tableView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                XYChart.Series setDays = new XYChart.Series<>();
                Box b = tableView.getSelectionModel().getSelectedItem();

                setDays.getData().add(new XYChart.Data("Montag", statisticService.getFrequencys(b.getBox_id(), beginTime, endTime)[0]));
                setDays.getData().add(new XYChart.Data("Dienstag", statisticService.getFrequencys(b.getBox_id(), beginTime, endTime)[1]));
                setDays.getData().add(new XYChart.Data("Mittwoch", statisticService.getFrequencys(b.getBox_id(), beginTime, endTime)[2]));
                setDays.getData().add(new XYChart.Data("Donnerstag", statisticService.getFrequencys(b.getBox_id(), beginTime, endTime)[3]));
                setDays.getData().add(new XYChart.Data("Freitag", statisticService.getFrequencys(b.getBox_id(), beginTime, endTime)[4]));
                setDays.getData().add(new XYChart.Data("Samstag", statisticService.getFrequencys(b.getBox_id(), beginTime, endTime)[5]));
                setDays.getData().add(new XYChart.Data("Sonntag", statisticService.getFrequencys(b.getBox_id(), beginTime, endTime)[6]));
                singleBoxBarChartDays.getData().setAll(setDays);
            } else {
                Alerts.informationDialog("Information Dialog", "Keine Box ausgewählt", "Bitte Box auswählen");
            }
        }catch (StatisticServiceValidationException | ServiceException e){
            LOGGER.error("ERROR",e);
            Alerts.errorDialog("Error","Ein unerwarteter Fehler ist aufgetreten","-");
        }

    }

}
