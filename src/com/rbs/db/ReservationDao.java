package com.rbs.db;

import com.rbs.model.Reservation;
import com.rbs.model.Ticket;

public interface ReservationDao {
    boolean createReservation(Ticket ticket, Reservation reservation);
    boolean cancelReservation(long reservationId);
}



