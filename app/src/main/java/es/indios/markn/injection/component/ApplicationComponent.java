package es.indios.markn.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import es.indios.markn.data.DataManager;
import es.indios.markn.data.services.IndicationsSyncService;
import es.indios.markn.data.local.DatabaseHelper;
import es.indios.markn.data.local.PreferencesHelper;
import es.indios.markn.data.remote.MarknApi;
import es.indios.markn.data.services.LocationsSyncService;
import es.indios.markn.data.services.MyFirebaseInstanceIDService;
import es.indios.markn.data.services.MyFirebaseMessagingService;
import es.indios.markn.data.services.SchedulesSyncService;
import es.indios.markn.data.services.TeachersSyncService;
import es.indios.markn.data.services.TopologySyncService;
import es.indios.markn.injection.module.ApplicationModule;
import es.indios.markn.injection.ApplicationContext;
import es.indios.markn.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {


    @ApplicationContext Context context();
    Application application();
    MarknApi ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

    void inject(IndicationsSyncService indicationsSyncService);
    void inject(LocationsSyncService locationsSyncService);
    void inject(TopologySyncService topologySyncService);
    void inject(SchedulesSyncService schedulesSyncService);
    void inject(TeachersSyncService teachersSyncService);
    void inject(MyFirebaseMessagingService myFirebaseMessagingService);
    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);
}
