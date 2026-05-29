package com.unpam.model;

public class MataKuliah {
    private String kodeMk, namaMk;
    private int jumlahSks;

    public MataKuliah() {}
    public MataKuliah(String kodeMk, String namaMk, int jumlahSks) {
        this.kodeMk = kodeMk; this.namaMk = namaMk; this.jumlahSks = jumlahSks;
    }

    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }
    public int getJumlahSks() { return jumlahSks; }
    public void setJumlahSks(int jumlahSks) { this.jumlahSks = jumlahSks; }
}