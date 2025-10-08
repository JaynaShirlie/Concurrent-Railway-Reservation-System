package com.rbs.model;

import java.time.LocalDateTime;

public class Reservation {
    public enum Status { CONFIRMED, WAITLISTED, CANCELLED }

    private long id;
    private long ticketId;
    private Status status;
    private LocalDateTime createdAt;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTicketId() { return ticketId; }
    public void setTicketId(long ticketId) { this.ticketId = ticketId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}



