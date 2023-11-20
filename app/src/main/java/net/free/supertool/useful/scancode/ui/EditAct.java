package net.free.supertool.useful.scancode.ui;

import static net.free.supertool.useful.scancode.util.MyUtil.bitmapToByteArray;
import static net.free.supertool.useful.scancode.util.MyUtil.byteArrayToBitmap;
import static net.free.supertool.useful.scancode.util.MyUtil.createQR;
import static net.free.supertool.useful.scancode.util.MyUtil.getFormat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.bean.QRCodeBean;
import net.free.supertool.useful.scancode.db.QRCodeDao;

public class EditAct extends AppCompatActivity implements View.OnClickListener{

    private QRCodeBean qrCodeBean;
    private ImageView ic_back,save,code_icon,testImage;
    private TextView code_type,code_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit);

        initUI();
        initData();
        initListener();

    }
    public void initUI(){
        ic_back = findViewById(R.id.ic_back);
        save = findViewById(R.id.save);
        code_icon = findViewById(R.id.code_icon);
        code_type = findViewById(R.id.code_type);
        code_content = findViewById(R.id.code_content);
        testImage = findViewById(R.id.testImage);
    }
    public void initData(){

        String JsCodeBean = MMKV.defaultMMKV().decodeString("qrCodeBean");

        try {
            qrCodeBean = new Gson().fromJson(JsCodeBean, QRCodeBean.class);
        }catch (Exception e){
            Log.e("fromJson", e.getMessage());
            qrCodeBean = null;
        }
        if (qrCodeBean!=null) {
            String code_content = qrCodeBean.getCode_content();
            String code_type = qrCodeBean.getCode_type();
            Bitmap code_icon = byteArrayToBitmap(qrCodeBean.getCode_icon());

            this.code_type.setText(code_type);
            this.code_content.setText(code_content);
            this.code_icon.setImageBitmap(code_icon);

        }
    }

    public void initListener(){
        ic_back.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ic_back){
            finish();
        }
        if (v.getId() == R.id.save){

            String QRContent = code_content.getText().toString();
            if (QRContent.equals("")){
                Toast.makeText(this,"The content should not be empty!",Toast.LENGTH_LONG).show();
            }
            //制作新的二维码
            QRCodeDao qrCodeDao = new QRCodeDao(this);
            Bitmap qr = createQR(this,QRContent, getFormat(qrCodeBean.getCode_format()));
            if (qr==null){
                return;
            }

           // testImage.setImageBitmap(qr);
            qrCodeBean.setCode_content(QRContent);
            qrCodeBean.setCode_image(bitmapToByteArray(qr));
            qrCodeDao.update(qrCodeBean.getId(),qrCodeBean);

            String JsQR = new Gson().toJson(qrCodeBean);

            MMKV.defaultMMKV().encode("qrCodeBean",JsQR);

            finish();

        }

    }

}