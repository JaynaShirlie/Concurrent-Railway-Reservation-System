package com.rbs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {}

    // MySQL Configuration - Update these with your credentials
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "Railway_Booking_System";
    private static final String USERNAME = "root"; // Change to your MySQL username
    private static final String PASSWORD = ""; // Change to your MySQL password
    
    private static final String URL = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", 
                                                   HOST, PORT, DATABASE);

    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. Please add mysql-connector-java to classpath.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // Set auto-commit to false for transaction management
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to MySQL database. Please check your credentials and ensure MySQL is running.", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("Error rolling back transaction: " + e.getMessage());
            }
        }
    }

    public static void commit(Connection conn) {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                System.err.println("Error committing transaction: " + e.getMessage());
                rollback(conn);
            }
        }
    }
}


