package es.indios.markn.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.indios.markn.injection.ApplicationContext;

@Singleton
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ribots.db";
    public static final int DATABASE_VERSION = 12;

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //Uncomment line below if you want to enable foreign keys
        //db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(Db.ProfessorsTable.CREATE);
            db.execSQL(Db.LocationTable.CREATE);
            db.execSQL(Db.IndicationTable.CREATE);
            db.execSQL(Db.TopologyTable.CREATE);
            db.execSQL(Db.SchedulesTable.CREATE);
            db.execSQL(Db.NotificationTable.CREATE);
            //Add other tables here
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<DATABASE_VERSION){
            db.execSQL("DROP TABLE IF EXISTS "+Db.LocationTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+Db.IndicationTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+Db.TopologyTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+Db.ProfessorsTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+Db.SchedulesTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+Db.NotificationTable.TABLE_NAME);
            db.execSQL(Db.LocationTable.CREATE);
            db.execSQL(Db.IndicationTable.CREATE);
            db.execSQL(Db.TopologyTable.CREATE);
            db.execSQL(Db.ProfessorsTable.CREATE);
            db.execSQL(Db.SchedulesTable.CREATE);
            db.execSQL(Db.NotificationTable.CREATE);
        }
    }

}
