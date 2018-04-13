package es.indios.markn.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.blescanner.models.Topology.Route;
import es.indios.markn.blescanner.models.Topology.IndicationsTopologyWrapper;
import es.indios.markn.data.model.user.TokenResponse;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.data.model.uvigo.Teacher;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import es.indios.markn.data.local.DatabaseHelper;
import es.indios.markn.data.local.PreferencesHelper;
import es.indios.markn.data.remote.RibotsService;

@Singleton
public class DataManager {

    private final RibotsService mRibotsService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }


    public Observable<Location> syncLocations(){
        return mRibotsService.getLocations().concatMap(new Function<List<Location>, ObservableSource<? extends Location>>() {
            @Override
            public ObservableSource<? extends Location> apply(List<Location> locations) throws Exception {
                return mDatabaseHelper.setLocations(locations);
            }
        });
    }

    public Observable<List<Location>> getLocations() {
        return mDatabaseHelper.getLocations().distinct();
    }

    public Observable<Indication> syncIndications(){
        return mRibotsService.getIndications().concatMap(new Function<List<Indication>, ObservableSource<? extends Indication>>() {
            @Override
            public ObservableSource<? extends Indication> apply(List<Indication> indications) throws Exception {
                return mDatabaseHelper.setIndications(indications);
            }
        });
    }

    public Observable<List<Indication>> getIndications() {
        return mDatabaseHelper.getIndications().distinct();
    }

    public Observable<Route> syncTopology(){
        return mRibotsService.getTopology().concatMap(new Function<List<Route>, ObservableSource<? extends Route>>() {
            @Override
            public ObservableSource<? extends Route> apply(List<Route> routes) throws Exception {
                return mDatabaseHelper.setTopology(routes);
            }
        });
    }

    public Observable<List<Route>> getTopology() {
        return mDatabaseHelper.getTopology().distinct();
    }

    public Observable<IndicationsTopologyWrapper> getIndicationsAndTopology(){
        return getTopology().zipWith(getIndications(), new BiFunction<List<Route>, List<Indication>, IndicationsTopologyWrapper>() {
            @Override
            public IndicationsTopologyWrapper apply(List<Route> routes, List<Indication> indications) throws Exception {
                return new IndicationsTopologyWrapper(indications, routes);
            }
        });
    }

    public Boolean isLoggedIn(){
        return mPreferencesHelper.isLoggedIn();
    }

    public Observable<TokenResponse> login(String email, String password) {
        return mRibotsService.login(email, password).concatMap(new Function<TokenResponse, ObservableSource<? extends TokenResponse>>() {
            @Override
            public ObservableSource<? extends TokenResponse> apply(TokenResponse tokenResponse) throws Exception {
                return mPreferencesHelper.setToken(tokenResponse);
            }
        });
    }

    public Observable<TokenResponse> signup(String name, String email, String password) {
        return mRibotsService.register(email, name, password).concatMap(new Function<TokenResponse, ObservableSource<? extends TokenResponse>>() {
            @Override
            public ObservableSource<? extends TokenResponse> apply(TokenResponse tokenResponse) throws Exception {
                return mPreferencesHelper.setToken(tokenResponse);
            }
        });
    }

    public Observable<Teacher> syncTeachers(){
        return mRibotsService.getTeachers().concatMap(new Function<List<Teacher>, ObservableSource<? extends Teacher>>() {
            @Override
            public ObservableSource<? extends Teacher> apply(List<Teacher> teachers) throws Exception {
                return mDatabaseHelper.setTeachers(teachers);
            }
        });
    }

    public Observable<List<Teacher>> getTeachers() {
        return mDatabaseHelper.getTeachers().distinct();
    }

    public Observable<Schedule> syncSchedules(){
        return mRibotsService.getSchedules().concatMap(new Function<List<Schedule>, ObservableSource<? extends Schedule>>() {
            @Override
            public ObservableSource<? extends Schedule> apply(List<Schedule> schedules) throws Exception {
                return mDatabaseHelper.setSchedules(schedules);
            }
        });
    }

    public Observable<List<Schedule>> getSchedules(){
        return mDatabaseHelper.getSchedules().distinct();
    }

    public Observable<List<Schedule>> getSchedulesByDay(int day){
        return mDatabaseHelper.getSchedulesByday(day).distinct();
    }

    public void logout() {
        mPreferencesHelper.logout();
    }
}
