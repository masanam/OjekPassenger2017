package ojekkeren.ojekkeren;

/**
 * Created by andi on 4/20/2016.
 */
public class FoodCart {
    private String id;
    private String foodname;
    private String price;
    private int quantity;
    private int tokoid;
    private String addNote;

    public String getAddNote() {
        return addNote;
    }

    public void setAddNote(String addNote) {
        this.addNote = addNote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTokoid() {
        return tokoid;
    }

    public void setTokoid(int tokoid) {
        this.tokoid = tokoid;
    }
}
