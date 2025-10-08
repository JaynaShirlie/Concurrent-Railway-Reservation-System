package com.rbs.db.impl;

import com.rbs.db.Database;
import com.rbs.db.ReservationDao;
import com.rbs.model.Reservation;
import com.rbs.model.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDaoImpl implements ReservationDao {
    
    @Override
    public boolean createReservation(Ticket ticket, Reservation reservation) {
        Connection conn = null;
        PreparedStatement ticketStmt = null;
        PreparedStatement reservationStmt = null;
        
        try {
            conn = Database.getConnection();
            
            // Insert ticket first
            String ticketSql = "INSERT INTO tickets (user_id, train_id, travel_class, category, journey_date, passengers, fare) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ticketStmt = conn.prepareStatement(ticketSql, Statement.RETURN_GENERATED_KEYS);
            
            ticketStmt.setLong(1, ticket.getUserId());
            ticketStmt.setLong(2, ticket.getTrainId());
            ticketStmt.setString(3, ticket.getTravelClass());
            ticketStmt.setString(4, ticket.getCategory());
            ticketStmt.setDate(5, Date.valueOf(ticket.getJourneyDate()));
            ticketStmt.setString(6, String.join(",", ticket.getPassengers()));
            ticketStmt.setDouble(7, ticket.getFare());
            
            int ticketRows = ticketStmt.executeUpdate();
            if (ticketRows == 0) {
                Database.rollback(conn);
                return false;
            }
            
            // Get generated ticket ID
            ResultSet ticketKeys = ticketStmt.getGeneratedKeys();
            if (ticketKeys.next()) {
                ticket.setId(ticketKeys.getLong(1));
            }
            
            // Insert reservation
            String reservationSql = "INSERT INTO reservations (ticket_id, status, created_at) VALUES (?, ?, ?)";
            reservationStmt = conn.prepareStatement(reservationSql, Statement.RETURN_GENERATED_KEYS);
            
            reservationStmt.setLong(1, ticket.getId());
            reservationStmt.setString(2, reservation.getStatus().name());
            reservationStmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            
            int reservationRows = reservationStmt.executeUpdate();
            if (reservationRows > 0) {
                ResultSet reservationKeys = reservationStmt.getGeneratedKeys();
                if (reservationKeys.next()) {
                    reservation.setId(reservationKeys.getLong(1));
                }
                Database.commit(conn);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            Database.rollback(conn);
        } finally {
            closeResources(conn, ticketStmt, null);
            if (reservationStmt != null) {
                try { reservationStmt.close(); } catch (SQLException e) { /* ignore */ }
            }
        }
        return false;
    }
    
    @Override
    public boolean cancelReservation(long reservationId) {
        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, reservationId);
            
            int rowsAffected = stmt.executeUpdate();
            Database.commit(conn);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelling reservation: " + e.getMessage());
            Database.rollback(conn);
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }
    
    public List<Reservation> findByUserId(long userId) {
        String sql = "SELECT r.*, t.* FROM reservations r JOIN tickets t ON r.ticket_id = t.id WHERE t.user_id = ? ORDER BY r.created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();
        
        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding reservations by user: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return reservations;
    }
    
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setTicketId(rs.getLong("ticket_id"));
        reservation.setStatus(Reservation.Status.valueOf(rs.getString("status")));
        reservation.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return reservation;
    }
    
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { /* ignore */ }
        }
        if (conn != null) {
            Database.closeConnection(conn);
        }
    }
}
