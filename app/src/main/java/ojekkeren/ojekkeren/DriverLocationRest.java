package ojekkeren.ojekkeren;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 3/30/2016.
 */
public interface DriverLocationRest {
    @GET("/api.php")
    public void getAll(@Query("task") String task , Callback<List<DriverLocationPojo>> response);
}
