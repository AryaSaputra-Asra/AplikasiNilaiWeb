package com.unpam.view;

/**
 * PesanDialog - class untuk menampilkan pesan notifikasi
 */
public class PesanDialog {
    
    public static final String TIPE_SUKSES = "sukses";
    public static final String TIPE_ERROR = "error";
    public static final String TIPE_INFO = "info";
    
    private String pesan;
    private String tipe;
    
    public PesanDialog() {
        this.pesan = "";
        this.tipe = TIPE_INFO;
    }
    
    public PesanDialog(String pesan, String tipe) {
        this.pesan = pesan;
        this.tipe = tipe;
    }
    
    public String getPesan() { return pesan; }
    public void setPesan(String pesan) { this.pesan = pesan; }
    
    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
    
    public String tampilkan() {
        String warna;
        switch (tipe) {
            case TIPE_SUKSES: warna = "green"; break;
            case TIPE_ERROR:  warna = "red";   break;
            default:          warna = "blue";  break;
        }
        return "<p style='color:" + warna + "; font-weight:bold;'>" + pesan + "</p>";
    }
    
    public static String sukses(String pesan) {
        return new PesanDialog(pesan, TIPE_SUKSES).tampilkan();
    }
    
    public static String error(String pesan) {
        return new PesanDialog(pesan, TIPE_ERROR).tampilkan();
    }
    
    public static String info(String pesan) {
        return new PesanDialog(pesan, TIPE_INFO).tampilkan();
    }
    
    @Override
    public String toString() {
        return "PesanDialog{pesan=" + pesan + ", tipe=" + tipe + "}";
    }
}