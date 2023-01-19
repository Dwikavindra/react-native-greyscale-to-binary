package com.dwikavindraharyoradithya.reactnativebase64grayscale

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Color
import android.util.Base64
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
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
    @ReactMethod
    private fun base64tobinaryString(base64: String, callback: Callback) {
        val decode = Base64.decode(base64, Base64.DEFAULT)
        val binaryStr = BigInteger(1, decode).toString(2)
        callback.invoke(binaryStr)
    }
}
