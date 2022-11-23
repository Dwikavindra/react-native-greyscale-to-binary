package com.nativemodule;

import static android.app.PendingIntent.getActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PixelsImage extends ReactContextBaseJavaModule {
    PixelsImage(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "PixelsImage";
    }

    Bitmap myBitmap;
    File imgFile;
    Bitmap outPut;
    static File grayScaleImg = null;

    @ReactMethod
    private void createBinaryPixels(String url, Integer width, Integer height, Callback callback) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] code = Base64.decode(url, Base64.DEFAULT);
        Bitmap test = BitmapFactory.decodeByteArray(code, 0, code.length);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmp_Copy = test.copy(Bitmap.Config.ARGB_8888,true);
        System.out.println("Helllo from Sys out");


        Log.d("PixelImageModule", "Helllo  ");
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                int pixel = test.getPixel(x, y);
                // retrieve color of all channels
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                // set new pixel color to output bitmap
                bmp_Copy.setPixel(x,y,Color.argb(A, R, G, B));
                System.out.println("Helllo from loop");
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp_Copy.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        callback.invoke(encoded);
    }

}