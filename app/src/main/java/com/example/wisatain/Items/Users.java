package com.example.wisatain.Items;

public class Users {
    public String Nama, Email, NomorTelepon, TanggalLahir, Alamat, Status, FotoURL;

    public Users(String nama, String email, String nomorTelepon, String tanggalLahir, String alamat, String status, String fotoURL) {
        Nama = nama;
        Email = email;
        NomorTelepon = nomorTelepon;
        TanggalLahir = tanggalLahir;
        Alamat = alamat;
        Status = status;
        FotoURL = fotoURL;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNomorTelepon() {
        return NomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        NomorTelepon = nomorTelepon;
    }

    public String getTanggalLahir() {
        return TanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        TanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFotoURL() {
        return FotoURL;
    }

    public void setFotoURL(String fotoURL) {
        FotoURL = fotoURL;
    }
}
