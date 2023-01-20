package com.dwikavindraharyoradithya.reactnativebase64grayscale

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.*
import java.io.ByteArrayOutputStream
import java.math.BigInteger

class RNBase64GrayscaleModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "RNBase64GrayscaleModule"
    @ReactMethod
    private fun base64Coloredtobase64Grayscale(base64: String, width: Int, height: Int, callback: Callback) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val code = Base64.decode(base64, Base64.DEFAULT)
        val test = BitmapFactory.decodeByteArray(code, 0, code.size)
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bmp_Copy = test.copy(Bitmap.Config.ARGB_8888, true)
        for (x in 0 until width) {
            for (y in 0 until height) {
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
        callback.invoke(encoded)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @ReactMethod
    private fun base64GrayscaletobinaryImage(base64: String, width: Int, height: Int,callback: Callback) {
        val code = Base64.decode(base64, Base64.DEFAULT)
        val test = BitmapFactory.decodeByteArray(code, 0, code.size)
        var binaryImageString= StringBuilder() //uses string builder instead of concatenating because
        // if we were to concat using += its slow and creates a new instance for every concat this one doesn't
        val n = Math.ceil(test.width.toDouble()/ 8).toInt();
        val scaledBitmap= Bitmap.createScaledBitmap(test,n,test.height,false)
        println("This is width ${width}")
        println("This is height ${height}")
        for (y in 0 until scaledBitmap.height) {
            for (x in 0 until scaledBitmap.width) {
                // get one pixel color
                val pixel = test.getPixel(x, y);
                var A= Color.alpha(pixel).toFloat()
                var G= Color.green(pixel).toFloat()
                var R = Color.red(pixel).toFloat()
                var B = Color.blue(pixel).toFloat()
                var finalInt= Color.argb(A,R,G,B)
                println("This is final int $finalInt");
                if (finalInt > 0.0) {
                    // console.log(`This is value larger than zero ${value}`);
                    binaryImageString.append("1")

                } else {
                    // console.log(`This is value not larger than zero ${value}`);
                    binaryImageString.append("0")

                }
            }
            binaryImageString.append("\n")
        }
        callback.invoke(binaryImageString.toString())
    }
    @ReactMethod
    private fun base64tobinaryString(base64: String, callback: Callback) {
        val decode = Base64.decode(base64, Base64.DEFAULT)
        val binaryStr = BigInteger(1, decode).toString(2)
        callback.invoke(binaryStr)
    }
}
