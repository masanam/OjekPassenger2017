package ojekkeren.ojekkeren;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/17/2016.
 */
public interface GetListingCategoryREST {
    @GET("/api.php")
    public void getLists(@Query("task") String task, Callback<List<WarungCategory>> callback);
}
