package com.proyek1.mstasys.response;

public class NilaiSiswa {
    private String id_nilai;
    private String id_detail;
    private String jenis_nilai;
    private String nilai;
    private String date_create;
    private String date_update;

    public String getId_nilai() {
        return id_nilai;
    }

    public void setId_nilai(String id_nilai) {
        this.id_nilai = id_nilai;
    }

    public String getId_detail() {
        return id_detail;
    }

    public void setId_detail(String id_detail) {
        this.id_detail = id_detail;
    }

    public String getJenis_nilai() {
        return jenis_nilai;
    }

    public void setJenis_nilai(String jenis_nilai) {
        this.jenis_nilai = jenis_nilai;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public String getDate_update() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
    }
}
