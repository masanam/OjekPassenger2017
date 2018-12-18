package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by andi on 4/28/2016.
 */
public interface OrderFoodRetOrderIDAPI {
    @FormUrlEncoded
    @POST("/api.php")
    public void setOrder(
            @Query("task") String task,
            @Field("phoneNum") String phoneNum,
            @Field("from") String from,
            @Field("to") String to,
            @Field("distance") String distance,
            @Field("price") String price,
            @Field("latlangFrom") String latlangFrom,
            @Field("latlangTo") String latlangTo,
            @Field("addressfrom") String addressfrom,
            @Field("addressto") String addressto,
            @Field("itemtodeliver") String itemtodeliver,
            @Field("typeorder") String typeorder,
            @Field("ordertime") String ordertime,
            Callback<RegisterResponsePojo> response);
}
