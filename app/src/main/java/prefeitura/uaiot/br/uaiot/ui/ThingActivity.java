package prefeitura.uaiot.br.uaiot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.helpers.ConnectionNet;
import prefeitura.uaiot.br.uaiot.models.LogAsset;
import prefeitura.uaiot.br.uaiot.models.Thing;
import prefeitura.uaiot.br.uaiot.sync.GetLogAsync;
import prefeitura.uaiot.br.uaiot.sync.GetThingAsync;
import prefeitura.uaiot.br.uaiot.ui.adapters.RecyclerTouchListener;
import prefeitura.uaiot.br.uaiot.ui.adapters.ThingAdapter;

public class ThingActivity extends AppCompatActivity {

    private ProgressBar pgbLoading;
    private TextView txtLoading;
    private RecyclerView recyclerView;
    private ThingAdapter thingAdapter;

    private List<Thing> thingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing);

        getSupportActionBar().setTitle("Localização");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pgbLoading = findViewById(R.id.txtLoading1);
        txtLoading = findViewById(R.id.txtLoading2);
        recyclerView = findViewById(R.id.recycler_view);

        thingAdapter = new ThingAdapter(this, thingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(thingAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Thing thing = thingAdapter.getList().get(position);

                LoadPositionThing(thing);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void LoadPositionThing(Thing thing) {

        try {
            if (ConnectionNet.hasConnection(this)) {

                new GetLogAsync(ThingActivity.this, logListener, thing.getId()).execute();

            } else {
                Snackbar.make(findViewById(R.id.coordinatorThingActivity),
                        "Não existe conexão com a internet!", Snackbar.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {

        } finally {
            visibilityLoading(View.GONE);
        }
    }

    private void loadThingServer() {

        visibilityLoading(View.VISIBLE);

        try {

            if (ConnectionNet.hasConnection(this)) {

                new GetThingAsync(ThingActivity.this, onThingListener).execute();

            } else {
                Snackbar.make(findViewById(R.id.coordinatorThingActivity),
                        "Não existe conexão com a internet!", Snackbar.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {

        } finally {
            visibilityLoading(View.GONE);
        }
    }

    GetThingAsync.onThingListener onThingListener = new GetThingAsync.onThingListener() {

        @Override
        public void onCompleted(List<Thing> thingList) {

            if (thingList != null) {
                thingAdapter.updateList(thingList);
            } else
                Snackbar.make(findViewById(R.id.coordinatorThingActivity),
                        "Não há dados para serem exibidos.", Snackbar.LENGTH_SHORT).show();

        }
    };

    GetLogAsync.onLogListener logListener = new GetLogAsync.onLogListener() {
        @Override
        public void onCompleted(List<LogAsset> logAsset) {

            if(logAsset != null && logAsset.size() > 0) {
                Intent intent = new Intent(ThingActivity.this, MapActivity.class);
                intent.putExtra("LAT", logAsset.get(0).getLatitude());
                intent.putExtra("LNG", logAsset.get(0).getLongitude());
                intent.putExtra("NAME", logAsset.get(0).getThing().getDescription());

                startActivity(intent);
            } else {
                Snackbar.make(findViewById(R.id.coordinatorThingActivity),
                        "Não foi possível localizar o veículo.", Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_update_thing_list) {
            loadThingServer();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void visibilityLoading(final int blnValue) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                pgbLoading.setVisibility(blnValue);
                pgbLoading.invalidate();
                txtLoading.setVisibility(blnValue);
                txtLoading.invalidate();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thing_list, menu);
        return true;
    }
}
