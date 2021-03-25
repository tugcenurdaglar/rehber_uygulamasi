package com.tugcenurdaglar.kisileruygulamasi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public  interface KisilerDaoInterface {

    @GET("kisiler/tum_kisiler.php")
    Call<KisilerCevap> tumKisiler();

    @POST("kisiler/tum_kisiler_arama.php")
    @FormUrlEncoded
    Call<KisilerCevap> kisiArama(@Field("kisi_ad") String kisi_ad);

    @POST("kisiler/delete_kisiler.php")
    @FormUrlEncoded
    Call<CRUDCevap> kisiSil(@Field("kisi_id") int kisi_id);

    @POST("kisiler/insert_kisiler.php")
    @FormUrlEncoded
    Call<CRUDCevap> kisiEkle(@Field("kisi_ad") String kisi_ad,
                             @Field("kisi_tel") String kisi_tel);

    @POST("kisiler/update_kisiler.php")
    @FormUrlEncoded
    Call<CRUDCevap> kisiGuncelle(@Field("kisi_id") int kisi_id,
                                 @Field("kisi_ad") String kisi_ad,
                                 @Field("kisi_tel") String kisi_tel);
}
