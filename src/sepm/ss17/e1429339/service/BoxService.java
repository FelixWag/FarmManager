package sepm.ss17.e1429339.service;


import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.BoxServiceException;
import sepm.ss17.e1429339.exceptions.BoxServiceValidationException;

import java.util.Date;
import java.util.List;

public interface BoxService {

    /**
     * Adds a Box to the database
     * @param b the box which should be saved
     * @return the box which should be saved
     * @throws BoxServiceException if an exception in the persistence layer occurs
     * @throws BoxServiceValidationException if parameter are invalid
     */
    public Box addBox(Box b) throws BoxServiceException, BoxServiceValidationException;

    /**
     * Deletes a box
     * @param b the box which should be deleted
     * @return boolean if the deletion was successful
     * @throws BoxServiceException if an exception in the persistence layer occurs
     * @throws BoxServiceValidationException if parameter are invalid
     */
    public boolean deleteBox(Box b) throws BoxServiceException, BoxServiceValidationException;

    /**
     * Searches the box with the given id
     * @param id the id of the Box
     * @return the box wirh the id
     * @throws BoxServiceException if an exception in the persistence layer occurs
     * @throws BoxServiceValidationException if parameter are invalid
     */
    public Box getBox(Integer id) throws BoxServiceException, BoxServiceValidationException;

    /**
     * Updates a box with the properties of the parameter
     * @param b the box with the properties
     * @return if the update was successful
     * @throws BoxServiceException if an exception  in the persistence layer occurs
     * @throws BoxServiceValidationException if parameter are invalid
     */
    public boolean updateBox(Box b) throws BoxServiceException, BoxServiceValidationException;

    /**
     * This method sets the BoxDAO for the reservationservice
     * @param boxDAO for setting the boxDAO
     */
    public void setBoxDAO(BoxDAO boxDAO);

    /**
     * Searches all boxes
     * @return a list of all boxes
     * @throws BoxServiceException if an exception  in the persistence layer occurs
     */
    public List<Box> searchall() throws BoxServiceException;

    /**
     * Searches all the Boxes with the properties of the box
     * @param b the box with the properties
     * @return a list of all the matching boxes
     * @throws BoxServiceException if an exception  in the persistence layer occurs
     * @throws BoxServiceValidationException if parameter are invalid
     */
    public List<Box> search(Box b) throws BoxServiceException, BoxServiceValidationException;


    /**
     * This method returns the number of reservations to a specific box_id
     * @param box_id the Box ID
     * @param from the startdate
     * @param to the enddate
     * @return the number of reservations
     * @throws BoxServiceException if an exception in the persistence layer occurs
     * @throws BoxServiceValidationException if parameter are invalid
     */
    public int getNumberOfReservations(int box_id, Date from, Date to) throws BoxServiceException, BoxServiceValidationException;

}
