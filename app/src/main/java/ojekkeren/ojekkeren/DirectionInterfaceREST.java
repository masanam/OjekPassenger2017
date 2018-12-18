package ojekkeren.ojekkeren;


import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by andi on 3/29/2016.
 */
public interface DirectionInterfaceREST
{
        @GET("/maps/api/directions/json")
        public void getJson(@Query("origin") String origin,@Query("destination") String destination, Callback<DirectionResultPojos> callback);
}
