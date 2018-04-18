package com.focusmedica.aqrshell;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by trisys-tejesh on 11/4/17.
 */

public interface ServiceApi {

    /*@POST("login.php")
    @FormUrlEncoded
    Call<String> getLogingResult(@FieldMap Map<String, String> params);
*/
   /* @POST("khlogin.php")
    @FormUrlEncoded
    Call<String> getLogingResult(@Field("uname") String user ,
                                 @Field("pwd") String  pass
                                 );*/

    @POST("index.php")
    @FormUrlEncoded
    Call<String> getLogingResult(@FieldMap Map<String, String> params);


    /* @POST("khlogin.php")
    @FormUrlEncoded
    Call<String> getLogingResult(@FieldMap Map<String, String> params);*/
    @POST("index.php")
    @FormUrlEncoded
    Call<String> getLogOut(@FieldMap Map<String, String> params);
   /* @POST("mlogout.php")
    @FormUrlEncoded
    Call<String> getLogOut(@Field("username") String user ,
                                @Field("password") String pass );*/
}
