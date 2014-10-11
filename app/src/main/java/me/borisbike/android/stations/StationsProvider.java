package me.borisbike.android.stations;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;


public class StationsProvider extends ContentProvider
{
    private StationsDbHelper mDbHelper;
    private static final UriMatcher sUriMatcher;
    private static final int STATION_TYPE_LIST = 1;
    private static final int STATION_TYPE_ONE = 2;
    private Context ctx;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(StationsMeta.AUTHORITY, "stations", STATION_TYPE_LIST);
        sUriMatcher.addURI(StationsMeta.AUTHORITY, "stations/#", STATION_TYPE_ONE);
    }

    private static final HashMap<String, String> sActivityProjectionMap;
    static {
        sActivityProjectionMap = new HashMap<String, String>();
        sActivityProjectionMap.put(StationsMeta.StationsTable.ID, StationsMeta.StationsTable.ID);
        sActivityProjectionMap.put(StationsMeta.StationsTable.LAT, StationsMeta.StationsTable.LAT);
        sActivityProjectionMap.put(StationsMeta.StationsTable.LON, StationsMeta.StationsTable.LON);
        sActivityProjectionMap.put(StationsMeta.StationsTable.NAME,StationsMeta.StationsTable.NAME);
        sActivityProjectionMap.put(StationsMeta.StationsTable.TERMINAL_NAME,StationsMeta.StationsTable.TERMINAL_NAME);
    }
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = 0;
        switch(sUriMatcher.match(uri)) {
            case STATION_TYPE_LIST:
                count = db.delete(StationsMeta.StationsTable.TABLE_NAME, where, whereArgs);
                break;

            case STATION_TYPE_ONE:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(StationsMeta.StationsTable.TABLE_NAME,
                        StationsMeta.StationsTable.ID + " = " + rowId +
                                (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""),
                        whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        ctx.getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case STATION_TYPE_LIST:
                return StationsMeta.CONTENT_TYPE_ARTICLES_LIST;

            case STATION_TYPE_ONE:
                return StationsMeta.CONTENT_TYPE_ARTICLE_ONE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(sUriMatcher.match(uri) != STATION_TYPE_LIST) {
            throw new IllegalArgumentException("[Insert](01)Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insert(StationsMeta.StationsTable.TABLE_NAME, null, values);
        if(rowId > 0) {
            Uri articleUri = ContentUris.withAppendedId(StationsMeta.CONTENT_URI, rowId);
            ctx.getContentResolver().notifyChange(articleUri, null);
            Log.d("me.borisbike.android", "SAVED SOMETHING IN DB YAY");
            return articleUri;
        }
        throw new IllegalArgumentException("[Insert](02)Unknown URI: " + uri);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new StationsDbHelper(getContext());
        this.ctx = getContext();
        return false;
    }

    public boolean createHelper(Context ctx){
        mDbHelper = new StationsDbHelper(ctx);
        this.ctx = ctx;
        return false;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch(sUriMatcher.match(uri)) {
            case STATION_TYPE_LIST:
                builder.setTables(StationsMeta.StationsTable.TABLE_NAME);
                builder.setProjectionMap(sActivityProjectionMap);
                break;

            case STATION_TYPE_ONE:
                builder.setTables(StationsMeta.StationsTable.TABLE_NAME);
                builder.setProjectionMap(sActivityProjectionMap);
                builder.appendWhere(StationsMeta.StationsTable.ID + " = " +
                        uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor queryCursor = builder.query(db, projection, selection, selectionArgs, null, null, null);
        queryCursor.setNotificationUri(ctx.getContentResolver(), uri);

        return queryCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
                      String[] whereArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = 0;
        switch(sUriMatcher.match(uri)) {
            case STATION_TYPE_LIST:
                count = db.update(StationsMeta.StationsTable.TABLE_NAME, values, where, whereArgs);
                break;

            case STATION_TYPE_ONE:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(StationsMeta.StationsTable.TABLE_NAME, values,
                        StationsMeta.StationsTable.ID + " = " + rowId +
                                (!TextUtils.isEmpty(where) ? " AND (" + ")" : ""),
                        whereArgs);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        ctx.getContentResolver().notifyChange(uri, null);
        return count;
    }
}
