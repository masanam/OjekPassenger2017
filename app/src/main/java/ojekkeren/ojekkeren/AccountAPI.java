package ojekkeren.ojekkeren;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/3/2016.
 */
public interface AccountAPI {
    @GET("/api.php")
    public void getIdFromPhoneNum(@Query("task") String task, @Query("phoneNum") String phoneNum, Callback<AccountPojo> callback);
}
