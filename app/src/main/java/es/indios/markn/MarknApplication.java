package es.indios.markn;

import android.app.Application;
import android.content.Context;


import es.indios.markn.blescanner.Scanner;
import timber.log.Timber;
import es.indios.markn.injection.module.ApplicationModule;
import es.indios.ribot.androidboilerplate.BuildConfig;
import es.indios.markn.injection.component.ApplicationComponent;
import es.indios.markn.injection.component.DaggerApplicationComponent;

public class MarknApplication extends Application  {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            //Fabric.with(this, new Crashlytics());
        }
        Scanner.getInstance().init(this);
    }

    public static MarknApplication get(Context context) {
        return (MarknApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
