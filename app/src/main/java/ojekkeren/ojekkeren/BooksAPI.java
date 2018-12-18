package ojekkeren.ojekkeren;

/**
 * Created by User Pc on 17/10/2016.
 */

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Belal on 11/3/2015.
 */
public interface BooksAPI {

    /*Retrofit get annotation with our URL
       And our method that will return us the list ob Book
    */
    @GET("/getOrdersLast")
    public void getOrdersLast(Callback<List<POJOOrdersData>> response);
}
