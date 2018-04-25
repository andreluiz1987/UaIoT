package prefeitura.uaiot.br.uaiot.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import java.io.IOException;
import java.util.List;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.models.Thing;
import prefeitura.uaiot.br.uaiot.services.http.APIClient;
import prefeitura.uaiot.br.uaiot.services.http.APIContract;
import retrofit2.Call;

/**
 * Created by AndreCoelho on 25/04/2018.
 */

public class GetThingAsync extends AsyncTask<Void, Void, List<Thing>> {

    public interface onThingListener {
        void onCompleted(List<Thing> thingList);
    }

    Context context;
    List<Thing> thingList;
    onThingListener listener;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    public GetThingAsync(Context context, onThingListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progress_layout);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected List<Thing> doInBackground(Void... voids) {

        APIContract apiContract = APIClient.getClient().create(APIContract.class);
        Call<List<Thing>> call = apiContract.getThings();

        try {
            thingList = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
//                (new Callback<List<Thing>>() {
//            @Override
//            public void onResponse(Call<List<Thing>> call, Response<List<Thing>> response) {
//                thingList = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<List<Thing>> call, Throwable t) {
//
//            }
//        });

        return thingList;
    }

    @Override
    protected void onPostExecute(List<Thing> thingList) {
        super.onPostExecute(thingList);

        alertDialog.dismiss();
        alertDialog.cancel();

        if (listener != null)
            listener.onCompleted(thingList);
    }
}
