package es.indios.markn.injection.component;

import dagger.Subcomponent;
import es.indios.markn.injection.PerFragment;
import es.indios.markn.injection.module.FragmentModule;

/**
 * Created by CristinaPosada on 22/03/2018.
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}
