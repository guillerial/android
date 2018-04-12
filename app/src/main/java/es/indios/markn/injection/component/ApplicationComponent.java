package es.indios.markn.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import es.indios.markn.data.DataManager;
import es.indios.markn.data.sync.IndicationsSyncService;
import es.indios.markn.data.local.DatabaseHelper;
import es.indios.markn.data.local.PreferencesHelper;
import es.indios.markn.data.remote.RibotsService;
import es.indios.markn.data.sync.LocationsSyncService;
import es.indios.markn.data.sync.SchedulesSyncService;
import es.indios.markn.data.sync.TeachersSyncService;
import es.indios.markn.data.sync.TopologySyncService;
import es.indios.markn.injection.module.ApplicationModule;
import es.indios.markn.injection.ApplicationContext;
import es.indios.markn.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {


    @ApplicationContext Context context();
    Application application();
    RibotsService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

    void inject(IndicationsSyncService indicationsSyncService);
    void inject(LocationsSyncService locationsSyncService);
    void inject(TopologySyncService topologySyncService);
    void inject(SchedulesSyncService schedulesSyncService);
    void inject(TeachersSyncService teachersSyncService);
}
