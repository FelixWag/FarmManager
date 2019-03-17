import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.dao.impl.BoxDAOJDBC;
import sepm.ss17.e1429339.dao.impl.ReservationDAOJDBC;
import sepm.ss17.e1429339.exceptions.ReservationServiceException;
import sepm.ss17.e1429339.exceptions.ReservationServiceValidationException;
import sepm.ss17.e1429339.exceptions.ServiceException;
import sepm.ss17.e1429339.service.ReservationService;
import sepm.ss17.e1429339.service.ReservationServiceImpl;
import sepm.ss17.e1429339.util.DBUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.*;

public class ReservationServiceTest {

    protected ReservationService reservationService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date from;
    private Date to;
    private ReservationDAO reservationDAO;
    private BoxDAO boxDAO;

    @Before
    public void setUp() throws Exception {
        // Before the test
        this.reservationService = new ReservationServiceImpl();
        ReservationDAO reservationDAO = new ReservationDAOJDBC();
        BoxDAO boxDAO = new BoxDAOJDBC();
        reservationService.setReservationDAO(reservationDAO);
        reservationService.setBoxDAO(boxDAO);
        try{
            from=sdf.parse("2016-02-28");
            to=sdf.parse("2016-03-30");

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
     * This test checks the computePrice method with Illegal arguments. Should throw an ReservationServiceValidationException
     * @throws Exception
     */
    @Test(expected=ReservationServiceValidationException.class)
    public void computePriceIllegalArgument() throws Exception{
        reservationService.computePrice(1,to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * This test checks if the computation of the price of box1 is right
     */
    @Test
    public void computePriceofBox1() {
        float price = 0.0f;
        try {
            price = reservationService.computePrice(1,from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            assertTrue(320.0f==price);
        } catch (ReservationServiceValidationException | ServiceException e) {}

    }

    /**
     * This test checks the checkAvailableBoxRes method with Illegal arguments. Should throw an ReservationServiceValidationException
     * @throws Exception
     */
    @Test(expected=ReservationServiceValidationException.class)
    public void checkBoxResAvailableIllegalArgument() throws Exception{
        reservationService.boxResCheckAvailable(1,to,from);
    }

    /**
     * This test checks if Box1 is already booked in the time range
     */
    @Test
    public void checkBoxResAvailableForBox1() {
        int bookedTimes;
        try {
            bookedTimes = reservationService.boxResCheckAvailable(1,from,to);
            assertTrue(bookedTimes>=1);
        } catch (ReservationServiceValidationException | ServiceException e) {}

    }


}
