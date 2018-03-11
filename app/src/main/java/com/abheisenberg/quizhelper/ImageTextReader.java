package com.abheisenberg.quizhelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

/**
 * Created by abheisenberg on 1/3/18.
 */

public class ImageTextReader {

    private static final String TAG = "OCR";

    private Context context;
    private TextRecognizer textRecognizer;

    public ImageTextReader(Context context){
        this.context = context;
        textRecognizer = new TextRecognizer
                .Builder(context)
                .build();

        if(!textRecognizer.isOperational()){
            Log.d(TAG, "OCR dependencies not available ");
        }
    }

    public String fromImage(Bitmap bitimg){
        Frame img = new Frame.Builder()
                .setBitmap(bitimg)
                .build();
        String searchString = "";
        if(textRecognizer.isOperational()){
            final SparseArray<TextBlock> items = textRecognizer.detect(img);
            if(items.size() != 0){
                int max_size = Integer.MIN_VALUE, index = -1;
                for(int i=0; i<items.size(); i++){
                    searchString = items.valueAt(i).getValue();
                    Log.d(TAG, "FOUND TEXT: "+searchString);
                    if(searchString.length() > max_size){
                        max_size = searchString.length();
                        index = i;
                    }
                }
                searchString = items.valueAt(index).getValue().replace("\n", " ");
                Log.d(TAG, "Biggest text: "+searchString);
            }
        } else Log.d(TAG, "TextRecognizer not available.");
        return searchString;
    }
}
