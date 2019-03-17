package sepm.ss17.e1429339.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.BoxDAOException;
import sepm.ss17.e1429339.exceptions.BoxServiceException;
import sepm.ss17.e1429339.exceptions.BoxServiceValidationException;

import java.util.Date;
import java.util.List;


public class BoxServiceImpl implements BoxService {

    private BoxDAO boxDAO;

    final static Logger LOGGER = LoggerFactory.getLogger(BoxServiceImpl.class);


    @Override
    public Box addBox(Box b) throws BoxServiceException, BoxServiceValidationException {

        if(b==null || b.getDaily_rate()==null || b.isWindow()==null || b.isInside()==null
                ||b.getLitter()==null || b.getBox_size()==null || b.getArea()==null || b.getDaily_rate()==null ||
                b.isDeleted()==null || !Float.toString(b.getDaily_rate()).matches("\\d*\\.?\\d{1,2}") ||
                !Float.toString(b.getBox_size()).matches("\\d*\\.?\\d{1,2}")|| b.getArea().equals("") || b.getLitter().equals("")){
            throw new BoxServiceValidationException();
        }

        try {
            return boxDAO.save(b);
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }

    @Override
    public boolean deleteBox(Box b) throws BoxServiceException, BoxServiceValidationException {
        if(b==null || b.getBox_id()==null || b.getBox_id()<0){
            throw new BoxServiceValidationException();
        }

        try {
            return boxDAO.delete(b);
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }

    @Override
    public Box getBox(Integer id) throws BoxServiceException, BoxServiceValidationException {
        if(id==null || id<0){
            throw new BoxServiceValidationException();
        }
        try {
            return boxDAO.getBox(id);
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }

    @Override
    public boolean updateBox(Box b) throws BoxServiceException, BoxServiceValidationException {
        if(b==null || b.getDaily_rate()==null || b.isWindow()==null || b.isInside()==null
                ||b.getLitter()==null || b.getBox_size()==null || b.getArea()==null || b.getDaily_rate()==null ||
                b.isDeleted()==null|| !Float.toString(b.getDaily_rate()).matches("\\d*\\.?\\d{1,2}") ||
                !Float.toString(b.getBox_size()).matches("\\d*\\.?\\d{1,2}") || b.getBox_id()<0|| b.getArea().equals("") || b.getLitter().equals("")){
            throw new BoxServiceValidationException();
        }
        try {
            return boxDAO.update(b);
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }

    @Override
    public void setBoxDAO(BoxDAO boxDAO) {
        this.boxDAO = boxDAO;
    }

    @Override
    public List<Box> searchall() throws BoxServiceException {
        try {
            return boxDAO.searchall();
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }

    @Override
    public List<Box> search(Box b) throws BoxServiceException, BoxServiceValidationException {
        if(b==null || (b.getBox_id()==null && b.getDaily_rate()==null && b.isWindow()==null && b.isInside()==null
                && b.getLitter()==null && b.getBox_size()==null && b.getArea()==null && b.getDaily_rate()==null &&
                b.isDeleted()==null)){
            throw new BoxServiceValidationException();
        }
        try {
            return boxDAO.search(b);
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }

    @Override
    public int getNumberOfReservations(int box_id, Date from, Date to) throws BoxServiceException, BoxServiceValidationException {
        if(box_id<0|| from.after(to) || from==null || to==null){
            throw new BoxServiceValidationException();
        }
        try {
            return boxDAO.getNumberOfReservations(box_id,from,to);
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }
}
