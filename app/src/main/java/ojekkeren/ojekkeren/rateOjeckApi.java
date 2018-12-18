package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 3/29/2016.
 */
public interface rateOjeckApi {
    @GET("/api.php")
    public void getRate(@Query("task") String task , Callback<rateOjek> response);
}
