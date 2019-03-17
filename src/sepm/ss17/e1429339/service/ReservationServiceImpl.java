package sepm.ss17.e1429339.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.*;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class ReservationServiceImpl implements ReservationService {

    private ReservationDAO reservationDAO;
    private BoxDAO boxDAO;

    final static Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Override
    public Reservation addReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException {
        if(r==null || r.getRes_from()==null || r.getRes_to()==null ||
                r.getRes_isDeleted()==null|| r.getCustomer()==null || r.getRes_from().after(r.getRes_to())|| r.getCustomer().equals("")){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.save(r);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public boolean deleteReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException {
        if(r==null || r.getRes_id()<0){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.delete(r);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public Reservation getReservation(long id) throws ReservationServiceException, ReservationServiceValidationException {
        if(id<0){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.getReservation(id);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public boolean updateReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException {

        if(r==null || r.getRes_from()==null || r.getRes_to()==null ||
                r.getRes_isDeleted()==null|| r.getCustomer()==null || r.getRes_from().after(r.getRes_to()) || r.getRes_id()<0 || r.getCustomer().equals("")){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.update(r);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }



    @Override
    public List<Reservation> searchall() throws ReservationServiceException {
        try {
            return reservationDAO.searchall();
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public List<Reservation> search(Reservation r) throws ReservationServiceException, ReservationServiceValidationException {
        if(r==null || r.getRes_from().after(r.getRes_to()) || r.getRes_from()==null || r.getRes_to()==null){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.search(r);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public List<BoxRes> getBoxReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException {
        if(r==null || r.getRes_id()<0){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.getBoxReservation(r);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public BoxRes addBoxRes(BoxRes br) throws ReservationServiceException, ReservationServiceValidationException {
        if(br==null || br.getBox_id()==null || br.getPrice()<=0 || br.getBox_id()<0 || br.getRes_id()<0 || br.getHorse().equals("")){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.saveBoxRes(br);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public int boxResCheckAvailable(int box_id, Date from, Date to) throws ReservationServiceException, ReservationServiceValidationException {
        if(box_id<0 || from==null || to==null || from.after(to)){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.boxResCheckAvailable(box_id,from,to);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public float computePrice(int box_id, LocalDate from, LocalDate to) throws ServiceException, ReservationServiceValidationException {
        if(box_id<=0 || from.isAfter(to)){
            throw new ReservationServiceValidationException();
        }
        try {
            float result;

            Box res = boxDAO.getBox(box_id);

            long days = DAYS.between(from, to) + 1;

            result = res.getDaily_rate() * days;
            return result;
        }catch (BoxDAOException e){
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }

    }

    @Override
    public boolean updateBoxRes(BoxRes br) throws ReservationServiceException, ReservationServiceValidationException {
        if(br==null || br.getPrice()<=0 || br.getBox_id()<0 || br.getRes_id()<0
                || br.getBox_id()==null || br.getHorse().equals("")){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.updateBoxRes(br);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public boolean pickUpHorse(Reservation r) throws ReservationServiceException, ReservationServiceValidationException {
        if(r==null || r.getRes_id()<0){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.pickUpRes(r);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public boolean deleteBoxRes(BoxRes br) throws ReservationServiceException, ReservationServiceValidationException {
        if(br==null || br.getBox_id()==null || br.getBox_id()<0 || br.getPrice()<0){
            throw new ReservationServiceValidationException();
        }
        try {
            return reservationDAO.deleteOnlyBoxRes(br);
        } catch (ReservationDAOException e) {
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }
    }

    @Override
    public void setBoxDAO(BoxDAO boxDAO) {this.boxDAO=boxDAO;}

}
