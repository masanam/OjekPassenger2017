package ojekkeren.ojekkeren;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andi on 3/27/2016.
 */
public class GeocodePojoClass {

    private List<GeoCodePojoClassResults> results;

    public List<GeoCodePojoClassResults> getResults() {
        return results;
    }

    public void setResults(List<GeoCodePojoClassResults> results) {
        this.results = results;
    }
}
