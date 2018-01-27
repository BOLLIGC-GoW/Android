package swiftproduct.swift;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Charles on 11/16/15.
 */
public class dbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SWIFT.db";
    public static final String TABLE_NAME = "PRODUCTS_TABLE";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "cmpName";
    public static final String COL_2 = "productName";
    public static final String COL_3 = "Price";
    public static final String COL_4 = "Unit";
    public static final String COL_5 = "shortDesc";

    public dbHelper(Context context){
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Setting up SQLite table only if it doesn't already exist.
        db.execSQL("CREATE TABLE if not exists " + TABLE_NAME + " (" +
                        COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_1 + " TEXT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT," + COL_5 + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists" + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String cmpName, String productName, String Price, String Unit, String shortDesc){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,cmpName);
        contentValues.put(COL_2,productName);
        contentValues.put(COL_3,Price);
        contentValues.put(COL_4,Unit);
        contentValues.put(COL_5, shortDesc);
        long insertion = db.insert(TABLE_NAME,null,contentValues);
        if(insertion == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
            return result;
        }catch(SQLiteException e) {
            Log.d(e.getMessage(), ": This isn't working");
        }
        return null;
    }

    public boolean updateData(int ID, String cmpName, String productName, String Price, String Unit, String shortDesc){
        if(ID != -1) {
            if(ID == 0)
                ID = ID +1;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, cmpName);
            contentValues.put(COL_2, productName);
            contentValues.put(COL_3, Price);
            contentValues.put(COL_4, Unit);
            contentValues.put(COL_5, shortDesc);
            db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{Integer.toString(ID)});
            return true;
        }else
            return false;
    }

}
