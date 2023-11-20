package net.free.supertool.useful.scancode.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyUtil {
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static void myLog(Object o){
        System.out.println("---***---:"+o);
    }

    public static String convertTimestampToDate(long timestamp) {
        try {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void copy(Context context,String text){
        // 在Android 11及更高版本中使用android.content.ClipboardManager类
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
        } else {
            // 在Android 10及更低版本中使用android.text.ClipboardManager类
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText("要复制的内容");
        }
    }

    public static void searchOnWeb(Activity activity,String query) {
        try {
            String url = "https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8");
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap decodeBitmapFromResource(Context context ,int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap createQR(Context context,String content, BarcodeFormat mformat){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(content, mformat, 600, 600);
            if(bitmap!=null){
                return bitmap;
            }else {
                return null;
            }

        } catch(Exception e) {
            Log.e("QR",e.getMessage());
            String[] parts = e.getMessage().split(":");
            String result = parts[1].trim();
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public static BarcodeFormat getFormat(String mformat){
         BarcodeFormat format = BarcodeFormat.QR_CODE;
        switch (mformat){
            case "contacts":
            case "sms":
            case "phone":
            case "e-mail":

                break;

            case "maxicode":
                format = BarcodeFormat.MAXICODE;

                break;
            case "url":

                break;
            case "data_matrix":
                format = BarcodeFormat.DATA_MATRIX;


                break;
            case "aztec":
                format = BarcodeFormat.AZTEC;

                break;
            case "upc_e":
                format = BarcodeFormat.UPC_E;


                break;
            case "upc_a":
                format = BarcodeFormat.UPC_A;

                break;
            case "pdf_417":
                format = BarcodeFormat.PDF_417;


                break;
            case "itf":
                format = BarcodeFormat.ITF;


                break;
            case "ean_8":
                format = BarcodeFormat.EAN_8;


                break;
            case "ean_13":
                format = BarcodeFormat.EAN_13;


                break;
            case "code_39":
                format = BarcodeFormat.CODE_39;

                break;
            case "code_93":
                format = BarcodeFormat.CODE_93;


                break;
            case "code_128":
                format = BarcodeFormat.CODE_128;


                break;
            case "codabar":
                format = BarcodeFormat.CODABAR;


                break;

            default:

                break;


        }
        return format;
    }
}
