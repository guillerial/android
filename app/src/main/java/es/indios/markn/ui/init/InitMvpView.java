package es.indios.markn.ui.init;

import android.support.v4.app.FragmentManager;

import es.indios.markn.ui.base.MvpView;

public interface InitMvpView extends MvpView {
    FragmentManager getMFragmentManager();
}
