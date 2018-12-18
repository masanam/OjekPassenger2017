package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by andi on 3/31/2016.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/api.php")
    public void doReg(
            @Query("task") String task,
            @Field("phoneNum") String phoneNum,
            @Field("email") String email,
            @Field("password") String password,
            Callback<RegisterResponsePojo> response);



}

