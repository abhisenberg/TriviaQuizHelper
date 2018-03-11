package com.abheisenberg.quizhelper;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.abheisenberg.quizhelper.ScreenshotFiles.ScreenshotCallback;
import com.abheisenberg.quizhelper.ScreenshotFiles.Screenshotter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorkerService extends Service implements View.OnClickListener
{

    private static final String TAG = WorkerService.class.getSimpleName();

    private static final String path = Environment.getExternalStorageDirectory() + "/QuizHelper/";

    private static final String gQuery = "https://www.google.com/search?q=";
    private WindowManager.LayoutParams webViewParams, ssBtParams, tvQuesParams;
    private View webView_floatV, ssBt_floatV, tv_floatV;
    private ImageTextReader imageTextReader;
    private WindowManager windowManager;
    private boolean webViewVisible = false;
    private ImageView ss_button, closeWV_button;
    private TextView tv_ques;
    private WebView webView;
    private int wv_pos;

    public WorkerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        wv_pos = intent.getIntExtra(MainActivity.WV_POSITION, MainActivity.WV_AT_TOP);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Service Started");


        //************************INITIALIZING THE FLOATING VIEWS*************************
        webView_floatV = LayoutInflater.from(this).inflate(R.layout.service_webview_layout,
                null);
        ssBt_floatV = LayoutInflater.from(this).inflate(R.layout.service_ssbutton_layout,
                null);
        tv_floatV = LayoutInflater.from(this).inflate(R.layout.service_ques_layout,
                null);

        tv_ques = tv_floatV.findViewById(R.id.tv_ques);
        closeWV_button = ssBt_floatV.findViewById(R.id.ib_close_wv);
        ss_button = ssBt_floatV.findViewById(R.id.ib_ss);
        closeWV_button.setOnClickListener(this);
        ss_button.setOnClickListener(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        int wv_height = (int) getResources().getDimension(R.dimen.webview_height);
        webViewParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                wv_height,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        webViewParams.x = 0;
        webViewParams.y = 0;
        webViewParams.gravity = Gravity.TOP | Gravity.CENTER;

        ssBtParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        ssBtParams.x = 0;
        ssBtParams.y = 0;
        ssBtParams.gravity = Gravity.BOTTOM | Gravity.START;

        int tv_ques_height = (int) getResources().getDimension(R.dimen.tvques_height);
        tvQuesParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                tv_ques_height,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        tvQuesParams.x = 0;
        tvQuesParams.y = 0;
        tvQuesParams.gravity = Gravity.BOTTOM | Gravity.END;

        tv_ques.setText("");
        windowManager.addView(tv_floatV, tvQuesParams);
        windowManager.addView(ssBt_floatV, ssBtParams);
        //************************INITIALIZATION DONE*************************************


        //************************WEBVIEW INITIALIZE**************************************
        webView = webView_floatV.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        //************************WEBVIEW MADE********************************************


        //*****************************TEXTRECOGNIZER********************************
        imageTextReader = new ImageTextReader(getApplicationContext());
        //******************************TEXTRECOGNIZER MADE***************************
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.ib_ss:
                removeWV();
                Screenshotter.getInstance()
                        .setSize(MainActivity.width, MainActivity.height)
                        .takeScreenshot(getApplicationContext(), -1, MainActivity.mediaProjData,
                                new ScreenshotCallback() {
                                    @Override
                                    public void onScreenshot(Bitmap bitmap) {
                                        String ques = imageTextReader.fromImage(bitmap);
                                        tv_ques.setText(ques);
                                        Log.d(TAG, "Text on TVQues: "+tv_ques.getText());
                                        webViewSearch(ques);
                                    }
                                });

                break;

            case R.id.ib_close_wv:
                removeWV();
                break;
        }
    }

    private void webViewSearch(String toSearch){
        webView.loadUrl(gQuery + toSearch);
        addWV();
    }

    private void removeWV(){
        if(webViewVisible) {
            windowManager.removeView(webView_floatV);
            webViewVisible = false;
        }
    }

    private void addWV(){
        if(!webViewVisible){
            windowManager.addView(webView_floatV, webViewParams);
            webViewVisible = true;
        }
    }

    private void createImage(Bitmap bmp){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File picFile = new File(path, Long.toString(System.currentTimeMillis())+".jpg");
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        try {
            Log.d(TAG, "Writing images");
            picFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(picFile);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if(webView_floatV != null) windowManager.removeView(webView_floatV);
        if(ssBt_floatV != null) windowManager.removeView(ssBt_floatV);
        super.onDestroy();
    }
}
