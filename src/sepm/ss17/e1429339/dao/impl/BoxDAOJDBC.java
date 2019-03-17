package sepm.ss17.e1429339.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.BoxDAOException;
import sepm.ss17.e1429339.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;




public class BoxDAOJDBC implements BoxDAO {

    final static Logger LOGGER = LoggerFactory.getLogger(BoxDAOJDBC.class);

    private String boxInsertString = "INSERT INTO box (photo, window, litter, inside, box_size, area, daily_rate, isDeleted) VALUES (?,?,?,?,?,?,?,?)";
    private String boxGetString = "SELECT * FROM box b WHERE b.box_id=?";
    private String boxDeleteString = "UPDATE box SET isDeleted=TRUE WHERE box_id=?";
    private String boxUpdateString = "UPDATE box SET photo=?, window=?, litter=?, inside=?, box_size=?, area=?, daily_rate=?, isDeleted=? WHERE box_id=?";
    private String boxSearchAllString = "SELECT * FROM box WHERE isDeleted=FALSE";

    private String boxGetNumberOfReservationsString = "SELECT b.box_id,COUNT(b.box_id) AS resCount FROM box b NATURAL JOIN box_res NATURAL JOIN reservation WHERE b.box_id=? AND res_from <=? AND res_to >=? AND b.isDeleted=FALSE GROUP BY b.box_id";

    @Override
    public Box save(Box b) throws BoxDAOException {

        if(b==null || b.getDaily_rate()==null || b.isWindow()==null || b.isInside()==null
                ||b.getLitter()==null || b.getBox_size()==null || b.getArea()==null || b.getDaily_rate()==null ||
                b.isDeleted()==null || !Float.toString(b.getDaily_rate()).matches("\\d*\\.?\\d{1,2}") ||
                !Float.toString(b.getBox_size()).matches("\\d*\\.?\\d{1,2}") || b.getArea().equals("") || b.getLitter().equals("")){
            throw new IllegalArgumentException();
        }

        try {
            Box temp=new Box();
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxInsertString, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, b.getPhoto());
            ps.setBoolean(2, b.isWindow());
            ps.setString(3,b.getLitter());
            ps.setBoolean(4,b.isInside());
            ps.setFloat(5,b.getBox_size());
            ps.setString(6,b.getArea());
            ps.setFloat(7,b.getDaily_rate());
            ps.setBoolean(8,b.isDeleted());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()) {
                b.setBox_id(generatedKeys.getInt(1));

                LOGGER.info("Saving box with id {}",b.getBox_id());

                temp=b;
            }
            generatedKeys.close();
            ps.close();
            return temp;
        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new BoxDAOException();
        }

    }


    @Override
    public boolean delete(Box b) throws BoxDAOException {

        if(b==null || b.getBox_id()==null || b.getBox_id()<0){
            throw new IllegalArgumentException();
        }

        int success;
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxDeleteString);
            ps.setInt(1,b.getBox_id());


            LOGGER.info("Deleting Box with id {}", b.getBox_id());
            success = ps.executeUpdate();

            if(success == 1){
                ps.close();
                return true;
            }else{
                ps.close();
                return false;
            }

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new BoxDAOException();
        }
    }

    @Override
    public List<Box> searchall() throws BoxDAOException {

        LOGGER.info("Searching all Boxes");

        List<Box> listBox = new LinkedList<Box>();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxSearchAllString);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Box b = new Box(rs.getString(2),rs.getBoolean(3),rs.getString(4),
                        rs.getBoolean(5),rs.getFloat(6),rs.getString(7),rs.getBoolean(9),rs.getFloat(8));
                b.setBox_id(rs.getInt(1));
                listBox.add(b);
            }
            ps.close();
            rs.close();

            return listBox;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new BoxDAOException();
        }

    }

    @Override
    public Box getBox(Integer id) throws BoxDAOException {

        if(id==null || id<0){
            throw new IllegalArgumentException();
        }
        try {
            Box b = new Box();
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxGetString);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                b.setBox_id(rs.getInt(1));
                b.setPhoto(rs.getString(2));
                b.setWindow(rs.getBoolean(3));
                b.setLitter(rs.getString(4));
                b.setInside(rs.getBoolean(5));
                b.setBox_size(rs.getFloat(6));
                b.setArea(rs.getString(7));
                b.setDaily_rate(rs.getFloat(8));
                b.setDeleted(rs.getBoolean(9));
            }
            rs.close();
            ps.close();
            return b;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new BoxDAOException();
        }

    }


    @Override
    public boolean update(Box b) throws BoxDAOException {

        if(b==null || b.getDaily_rate()==null || b.isWindow()==null || b.isInside()==null
                ||b.getLitter()==null || b.getBox_size()==null || b.getArea()==null || b.getDaily_rate()==null ||
                b.isDeleted()==null|| !Float.toString(b.getDaily_rate()).matches("\\d*\\.?\\d{1,2}") ||
                !Float.toString(b.getBox_size()).matches("\\d*\\.?\\d{1,2}") || b.getBox_id()<0 || b.getArea().equals("") || b.getLitter().equals("")){
            throw new IllegalArgumentException();
        }

        int success;
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxUpdateString);
            ps.setString(1, b.getPhoto());
            ps.setBoolean(2, b.isWindow());
            ps.setString(3,b.getLitter());
            ps.setBoolean(4,b.isInside());
            ps.setFloat(5,b.getBox_size());
            ps.setString(6,b.getArea());
            ps.setFloat(7,b.getDaily_rate());
            ps.setBoolean(8,b.isDeleted());
            ps.setInt(9,b.getBox_id());
            LOGGER.info("Updating Box with id {}", b.getBox_id());
            success = ps.executeUpdate();

            if(success==1){
                ps.close();
                return true;
            }else{
                ps.close();
                return false;
            }

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new BoxDAOException();
        }
    }

    @Override
    public List<Box> search(Box b) throws BoxDAOException {

        if(b==null || (b.getBox_id()==null && b.getDaily_rate()==null && b.isWindow()==null && b.isInside()==null
                && b.getLitter()==null && b.getBox_size()==null && b.getArea()==null && b.getDaily_rate()==null &&
                b.isDeleted()==null)){
            throw new IllegalArgumentException();
        }

        String boxSearchString = "SELECT * FROM box WHERE isDeleted=FALSE AND ";

        List<Box> listBox = new LinkedList<Box>();
        Map<String,Object> map = new TreeMap<String,Object>();
        if(b.getBox_id()!=null){
            map.put("box_id",b.getBox_id());
        }
        if(b.isWindow()!=null){
            map.put("window",b.isWindow());
        }
        if(b.getLitter()!=null){
            map.put("litter",b.getLitter());
        }
        if(b.isInside()!=null){
            map.put("inside",b.isInside());
        }
        if(b.getBox_size()!=null){
            map.put("box_size",b.getBox_size());
        }
        if(b.getArea()!=null){
            map.put("area",b.getArea());
        }
        if(b.getDaily_rate()!=null){
            map.put("daily_rate",b.getDaily_rate());
        }

        try {
            int mapSize=0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                mapSize++;
                if(mapSize<map.size()) {
                    boxSearchString += entry.getKey() + "=? AND ";
                }else{
                    boxSearchString += entry.getKey() + "=?";
                }

            }

            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxSearchString);
            int index=0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                index++;
                if(entry.getKey().equals("box_id")){
                    ps.setInt(index,(Integer) entry.getValue());
                }
                if(entry.getKey().equals("window") || entry.getKey().equals("inside")){
                    ps.setBoolean(index,(Boolean) entry.getValue());
                }
                if(entry.getKey().equals("litter") || entry.getKey().equals("area")){
                    ps.setString(index,(String) entry.getValue());
                }
                if(entry.getKey().equals("daily_rate") || entry.getKey().equals("box_size")){
                    ps.setFloat(index,(Float) entry.getValue());
                }
            }

            LOGGER.info("Filtering following Boxes" + boxSearchString);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Box result = new Box(rs.getString(2),rs.getBoolean(3),rs.getString(4),
                        rs.getBoolean(5),rs.getFloat(6),rs.getString(7),rs.getBoolean(9),rs.getFloat(8));
                result.setBox_id(rs.getInt(1));
                listBox.add(result);
            }
            ps.close();
            rs.close();
            return listBox;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new BoxDAOException();
        }
    }

    @Override
    public int getNumberOfReservations(int box_id, Date from, Date to) throws BoxDAOException {

        if(box_id<0 || from.after(to) || from==null || to==null){
            throw new IllegalArgumentException();
        }

        try {
            int numberReservations=0;
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxGetNumberOfReservationsString);
            ps.setInt(1,box_id);
            ps.setDate(2,new java.sql.Date(to.getTime()));
            ps.setDate(3,new java.sql.Date(from.getTime()));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                numberReservations=rs.getInt(2);
            }
            rs.close();
            ps.close();

            LOGGER.info("Box with id "+box_id+" has reservations:"+numberReservations);

            return numberReservations;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new BoxDAOException();
        }

    }
}
