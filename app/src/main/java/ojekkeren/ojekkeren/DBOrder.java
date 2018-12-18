package ojekkeren.ojekkeren;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andi on 3/31/2016.
 */
public class DBOrder extends SQLiteOpenHelper {

    public DBOrder(Context context) {
        super(context, "ojek", null, 1);
    }

    public void reCreateDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Orderan");
        db.execSQL("DROP TABLE IF EXISTS FoodOrder");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE Orderan(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "type TEXT NULL," +
                "phoneNum TEXT NULL," +
                "dari TEXT NULL," +
                "ke TEXT NULL," +
                "latFrom TEXT NULL," +
                "longFrom TEXT NULL," +
                "latTo TEXT NULL," +
                "longTo TEXT NULL," +
                "orderTime TEXT NULL," +
                "distance TEXT NULL," +
                "price TEXT NULL," +
                "addressFrom TEXT NULL," +
                "addressTo TEXT NULL," +
                "itemToDeliver TEXT NULL," +
                "status TEXT )";
        String CREATE_FOOD_DEL_TABLE = "CREATE TABLE FoodOrder(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "orderid TEXT NULL," +
                "foodid TEXT NULL," +
                "foodname TEXT NULL," +
                "note TEXT NULL," +
                "quantity TEXT NULL )";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FOOD_DEL_TABLE);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Orderan");
        db.execSQL("DROP TABLE IF EXISTS FoodOrder");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE Orderan(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "type TEXT NULL," +
                "phoneNum TEXT NULL," +
                "dari TEXT NULL," +
                "ke TEXT NULL," +
                "latFrom TEXT NULL," +
                "longFrom TEXT NULL," +
                "latTo TEXT NULL," +
                "longTo TEXT NULL," +
                "orderTime TEXT NULL," +
                "distance TEXT NULL," +
                "price TEXT NULL," +
                "addressFrom TEXT NULL," +
                "addressTo TEXT NULL," +
                "itemToDeliver TEXT NULL," +
                "status TEXT NULL )";
        String CREATE_FOOD_DEL_TABLE = "CREATE TABLE FoodOrder(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "orderid TEXT NULL," +
                "foodid TEXT NULL," +
                "foodname TEXT NULL," +
                "note TEXT NULL," +
                "quantity TEXT NULL )";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FOOD_DEL_TABLE);
    }

    public boolean insertOrderTemporary(OrderPojo order){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("type", order.getType());
        values.put("phoneNum", order.getPhoneNum());
        values.put("dari", order.getFrom());
        values.put("ke", order.getTo());
        values.put("latFrom", order.getLatFrom());
        values.put("longFrom", order.getLongFrom());
        values.put("latTo", order.getLatTo());
        values.put("longTo", order.getLongTo());
        values.put("distance", order.getDistance());
        values.put("price", order.getPrice());
        values.put("addressFrom", order.getAddressFrom());
        values.put("addressTo", order.getAddressTo());
        values.put("itemToDeliver", order.getItemToDeliver());
        values.put("status", "3");


        // Inserting Row
        db.insert("Orderan", null, values);
        db.close(); // Closing database connection

        return true;
    }


    public boolean insertOrderTemporaryFood(List<OrderFoodPojo> order){
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < order.size(); i++){
            ContentValues values = new ContentValues();
            values.put("orderid", order.get(i).getOrderid());
            values.put("foodid", order.get(i).getFoodid());
            values.put("quantity", order.get(i).getQuantity());
            values.put("foodname", order.get(i).getFoodName());
            values.put("note", order.get(i).getNote());
            // Inserting Row
            db.insert("FoodOrder", null, values);
        }
        db.close(); // Closing database connection
        return true;
    }


    public List<OrderFoodPojo> getLatestFoodByOrderId(Integer orderId){
        SQLiteDatabase db = this.getWritableDatabase();

        List<OrderFoodPojo> groupAddOrder = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT orderid,foodid,quantity,note,foodname FROM FoodOrder where orderid='"+orderId+"' ORDER BY id DESC", null);
        try {
            while (cursor.moveToNext()) {
                OrderFoodPojo arrEntryFoodPojo = new OrderFoodPojo();
                arrEntryFoodPojo.setFoodid(Integer.parseInt(cursor.getString(1)));
                arrEntryFoodPojo.setQuantity(Integer.parseInt(cursor.getString(2)));
                arrEntryFoodPojo.setOrderid(Integer.parseInt(cursor.getString(0)));
                arrEntryFoodPojo.setFoodName(cursor.getString(4));
                arrEntryFoodPojo.setNote(cursor.getString(3));
                groupAddOrder.add(arrEntryFoodPojo);
            }

        } finally {
            cursor.close();
        }
        db.close(); // Closing database connection
        return groupAddOrder;
    }

    public List<OrderFoodPojo> getLatestFoodByOrderIdGroup(Integer orderId){
        SQLiteDatabase db = this.getWritableDatabase();
        OrderFoodPojo arrEntryFoodPojo = new OrderFoodPojo();
        List<OrderFoodPojo> arrGroup = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT orderid,foodid,quantity,note,foodname FROM FoodOrder where orderid='"+orderId+"' ORDER BY id DESC", null);
        if (cursor != null && cursor.moveToFirst()){
            arrEntryFoodPojo.setFoodid(Integer.parseInt(cursor.getString(1)));
            arrEntryFoodPojo.setQuantity(Integer.parseInt(cursor.getString(2)));
            arrEntryFoodPojo.setOrderid(Integer.parseInt(cursor.getString(0)));
            arrEntryFoodPojo.setFoodName(cursor.getString(4));
            arrEntryFoodPojo.setNote(cursor.getString(3));
        }
        db.close(); // Closing database connection
        return arrGroup;
    }


    public int getLatestOrderId(){
        SQLiteDatabase db = this.getWritableDatabase();
        int ret = 0;
        Cursor cursor = db.rawQuery("SELECT id FROM Orderan ORDER BY id DESC LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()){

            ret = Integer.parseInt(cursor.getString(0));

        }
        db.close(); // Closing database connection
        return ret;
    }

    public boolean setOrderStatusTo2LastOrder(){
        OrderPojo ret = new OrderPojo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id,phoneNum from Orderan where status='3' order by id DESC limit 1", null);
        if (cursor != null && cursor.moveToFirst()){
            ret.setId(Integer.parseInt(cursor.getString(0)));
            ret.setPhoneNum(cursor.getString(1));
        }
        db.execSQL("update Orderan set status='2',phoneNum='"+ret.getPhoneNum()+"' where id='"+String.valueOf(ret.getId())+"'");
        return true;
    }

    public String getTheLatestOrderStatus(){
        OrderPojo ret = new OrderPojo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select status from Orderan order by id DESC limit 1", null);
        if (cursor != null && cursor.moveToFirst()){
            ret.setStatus(cursor.getString(0));
        }

        return ret.getStatus();
    }

    public OrderPojo getTheLatestOrderData(){
        OrderPojo ret = new OrderPojo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Orderan order by id DESC limit 1", null);
        if (cursor != null && cursor.moveToFirst()){
            ret.setId(cursor.getInt(0));
            ret.setType(cursor.getString(1));
            ret.setPhoneNum(cursor.getString(2));
            ret.setFrom(cursor.getString(3));
            ret.setTo(cursor.getString(4));
            ret.setLatFrom(cursor.getString(5));
            ret.setLongFrom(cursor.getString(6));
            ret.setLatTo(cursor.getString(7));
            ret.setLongTo(cursor.getString(8));
            ret.setOrderTime(cursor.getString(9));
            ret.setDistance(cursor.getString(10));
            ret.setPrice(cursor.getString(11));
            ret.setAddressFrom(cursor.getString(12));
            ret.setAddressTo(cursor.getString(13));
            ret.setItemToDeliver(cursor.getString(14));
            ret.setStatus(cursor.getString(15));
        }

        return ret;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS Orderan");

        // Create tables again
        onCreate(db);
    }

    public void setOrder(OrderPojo order){

    }
}
