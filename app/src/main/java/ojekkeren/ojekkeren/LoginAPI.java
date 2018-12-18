package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 3/30/2016.
 */
public interface LoginAPI
{
    @GET("/api.php")
    public void getJson(@Query("task") String task, @Query("phonenum") String phonenum,@Query("password") String password, Callback<LoginPojo> callback);
}
