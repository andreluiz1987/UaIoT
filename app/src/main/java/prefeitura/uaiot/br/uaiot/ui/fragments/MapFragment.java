package prefeitura.uaiot.br.uaiot.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.helpers.VectorHelpers;
import prefeitura.uaiot.br.uaiot.models.LogAsset;
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

        return view;
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

        if (mMap == null)
            return;

        try {

            mMap.clear();

            for (int i = 0; i < logAssets.size(); i++) {

                LatLng latLng = new LatLng(logAssets.get(i).getLatitude(), logAssets.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(logAssets.get(i).getThing().getDescription())
                        .icon(VectorHelpers.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker_car)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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