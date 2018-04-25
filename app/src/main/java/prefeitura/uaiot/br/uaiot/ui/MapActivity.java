package prefeitura.uaiot.br.uaiot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.helpers.VectorHelpers;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_activity)).getMapAsync(this);
    }

    private void loadData() {

        Intent intent = getIntent();

        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(intent.getFloatExtra("LAT", 0), intent.getFloatExtra("LNG", 0));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(VectorHelpers.bitmapDescriptorFromVector(this, R.drawable.ic_marker_car)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
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

        loadData();
    }
}