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
//    int w = bitmap.getWidth();
//    int h = bitmap.getHeight();
      Uri getPath;
      int alpha = 255;
      int red = 100;
      int green = 150;
      int blue = 200;



    @NonNull
    @Override
    public String getName() {
        return "PixelsImage";
    }

//    @ReactMethod
//    public void decodeSampledBitmapFromResource(String img ) {
//        String uri = "https://www.referenseo.com/wp-content/uploads/2019/03/image-attractive.jpg";
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.inSampleSize = 2;
//        options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
//        File test = new File(uri);
//        Bitmap IMG = BitmapFactory.decodeFile(String.valueOf(test), options);
//        if(IMG == null){
//            System.out.println("File est null");
//        }else{
//            System.out.println("file différent de null");
//        }
//        System.out.println("Voir image "+  IMG);
//
////        Bitmap bitmap =
//    }

//            decodeSampledBitmapFromResource

    @ReactMethod
    private Bitmap createBinaryPixels(String url) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if(!(imgFile==null)){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getPath() + "/assets/g.png");
//            myImage.setImageBitmap(myBitmap);
            System.out.println("mon chemin" + myBitmap);
        }
        else {
//            myImage.setImageResource(R.drawable.ic_launcher_background);
            System.out.println("mon chemin introuvable");
        }

        Bitmap IMG = BitmapFactory.decodeResource(this.getCurrentActivity().getResources(), R.drawable.image_attractive);

        // image size
        int width = IMG.getWidth();
        int height = IMG.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, IMG.getConfig());
        // color information
        int A, R, G, B;
        int pixel = 0;
        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = IMG.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);

                //average calcul
                int avg = (R+G+B) / 3;

                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
            IMG.recycle();

            // return final image
            return bmOut;
        }


        System.out.println("Dataaaaaaaaaaaa width " + bmOut);
        return BitmapFactory.decodeFile(url, options);


    }
}
