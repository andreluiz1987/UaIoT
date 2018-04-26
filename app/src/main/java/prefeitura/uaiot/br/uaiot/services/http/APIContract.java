package prefeitura.uaiot.br.uaiot.services.http;

import java.util.List;

import prefeitura.uaiot.br.uaiot.models.LogAsset;
import prefeitura.uaiot.br.uaiot.models.Thing;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AndreCoelho on 24/04/2018.
 */

public interface APIContract {

    @GET("/serv/thing")
    Call<List<Thing>> getThings();

    @GET("/serv/log?")
    Call<List<LogAsset>> getThingPosition(@Query("thingImei")int thingImei);

    @GET("/serv/log")
    Call<List<LogAsset>> getAssetsPosition();
}