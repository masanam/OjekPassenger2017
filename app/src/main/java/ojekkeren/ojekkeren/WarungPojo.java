package ojekkeren.ojekkeren;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andi on 4/13/2016.
 */
public class WarungPojo {
    private String id;
    private String namawarung;
    private String jambuka;
    private String jamtutup;
    private String desc;
    private String lat;
    private String lng;
    private String distance;
    private String foodname;
    private String price;


    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescriptionFood() {
        return descriptionFood;
    }

    public void setDescriptionFood(String descriptionFood) {
        this.descriptionFood = descriptionFood;
    }

    private String descriptionFood;


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    private String address;
    private String notlp;
    private String pic;
    private String active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamawarung() {
        return namawarung;
    }

    public void setNamawarung(String namawarung) {
        this.namawarung = namawarung;
    }

    public String getJambuka() {
        return jambuka;
    }

    public void setJambuka(String jambuka) {
        this.jambuka = jambuka;
    }

    public String getJamtutup() {
        return jamtutup;
    }

    public void setJamtutup(String jamtutup) {
        this.jamtutup = jamtutup;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotlp() {
        return notlp;
    }

    public void setNotlp(String notlp) {
        this.notlp = notlp;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }


    // Constructor to convert JSON object into a Java class instance
    public WarungPojo(JSONObject object) throws JSONException {
        this.namawarung = object.getString("namawarung");
        this.pic = object.getString("pic");
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<WarungPojo> fromJson(JSONArray jsonObjects) {
        ArrayList<WarungPojo> warungPojos = new ArrayList<WarungPojo>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                warungPojos.add(new WarungPojo(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return warungPojos;
    }

}
