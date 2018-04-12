package es.indios.markn.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.blescanner.models.Topology.Route;
import es.indios.markn.data.model.user.TokenResponse;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.ribot.androidboilerplate.BuildConfig;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import es.indios.markn.data.model.Ribot;
import es.indios.markn.util.MyGsonTypeAdapterFactory;
import retrofit2.http.POST;

public interface RibotsService {


    @FormUrlEncoded
    @POST("/register/")
    Observable<TokenResponse> register(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("/login/")
    Observable<TokenResponse> login(
            @Field("email") String email,
            @Field("password") String password);

    @GET("/classrooms/")
    Observable<List<Location>> getLocations();

    @GET("/indications/")
    Observable<List<Indication>> getIndications();

    @GET("/topology/")
    Observable<List<Route>> getTopology();

    @GET("/teachers/")
    Observable<List<Teacher>> getTeachers();


    /******** Helper class that sets up a new services *******/
    class Creator {

        public static RibotsService newRibotsService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.MARKN_API_HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(RibotsService.class);
        }
    }
}
