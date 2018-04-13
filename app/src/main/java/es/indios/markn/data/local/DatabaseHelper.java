package es.indios.markn.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.blescanner.models.Topology.Route;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.data.model.uvigo.Teacher;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import es.indios.markn.data.model.Ribot;
import timber.log.Timber;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        this(dbOpenHelper, Schedulers.io());
    }

    @VisibleForTesting
    public DatabaseHelper(DbOpenHelper dbOpenHelper, Scheduler scheduler) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, scheduler);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Location> setLocations(final Collection<Location> newLocations){
        return Observable.create(new ObservableOnSubscribe<Location>() {
            @Override
            public void subscribe(ObservableEmitter<Location> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.LocationTable.TABLE_NAME, null);
                    for (Location location : newLocations) {
                        long result = mDb.insert(Db.LocationTable.TABLE_NAME,
                                Db.LocationTable.toContentValues(location),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) emitter.onNext(location);
                    }
                    transaction.markSuccessful();
                    emitter.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Location>> getLocations() {
        return mDb.createQuery(Db.LocationTable.TABLE_NAME,
                "SELECT * FROM " + Db.LocationTable.TABLE_NAME)
                .mapToList(new Function<Cursor, Location>() {
                    @Override
                    public Location apply(@NonNull Cursor cursor) throws Exception {
                        return Db.LocationTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Indication> setIndications(final Collection<Indication> newIndications){
        return Observable.create(new ObservableOnSubscribe<Indication>() {
            @Override
            public void subscribe(ObservableEmitter<Indication> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.IndicationTable.TABLE_NAME, null);
                    for (Indication indication : newIndications) {
                        long result = mDb.insert(Db.IndicationTable.TABLE_NAME,
                                Db.IndicationTable.toContentValues(indication),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) emitter.onNext(indication);
                    }
                    transaction.markSuccessful();
                    emitter.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Indication>> getIndications() {
        return mDb.createQuery(Db.IndicationTable.TABLE_NAME,
                "SELECT * FROM " + Db.IndicationTable.TABLE_NAME)
                .mapToList(new Function<Cursor, Indication>() {
                    @Override
                    public Indication apply(@NonNull Cursor cursor) throws Exception {
                        return Db.IndicationTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Route> setTopology(final Collection<Route> newRoutes){
        return Observable.create(new ObservableOnSubscribe<Route>() {
            @Override
            public void subscribe(ObservableEmitter<Route> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.TopologyTable.TABLE_NAME, null);
                    for (Route route : newRoutes) {
                        long result = mDb.insert(Db.TopologyTable.TABLE_NAME,
                                Db.TopologyTable.toContentValues(route),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) emitter.onNext(route);
                    }
                    transaction.markSuccessful();
                    emitter.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Route>> getTopology() {
        return mDb.createQuery(Db.TopologyTable.TABLE_NAME,
                "SELECT * FROM " + Db.TopologyTable.TABLE_NAME)
                .mapToList(new Function<Cursor, Route>() {
                    @Override
                    public Route apply(@NonNull Cursor cursor) throws Exception {
                        return Db.TopologyTable.parseCursor(cursor);
                    }
                });
    }

    public ObservableSource<? extends Teacher> setTeachers(final Collection<Teacher> newTeachers) {
        return Observable.create(new ObservableOnSubscribe<Teacher>() {
            @Override
            public void subscribe(ObservableEmitter<Teacher> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.ProfessorsTable.TABLE_NAME, null);
                    for (Teacher teacher : newTeachers) {
                        long result = mDb.insert(Db.ProfessorsTable.TABLE_NAME,
                                Db.ProfessorsTable.toContentValues(teacher),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) emitter.onNext(teacher);
                    }
                    transaction.markSuccessful();
                    emitter.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Teacher>> getTeachers() {
        return mDb.createQuery(Db.ProfessorsTable.TABLE_NAME,
                "SELECT * FROM " + Db.ProfessorsTable.TABLE_NAME)
                .mapToList(new Function<Cursor, Teacher>() {
                    @Override
                    public Teacher apply(@NonNull Cursor cursor) throws Exception {
                        return Db.ProfessorsTable.parseCursor(cursor);
                    }
                });
    }

    public ObservableSource<? extends Schedule> setSchedules(final Collection<Schedule> newSchedules) {
        return Observable.create(new ObservableOnSubscribe<Schedule>() {
            @Override
            public void subscribe(ObservableEmitter<Schedule> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.SchedulesTable.TABLE_NAME, null);
                    for (Schedule schedule : newSchedules) {
                        long result = mDb.insert(Db.SchedulesTable.TABLE_NAME,
                                Db.SchedulesTable.toContentValues(schedule),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        Timber.i("Schedule: "+schedule.getGroup().getSubject_name()+" con result: "+result);
                        if (result >= 0) emitter.onNext(schedule);
                    }
                    transaction.markSuccessful();
                    emitter.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Schedule>> getSchedules() {
        return mDb.createQuery(Db.SchedulesTable.TABLE_NAME,
                "SELECT * FROM " + Db.SchedulesTable.TABLE_NAME)
                .mapToList(new Function<Cursor, Schedule>() {
                    @Override
                    public Schedule apply(@NonNull Cursor cursor) throws Exception {
                        return Db.SchedulesTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<List<Schedule>> getSchedulesByday(int day) {
        return mDb.createQuery(Db.SchedulesTable.TABLE_NAME,
                "SELECT * FROM " + Db.SchedulesTable.TABLE_NAME + " WHERE " + Db.SchedulesTable.COLUMN_DAY + " == '" + day+"'")
                .mapToList(new Function<Cursor, Schedule>() {
                    @Override
                    public Schedule apply(@NonNull Cursor cursor) throws Exception {
                        return Db.SchedulesTable.parseCursor(cursor);
                    }
                });
    }
}
