package prefeitura.uaiot.br.uaiot.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.helpers.VectorHelpers;
import prefeitura.uaiot.br.uaiot.models.Alarm;
import prefeitura.uaiot.br.uaiot.models.LogAsset;
import prefeitura.uaiot.br.uaiot.models.Thing;
import prefeitura.uaiot.br.uaiot.services.http.APIClient;
import prefeitura.uaiot.br.uaiot.services.http.APIContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private int delay = 2000;   // delay de 5 seg.
    private int interval = 10000;  // intervalo de 1 seg.
    private Timer timer = new Timer();
    private Timer timerAlarm = new Timer();
    private NotificationManagerCompat notificationManager;

    private GoogleMap mMap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                getLogAssets();
            }
        }, delay, interval);

        timerAlarm.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                createAlarm();
            }
        }, delay, 20000);

        return view;
    }

    private void createAlarm() {

        APIContract apiContract = APIClient.getClient().create(APIContract.class);
        Call<List<Alarm>> call = apiContract.getAlarms();

        call.enqueue(new Callback<List<Alarm>>() {
            @Override
            public void onResponse(Call<List<Alarm>> call, Response<List<Alarm>> response) {
                List<Alarm> alarms = response.body();

                for (Alarm alarm : alarms) {
                    notificationAlarm(alarm);
                }
            }

            @Override
            public void onFailure(Call<List<Alarm>> call, Throwable t) {

            }
        });

    }

    private void notificationAlarm(Alarm alarm) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), "1")
                .setSmallIcon(R.drawable.ic_notification_alarm)
                .setContentTitle(alarm.getName())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(alarm.getDescription()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager = NotificationManagerCompat.from(getActivity());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (notificationManager != null)
            notificationManager.cancelAll();

        if (timer != null)
            timer.cancel();

        if (timerAlarm != null)
            timerAlarm.cancel();
    }

    private void getLogAssets() {

        APIContract apiContract = APIClient.getClient().create(APIContract.class);
        Call<List<LogAsset>> call = apiContract.getAssetsPosition();

        call.enqueue(new Callback<List<LogAsset>>() {
            @Override
            public void onResponse(Call<List<LogAsset>> call, Response<List<LogAsset>> response) {
                List<LogAsset> logAssets = response.body();

                drawMarkerInMap(logAssets);
            }

            @Override
            public void onFailure(Call<List<LogAsset>> call, Throwable t) {

            }
        });
    }

    private void drawMarkerInMap(List<LogAsset> logAssets) {

        LatLng latLngFirst = null;

        if (mMap == null)
            return;

        try {

            mMap.clear();

            for (int i = 0; i < logAssets.size(); i++) {

                LatLng latLng = new LatLng(logAssets.get(i).getLatitude(), logAssets.get(i).getLongitude());

                if (latLngFirst == null)
                    latLngFirst = latLng;

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(logAssets.get(i).getThing().getDescription())
                        .icon(getBitmapByType(logAssets.get(i).getThing())));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFirst, 17.0f));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private BitmapDescriptor getBitmapByType(Thing thing) {

        if (thing.getTypeThing().getName().toLowerCase().equals("viatura")) {
            return VectorHelpers.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker_car_viatura);
        } else if (thing.getTypeThing().getName().toLowerCase().equals("van escolar")) {
            return VectorHelpers.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker_car_escolar);
        } else if (thing.getTypeThing().getName().toLowerCase().equals("onibus")
                || thing.getTypeThing().getName().toLowerCase().equals("transp. escolar")) {
            return VectorHelpers.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker_escolar_bus);
        } else {
            return VectorHelpers.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker_car);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(-19.9191248, -43.9408178);
        //mMap.addMarker(new MarkerOptions().position(latLng));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
    }
}