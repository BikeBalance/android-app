package me.borisbike.android.stations;

import android.net.Uri;
import android.provider.BaseColumns;


public class StationsMeta {
    private StationsMeta() { }

    public static final String AUTHORITY = "me.borisbike.android.content.Stations";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/stations"
    );

    public static final String DATABASE_NAME = "stations.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CONTENT_TYPE_ARTICLES_LIST = "vnd.android.cursor.dir/vnd.borisbike.activities";
    public static final String CONTENT_TYPE_ARTICLE_ONE = "vnd.android.cursor.item/vnd.borisbike.activities";

    public class StationsTable implements BaseColumns {
        private StationsTable() { }

        public static final String TABLE_NAME = "tbl_stations";

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String LAT = "latitude";
        public static final String LON = "longitude";
        public static final String TERMINAL_NAME = "terminal_name";

    }


}
