package com.example.wisatain.Items;

public class Tiket {

    public String UIDUser, RafTransaksi, TanggalPembelian, NamaWisata, LokasiWisata, WilayahWisata, Jumlah, TanggalKunjungan, TotalHarga;

    public Tiket(String UIDUser, String rafTransaksi, String tanggalPembelian, String namaWisata, String lokasiWisata, String wilayahWisata, String jumlah, String tanggalKunjungan, String totalHarga) {
        this.UIDUser = UIDUser;
        RafTransaksi = rafTransaksi;
        TanggalPembelian = tanggalPembelian;
        NamaWisata = namaWisata;
        LokasiWisata = lokasiWisata;
        WilayahWisata = wilayahWisata;
        Jumlah = jumlah;
        TanggalKunjungan = tanggalKunjungan;
        TotalHarga = totalHarga;
    }

    public Tiket() {

    }

}
