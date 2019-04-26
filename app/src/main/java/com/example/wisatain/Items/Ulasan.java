package com.example.wisatain.Items;

public class Ulasan {
    public String WisataID, NamaWisata, UIDUser, NamaUser,GambarProfilUser, RatingUser,UlasanUser;

    public Ulasan(String wisataID, String namaWisata, String UIDUser, String namaUser, String gambarProfilUser, String ratingUser, String ulasanUser) {
        WisataID = wisataID;
        NamaWisata = namaWisata;
        this.UIDUser = UIDUser;
        NamaUser = namaUser;
        GambarProfilUser = gambarProfilUser;
        RatingUser = ratingUser;
        UlasanUser = ulasanUser;
    }
}