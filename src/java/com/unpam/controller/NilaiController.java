package com.unpam.controller;

import com.unpam.model.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "NilaiController", urlPatterns = {"/NilaiController"})
public class NilaiController extends HttpServlet {
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

        if (aksi.equals("cariMahasiswa")) {
            String nim = request.getParameter("nim");
            Mahasiswa m = Nilai.cariMahasiswa(nim);
            if (m != null) {
                session.setAttribute("mhs", m);
                session.removeAttribute("mk");
                session.removeAttribute("nilaiTugas");
                session.removeAttribute("nilaiUts");
                session.removeAttribute("nilaiUas");
                session.removeAttribute("nilaiId");
            } else {
                session.setAttribute("pesan", "Mahasiswa tidak ditemukan!");
            }
            response.sendRedirect("NilaiController");
            return;
        }

        if (aksi.equals("cariMK")) {
            String kodeMk = request.getParameter("kodeMk");
            MataKuliah mk = Nilai.cariMataKuliah(kodeMk);
            if (mk != null) {
                session.setAttribute("mk", mk);
                Mahasiswa mhs = (Mahasiswa) session.getAttribute("mhs");
                if (mhs != null) {
                    try {
                        Connection con = Koneksi.getKoneksi();
                        PreparedStatement ps = con.prepareStatement(
                            "SELECT * FROM nilai WHERE nim=? AND kode_mk=?");
                        ps.setString(1, mhs.getNim());
                        ps.setString(2, kodeMk);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            session.setAttribute("nilaiTugas", rs.getDouble("nilai_tugas"));
                            session.setAttribute("nilaiUts",   rs.getDouble("nilai_uts"));
                            session.setAttribute("nilaiUas",   rs.getDouble("nilai_uas"));
                            session.setAttribute("nilaiId",    rs.getInt("id"));
                        } else {
                            session.removeAttribute("nilaiTugas");
                            session.removeAttribute("nilaiUts");
                            session.removeAttribute("nilaiUas");
                            session.removeAttribute("nilaiId");
                        }
                        con.close();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                session.setAttribute("pesan", "Mata kuliah tidak ditemukan!");
            }
            response.sendRedirect("NilaiController");
            return;
        }

        if (aksi.equals("simpan")) {
            try {
                String nim    = request.getParameter("nim");
                String kodeMk = request.getParameter("kodeMk");
                double tugas  = Double.parseDouble(request.getParameter("nilaiTugas"));
                double uts    = Double.parseDouble(request.getParameter("nilaiUts"));
                double uas    = Double.parseDouble(request.getParameter("nilaiUas"));

                Connection con = Koneksi.getKoneksi();

                // Cek apakah data sudah ada
                PreparedStatement cek = con.prepareStatement(
                    "SELECT id FROM nilai WHERE nim=? AND kode_mk=?");
                cek.setString(1, nim);
                cek.setString(2, kodeMk);
                ResultSet rs = cek.executeQuery();

                if (rs.next()) {
                    // Update data yang sudah ada
                    PreparedStatement ps = con.prepareStatement(
                        "UPDATE nilai SET nilai_tugas=?, nilai_uts=?, nilai_uas=? WHERE nim=? AND kode_mk=?");
                    ps.setDouble(1, tugas);
                    ps.setDouble(2, uts);
                    ps.setDouble(3, uas);
                    ps.setString(4, nim);
                    ps.setString(5, kodeMk);
                    ps.executeUpdate();
                    session.setAttribute("pesan", "Data nilai berhasil diupdate!");
                } else {
                    // Insert data baru
                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO nilai (nim, kode_mk, nilai_tugas, nilai_uts, nilai_uas) VALUES (?,?,?,?,?)");
                    ps.setString(1, nim);
                    ps.setString(2, kodeMk);
                    ps.setDouble(3, tugas);
                    ps.setDouble(4, uts);
                    ps.setDouble(5, uas);
                    ps.executeUpdate();
                    session.setAttribute("pesan", "Data nilai berhasil disimpan!");
                }
                con.close();
            } catch (Exception e) {
                session.setAttribute("pesan", "Error: " + e.getMessage());
            }
            session.removeAttribute("mhs");
            session.removeAttribute("mk");
            session.removeAttribute("nilaiTugas");
            session.removeAttribute("nilaiUts");
            session.removeAttribute("nilaiUas");
            session.removeAttribute("nilaiId");
            response.sendRedirect("NilaiController");
            return;
        }

        if (aksi.equals("hapus")) {
            try {
                Nilai nv = new Nilai();
                nv.setId(Integer.parseInt(request.getParameter("id")));
                nv.hapus();
                session.setAttribute("pesan", "Data berhasil dihapus!");
            } catch (Exception e) {
                session.setAttribute("pesan", "Error: " + e.getMessage());
            }
            response.sendRedirect("NilaiController");
            return;
        }

        // ===== LAPORAN =====
        if (aksi.equals("laporan")) {
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><head><title>Laporan Nilai</title>");
            out.println("<link rel='stylesheet' href='style.css'></head><body>");
            out.println("<div id='header'>");
            out.println("<h2>Informasi Nilai Mahasiswa</h2>");
            out.println("<h1>UNIVERSITAS PAMULANG</h1>");
            out.println("<p>JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>");
            out.println("</div>");
            out.println("<div id='wrapper'>");

            out.println("<div id='sidebar'>");
            out.println("<div class='menu-group'><p>Master Data</p>");
            out.println("<a href='MahasiswaController'><button class='btn-menu'>Mahasiswa</button></a>");
            out.println("<a href='MataKuliahController'><button class='btn-menu'>Mata Kuliah</button></a></div>");
            out.println("<div class='menu-group'><p>Transaksi</p>");
            out.println("<a href='NilaiController'><button class='btn-menu'>Nilai</button></a></div>");
            out.println("<div class='menu-group'><p>Laporan</p>");
            out.println("<a href='NilaiController?aksi=laporan'><button class='btn-menu active'>Nilai</button></a></div>");
            out.println("<a href='LogoutController'><button class='btn-logout'>Logout</button></a>");
            out.println("</div>");

            out.println("<div id='content'>");
            out.println("<div id='navbar'>");
            out.println("<a href='NilaiController'>Home</a> | ");
            out.println("<a href='MahasiswaController'>Master Data</a> | ");
            out.println("<a href='NilaiController'>Transaksi</a> | ");
            out.println("<a href='NilaiController?aksi=laporan'>Laporan</a> | ");
            out.println("<a href='LogoutController'>Logout</a>");
            out.println("</div>");

            out.println("<h3>Laporan Nilai Mahasiswa</h3>");
            out.println("<table border='1' cellpadding='5' cellspacing='0' style='font-size:12px; width:100%'>");
            out.println("<tr style='background:#ddd; text-align:center'>");
            out.println("<th>No</th><th>NIM</th><th>Nama</th><th>Semester</th><th>Kelas</th>");
            out.println("<th>Kode MK</th><th>Nama MK</th><th>SKS</th>");
            out.println("<th>Tugas</th><th>UTS</th><th>UAS</th><th>Aksi</th>");
            out.println("</tr>");

            try {
                Connection con = Koneksi.getKoneksi();
                String sql = "SELECT n.id, n.nim, m.nama, m.semester, m.kelas, " +
                             "mk.kode_mk, mk.nama_mk, mk.jumlah_sks, " +
                             "n.nilai_tugas, n.nilai_uts, n.nilai_uas " +
                             "FROM nilai n " +
                             "JOIN mahasiswa m ON n.nim = m.nim " +
                             "JOIN mata_kuliah mk ON n.kode_mk = mk.kode_mk " +
                             "ORDER BY n.id";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                int no = 1;
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td style='text-align:center'>" + no++ + "</td>");
                    out.println("<td>" + rs.getString("nim") + "</td>");
                    out.println("<td>" + rs.getString("nama") + "</td>");
                    out.println("<td style='text-align:center'>" + rs.getInt("semester") + "</td>");
                    out.println("<td style='text-align:center'>" + rs.getString("kelas") + "</td>");
                    out.println("<td style='text-align:center'>" + rs.getString("kode_mk") + "</td>");
                    out.println("<td>" + rs.getString("nama_mk") + "</td>");
                    out.println("<td style='text-align:center'>" + rs.getInt("jumlah_sks") + "</td>");
                    out.println("<td style='text-align:center'>" + rs.getDouble("nilai_tugas") + "</td>");
                    out.println("<td style='text-align:center'>" + rs.getDouble("nilai_uts") + "</td>");
                    out.println("<td style='text-align:center'>" + rs.getDouble("nilai_uas") + "</td>");
                    out.println("<td style='text-align:center'><a href='NilaiController?aksi=hapus&id=" +
                        rs.getInt("id") + "' onclick=\"return confirm('Yakin hapus?')\">Hapus</a></td>");
                    out.println("</tr>");
                }
                con.close();
            } catch (Exception e) {
                out.println("<tr><td colspan='12'>Error: " + e.getMessage() + "</td></tr>");
            }

            out.println("</table>");
            out.println("</div></div>");
            out.println("<div id='footer'>");
            out.println("<p>Copyright &copy; 2014 Universitas Pamulang</p>");
            out.println("<p>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>");
            out.println("</div>");
            out.println("</body></html>");
            return;
        }

        // ===== TAMPILKAN FORM INPUT =====
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Input Nilai Mahasiswa</title>");
        out.println("<link rel='stylesheet' href='style.css'></head><body>");
        out.println("<div id='header'>");
        out.println("<h2>Informasi Nilai Mahasiswa</h2>");
        out.println("<h1>UNIVERSITAS PAMULANG</h1>");
        out.println("<p>JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>");
        out.println("</div>");
        out.println("<div id='wrapper'>");

        out.println("<div id='sidebar'>");
        out.println("<div class='menu-group'><p>Master Data</p>");
        out.println("<a href='MahasiswaController'><button class='btn-menu'>Mahasiswa</button></a>");
        out.println("<a href='MataKuliahController'><button class='btn-menu'>Mata Kuliah</button></a></div>");
        out.println("<div class='menu-group'><p>Transaksi</p>");
        out.println("<a href='NilaiController'><button class='btn-menu active'>Nilai</button></a></div>");
        out.println("<div class='menu-group'><p>Laporan</p>");
        out.println("<a href='NilaiController?aksi=laporan'><button class='btn-menu'>Nilai</button></a></div>");
        out.println("<a href='LogoutController'><button class='btn-logout'>Logout</button></a>");
        out.println("</div>");

        out.println("<div id='content'>");
        out.println("<div id='navbar'>");
        out.println("<a href='NilaiController'>Home</a> | ");
        out.println("<a href='MahasiswaController'>Master Data</a> | ");
        out.println("<a href='NilaiController'>Transaksi</a> | ");
        out.println("<a href='NilaiController?aksi=laporan'>Laporan</a> | ");
        out.println("<a href='LogoutController'>Logout</a>");
        out.println("</div>");

        if (session.getAttribute("pesan") != null) {
            out.println("<p style='color:green'>" + session.getAttribute("pesan") + "</p>");
            session.removeAttribute("pesan");
        }

        Mahasiswa mhs = (Mahasiswa) session.getAttribute("mhs");
        MataKuliah mk  = (MataKuliah) session.getAttribute("mk");

        String nimVal   = mhs != null ? mhs.getNim()                       : "";
        String namaVal  = mhs != null ? mhs.getNama()                      : "";
        String semVal   = mhs != null ? String.valueOf(mhs.getSemester())  : "";
        String kelasVal = mhs != null ? mhs.getKelas()                     : "";
        String mkVal    = mk  != null ? mk.getKodeMk()                    : "";
        String nmkVal   = mk  != null ? mk.getNamaMk()                    : "";
        String sksVal   = mk  != null ? String.valueOf(mk.getJumlahSks()) : "";

        // Ambil nilai dari session kalau ada
        String tugasVal = session.getAttribute("nilaiTugas") != null ? String.valueOf(session.getAttribute("nilaiTugas")) : "";
        String utsVal   = session.getAttribute("nilaiUts")   != null ? String.valueOf(session.getAttribute("nilaiUts"))   : "";
        String uasVal   = session.getAttribute("nilaiUas")   != null ? String.valueOf(session.getAttribute("nilaiUas"))   : "";

        out.println("<h3>Input Nilai Mahasiswa</h3>");
        out.println("<form method='post' action='NilaiController'>");
        out.println("<input type='hidden' name='aksi' value='simpan'>");
        out.println("<table class='form-table'>");

        out.println("<tr><td>NIM</td><td>");
        out.println("<input type='text' id='nimInput' name='nim' value='" + nimVal + "'>");
        out.println("<button type='button' onclick=\"window.location='NilaiController?aksi=cariMahasiswa&nim='+document.getElementById('nimInput').value\">Cari</button>");
        out.println("<button type='button'>Lihat</button>");
        out.println("</td></tr>");

        out.println("<tr><td>Nama</td><td><input type='text' value='" + namaVal + "' readonly></td></tr>");
        out.println("<tr><td>Semester</td><td><input type='text' value='" + semVal + "' readonly size='5'></td></tr>");
        out.println("<tr><td>Kelas</td><td><input type='text' value='" + kelasVal + "' readonly size='5'></td></tr>");

        out.println("<tr><td>Kode Mata Kuliah</td><td>");
        out.println("<input type='text' id='mkInput' name='kodeMk' value='" + mkVal + "'>");
        out.println("<button type='button' onclick=\"window.location='NilaiController?aksi=cariMK&kodeMk='+document.getElementById('mkInput').value\">Cari</button>");
        out.println("<button type='button'>Lihat</button>");
        out.println("</td></tr>");

        out.println("<tr><td>Nama Mata Kuliah</td><td><input type='text' value='" + nmkVal + "' readonly></td></tr>");
        out.println("<tr><td>Jumlah SKS</td><td><input type='text' value='" + sksVal + "' readonly size='5'></td></tr>");
        out.println("<tr><td>Nilai Tugas</td><td><input type='text' name='nilaiTugas' value='" + tugasVal + "' size='5'></td></tr>");
        out.println("<tr><td>Nilai UTS</td><td><input type='text' name='nilaiUts' value='" + utsVal + "' size='5'></td></tr>");
        out.println("<tr><td>Nilai UAS</td><td><input type='text' name='nilaiUas' value='" + uasVal + "' size='5'></td></tr>");

        out.println("<tr><td colspan='2'>");
        out.println("<input type='submit' value='Simpan'>");
        out.println("<input type='reset' value='Hapus'>");
        out.println("</td></tr>");
        out.println("</table></form>");

        out.println("</div></div>");
        out.println("<div id='footer'>");
        out.println("<p>Copyright &copy; 2014 Universitas Pamulang</p>");
        out.println("<p>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>");
        out.println("</div>");
        out.println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { processRequest(req, resp); }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { processRequest(req, resp); }
}