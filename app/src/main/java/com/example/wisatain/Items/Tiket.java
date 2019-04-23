package com.example.wisatain.Items;

public class Tiket {

    public String UIDUser, RefTransaksi, TanggalPembelian, NamaWisata, LokasiWisata, WilayahWisata, Jumlah, TanggalKunjungan, TotalHarga, BuktiPembayaranURL, TiketStatus;

    public Tiket(String UIDUser, String refTransaksi, String tanggalPembelian, String namaWisata, String lokasiWisata, String wilayahWisata, String jumlah, String tanggalKunjungan, String totalHarga, String buktiPembayaranURL, String tiketStatus) {
        this.UIDUser = UIDUser;
        RefTransaksi = refTransaksi;
        TanggalPembelian = tanggalPembelian;
        NamaWisata = namaWisata;
        LokasiWisata = lokasiWisata;
        WilayahWisata = wilayahWisata;
        Jumlah = jumlah;
        TanggalKunjungan = tanggalKunjungan;
        TotalHarga = totalHarga;
        BuktiPembayaranURL = buktiPembayaranURL;
        TiketStatus = tiketStatus;
    }

    public Tiket() {

    }

}
