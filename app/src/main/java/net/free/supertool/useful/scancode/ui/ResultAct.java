package net.free.supertool.useful.scancode.ui;

import static net.free.supertool.useful.scancode.util.MyUtil.byteArrayToBitmap;
import static net.free.supertool.useful.scancode.util.MyUtil.convertTimestampToDate;
import static net.free.supertool.useful.scancode.util.MyUtil.copy;
import static net.free.supertool.useful.scancode.util.MyUtil.searchOnWeb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.bean.QRCodeBean;
import net.free.supertool.useful.scancode.db.QRCodeDao;

public class ResultAct extends AppCompatActivity implements View.OnClickListener{

    private ImageView ic_text,result_image,ic_back,ic_copy,ic_edit,ic_delete,ic_search;
    private TextView text_format,code_type,code_time,text_context;
    private QRCodeBean qrCodeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_result);

        initUI();
       // initData();
        initListener();
    }

    public void initUI(){
        ic_text = findViewById(R.id.ic_text);
        result_image = findViewById(R.id.result_image);
        text_format = findViewById(R.id.text_format);
        code_type = findViewById(R.id.code_type);
        code_time = findViewById(R.id.code_time);
        text_context = findViewById(R.id.text_context);

        ic_back = findViewById(R.id.ic_back);
        ic_edit = findViewById(R.id.ic_edit);
        ic_copy = findViewById(R.id.ic_copy);
        ic_delete = findViewById(R.id.ic_delete);
        ic_search = findViewById(R.id.ic_search);

    }

    public void initData(){
     //   String JsQR = new Gson().toJson(qrCodeBean);

        String JsCodeBean = MMKV.defaultMMKV().decodeString("qrCodeBean");

        try {
            qrCodeBean = new Gson().fromJson(JsCodeBean, QRCodeBean.class);
        }catch (Exception e){
            Log.e("fromJson", e.getMessage());
            qrCodeBean = null;
        }
        if (qrCodeBean!=null){
            String code_content = qrCodeBean.getCode_content();
            String code_format = qrCodeBean.getCode_format();
            Bitmap code_icon = byteArrayToBitmap(qrCodeBean.getCode_icon());
            Bitmap code_image = byteArrayToBitmap(qrCodeBean.getCode_image());
            String code_time = convertTimestampToDate(Long.parseLong(qrCodeBean.getCode_time()));
            String code_type = qrCodeBean.getCode_type();

            ic_text.setImageBitmap(code_icon);
            result_image.setImageBitmap(code_image);
            text_format.setText(code_format);
            this.code_type.setText(code_type);
            this.code_time.setText(code_time);
            text_context.setText(code_content);
        }
    }

    public void initListener(){
        ic_back.setOnClickListener(this);
        ic_edit.setOnClickListener(this);
        ic_copy.setOnClickListener(this);
        ic_delete.setOnClickListener(this);
        ic_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ic_back){
            finish();
        }
        if (v.getId() == R.id.ic_edit){
            startEditAct();
        }
        if (v.getId() == R.id.ic_copy){
            copy(this,qrCodeBean.getCode_content());
            Toast.makeText(this,"Successful replication",Toast.LENGTH_LONG).show();
        }
        if (v.getId() == R.id.ic_delete){
            startDialog();
        }
        if (v.getId() == R.id.ic_search){
           searchOnWeb(this,qrCodeBean.getCode_content());
        }

    }
    public void startEditAct(){
        Intent intent = new Intent(this,EditAct.class);
        String JsQR = new Gson().toJson(qrCodeBean);
        MMKV.defaultMMKV().encode("qrCodeBean",JsQR);
        startActivity(intent);
        //finish();
    }

    public void startDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.my_colors);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.dialog_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.findViewById(R.id.dialog_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new QRCodeDao(ResultAct.this).deleteQRCodeById(qrCodeBean.getId());
                finish();

            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("---***---onResume");
        initData();
    }
}