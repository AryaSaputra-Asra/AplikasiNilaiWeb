package com.unpam.controller;

import com.unpam.model.Koneksi;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MataKuliahController", urlPatterns = {"/MataKuliahController"})
public class MataKuliahController extends HttpServlet {
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

        // Simpan mata kuliah
        if (aksi.equals("simpan")) {
            try {
                Connection con = Koneksi.getKoneksi();
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO mata_kuliah VALUES (?,?,?)");
                ps.setString(1, request.getParameter("kodeMk"));
                ps.setString(2, request.getParameter("namaMk"));
                ps.setInt(3, Integer.parseInt(request.getParameter("jumlahSks")));
                ps.executeUpdate();
                con.close();
                session.setAttribute("pesan", "Data mata kuliah berhasil disimpan!");
            } catch (Exception e) {
                session.setAttribute("pesan", "Gagal simpan: " + e.getMessage());
            }
            response.sendRedirect("MataKuliahController");
            return;
        }

        // Hapus mata kuliah
        if (aksi.equals("hapus")) {
            try {
                Connection con = Koneksi.getKoneksi();
                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM mata_kuliah WHERE kode_mk=?");
                ps.setString(1, request.getParameter("kodeMk"));
                ps.executeUpdate();
                con.close();
                session.setAttribute("pesan", "Data mata kuliah berhasil dihapus!");
            } catch (Exception e) {
                session.setAttribute("pesan", "Gagal hapus: " + e.getMessage());
            }
            response.sendRedirect("MataKuliahController");
            return;
        }

        // Tampilkan halaman
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Data Mata Kuliah</title>");
        out.println("<link rel='stylesheet' href='style.css'></head><body>");

        // Header
        out.println("<div id='header'><h2>Informasi Nilai Mahasiswa</h2>");
        out.println("<h1>UNIVERSITAS PAMULANG</h1>");
        out.println("<p>JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p></div>");
        out.println("<div id='wrapper'>");

        // Sidebar
        out.println("<div id='sidebar'>");
        out.println("<div class='menu-group'><p>Master Data</p>");
        out.println("<a href='MahasiswaController'><button class='btn-menu'>Mahasiswa</button></a>");
        out.println("<a href='MataKuliahController'><button class='btn-menu active'>Mata Kuliah</button></a></div>");
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
        out.println("<h3>Input Data Mata Kuliah</h3>");
        out.println("<form method='post' action='MataKuliahController'>");
        out.println("<input type='hidden' name='aksi' value='simpan'>");
        out.println("<table class='form-table'>");
        out.println("<tr><td>Kode Mata Kuliah</td><td><input type='text' name='kodeMk'></td></tr>");
        out.println("<tr><td>Nama Mata Kuliah</td><td><input type='text' name='namaMk'></td></tr>");
        out.println("<tr><td>Jumlah SKS</td><td><input type='text' name='jumlahSks' size='5'></td></tr>");
        out.println("<tr><td colspan='2'>");
        out.println("<input type='submit' value='Simpan'>");
        out.println("<input type='reset' value='Reset'></td></tr>");
        out.println("</table></form>");

        // Tabel daftar mata kuliah
        out.println("<h3>Daftar Mata Kuliah</h3>");
        out.println("<table border='1' cellpadding='5' cellspacing='0' style='font-size:13px'>");
        out.println("<tr style='background:#ddd'><th>Kode MK</th><th>Nama MK</th><th>SKS</th><th>Aksi</th></tr>");

        try {
            Connection con = Koneksi.getKoneksi();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mata_kuliah");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("kode_mk") + "</td>");
                out.println("<td>" + rs.getString("nama_mk") + "</td>");
                out.println("<td>" + rs.getInt("jumlah_sks") + "</td>");
                out.println("<td><a href='MataKuliahController?aksi=hapus&kodeMk="
                    + rs.getString("kode_mk") + "' onclick=\"return confirm('Yakin hapus?')\">Hapus</a></td>");
                out.println("</tr>");
            }
            con.close();
        } catch (Exception e) {
            out.println("<tr><td colspan='4'>Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("</table>");
        out.println("</div></div>");

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