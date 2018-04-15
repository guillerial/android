package es.indios.markn.data.remote;

import java.util.List;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.blescanner.models.Topology.Route;
import es.indios.markn.data.model.user.TokenResponse;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.data.model.uvigo.Teacher;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MarknApi {


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

    @AuthenticatedEndpoint
    @GET("/schedules/")
    Observable<List<Schedule>> getSchedules();
}
