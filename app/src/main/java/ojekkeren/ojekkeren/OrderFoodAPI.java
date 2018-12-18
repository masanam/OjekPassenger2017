package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by andi on 4/28/2016.
 */
public interface OrderFoodAPI {
    @FormUrlEncoded
    @POST("/api.php")
    public void setOrderFood(
            @Query("task") String task,
            @Field("orderid") String orderid,
            @Field("foodid") String foodid,
            @Field("quantity") String quantity,
            @Field("note") String note,
            Callback<RegisterResponsePojo> response);
}
