package udl.eps.testaccelerometre;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TestAccelerometreActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private boolean color = false;
    private TextView textView1, textView2, textView3;
    private long lastUpdate;
    private Sensor mAccelerometer;
    private String middleText;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView1 = findViewById(R.id.textView1);
        textView1.setBackgroundColor(Color.GREEN);

        textView2 = findViewById(R.id.textView2);

        textView3 = findViewById(R.id.textView3);
        textView3.setBackgroundColor(Color.YELLOW);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {

            //Debemos asegurar que el sensor específico existe.
            if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

                mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                textView2.setText(getString(R.string.shake) + "\n"
                                  + "\n" + getString(R.string.resolution) + " " + mAccelerometer.getResolution()
                                  + "\n" + getString(R.string.max_range)  + " " + mAccelerometer.getMaximumRange()
                                  + "\n" + getString(R.string.power)      + " " + mAccelerometer.getPower()
                                  + "\n" + getString(R.string.vendor)     + " " + mAccelerometer.getVendor()
                                  + "\n" + getString(R.string.version)    + " " + mAccelerometer.getVersion());
            }
            else
            {
                textView2.setText(R.string.AccelerometerSensorException);
            }
        }

        lastUpdate = System.currentTimeMillis();

    }

    @Override
    protected void onResume() {
        // register this class as a listener for the accelerometer sensor
        super.onResume();
        sensorManager.registerListener(this,mAccelerometer,mAccelerometer.getMinDelay());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        getAccelerometer(event);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot >= 2)
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;

            Toast.makeText(this, R.string.shuffed, Toast.LENGTH_SHORT).show();

            if (color) {
                textView1.setBackgroundColor(Color.GREEN);

            } else {
                textView1.setBackgroundColor(Color.RED);
            }
            color = !color;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}