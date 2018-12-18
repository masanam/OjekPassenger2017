package ojekkeren.ojekkeren;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andi on 3/29/2016.
 */
public class DirectionResultPojos {

    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }
}

