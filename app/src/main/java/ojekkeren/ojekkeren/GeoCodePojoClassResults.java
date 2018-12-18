package ojekkeren.ojekkeren;

import java.util.List;

/**
 * Created by andi on 3/27/2016.
 */
public class GeoCodePojoClassResults {
    private String formatted_address;

    public GeocodeGeometryPojoClass getGeometry() {
        return geometry;
    }

    public void setGeometry(GeocodeGeometryPojoClass geometry) {
        this.geometry = geometry;
    }

    private GeocodeGeometryPojoClass geometry;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
}
