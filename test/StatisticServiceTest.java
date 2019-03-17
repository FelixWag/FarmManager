import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.dao.impl.BoxDAOJDBC;
import sepm.ss17.e1429339.dao.impl.ReservationDAOJDBC;
import sepm.ss17.e1429339.exceptions.ServiceException;
import sepm.ss17.e1429339.exceptions.StatisticServiceValidationException;
import sepm.ss17.e1429339.service.StatisticService;
import sepm.ss17.e1429339.service.StatisticServiceImpl;
import sepm.ss17.e1429339.util.DBUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class StatisticServiceTest {

    protected StatisticService statisticService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date beginTime;
    private Date endTime;
    private Date from;
    private Date to;
    private Date from2;
    private Date to2;
    private ReservationDAO reservationDAO;
    private BoxDAO boxDAO;

    @Before
    public void setUp() throws Exception {
        // Before the test
        this.statisticService = new StatisticServiceImpl();
        BoxDAO boxDAO = new BoxDAOJDBC();
        ReservationDAO reservationDAO = new ReservationDAOJDBC();
        statisticService.setBoxDAO(boxDAO);
        statisticService.setReservationDAO(reservationDAO);
        try{
            beginTime= sdf.parse("1995-01-01");
            endTime = sdf.parse("4000-12-31");
            from=sdf.parse("2016-02-28");
            to=sdf.parse("2016-03-30");
            from2=sdf.parse("2016-01-01");
            to2=sdf.parse("2016-12-31");

        }catch (ParseException e){}
        DBUtil.getConnection();
        DBUtil.getConnection().setAutoCommit(false);

    }
    @After
    public void tearDown() throws Exception {
        // After the test
        DBUtil.getConnection().rollback();
    }


    /**
     * This test tries to call the method getNumberOfDay with illegal Arguments
     * @throws Exception
     */
    @Test(expected=StatisticServiceValidationException.class)
    public void getNumberOfDaysOfBoxWithWrongArguments() throws Exception {
        statisticService.getNumberOfDays(1,to,from);
    }

    /**
     * This test checks the sum of the booked days of box1
     */
    @Test
    public void getNumberOfDaysOfBox1() {
        int number = 0;
        try {
            number = statisticService.getNumberOfDays(1,from,to);
            assertEquals(32,number);
        } catch (StatisticServiceValidationException | ServiceException e) {}

    }

    /**
     * This test tries to call the method getNumberOfAllDay with illegal Arguments
     * @throws Exception
     */
    @Test(expected=StatisticServiceValidationException.class)
    public void getNumberOfAllDaysOfAllBoxesWithWrongArguments() throws Exception {
        statisticService.getNumberOfAllDays(to,from);
    }

    /**
     * This test checks the sum of the booked days of all boxes
     */
    @Test
    public void getNumberOfDaysOfAllBoxes() {
        int number = 0;
        try {
            number = statisticService.getNumberOfAllDays(beginTime,endTime);
            assertEquals(316,number);
        } catch (StatisticServiceValidationException | ServiceException e) {}

    }

    /**
     * This test tries to call the method getFrequencys with illegal Arguments
     * @throws Exception
     */
    @Test(expected=StatisticServiceValidationException.class)
    public void getFrequencysOfAllDaysOfBox1WithWrongArguments() throws Exception {
        statisticService.getFrequencys(1,to,from);
    }

    /**
     * This test checks frequencies of the booked weekdays of box1
     */
    @Test
    public void getFrequencysOfAllDaysOfBox1(){
        int frequencys[];
        try {
            frequencys = statisticService.getFrequencys(1,beginTime,endTime);
            assertEquals(7,frequencys[0]);
            assertEquals(5,frequencys[1]);
            assertEquals(6,frequencys[2]);
            assertEquals(5,frequencys[3]);
            assertEquals(4,frequencys[4]);
            assertEquals(6,frequencys[5]);
            assertEquals(7,frequencys[6]);
        }catch (StatisticServiceValidationException | ServiceException e) {}
    }

    /**
     * This test tries to call the method getAllFrequencys with illegal Arguments
     * @throws Exception
     */
    @Test(expected=StatisticServiceValidationException.class)
    public void getFrequencysOfAllDaysOfWithWrongArguments() throws Exception {
        statisticService.getAllFrequencys(to,from);
    }

    /**
     * This test checks frequencies of the booked weekdays of all boxes in a specific time range
     */
    @Test
    public void getFrequencysOfAllDaysOfAllBoxes(){
        int frequencys[];
        try {
            frequencys = statisticService.getAllFrequencys(from2,to2);
            assertEquals(16,frequencys[0]);
            assertEquals(15,frequencys[1]);
            assertEquals(15,frequencys[2]);
            assertEquals(12,frequencys[3]);
            assertEquals(12,frequencys[4]);
            assertEquals(13,frequencys[5]);
            assertEquals(16,frequencys[6]);
        }catch (StatisticServiceValidationException | ServiceException e) {}
    }

    /**
     * This test tires compute the price adjustment of a weekday with wrong arguments
     * @throws Exception
     */
    @Test(expected=StatisticServiceValidationException.class)
    public void computePriceAdjustmentIllegalArgument() throws Exception{
        statisticService.priceAdjustmentWeekday(7,from,to,0.0f,true,false);
    }

    /**
     * This test checks the priceAdjustment of the best box on a weekday
     */
    @Test
    public void computePriceAdjustmentBestBoxWeekday(){
        float price[];
        try {
            price=statisticService.priceAdjustmentWeekday(0,beginTime,endTime,2.0f,true,false);

            assertTrue(438.0f==price[0]);
            assertTrue(584.0f==price[1]);
        } catch (StatisticServiceValidationException | ServiceException e) {}

    }

    /**
     * This test tires compute the price adjustment of a time range with wrong arguments
     * @throws Exception
     */
    @Test(expected=StatisticServiceValidationException.class)
    public void computePriceAdjustmentRangeIllegalArgument() throws Exception{
        statisticService.priceAdjustmentRange(to,from,0.0f,true,false);
    }

    /**
     * This test checks the priceAdjustment of the best box in a timerange with percentage
     */
    @Test
    public void computePriceAdjustmentBestBoxTimeRange(){
        float price[];
        try {
            price=statisticService.priceAdjustmentRange(from2,to2,2.0f,true,true);

            assertTrue(350.0f==price[0]);
            assertTrue(357.0f==price[1]);
        } catch (StatisticServiceValidationException | ServiceException e) {}

    }

    /**
     * This test checks the priceAdjustment of the worst box of all the time with value
     */
    @Test
    public void computePriceAdjustmentBestBoxTimeRangeWorst(){
        float price[];
        try {
            price=statisticService.priceAdjustmentRange(beginTime,endTime,2.0f,false,false);

            assertTrue(21.0f==price[0]);
            assertTrue(35.0f==price[1]);
        } catch (StatisticServiceValidationException | ServiceException e) {}

    }

}
