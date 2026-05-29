package com.unpam.view;

/**
 * MainForm - class view utama aplikasi
 * Digunakan sebagai referensi struktur tampilan
 */
public class MainForm {
    
    private String judul;
    private String konten;
    
    public MainForm() {
        this.judul = "Informasi Nilai Mahasiswa";
        this.konten = "";
    }
    
    public MainForm(String judul, String konten) {
        this.judul = judul;
        this.konten = konten;
    }
    
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    
    public String getKonten() { return konten; }
    public void setKonten(String konten) { this.konten = konten; }
    
    public String getHeader() {
        return "<div id='header'>" +
               "<h2>Informasi Nilai Mahasiswa</h2>" +
               "<h1>UNIVERSITAS PAMULANG</h1>" +
               "<p>JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>" +
               "</div>";
    }
    
    public String getFooter() {
        return "<div id='footer'>" +
               "<p>Copyright &copy; 2014 Universitas Pamulang</p>" +
               "<p>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>" +
               "</div>";
    }
    
    public String getSidebar(String menuAktif) {
        return "<div id='sidebar'>" +
               "<div class='menu-group'><p>Master Data</p>" +
               "<a href='MahasiswaController'><button class='btn-menu" + 
                   (menuAktif.equals("mahasiswa") ? " active" : "") + "'>Mahasiswa</button></a>" +
               "<a href='MataKuliahController'><button class='btn-menu" + 
                   (menuAktif.equals("matakuliah") ? " active" : "") + "'>Mata Kuliah</button></a></div>" +
               "<div class='menu-group'><p>Transaksi</p>" +
               "<a href='NilaiController'><button class='btn-menu" + 
                   (menuAktif.equals("nilai") ? " active" : "") + "'>Nilai</button></a></div>" +
               "<div class='menu-group'><p>Laporan</p>" +
               "<a href='NilaiController?aksi=laporan'><button class='btn-menu'>Nilai</button></a></div>" +
               "<a href='LogoutController'><button class='btn-logout'>Logout</button></a>" +
               "</div>";
    }
    
    @Override
    public String toString() {
        return "MainForm{judul=" + judul + "}";
    }
}