package com.unpam.model;

import java.sql.*;

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/db_nilai";
    private static final String USER = "root";
    private static final String PASS = ""; // kosong kalau XAMPP default

    public static Connection getKoneksi() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Error koneksi: " + e.getMessage());
        }
        return con;
    }
}