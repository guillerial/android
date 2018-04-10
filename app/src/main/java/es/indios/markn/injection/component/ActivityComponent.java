package es.indios.markn.injection.component;

import dagger.Subcomponent;
import es.indios.markn.injection.PerActivity;
import es.indios.markn.injection.module.ActivityModule;
import es.indios.markn.ui.init.InitActivity;
import es.indios.markn.ui.login.LoginActivity;
import es.indios.markn.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(InitActivity initActivity);

    void inject(LoginActivity loginActivity);
}
