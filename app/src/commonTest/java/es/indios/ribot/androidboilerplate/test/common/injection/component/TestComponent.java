package es.indios.ribot.androidboilerplate.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import es.indios.markn.injection.component.ApplicationComponent;
import es.indios.ribot.androidboilerplate.test.common.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
