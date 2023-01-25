package com.dwikavindraharyoradithya.reactnativebase64grayscale

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission.Write
import com.facebook.react.bridge.*
import java.io.ByteArrayOutputStream
import java.math.BigInteger


class RNBase64GrayscaleModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "RNBase64GrayscaleModule"
    @ReactMethod
    private fun base64Coloredtobase64Grayscale(base64: String, promise:Promise) {
        try{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val code = Base64.decode(base64, Base64.DEFAULT)
        val test = BitmapFactory.decodeByteArray(code, 0, code.size)
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bmp_Copy = test.copy(Bitmap.Config.ARGB_8888, true)
        for (x in 0 until test.width) {
            for (y in 0 until test.height) {
                // get one pixel color
                val pixel = test.getPixel(x, y)
                // retrieve color of all channels
                val A = Color.alpha(pixel)
                var R = Color.red(pixel)
                var G = Color.green(pixel)
                var B = Color.blue(pixel)
                // take conversion up to one single value
                B = (0.299 * R + 0.587 * G + 0.114 * B).toInt()
                G = B
                R = G
                // set new pixel color to output bitmap
                bmp_Copy.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        bmp_Copy.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
        promise.resolve(encoded)
        }
        catch(e:Throwable){
            promise.reject(e);
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @ReactMethod
    private fun base64GrayscaletobinaryImage(base64: String, promise:Promise) {
        try{
        val code = Base64.decode(base64, Base64.DEFAULT)
        val test = BitmapFactory.decodeByteArray(code, 0, code.size)
        var binaryImageString= StringBuilder() //uses string builder instead of concatenating because
        // if we were to concat using += its slow and creates a new instance for every concat Stringbuilder doesn't
        val n = Math.ceil(test.width.toDouble()/ 8).toInt();
        val scaledBitmap= Bitmap.createScaledBitmap(test,n,test.height,false)
        for (y in 0 until scaledBitmap.height) {
            for (x in 0 until scaledBitmap.width) {
                // get one pixel color
                val pixel = test.getPixel(x, y);
                var A= Color.alpha(pixel).toFloat()
                var G= Color.green(pixel).toFloat()
                var R = Color.red(pixel).toFloat()
                var B = Color.blue(pixel).toFloat()
                var finalInt= Color.argb(A,R,G,B)
                if (finalInt > 0.0) {
                    binaryImageString.append("1")

                } else {
                    binaryImageString.append("0")

                }
            }
            binaryImageString.append("\n")
        }
        promise.resolve(binaryImageString.toString())}
        catch(e:Throwable){
            promise.reject(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ReactMethod
    private fun base64GrayscaletoArrayofPixels(base64: String, promise:Promise) {
        try{
            val code = Base64.decode(base64, Base64.DEFAULT)
            val test = BitmapFactory.decodeByteArray(code, 0, code.size)
            var pixels= Arguments.createArray() //uses string builder instead of concatenating because
            // if we were to concat using += its slow and creates a new instance for every concat Stringbuilder doesn't
            val n = Math.ceil(test.width.toDouble()/ 8).toInt();
            val scaledBitmap= Bitmap.createScaledBitmap(test,n,test.height,false)
            for (y in 0 until scaledBitmap.height) {
                for (x in 0 until scaledBitmap.width) {
                    // get one pixel color
                    val pixel = test.getPixel(x, y);
                    var A= Color.alpha(pixel)
                    var G= Color.green(pixel)
                    var R = Color.red(pixel)
                    var B = Color.blue(pixel)
//                    var finalInt= Color.argb(A,R,G,B)
                    pixels.pushInt(G)
                }

            }
            promise.resolve(pixels)}
        catch(e:Throwable){
            promise.reject(e)
        }
    }

}
