package com.unpam.model;

public class Mahasiswa {
    private String nim, nama, kelas, password;
    private int semester;

    public Mahasiswa() {}

    public Mahasiswa(String nim, String nama, int semester, String kelas, String password) {
        this.nim = nim;
        this.nama = nama;
        this.semester = semester;
        this.kelas = kelas;
        this.password = password;
    }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}