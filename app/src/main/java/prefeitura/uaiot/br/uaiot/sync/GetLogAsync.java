package prefeitura.uaiot.br.uaiot.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import java.io.IOException;
import java.util.List;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.models.LogAsset;
import prefeitura.uaiot.br.uaiot.services.http.APIClient;
import prefeitura.uaiot.br.uaiot.services.http.APIContract;
import retrofit2.Call;

/**
 * Created by AndreCoelho on 25/04/2018.
 */

public class GetLogAsync extends AsyncTask<Void, Void, List<LogAsset>> {

    public interface onLogListener {
        void onCompleted(List<LogAsset> thingList);
    }

    Context context;
    List<LogAsset> logList;
    GetLogAsync.onLogListener listener;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    int id;

    public GetLogAsync(Context context, GetLogAsync.onLogListener listener, int id) {
        this.context = context;
        this.listener = listener;
        this.id = id;
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
    protected List<LogAsset> doInBackground(Void... voids) {

        APIContract apiContract = APIClient.getClient().create(APIContract.class);
        Call<List<LogAsset>> call = apiContract.getThingPosition(id);

        try {
            logList = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logList;
    }

    @Override
    protected void onPostExecute(List<LogAsset> logList) {
        super.onPostExecute(logList);

        alertDialog.dismiss();
        alertDialog.cancel();

        if (listener != null)
            listener.onCompleted(logList);
    }
}
