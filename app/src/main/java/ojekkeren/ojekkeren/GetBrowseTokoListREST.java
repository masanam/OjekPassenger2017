package ojekkeren.ojekkeren;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/14/2016.
 */
public interface GetBrowseTokoListREST {
    @GET("/api.php")
    public void getBrowseList(@Query("task") String task,@Query("lat") String lat,@Query("long") String lng, Callback<List<WarungPojo>> callback);

    @GET("/api.php")
    public void getTokoDetailsById(@Query("task") String task,@Query("id") String lat, Callback<WarungPojo> callback);

    @GET("/api.php")
    public void getFoodListByTokoId(@Query("task") String task,@Query("id") String id, Callback<List<WarungPojo>> callback);
}
