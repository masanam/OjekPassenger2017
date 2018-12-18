package ojekkeren.ojekkeren;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by andi on 3/27/2016.
 */
public interface  GeoCodeAPI {
    @GET("/maps/api/geocode/json")
    public void getAddressName(@Query("latlng") String latlng ,Callback<GeocodePojoClass> response);
}
