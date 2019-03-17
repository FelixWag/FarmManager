import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.dao.impl.BoxDAOJDBC;
import sepm.ss17.e1429339.entities.Box;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sepm.ss17.e1429339.exceptions.BoxDAOException;
import sepm.ss17.e1429339.util.DBUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class BoxDAOTest {

    protected BoxDAO boxDAO;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date beginTime;
    private Date endTime;

    @Before
    public void setUp() throws Exception {
        // Before the test
        this.boxDAO = new BoxDAOJDBC();
        try{
            beginTime= sdf.parse("1995-01-01");
            endTime = sdf.parse("4000-12-31");

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
     * This test tries to insert an illegal value into the Database. Should throw an IllegalArgumentException.
     */
    @Test(expected=IllegalArgumentException.class)
    public void saveBoxWithNull(){
        try {
            boxDAO.save(null);
        } catch (BoxDAOException e) {}
    }

    /**
     * This Test creates a Box and inserts it into the database.
     */
    @Test
    public void saveBoxWithValid(){
        Box b = new Box(null,true,"Stroh",false,20.2f,"A",false,10.8f);

        List<Box> listBox = null;
        try {
            listBox = boxDAO.searchall();

            assertFalse(listBox.contains(b));

            boxDAO.save(b);

            listBox=boxDAO.searchall();

            assertTrue(listBox.contains(b));

        } catch (BoxDAOException e) {}

    }

    /**
     * This test tries to delete an illegal value into the Database. Should throw an IllegalArgumentException.
     */
    @Test(expected=IllegalArgumentException.class)
    public void deleteWithNull(){
        try {
            boxDAO.delete(null);
        } catch (BoxDAOException e) {}
    }

    /**
     * This test checks if the correct box gets deleted
     */
    @Test
    public void deleteBoxWithValid(){

        try {
            Box b = new Box(null,false,"Stroh",false,2.2f,"C",false,6.0f);
            b.setBox_id(9);

            List<Box> listBox = boxDAO.searchall();
            assertTrue(listBox.contains(b));

            boxDAO.delete(b);

            listBox=boxDAO.searchall();

            assertFalse(listBox.contains(b));

        } catch (BoxDAOException e) {}

    }

    /**
     * This test checks if all boxes from searchall are returned
     */
    @Test
    public void searchAllNumberTest(){
        try {
            List<Box> list = boxDAO.searchall();
            assertEquals(list.size(),10);
        } catch (BoxDAOException e) {}

    }

    /**
     * This test checks the getBox Method with an illegal argument. Should return an IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void getBoxNull(){
        try {
            boxDAO.getBox(null);
        } catch (BoxDAOException e) {}
    }

    /**
     * This test checks if the right box is returned from the getBoxMethod
     */
    @Test
    public void getBoxValid(){
        try {
            Box b1 = new Box(null,false,"Stroh",false,2.2f,"C",false,6.0f);
            b1.setBox_id(9);
            Box b2 = boxDAO.getBox(9);
            assertEquals(b1,b2);
            //assertEquals(b.getArea().to);
        } catch (BoxDAOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This test checks the update method, if a box is updated with null values. Should return an IllegalArgument Exception
     */
    @Test(expected=IllegalArgumentException.class)
    public void updateBoxNull(){
        Box b = new Box();
        b.setArea("Test");
        try {
            boxDAO.delete(b);
        } catch (BoxDAOException e) {}

    }

    /**
     * This test checks if the update of a Box is correctly executed
     */
    @Test
    public void updateBoxValid(){
        Box b1 = new Box(null,false,"Pellets",false,2.2f,"A",false,6.3f);
        b1.setBox_id(9);
        try {
            boxDAO.update(b1);
            Box b2 = boxDAO.getBox(9);
            assertEquals(b1,b2);
        } catch (BoxDAOException e) {}
    }

    /**
     * This test checks the search method with Illegal arguments. Should return an IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void searchBoxNull(){
        try {
            boxDAO.search(null);
        } catch (BoxDAOException e) {}

    }

    /**
     * This test checks the search method; and searches all Boxes in area A. Should return 3 boxes.
     */
    @Test
    public void searchBoxValid(){
        Box b1= new Box(null,true,"Stroh",false,10.0f,"A",false,10.0f);
        b1.setBox_id(1);
        Box b2= new Box(null,true,"Stroh",false,4.3f,"A",false,4.0f);
        b2.setBox_id(5);
        Box b3= new Box(null,false,"Stroh",true,7.7f,"A",false,9.0f);
        b3.setBox_id(10);
        Box searchBox=new Box();
        searchBox.setArea("A");
        try {
            List<Box> listBox = boxDAO.search(searchBox);

            assertTrue(listBox.contains(b1));
            assertTrue(listBox.contains(b2));
            assertTrue(listBox.contains(b3));
            assertEquals(listBox.size(),3);
        } catch (BoxDAOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This test checks the number of reservations with a wrong Box ID
     */
    @Test(expected=IllegalArgumentException.class)
    public void numberOfReservationsInvalid(){
        try {
            boxDAO.getNumberOfReservations(-1,null,null);
        } catch (BoxDAOException e) {}
    }

    /**
     * This test checks the number of reservation for the box id 9
     */
    @Test
    public void numberOfReservationsValid(){
        try {
            int number = boxDAO.getNumberOfReservations(9,beginTime,endTime);
            assertEquals(number,2);
        } catch (BoxDAOException e) {}
    }



}