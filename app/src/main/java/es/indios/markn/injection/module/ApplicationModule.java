package es.indios.markn.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import es.indios.markn.data.remote.MarknApi;
import es.indios.markn.data.remote.MarknService;
import es.indios.markn.injection.ApplicationContext;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    MarknApi provideRibotsService() {
        return new MarknService().newRibotsService(mApplication.getApplicationContext());
    }

}
