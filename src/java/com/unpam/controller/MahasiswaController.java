package com.unpam.controller;

import com.unpam.model.Mahasiswa;
import com.unpam.model.Enkripsi;
import com.unpam.model.Koneksi;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MahasiswaController", urlPatterns = {"/MahasiswaController"})
public class MahasiswaController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String aksi = request.getParameter("aksi");
        if (aksi == null) aksi = "";

        // Simpan mahasiswa baru
        if (aksi.equals("simpan")) {
            try {
                Connection con = Koneksi.getKoneksi();
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO mahasiswa VALUES (?,?,?,?,?)");
                ps.setString(1, request.getParameter("nim"));
                ps.setString(2, request.getParameter("nama"));
                ps.setInt(3, Integer.parseInt(request.getParameter("semester")));
                ps.setString(4, request.getParameter("kelas"));
                ps.setString(5, Enkripsi.md5(request.getParameter("password")));
                ps.executeUpdate();
                con.close();
                session.setAttribute("pesan", "Data mahasiswa berhasil disimpan!");
            } catch (Exception e) {
                session.setAttribute("pesan", "Gagal simpan: " + e.getMessage());
            }
            response.sendRedirect("MahasiswaController");
            return;
        }

        // Hapus mahasiswa
        if (aksi.equals("hapus")) {
            try {
                Connection con = Koneksi.getKoneksi();
                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM mahasiswa WHERE nim=?");
                ps.setString(1, request.getParameter("nim"));
                ps.executeUpdate();
                con.close();
                session.setAttribute("pesan", "Data mahasiswa berhasil dihapus!");
            } catch (Exception e) {
                session.setAttribute("pesan", "Gagal hapus: " + e.getMessage());
            }
            response.sendRedirect("MahasiswaController");
            return;
        }

        // Tampilkan halaman
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Data Mahasiswa</title>");
        out.println("<link rel='stylesheet' href='style.css'></head><body>");

        // Header
        out.println("<div id='header'><h2>Informasi Nilai Mahasiswa</h2>");
        out.println("<h1>UNIVERSITAS PAMULANG</h1>");
        out.println("<p>JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p></div>");
        out.println("<div id='wrapper'>");

        // Sidebar
        out.println("<div id='sidebar'>");
        out.println("<div class='menu-group'><p>Master Data</p>");
        out.println("<a href='MahasiswaController'><button class='btn-menu active'>Mahasiswa</button></a>");
        out.println("<a href='MataKuliahController'><button class='btn-menu'>Mata Kuliah</button></a></div>");
        out.println("<div class='menu-group'><p>Transaksi</p>");
        out.println("<a href='NilaiController'><button class='btn-menu'>Nilai</button></a></div>");
        out.println("<div class='menu-group'><p>Laporan</p>");
        out.println("<a href='NilaiController?aksi=laporan'><button class='btn-menu'>Nilai</button></a></div>");
        out.println("<a href='LogoutController'><button class='btn-logout'>Logout</button></a></div>");

        // Content
        out.println("<div id='content'>");
        out.println("<div id='navbar'><a href='NilaiController'>Home</a> | ");
        out.println("<a href='MahasiswaController'>Master Data</a> | ");
        out.println("<a href='NilaiController'>Transaksi</a> | ");
        out.println("<a href='LogoutController'>Logout</a></div>");

        // Pesan
        if (session.getAttribute("pesan") != null) {
            out.println("<p style='color:green'>" + session.getAttribute("pesan") + "</p>");
            session.removeAttribute("pesan");
        }

        // Form input
        out.println("<h3>Input Data Mahasiswa</h3>");
        out.println("<form method='post' action='MahasiswaController'>");
        out.println("<input type='hidden' name='aksi' value='simpan'>");
        out.println("<table class='form-table'>");
        out.println("<tr><td>NIM</td><td><input type='text' name='nim'></td></tr>");
        out.println("<tr><td>Nama</td><td><input type='text' name='nama'></td></tr>");
        out.println("<tr><td>Semester</td><td><input type='text' name='semester' size='5'></td></tr>");
        out.println("<tr><td>Kelas</td><td><input type='text' name='kelas' size='10'></td></tr>");
        out.println("<tr><td>Password</td><td><input type='password' name='password'></td></tr>");
        out.println("<tr><td colspan='2'>");
        out.println("<input type='submit' value='Simpan'>");
        out.println("<input type='reset' value='Reset'></td></tr>");
        out.println("</table></form>");

        // Tabel data mahasiswa
        out.println("<h3>Daftar Mahasiswa</h3>");
        out.println("<table border='1' cellpadding='5' cellspacing='0' style='font-size:13px'>");
        out.println("<tr style='background:#ddd'><th>NIM</th><th>Nama</th><th>Semester</th><th>Kelas</th><th>Aksi</th></tr>");

        try {
            Connection con = Koneksi.getKoneksi();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mahasiswa");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("nim") + "</td>");
                out.println("<td>" + rs.getString("nama") + "</td>");
                out.println("<td>" + rs.getInt("semester") + "</td>");
                out.println("<td>" + rs.getString("kelas") + "</td>");
                out.println("<td><a href='MahasiswaController?aksi=hapus&nim=" 
                    + rs.getString("nim") + "' onclick=\"return confirm('Yakin hapus?')\">Hapus</a></td>");
                out.println("</tr>");
            }
            con.close();
        } catch (Exception e) {
            out.println("<tr><td colspan='5'>Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("</table>");
        out.println("</div></div>"); // end content & wrapper

        // Footer
        out.println("<div id='footer'>");
        out.println("<p>Copyright &copy; 2014 Universitas Pamulang</p>");
        out.println("<p>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p></div>");
        out.println("</body></html>");
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { processRequest(req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { processRequest(req, resp); }
}