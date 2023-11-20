package net.free.supertool.useful.scancode.db;

import static net.free.supertool.useful.scancode.util.MyUtil.bitmapToByteArray;
import static net.free.supertool.useful.scancode.util.MyUtil.byteArrayToBitmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.free.supertool.useful.scancode.bean.QRCodeBean;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class QRCodeDao {
    private SQLiteOpenHelper myDbHelper;

    private Context context;
    public QRCodeDao(Context context){
        myDbHelper = QRCodeDbHelper.getInstance(context);
        System.out.println("myDbHelper:"+myDbHelper);
        this.context = context;
    }

    //添加配置
    public int add(QRCodeBean qrCodeBean){
        Bitmap getCode_image = byteArrayToBitmap(qrCodeBean.getCode_image());
        Bitmap getCode_icon = byteArrayToBitmap(qrCodeBean.getCode_icon());
        String getCode_content = qrCodeBean.getCode_content();
        String getCode_format = qrCodeBean.getCode_format();
        String getCode_time = qrCodeBean.getCode_time();
        String getCode_type = qrCodeBean.getCode_type();
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if(db.isOpen()){
                ContentValues values = new ContentValues();
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                getCode_image.compress(Bitmap.CompressFormat.PNG, 100, os);

                final ByteArrayOutputStream os2 = new ByteArrayOutputStream();
                getCode_icon.compress(Bitmap.CompressFormat.PNG, 100, os2);

                values.put("code_image", os.toByteArray());
                values.put("code_icon", os2.toByteArray());
                values.put("code_content", getCode_content);
                values.put("code_format", getCode_format);
                values.put("code_time", getCode_time);
                values.put("code_type", getCode_type);
                db.insert("QRCodeConfig", "_id", values);
                db.close();
                return 0;
        }
        return 1;
    }

    //更新Hook配置
    public void update(int id,QRCodeBean qrCodeBean){
        Bitmap getCode_image = byteArrayToBitmap(qrCodeBean.getCode_image());
        Bitmap getCode_icon = byteArrayToBitmap(qrCodeBean.getCode_icon());
        String getCode_content = qrCodeBean.getCode_content();
        String getCode_format = qrCodeBean.getCode_format();
        String getCode_time = qrCodeBean.getCode_time();
        String getCode_type = qrCodeBean.getCode_type();


        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            getCode_image.compress(Bitmap.CompressFormat.PNG, 100, os);

            final ByteArrayOutputStream os2 = new ByteArrayOutputStream();
            getCode_icon.compress(Bitmap.CompressFormat.PNG, 100, os2);

            values.put("code_image", os.toByteArray());
            values.put("code_icon", os2.toByteArray());
            values.put("code_content", getCode_content);
            values.put("code_format", getCode_format);
            values.put("code_time", getCode_time);
            values.put("code_type", getCode_type);
            db.update("QRCodeConfig", values, " _id = ? ", new String[]{String.valueOf(id)});
            db.close();
        }

    }

    //得到所有配置
    public ArrayList<QRCodeBean> findAll(){
        ArrayList<QRCodeBean> qrCodeBeans = new ArrayList<>();

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor c = db.query("QRCodeConfig", new String[]{"code_image","code_icon","code_content","code_format","code_time","code_type","_id"}, null, null, null, null, null,null);
            System.out.println("---"+c.getColumnCount());

            while(c.moveToNext()){
                byte[]  in = c.getBlob(0);
                Bitmap getCode_image = BitmapFactory.decodeByteArray(in, 0, in.length);
                byte[]  in2 = c.getBlob(1);
                Bitmap getCode_icon = BitmapFactory.decodeByteArray(in2, 0, in2.length);

                String getCode_content = c.getString(2);
                String getCode_format = c.getString(3);
                String getCode_time = c.getString(4);
                String getCode_type = c.getString(5);
                int ID = c.getInt(6);
                qrCodeBeans.add(new QRCodeBean(in,in2,getCode_content,getCode_format,getCode_time,getCode_type,ID));
            }
            c.close();
            db.close();
        }
        return qrCodeBeans;
    }

    //线程查询
    public void findAllAsync(final QueryCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<QRCodeBean> qrCodeBeans = new ArrayList<>();

                SQLiteDatabase db = myDbHelper.getReadableDatabase();
                if (db.isOpen()) {
                    Cursor c = db.query("QRCodeConfig", new String[]{"code_image", "code_icon", "code_content", "code_format", "code_time", "code_type", "_id"}, null, null, null, null, null, null);
                    System.out.println("---" + c.getColumnCount());

                    while (c.moveToNext()) {
                        byte[] in = c.getBlob(0);
                        Bitmap getCode_image = BitmapFactory.decodeByteArray(in, 0, in.length);
                        byte[] in2 = c.getBlob(1);
                        Bitmap getCode_icon = BitmapFactory.decodeByteArray(in2, 0, in2.length);

                        String getCode_content = c.getString(2);
                        String getCode_format = c.getString(3);
                        String getCode_time = c.getString(4);
                        String getCode_type = c.getString(5);
                        int ID = c.getInt(6);
                        qrCodeBeans.add(new QRCodeBean(in, in2, getCode_content, getCode_format, getCode_time, getCode_type, ID));
                    }
                    c.close();
                    db.close();
                }


                callback.onQueryComplete(qrCodeBeans);
            }
        }).start();
    }

    public interface QueryCallback {
        void onQueryComplete(ArrayList<QRCodeBean> qrCodeBeans);
    }
  /*  public ArrayList<AppInfo> findByPgName(String pgName){
        ArrayList<AppInfo> AppHookInfo = new ArrayList<AppInfo>();
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor c = db.query("HookAppInfo", new String[]{"hook_app_AppName","hook_app_AppImage","hook_app_pgName","hook_app_class","hook_app_modelName","hook_app_canshu","hook_app_return","hook_app_BZname","_id"}, "hook_app_pgName=?",new String[]{pgName}, null, null, null);
            while(c.moveToNext()){
                String hook_app_AppName = c.getString(0);
                byte[]  in = c.getBlob(1);
                Bitmap hook_app_AppImage = BitmapFactory.decodeByteArray(in, 0, in.length);
                String hook_app_pgName = c.getString(2);
                String hook_app_class = c.getString(3);
                String hook_app_modelName = c.getString(4);
                String hook_app_canshu = c.getString(5);
                String hook_app_return = c.getString(6);
                String hook_app_BZname = c.getString(7);
                int ID = c.getInt(8);
                AppHookInfo.add(new AppInfo(hook_app_pgName,hook_app_AppName,hook_app_AppImage,hook_app_class,hook_app_modelName,hook_app_return,hook_app_canshu,hook_app_BZname,ID));
            }
            c.close();
            db.close();
        }
        return AppHookInfo;
    }
    public ArrayList<AppInfo> findAppName(Boolean distinct){
        ArrayList<AppInfo> AppHookInfo = new ArrayList<AppInfo>();
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor c = db.query(distinct,"HookAppInfo", new String[]{"hook_app_AppName","hook_app_AppImage","hook_app_pgName"}, null, null, null, null, null,null);
            while(c.moveToNext()){
                String hook_app_AppName = c.getString(0);
                byte[]  in = c.getBlob(1);
                Bitmap hook_app_AppImage = BitmapFactory.decodeByteArray(in, 0, in.length);
                String hook_app_pgName = c.getString(2);
                AppHookInfo.add(new AppInfo(hook_app_pgName,hook_app_AppName,hook_app_AppImage,null,null,null,null,null));
            }
            c.close();
            db.close();
        }
        return AppHookInfo;
    }
*/
    public void deleteQRCodeById(int id){
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        int result = db.delete("QRCodeConfig", "_id=?", new String[]{String.valueOf(id)});
    }

}
