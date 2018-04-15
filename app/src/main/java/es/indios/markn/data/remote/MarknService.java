package es.indios.markn.data.remote;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import es.indios.markn.MarknApplication;
import es.indios.markn.data.local.PreferencesHelper;
import es.indios.markn.util.MyGsonTypeAdapterFactory;
import es.indios.markn.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import timber.log.Timber;

/**
 * Created by guille on 15/04/18.
 */

@Singleton
public class MarknService {
        private String token;

        private SharedPreferences sharedPreferences;

        private OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .readTimeout(240, TimeUnit.SECONDS)
                .connectTimeout(240, TimeUnit.SECONDS).addInterceptor(chain -> {
                    Request original = chain.request();
                    String pathRequest = original.url().encodedPath();
                    for (Method method :
                            MarknApi.class.getMethods()) {
                        if (method.isAnnotationPresent(AuthenticatedEndpoint.class)) {
                            if (method.isAnnotationPresent(GET.class)) {
                                if (method.getAnnotation(GET.class).value().equals(pathRequest)) {
                                    original = addAuthenticationHeader(sharedPreferences, original);
                                }
                            } else {
                                if (method.getAnnotation(POST.class).value().equals(pathRequest)) {
                                    original = addAuthenticationHeader(sharedPreferences, original);
                                }
                            }
                        }
                    }
                    return chain.proceed(original);
                });


        public MarknApi newRibotsService(Context context) {
            sharedPreferences = context.getSharedPreferences(PreferencesHelper.PREF_FILE_NAME, Context.MODE_PRIVATE);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient.build())
                    .baseUrl(BuildConfig.MARKN_API_HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(MarknApi.class);
        }

        private Request addAuthenticationHeader(SharedPreferences preferences, Request original) {
            if(this.token != null){
                original = original.newBuilder().addHeader("Authorization", "Token "+this.token).build();
            }else{
                String token = preferences.getString(PreferencesHelper.PREF_USER_TOKEN, "");
                original = original.newBuilder().addHeader("Authorization", "Token "+token).build();
            }
            Timber.i("---Retrieving authentication token..");
            Timber.i("---Token: "+original.header("Authorization"));
            Timber.i("Retrieved authentication token");
            return original;
        }
}
