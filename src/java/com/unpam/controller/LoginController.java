package com.unpam.controller;

import com.unpam.model.Enkripsi;
import com.unpam.model.Koneksi;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = Enkripsi.md5(request.getParameter("password"));
        HttpSession session = request.getSession();

        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM user_login WHERE username=? AND password=?");
            ps.setString(1, username); ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                session.setAttribute("username", username);
                session.setAttribute("role", rs.getString("role"));
                response.sendRedirect("NilaiController");
            } else {
                session.setAttribute("pesan", "Username atau password salah!");
                response.sendRedirect("index.jsp");
            }
            con.close();
        } catch (Exception e) {
            session.setAttribute("pesan", "Error: " + e.getMessage());
            response.sendRedirect("index.jsp");
        }
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { processRequest(req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { processRequest(req, resp); }
}