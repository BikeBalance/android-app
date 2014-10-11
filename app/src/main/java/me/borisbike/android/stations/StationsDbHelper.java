package me.borisbike.android.stations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationsDbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public StationsDbHelper(Context c) {
        super(c, StationsMeta.DATABASE_NAME, null, StationsMeta.DATABASE_VERSION);
    }

    private static final String SQL_QUERY_CREATE =
            "CREATE TABLE " + StationsMeta.StationsTable.TABLE_NAME + " (" +
                    StationsMeta.StationsTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    StationsMeta.StationsTable.NAME + " VARCHAR(255) NOT NULL, " +
                    StationsMeta.StationsTable.TERMINAL_NAME + " INTEGER NOT NULL," + //the unique terminal id
                    StationsMeta.StationsTable.LAT + " DOUBLE NOT NULL, " +
                    StationsMeta.StationsTable.LON + " DOUBLE NOT NULL " +
                    ");"
            ;

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        this.db.execSQL(SQL_QUERY_CREATE);
    }

    private static final String SQL_QUERY_DROP =
            "DROP TABLE IF EXISTS " + StationsMeta.StationsTable.TABLE_NAME + ";"
            ;

    public void resetTable(){
        this.db.execSQL(SQL_QUERY_DROP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(SQL_QUERY_DROP);
        onCreate(db);
    }
}
