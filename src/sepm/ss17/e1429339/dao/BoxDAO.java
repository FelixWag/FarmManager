package sepm.ss17.e1429339.dao;


import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.BoxDAOException;

import java.util.Date;
import java.util.List;

public interface BoxDAO {

    /**
     * This method inserts a box into to box table
     * @param b is the box which should be inserted
     * @return Box which is inserted
     * @throws BoxDAOException if SQL-Exception occurs
     */
    public Box save(Box b) throws BoxDAOException;


    /**
     * This method deletes a box from the box table
     * @param b is the box which should be deleted
     * @return if the deletion was successful
     * @throws BoxDAOException if SQL-Exception occurs
     */
    public boolean delete(Box b) throws BoxDAOException;

    /**
     * This method searches all Boxes
     * @return a List of all boxes
     * @throws BoxDAOException if SQL-Exception occurs
     */
    public List<Box> searchall() throws BoxDAOException;

    /**
     * Gets the box with the corresponding id
     * @param id the id of the box
     * @return the box which is searched
     * @throws BoxDAOException if SQL-Exception occurs
     */
    public Box getBox(Integer id) throws BoxDAOException;

    /**
     * Updates a box with the values of the parameterobject
     * @param b the Box with the values
     * @return if the update was successful
     * @throws BoxDAOException if SQL-Exception occurs
     */
    public boolean update(Box b) throws BoxDAOException;

    /**
     * Searches all boxes with the specified properties
     * @param b the Box with the desired properties
     * @return if the update was successful
     * @throws BoxDAOException if SQL-Exception occurs
     */
    public List<Box> search(Box b) throws BoxDAOException;

    /**
     * Returns the number of reservation of a Box_id in the timerange
     * @param box_id the box id
     * @param from the startdate
     * @param to the enddate
     * @return the number of reservations of this box
     * @throws BoxDAOException if SQL-Exception occurs
     */
    public int getNumberOfReservations(int box_id, Date from, Date to) throws BoxDAOException;

}
