package com.nativemodule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import java.io.File;
import java.io.EOFException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;

public class PixelsImage extends ReactContextBaseJavaModule {
    PixelsImage(ReactApplicationContext context) {
        super(context);
    }

    Bitmap bitmap;
    Uri getPath;



    @NonNull
    @Override
    public String getName() {
        return "PixelsImage";
    }

    @ReactMethod
    private Bitmap createBinaryPixels(String url ) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Bitmap IMG = BitmapFactory.decodeResource(this.getCurrentActivity().getResources(), R.drawable.image_attractive);

        if(!(imgFile==null)){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getPath() + "/assets/image.png");
            Bitmap output = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
            int A, R, G, B;
            int a, r, g, b;
            int pixelColor;
            int height = myBitmap.getHeight();
            int width = myBitmap.getWidth();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelColor = myBitmap.getPixel(x, y);

                    a = (pixelColor >> 24) & 0xff;
                    r = (pixelColor >> 16) & 0xff;
                    g = (pixelColor >> 8) & 0xff;
                    b = pixelColor & 0xff;

                    //calculate avg
                     int avg = (r+g+b) / 3;

                    //replace pixels
                    pixelColor = (a << 24) | (avg << 16) | (avg << 8) | avg;

                    System.out.println("Calcule de avg " + pixelColor);
                    output.setPixel(x, y, pixelColor);
                }
//                myBitmap.recycle();
//                myBitmap = null;
//            System.out.println("les valeurs de Y sont " + y);

            }
        }
        else {
//            myImage.setImageResource(R.drawable.ic_launcher_background);
            System.out.println("mon chemin introuvable ");
        }

//        System.out.println("Dataaaaaaaaaaaa width " + bmOut);
        return BitmapFactory.decodeFile(url, options);

    }
}
