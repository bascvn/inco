package vn.com.basc.inco.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.com.basc.inco.gcm.Push;

/**
 * Created by Droid King on 28/06/2015.
 */
public class INCODatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "_database";


    public static final String PUSH_TABLE_NAME = "push_table";
    public static final String PUSH_ID = "_id";
    public static final String PUSH_TITLE  = "title";
    public static final String PUSH_PHOTO = "photo";
    public static final String PUSH_MESSAGE = "message";
    public static final String PUSH_COMPONENT = "component";
    public static final String PUSH_PROJECT = "project";
    public static final String PUSH_ID_COM = "id_com";
    public static final String PUSH_DATE = "date";
    public static final String PUSH_PARENT = "parent";
    public static final String PUSH_ATTACH = "attach";
    public static final String PUSH_STATUS = "status";

    private static final String CREATE_PLUSH_TABLE = "CREATE TABLE " + PUSH_TABLE_NAME + " ( "
            + PUSH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PUSH_TITLE + " TEXT,"
            + PUSH_MESSAGE + " TEXT,"
            + PUSH_PHOTO + " TEXT,"
            + PUSH_COMPONENT + " TEXT,"
            + PUSH_PROJECT + " TEXT,"
            + PUSH_PARENT + " TEXT,"
            + PUSH_ATTACH + " TEXT,"
            + PUSH_ID_COM + " TEXT,"
            + PUSH_STATUS + " INTEGER,"
            + PUSH_DATE + " TEXT );";

    private SQLiteDatabase database;

    public INCODatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLUSH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PUSH_TABLE_NAME);
        onCreate(db);
    }


    public long insertPush(Push push) {
        database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PUSH_TITLE, push.getTitle());
        cv.put(PUSH_MESSAGE, push.getMessage());
        cv.put(PUSH_PHOTO, push.getPhoto());
        cv.put(PUSH_COMPONENT, push.getComponent());
        cv.put(PUSH_PROJECT, push.getProject());
        cv.put(PUSH_DATE, push.getDate());
        cv.put(PUSH_ID_COM, push.getId());
        cv.put(PUSH_PARENT, push.getParent());
        cv.put(PUSH_ATTACH, push.getAttach());
        cv.put(PUSH_STATUS,0);
        return database.insert(PUSH_TABLE_NAME, null, cv);
    }
    public void readPush(long id){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PUSH_STATUS, "1");
        String selection = PUSH_ID+" =?";
        String[] selectionArgs ={String.valueOf(id)};
        database.update(PUSH_TABLE_NAME, values,selection, selectionArgs);
    }
    public Cursor getCursorPush() {
        database = getReadableDatabase();
        Cursor c = database.query(PUSH_TABLE_NAME, null, null, null, null, null, PUSH_ID  + " DESC");
        return c;
    }
    public int getNumberPush(){
        database = getReadableDatabase();
        Cursor c = database.query(PUSH_TABLE_NAME, null, null, null, null, null, PUSH_ID  + " DESC");
        return c.getCount();
    }
    public void cleanDatabase(){
        database = this.getWritableDatabase();
        database.delete(PUSH_TABLE_NAME,null,null);
    }
    public int getNumberPushUnRead(){
        database = getReadableDatabase();
        String selection = PUSH_STATUS+" =?";
        String[] selectionArgs ={"0"};
        Cursor c = database.query(PUSH_TABLE_NAME, null, selection, selectionArgs, null, null, PUSH_ID  + " DESC");
        return c.getCount();
    }
    public void readAlLPushes(){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PUSH_STATUS, "1");
        database.update(PUSH_TABLE_NAME, values,null, null);
    }
    public void test(){
        Cursor c = this.getCursorPush();
        int count  = c.getColumnCount() ;
        for (int i = 0 ;i<count;i++){
            Log.d("kienbk1910",i+":"+c.getColumnName(i));
        }
    }
    public List<Push> getPushes() {
        List<Push> data = new ArrayList<>();
        Push push = null;
        Cursor c = this.getCursorPush();
        if (c != null) {

            while (c.moveToNext()) {
                Log.d("kienbk1910"," getPushes have item");
                String title = c.getString(c.getColumnIndex(this.PUSH_TITLE));
                String photo = c.getString(c.getColumnIndex(this.PUSH_PHOTO));
                String message = c.getString(c.getColumnIndex(this.PUSH_MESSAGE));
                String date = c.getString(c.getColumnIndex(this.PUSH_DATE));
                String parent = c.getString(c.getColumnIndex(this.PUSH_PARENT));
                String component = c.getString(c.getColumnIndex(this.PUSH_COMPONENT));
                String project = c.getString(c.getColumnIndex(this.PUSH_PROJECT));
                String id =  c.getString(c.getColumnIndex(this.PUSH_ID_COM));
                long idDb = c.getLong(c.getColumnIndex(this.PUSH_ID));
                String attach =  c.getString(c.getColumnIndex(this.PUSH_ATTACH));
                push = new Push();
                push.setTitle(title);
                push.setMessage(message);
                push.setPhoto(photo);
                push.setDate(date);
                push.setComponent(component);
                push.setParent(parent);
                push.setProject(project);
                push.setId(id);
                push.setAttach(attach);
                push.setIdDB(idDb);
                data.add(push);
            }
            c.close();
        }
        return data;
    }
}
