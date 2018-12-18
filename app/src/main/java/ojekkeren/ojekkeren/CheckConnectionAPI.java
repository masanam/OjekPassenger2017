package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 3/31/2016.
 */
public interface CheckConnectionAPI {
    @GET("/api.php")
    public void checkConn(@Query("task") String task , Callback<CheckConnectionPojo> response);
}
