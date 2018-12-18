package ojekkeren.ojekkeren;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/29/2016.
 */
public interface GetOrdersInProgressREST {
    @GET("/api.php")
    public void getOrdersList(@Query("task") String task, @Query("phoneNum") String phoneNum, Callback<List<OrdersData>> callback);
}
