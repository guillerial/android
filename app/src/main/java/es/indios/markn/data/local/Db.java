package es.indios.markn.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.blescanner.models.Topology.Route;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.data.model.Profile;
import es.indios.markn.data.model.Name;
import es.indios.markn.data.model.uvigo.Teacher;

public class Db {

    public Db() { }

    public abstract static class LocationTable{
        public static final String TABLE_NAME = "location";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NEARBY_BEACON = "nearby_beacon";
        public static final String COLUMN_LAST_INDICATION = "last_indication";
        public static final String COLUMN_LAST_IMAGE = "last_image";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME + " TEXT PRIMARY KEY, " +
                        COLUMN_LAST_IMAGE + " TEXT, " +
                        COLUMN_LAST_INDICATION + " TEXT, " +
                        COLUMN_NEARBY_BEACON + " TEXT NOT NULL " +
                        " ); ";

        public static ContentValues toContentValues(Location location) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, location.getName());
            values.put(COLUMN_NEARBY_BEACON, location.getNearby_beacon());
            values.put(COLUMN_LAST_INDICATION, location.getLast_indication());
            values.put(COLUMN_LAST_IMAGE, location.getLast_image());
            return values;
        }

        public static Location parseCursor(Cursor cursor) {
            return new Location(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NEARBY_BEACON)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_INDICATION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_IMAGE))
            );
        }
    }

    public abstract static class IndicationTable{
        public static final String TABLE_NAME = "indication";

        public static final String COLUMN_ROUTE = "route";
        public static final String COLUMN_INDICATION = "indication";
        public static final String COLUMN_IMAGE_URL = "image_url";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ROUTE + " TEXT PRIMARY KEY, " +
                        COLUMN_INDICATION + " TEXT, " +
                        COLUMN_IMAGE_URL + " TEXT " +
                        " ); ";

        public static ContentValues toContentValues(Indication indication) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ROUTE, indication.getRoute());
            values.put(COLUMN_INDICATION, indication.getIndication());
            values.put(COLUMN_IMAGE_URL, indication.getImage_url());
            return values;
        }

        public static Indication parseCursor(Cursor cursor) {
            return new Indication(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ROUTE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_INDICATION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL))
            );
        }
    }

    public abstract static class TopologyTable{
        public static final String TABLE_NAME = "topology";

        public static final String COLUMN_ROUTE = "route";
        public static final String COLUMN_NEXT = "next";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ROUTE + " TEXT PRIMARY KEY, " +
                        COLUMN_NEXT + " TEXT " +
                        " ); ";

        public static ContentValues toContentValues(Route route) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ROUTE, route.getRoute());
            values.put(COLUMN_NEXT, route.getNext());
            return values;
        }

        public static Route parseCursor(Cursor cursor) {
            return new Route(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ROUTE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NEXT))
            );
        }
    }

    public abstract static class ProfessorsTable{
        public static final String TABLE_NAME = "teachers";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_OFFICE = "office";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " TEXT PRIMARY KEY, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_EMAIL + " TEXT, " +
                        COLUMN_OFFICE + " TEXT " +
                        " ); ";

        public static ContentValues toContentValues(Teacher teacher) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, teacher.getId());
            values.put(COLUMN_NAME, teacher.getName());
            values.put(COLUMN_EMAIL, teacher.getEmail());
            values.put(COLUMN_OFFICE, teacher.getOffice());
            return values;
        }

        public static Teacher parseCursor(Cursor cursor) {
            return new Teacher(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_OFFICE))
            );
        }
    }
}
