package ojekkeren.ojekkeren;

/**
 * Created by andi on 3/31/2016.
 */
public class OrderPojo {
    private int id;
    private String type;
    private String phoneNum;
    private String from;
    private String to;
    private String latFrom;
    private String longFrom;
    private String latTo;
    private String longTo;
    private String orderTime;
    private String distance;
    private String price;
    private String addressFrom;
    private String addressTo;
    private String itemToDeliver;
    private String restaurantName;
    private String driverid;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLatFrom() {
        return latFrom;
    }

    public void setLatFrom(String latFrom) {
        this.latFrom = latFrom;
    }

    public String getLongFrom() {
        return longFrom;
    }

    public void setLongFrom(String longFrom) {
        this.longFrom = longFrom;
    }

    public String getLatTo() {
        return latTo;
    }

    public void setLatTo(String latTo) {
        this.latTo = latTo;
    }

    public String getLongTo() {
        return longTo;
    }

    public void setLongTo(String longTo) {
        this.longTo = longTo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getItemToDeliver() {
        return itemToDeliver;
    }

    public void setItemToDeliver(String itemToDeliver) {        this.itemToDeliver = itemToDeliver;    }
    public String getDriverid() {  return driverid;  }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }}
