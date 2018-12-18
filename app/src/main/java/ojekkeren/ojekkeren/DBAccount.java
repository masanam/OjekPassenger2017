package ojekkeren.ojekkeren;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by andi on 3/30/2016.
 */
public class DBAccount extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ojek";

    // Contacts table name
    private static final String TABLE_MEMBER = "MemberAkun";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "PhoneNumber";
    private static final String IS_LOGGED = "isLogged";

    public DBAccount(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void reCreateDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MEMBER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + "," + IS_LOGGED + " TEXT , uidReal TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MEMBER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + "," + IS_LOGGED + " TEXT , uidReal TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);

        // Create tables again
        onCreate(db);
    }

    public void setUidByPhoneNumber(MemberAkun akun){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update MemberAkun set uidReal='" + akun.getId() + "' where PhoneNumber='" + akun.getPhoneNumber() + "'");
    }

    public int getUidByPhoneNumber(String phonenum){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select uidReal from MemberAkun where PhoneNumber='"+phonenum+"'";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        int ret = 0;
        if (cursor != null && cursor.moveToFirst()){
            ret = cursor.getInt(0);
        }
        return ret;
    }

    // Getting All Contacts
    public MemberAkun getCurrentMemberDetails() {

        String query = "select * from MemberAkun where id=0";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        MemberAkun member = new MemberAkun();
        if (cursor != null && cursor.moveToFirst()){

            member.setId(cursor.getInt(0));
            member.setPhoneNumber(cursor.getString(2));
            member.setIsLogged(cursor.getString(3));
        }
        // return contact list
        return member;
    }

    public boolean isLoggedInOrNot(){
        return false;
    }

    // Adding new memberbaru
    void startUpMemberTable(MemberAkun memberAkun) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, memberAkun.getIsLogged());
        values.put(KEY_PH_NO, memberAkun.getPhoneNumber());
        values.put(KEY_ID, memberAkun.getId());
        values.put(IS_LOGGED, memberAkun.getIsLogged());

        // Inserting Row
        db.insert(TABLE_MEMBER, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public int setLoginFlag(MemberAkun akun) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PH_NO, akun.getPhoneNumber());
        values.put(IS_LOGGED, "1");
        return db.update(TABLE_MEMBER, values, KEY_ID + " = ?",new String[] { String.valueOf(akun.getId())});
    }

    // Updating single contact
    public int setLogout() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PH_NO, "0");
        values.put(IS_LOGGED, "0");
        return db.update(TABLE_MEMBER, values, KEY_ID + " = ?",new String[] { String.valueOf("0")});
    }

    // Updating single contact
    public int setReset(MemberAkun akun) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PH_NO, "0");
        values.put(IS_LOGGED, "0");
        return db.update(TABLE_MEMBER, values, KEY_ID + " = ?",new String[] { String.valueOf(akun.getId())});
    }

}
