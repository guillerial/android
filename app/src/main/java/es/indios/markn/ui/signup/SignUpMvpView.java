package es.indios.markn.ui.signup;

import es.indios.markn.ui.base.MvpView;

public interface SignUpMvpView extends MvpView {
    void onLoggedUser();

    void onForbiddenLogin(int code);
}
