package com.abheisenberg.quizhelper;

import android.Manifest;
import android.content.Intent;
import android.graphics.Point;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String path = Environment.getExternalStorageDirectory() + "/QuizHelper/";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ALL_PERM_GRANTED = 1;
    private static final int MEDIA_PROJ_GRANTED = 2;
    public static final int WV_AT_TOP = 89;
    public static final int WV_AT_BOTTOM = 90;
    public static final String WV_POSITION = "positionwv";
    private int wv_pos;


    public static Intent mediaProjData;
    public static int width = -1, height = -1;


    Button bt_startTop, bt_startBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_startBottom = findViewById(R.id.bt_startBottom);
        bt_startTop = findViewById(R.id.bt_startTop);
        bt_startTop.setOnClickListener(this);
        bt_startBottom.setOnClickListener(this);

        File directory = new File(path);
        directory.mkdirs();

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.CAPTURE_VIDEO_OUTPUT,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                ALL_PERM_GRANTED);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_startTop:
                wv_pos = WV_AT_TOP;
                initialize();
                break;

            case R.id.bt_startBottom:
                wv_pos = WV_AT_BOTTOM;
                initialize();
                break;
        }
    }

    private void initialize(){
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                MEDIA_PROJ_GRANTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MEDIA_PROJ_GRANTED){
            if(resultCode == RESULT_OK){
                mediaProjData = data;
                Log.d(TAG, "Is mediaProjData null? "+ (mediaProjData == null));

                Intent serviceIn = new Intent(MainActivity.this, WorkerService.class);
                serviceIn.putExtra(WV_POSITION, wv_pos);
                startService(serviceIn);

                startService(new Intent(MainActivity.this, WorkerService.class));
                finish();
            } else {
                Toast.makeText(this, "Please give required permissions."
                        ,Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
