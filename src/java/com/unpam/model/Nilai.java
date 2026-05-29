package com.unpam.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Nilai {
    private int id;
    private String nim, kodeMk, namaMahasiswa, kelas, namaMk;
    private int semester, jumlahSks;
    private double nilaiTugas, nilaiUts, nilaiUas;

    public Nilai() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    public String getNamaMahasiswa() { return namaMahasiswa; }
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    public int getJumlahSks() { return jumlahSks; }
    public void setJumlahSks(int jumlahSks) { this.jumlahSks = jumlahSks; }
    public double getNilaiTugas() { return nilaiTugas; }
    public void setNilaiTugas(double nilaiTugas) { this.nilaiTugas = nilaiTugas; }
    public double getNilaiUts() { return nilaiUts; }
    public void setNilaiUts(double nilaiUts) { this.nilaiUts = nilaiUts; }
    public double getNilaiUas() { return nilaiUas; }
    public void setNilaiUas(double nilaiUas) { this.nilaiUas = nilaiUas; }

    // DB Methods
    public static Mahasiswa cariMahasiswa(String nim) {
        Mahasiswa m = null;
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mahasiswa WHERE nim=?");
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m = new Mahasiswa(rs.getString("nim"), rs.getString("nama"),
                        rs.getInt("semester"), rs.getString("kelas"), rs.getString("password"));
            }
            con.close();
        } catch (Exception e) { System.out.println(e.getMessage()); }
        return m;
    }

    public static MataKuliah cariMataKuliah(String kodeMk) {
        MataKuliah mk = null;
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mata_kuliah WHERE kode_mk=?");
            ps.setString(1, kodeMk);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                mk = new MataKuliah(rs.getString("kode_mk"), rs.getString("nama_mk"), rs.getInt("jumlah_sks"));
            }
            con.close();
        } catch (Exception e) { System.out.println(e.getMessage()); }
        return mk;
    }

    public boolean simpan() {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO nilai (nim, kode_mk, nilai_tugas, nilai_uts, nilai_uas) VALUES (?,?,?,?,?)");
            ps.setString(1, nim); ps.setString(2, kodeMk);
            ps.setDouble(3, nilaiTugas); ps.setDouble(4, nilaiUts); ps.setDouble(5, nilaiUas);
            ps.executeUpdate(); con.close(); return true;
        } catch (Exception e) { System.out.println(e.getMessage()); return false; }
    }

    public boolean hapus() {
        try {
            Connection con = Koneksi.getKoneksi();
            PreparedStatement ps = con.prepareStatement("DELETE FROM nilai WHERE id=?");
            ps.setInt(1, id); ps.executeUpdate(); con.close(); return true;
        } catch (Exception e) { System.out.println(e.getMessage()); return false; }
    }

    public static List<Nilai> getSemuaNilai() {
        List<Nilai> list = new ArrayList<>();
        try {
            Connection con = Koneksi.getKoneksi();
            String sql = "SELECT n.id, n.nim, m.nama, m.semester, m.kelas, " +
                         "mk.kode_mk, mk.nama_mk, mk.jumlah_sks, n.nilai_tugas, n.nilai_uts, n.nilai_uas " +
                         "FROM nilai n JOIN mahasiswa m ON n.nim=m.nim JOIN mata_kuliah mk ON n.kode_mk=mk.kode_mk";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Nilai nv = new Nilai();
                nv.setId(rs.getInt("id")); nv.setNim(rs.getString("nim"));
                nv.setNamaMahasiswa(rs.getString("nama")); nv.setSemester(rs.getInt("semester"));
                nv.setKelas(rs.getString("kelas")); nv.setKodeMk(rs.getString("kode_mk"));
                nv.setNamaMk(rs.getString("nama_mk")); nv.setJumlahSks(rs.getInt("jumlah_sks"));
                nv.setNilaiTugas(rs.getDouble("nilai_tugas")); nv.setNilaiUts(rs.getDouble("nilai_uts"));
                nv.setNilaiUas(rs.getDouble("nilai_uas"));
                list.add(nv);
            }
            con.close();
        } catch (Exception e) { System.out.println(e.getMessage()); }
        return list;
    }
}