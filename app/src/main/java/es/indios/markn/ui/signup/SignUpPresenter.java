package es.indios.markn.ui.signup;

import javax.inject.Inject;

import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.user.TokenResponse;
import es.indios.markn.injection.ConfigPersistent;
import es.indios.markn.ui.base.BasePresenter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@ConfigPersistent
public class SignUpPresenter extends BasePresenter<SignUpMvpView>{

    private final DataManager mDataManager;

    @Inject
    public SignUpPresenter(DataManager dataManager){mDataManager = dataManager;}


    public void signUp(String name, String email, String password) {
        mDataManager.signup(name, email, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
