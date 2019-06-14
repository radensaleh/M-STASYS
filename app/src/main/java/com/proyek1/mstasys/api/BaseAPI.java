package com.proyek1.mstasys.api;

import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.response.AmpuMapel;
import com.proyek1.mstasys.response.DetailNilai;
import com.proyek1.mstasys.response.DetailSoal;
import com.proyek1.mstasys.response.HasilSoal;
import com.proyek1.mstasys.response.Kelas;
import com.proyek1.mstasys.response.Mapel;
import com.proyek1.mstasys.response.MapelSiswa;
import com.proyek1.mstasys.response.NilaiSiswa;
import com.proyek1.mstasys.response.Response;
import com.proyek1.mstasys.response.Semester;
import com.proyek1.mstasys.response.Soal;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BaseAPI {

    @FormUrlEncoded
    @POST("apiLoginGuru")
    Call<Response> loginGuru(
            @Field("nip") String nip,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("apiLoginSiswa")
    Call<Response> loginSiswa(
            @Field("nis") String nis,
            @Field("password") String password
    );

    @GET("apiDataGuru/{nip}")
    Call<Guru> dataGuru(
            @Path("nip") String nip
    );

    @FormUrlEncoded
    @POST("apiAmpuMapel")
    Call<List<AmpuMapel>> ampuMapel(
            @Field("nip") String nip
    );

    @GET("apiDataSiswa/{nis}")
    Call<Siswa> dataSiswa(
            @Path("nis") String nis
    );

//    @GET("apiGetSiswa/{id_kelas}")
//    Call<List<Siswa>> getSiswa(
//            @Path("id_kelas") String id_kelas
//    );
    @FormUrlEncoded
    @POST("apiGetSiswa")
    Call<List<Siswa>> getSiswa(
            @Field("nip") String nip,
            @Field("mapel") String mapel,
            @Field("kelas") String kelas,
            @Field("semester") String semester
    );

    @GET("apiSemester")
    Call<List<Semester>> getSemester();

    @FormUrlEncoded
    @POST("apiSemesterSiswa")
    Call<List<Semester>> getSemesterSiswa(
            @Field("id_kelas") String id_kelas
    );

    @FormUrlEncoded
    @POST("apiMapelGuru")
    Call<List<Mapel>> getMapelGuru(
            @Field("nip") String nip
    );

    @FormUrlEncoded
    @POST("apiKelasGuru")
    Call<List<Kelas>> getKelasGuru(
            @Field("nip") String nip,
            @Field("mapel") String mapel
    );


    @FormUrlEncoded
    @POST("apiSemesterGuru")
    Call<List<Semester>> getSemesterGuru(
            @Field("nip") String nip,
            @Field("kelas") String kelas,
            @Field("mapel") String mapel
    );


    @FormUrlEncoded
    @POST("apiSiswaMapel")
    Call<List<MapelSiswa>> getMapelSiswa(
            @Field("kelas") String kelas,
            @Field("semester") String semester
    );

    @FormUrlEncoded
    @POST("apiNilaiSiswa")
    Call<List<NilaiSiswa>> getNilaiSiswa(
            @Field("nis") String nis,
            @Field("id_ampu") String id_ampu
    );

    @FormUrlEncoded
    @POST("apiLihatNilai")
    Call<List<NilaiSiswa>> getLihatNilai(
            @Field("nis") String nis,
            @Field("nip") String nip,
            @Field("id_mapel") String id_mapel,
            @Field("id_kelas") String id_kelas,
            @Field("id_semester") String id_semester
    );

    @FormUrlEncoded
    @POST("apiGetKategoriMapel")
    Call<Mapel> getKategoriMapel(
            @Field("id_mapel") String id_mapel,
            @Field("id_kelas") String id_kelas,
            @Field("id_semester") String id_semester
    );

    @GET("apiJenisNilai")
    Call<List<DetailNilai>> getJenisNilai();

    @FormUrlEncoded
    @POST("apiTambahNilai")
    Call<Response> tambahNilai(
            @Field("nis") String nis,
            @Field("nip") String nip,
            @Field("id_mapel") String id_mapel,
            @Field("id_kelas") String id_kelas,
            @Field("id_semester") String id_semester,
            @Field("id_detail") String id_detail,
            @Field("nilai") String nilai
    );

    @FormUrlEncoded
    @POST("apiUbahNilai")
    Call<Response> ubahNilai(
            @Field("id_nilai") String id_nilai,
            @Field("nilai") String nilai,
            @Field("id_detail") String id_detail
    );

    @FormUrlEncoded
    @POST("apiHapusNilai")
    Call<Response> hapusNilai(
            @Field("id_nilai") String id_nilai,
            @Field("id_detail") String id_detail
    );

    @Multipart
    @POST("apiSoal")
    Call<Response> unggahSoal(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("apiGetSoal")
    Call<List<Soal>> getSoal(
            @Field("nip") String nip
    );

    @FormUrlEncoded
    @POST("apiLihatSoal")
    Call<List<DetailSoal>> getLihatSoal(
            @Field("date_create") String date_create
    );

    @FormUrlEncoded
    @POST("apiAktifSoal")
    Call<Response> aktifSoal(
            @Field("date_create") String date_create,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("apiHapusSoal")
    Call<Response> hapusSoal(
            @Field("date_create") String date_create
    );


    @FormUrlEncoded
    @POST("apiSiswaSoal")
    Call<List<Soal>> getSiswaSoal(
            @Field("id_kelas") String id_kelas,
            @Field("id_semester") String id_semester
    );

    @FormUrlEncoded
    @POST("apiHasilSoal")
    Call<Response> hasilSoal(
            @Field("nomer[]") Integer[] nomer,
            @Field("jawaban[]") String[] jawaban,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("apiInsertHasil")
    Call<Response> insertHasil(
            @Field("nis") String nis,
            @Field("benar") String benar,
            @Field("salah") String salah,
            @Field("nilai") String nilai,
            @Field("date_soal") String date
    );

    @FormUrlEncoded
    @POST("apiRanking")
    Call<List<HasilSoal>> ranking(
            @Field("date_soal") String date
    );


}
