package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by andi on 4/3/2016.
 */
public interface OrderAPI {
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


        @FormUrlEncoded
        @POST("/api.php")
        public void setOrderFood(
                @Query("task") String task,
                @Field("orderid") String orderid,
                @Field("foodid") String foodid,
                @Field("quantity") String quantity,
                @Field("foodname") String foodname,
                @Field("note") String note,
                Callback<RegisterResponsePojo> response);
}
