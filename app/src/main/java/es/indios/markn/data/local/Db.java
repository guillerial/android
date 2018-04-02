package es.indios.markn.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.blescanner.models.Topology.Route;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.data.model.Profile;
import es.indios.markn.data.model.Name;

public class Db {

    public Db() { }

    public abstract static class RibotProfileTable {
        public static final String TABLE_NAME = "ribot_profile";

        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_HEX_COLOR = "hex_color";
        public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_BIO = "bio";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                        COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                        COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                        COLUMN_HEX_COLOR + " TEXT NOT NULL, " +
                        COLUMN_DATE_OF_BIRTH + " INTEGER NOT NULL, " +
                        COLUMN_AVATAR + " TEXT, " +
                        COLUMN_BIO + " TEXT" +
                " ); ";

        public static ContentValues toContentValues(Profile profile) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, profile.email());
            values.put(COLUMN_FIRST_NAME, profile.name().first());
            values.put(COLUMN_LAST_NAME, profile.name().last());
            values.put(COLUMN_HEX_COLOR, profile.hexColor());
            values.put(COLUMN_DATE_OF_BIRTH, profile.dateOfBirth().getTime());
            values.put(COLUMN_AVATAR, profile.avatar());
            if (profile.bio() != null) values.put(COLUMN_BIO, profile.bio());
            return values;
        }

        public static Profile parseCursor(Cursor cursor) {
            Name name = Name.create(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
            long dobTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_BIRTH));

            return Profile.builder()
                    .setName(name)
                    .setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)))
                    .setHexColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HEX_COLOR)))
                    .setDateOfBirth(new Date(dobTime))
                    .setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVATAR)))
                    .setBio(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIO)))
                    .build();
        }
    }


    public abstract static class LocationTable{
        public static final String TABLE_NAME = "location";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NEARBY_BEACON = "nearby_beacon";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME + " TEXT PRIMARY KEY, " +
                        COLUMN_NEARBY_BEACON + " TEXT NOT NULL " +
                        " ); ";

        public static ContentValues toContentValues(Location location) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, location.getName());
            values.put(COLUMN_NEARBY_BEACON, location.getNearby_beacon());
            return values;
        }

        public static Location parseCursor(Cursor cursor) {
            return new Location(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NEARBY_BEACON))
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
}
