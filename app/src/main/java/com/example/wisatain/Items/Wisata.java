package com.example.wisatain.Items;

public class Wisata {

    public String namawisata, uiduser, kategoriwisata, lokasiwisata, wilayahwisata, nomorteleponwisata, hargawisata, deskripsiwisata, fotowisataurl, verifikasi;

    public Wisata(String namawisata, String uiduser, String kategoriwisata, String lokasiwisata, String wilayahwisata, String nomorteleponwisata, String hargawisata, String deskripsiwisata, String fotowisataurl, String verifikasi) {
        this.namawisata = namawisata;
        this.uiduser = uiduser;
        this.kategoriwisata = kategoriwisata;
        this.lokasiwisata = lokasiwisata;
        this.wilayahwisata = wilayahwisata;
        this.nomorteleponwisata = nomorteleponwisata;
        this.hargawisata = hargawisata;
        this.deskripsiwisata = deskripsiwisata;
        this.fotowisataurl = fotowisataurl;
        this.verifikasi = verifikasi;
    }

    public Wisata () {

    }

    public String getNamawisata() {
        return namawisata;
    }

    public void setNamawisata(String namawisata) {
        this.namawisata = namawisata;
    }

    public String getUiduser() {
        return uiduser;
    }

    public void setUiduser(String uiduser) {
        this.uiduser = uiduser;
    }

    public String getKategoriwisata() {
        return kategoriwisata;
    }

    public void setKategoriwisata(String kategoriwisata) {
        this.kategoriwisata = kategoriwisata;
    }

    public String getLokasiwisata() {
        return lokasiwisata;
    }

    public void setLokasiwisata(String lokasiwisata) {
        this.lokasiwisata = lokasiwisata;
    }

    public String getWilayahwisata() {
        return wilayahwisata;
    }

    public void setWilayahwisata(String wilayahwisata) {
        this.wilayahwisata = wilayahwisata;
    }

    public String getNomorteleponwisata() {
        return nomorteleponwisata;
    }

    public void setNomorteleponwisata(String nomorteleponwisata) {
        this.nomorteleponwisata = nomorteleponwisata;
    }

    public String getHargawisata() {
        return hargawisata;
    }

    public void setHargawisata(String hargawisata) {
        this.hargawisata = hargawisata;
    }

    public String getDeskripsiwisata() {
        return deskripsiwisata;
    }

    public void setDeskripsiwisata(String deskripsiwisata) {
        this.deskripsiwisata = deskripsiwisata;
    }

    public String getFotowisataurl() {
        return fotowisataurl;
    }

    public void setFotowisataurl(String fotowisataurl) {
        this.fotowisataurl = fotowisataurl;
    }

    public String getVerifikasi() {
        return verifikasi;
    }

    public void setVerifikasi(String verifikasi) {
        this.verifikasi = verifikasi;
    }
}
