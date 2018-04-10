package es.indios.markn.ui.login;

import javax.inject.Inject;

import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.user.TokenResponse;
import es.indios.markn.injection.ConfigPersistent;
import es.indios.markn.ui.base.BasePresenter;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMvpView>{

    private final DataManager mDataManager;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void checkLoggedIn() {
        if(mDataManager.isLoggedIn()){
            if(getMvpView()!=null){
                getMvpView().onLoggedUser();
            }
        }
    }

    public void logIn(String email, String password) {
        mDataManager.login(email, password).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TokenResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TokenResponse tokenResponse) {
                        if(getMvpView()!=null)
                            getMvpView().onLoggedUser();
                    }

                    @Override
                    public void onError(Throwable e) {
                            if (e instanceof HttpException){
                                if(getMvpView()!=null){
                                    getMvpView().onForbiddenLogin(((HttpException) e).code());
                                }
                            }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
