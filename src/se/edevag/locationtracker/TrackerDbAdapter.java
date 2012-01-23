package se.edevag.locationtracker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;


public class TrackerDbAdapter {

	
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;    
    
    /**
     * Database sql statement
     */
    private static final String DATABASE_CREATE =
        "create table location_entry (_id integer primary key autoincrement, "
        + "latitude double, longitude double, accuracy float, timestamp long);";

    private static final String DATABASE_NAME = "tracker_db";
    private static final String DATABASE_TABLE = "location_entry";
    
    private static final String TABLE_LAT = "latitude";
    private static final String TABLE_LONG = "longitude";
    private static final String TABLE_ACCU = "accuracy";
    private static final String TABLE_TIME = "timestamp";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS location_entry");
            onCreate(db);
        }
    }

    public TrackerDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public TrackerDbAdapter open() throws SQLException {
    	dbHelper = new DatabaseHelper(mCtx);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }



    public long insert(Location location) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TABLE_LAT, location.getLatitude());
        initialValues.put(TABLE_LONG, location.getLongitude());
        initialValues.put(TABLE_ACCU, location.getAccuracy());
        initialValues.put(TABLE_TIME, System.currentTimeMillis());

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public List<TrackerEntry> fetch(int numberOfRecords, boolean startFromBeginning) {

    	List<TrackerEntry> result = new ArrayList<TrackerEntry>();
    	
    	Cursor cur = database.query(DATABASE_TABLE, new String[] {TABLE_LAT, TABLE_LONG,
        		TABLE_ACCU, TABLE_TIME}, null, null, null, null, TABLE_TIME + " desc");
    	
    	cur.moveToFirst();
        while (cur.isAfterLast() == false) {
        	TrackerEntry te = new TrackerEntry();
        	te.latitude = cur.getDouble(0);
        	te.longitude = cur.getDouble(1);
        	te.accuracy = cur.getFloat(2);
        	te.time = cur.getLong(3);
        	result.add(te);
        	
       	    cur.moveToNext();
        }
        cur.close();
        
        return result;
    }

    /*
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    */
	
}
