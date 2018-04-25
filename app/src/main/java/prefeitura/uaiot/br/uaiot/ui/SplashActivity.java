package prefeitura.uaiot.br.uaiot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import prefeitura.uaiot.br.uaiot.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            Thread.sleep(800);

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
