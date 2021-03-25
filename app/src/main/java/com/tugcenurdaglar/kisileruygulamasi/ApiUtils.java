package com.tugcenurdaglar.kisileruygulamasi;

public class ApiUtils {

    public static final String BASE_URL = "http://kasimadalan.pe.hu/";

    public static KisilerDaoInterface getKisilerDaoInterface(){
        return RetrofitClient.getClient(BASE_URL).create(KisilerDaoInterface.class);
    }

}
