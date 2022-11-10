package com.nativemodule;

import static android.app.PendingIntent.getActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    Bitmap output;
    File imgFile;

    @ReactMethod
    private Bitmap createBinaryPixels(String url, String name, String ext ) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap test =  BitmapFactory.decodeFile(url, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        imgFile = Environment.getExternalStoragePublicDirectory(url);

        try{
            if(!(url==null)){
                myBitmap = BitmapFactory.decodeFile(imgFile.getPath());
                output = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
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

                        output.setPixel(x, y, pixelColor);


                    }
                }
            }
            else {
                System.out.println("my untraceable path ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Dataaaaaaaaaaaa --900 " + ext);
        System.out.println("Dataaaaaaaaaaaa --my " + myBitmap);
        System.out.println("Dataaaaaaaaaaaa --URL " + name);
        bitmapToFile(output, name + '.' + ext);
        return test;
    }

    public static File bitmapToFile(Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {

            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            System.out.println("new filllllllllle " + file);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }

}
