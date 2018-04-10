package es.indios.markn.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.indios.markn.data.model.user.TokenResponse;
import es.indios.markn.injection.ApplicationContext;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "markn_pref_file";

    public static final String PREF_USER_ACCOUNT = "pref_user_account";
    public static final String PREF_USER_TOKEN = "pref_user_token";
    public static final String PREF_USER_NAME = "pref_user_name";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public Boolean isLoggedIn(){
        if(!mPref.getString(PREF_USER_TOKEN,"").equals(""))
            return true;
        else
            return false;
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public ObservableSource<TokenResponse> setToken(final TokenResponse tokenResponse) {
        return Observable.create(new ObservableOnSubscribe<TokenResponse>() {
            @Override
            public void subscribe(ObservableEmitter<TokenResponse> e) throws Exception {
                if(e.isDisposed()) return;
                String authToken = tokenResponse.getToken();
                String email = tokenResponse.getEmail();
                String username = tokenResponse.getUsername();
                mPref.edit().putString(PREF_USER_TOKEN, authToken).apply();
                mPref.edit().putString(PREF_USER_ACCOUNT, email).apply();
                mPref.edit().putString(PREF_USER_NAME, username).apply();
                e.onNext(tokenResponse);
                e.onComplete();
            }
        });
    }
}
