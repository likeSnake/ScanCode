package net.free.supertool.useful.scancode.ui;

import static net.free.supertool.useful.scancode.util.MyUtil.bitmapToByteArray;
import static net.free.supertool.useful.scancode.util.MyUtil.byteArrayToBitmap;
import static net.free.supertool.useful.scancode.util.MyUtil.createQR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tencent.mmkv.MMKV;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.bean.CreateBean;
import net.free.supertool.useful.scancode.bean.QRCodeBean;
import net.free.supertool.useful.scancode.db.QRCodeDao;

public class CreateAct extends AppCompatActivity implements View.OnClickListener{

    private ImageView ic_icon,ic_back,ic_create;
    private EditText name,Organization,Address,Phone,mail,Notes;
    private CreateBean createBean;
    private TextView text_type;
    private String QRContent = "";
    private BarcodeFormat format = BarcodeFormat.QR_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create);

        initUI();
        initData();
        initListener();

    }
    public void initUI(){
        ic_icon = findViewById(R.id.ic_icon);
        ic_back = findViewById(R.id.ic_back);
        ic_create = findViewById(R.id.ic_create);
        name = findViewById(R.id.name);
        Organization = findViewById(R.id.Organization);
        Address = findViewById(R.id.Address);
        Phone = findViewById(R.id.Phone);
        mail = findViewById(R.id.mail);
        Notes = findViewById(R.id.Notes);
        text_type = findViewById(R.id.text_type);
    }
    public void initData(){

        String JsCodeBean = MMKV.defaultMMKV().decodeString("createBean");

        try {
            createBean = new Gson().fromJson(JsCodeBean, CreateBean.class);
        }catch (Exception e){
            Log.e("createBean fromJson", e.getMessage());
            createBean = null;
        }
        if (createBean==null){
            return;
        }
        ic_icon.setImageBitmap(byteArrayToBitmap(createBean.getBitmap()));
        text_type.setText(createBean.getContent());

        switch (createBean.getContent()){
            case "contacts":
                name.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.VISIBLE);
                Address.setVisibility(View.VISIBLE);
                Phone.setVisibility(View.VISIBLE);
                mail.setVisibility(View.VISIBLE);
                Notes.setVisibility(View.VISIBLE);

                break;
            case "e-mail":
                name.setHint("E-mail address");
                Organization.setHint("Subject");
                Notes.setHint("Content");

                name.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.VISIBLE);
                Notes.setVisibility(View.VISIBLE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);


                break;
            case "phone":
                name.setHint("Phone");
                name.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.GONE);
                break;
            case "location":
                name.setHint("Latitude");
                Organization.setHint("Longitude");
                Address.setHint("Notes");

                name.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.VISIBLE);
                Address.setVisibility(View.VISIBLE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.GONE);
                break;
            case "maxicode":
                format = BarcodeFormat.MAXICODE;
                name.setHint("Content");
                name.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.GONE);
                break;
            case "sms":
                name.setHint("Phone");
                Notes.setHint("Content");

                name.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "url":
                name.setHint("url");
                name.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.GONE);
                break;
            case "data_matrix":
                format = BarcodeFormat.DATA_MATRIX;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "aztec":
                format = BarcodeFormat.AZTEC;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "upc_e":
                format = BarcodeFormat.UPC_E;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "upc_a":
                format = BarcodeFormat.UPC_A;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "pdf_417":
                format = BarcodeFormat.PDF_417;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "itf":
                format = BarcodeFormat.ITF;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "ean_8":
                format = BarcodeFormat.EAN_8;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "ean_13":
                format = BarcodeFormat.EAN_13;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "code_39":
                format = BarcodeFormat.CODE_39;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "code_93":
                format = BarcodeFormat.CODE_93;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "code_128":
                format = BarcodeFormat.CODE_128;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;
            case "codabar":
                format = BarcodeFormat.CODABAR;

                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;

            default:
                Notes.setHint("Content");
                name.setVisibility(View.GONE);
                Organization.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                Phone.setVisibility(View.GONE);
                mail.setVisibility(View.GONE);
                Notes.setVisibility(View.VISIBLE);
                break;


        }
    }
    public void initListener(){
        ic_create.setOnClickListener(this);
        ic_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ic_create){

            setContent();
            System.out.println(QRContent);
            Bitmap qr = createQR(this,QRContent, format);
            if (qr==null){
                return;
            }

            QRCodeBean qrCodeBean = new QRCodeBean(bitmapToByteArray(qr), createBean.getBitmap(), QRContent, format.toString(), String.valueOf(System.currentTimeMillis()),createBean.getContent() );

            QRCodeDao qrCodeDao = new QRCodeDao(this);
            qrCodeDao.add(qrCodeBean);

            Intent intent = new Intent(CreateAct.this,ResultAct.class);
            String JsQR = new Gson().toJson(qrCodeBean);
            MMKV.defaultMMKV().encode("qrCodeBean",JsQR);
            startActivity(intent);
           // finish();
        }
        if (v.getId() == R.id.ic_back){
            finish();
        }
    }

    public void setContent(){
        QRContent = "";
        String text = name.getText().toString();
        String text1 = Organization.getText().toString();
        String text2 = Address.getText().toString();
        String text3 = Phone.getText().toString();
        String text4 = mail.getText().toString();
        String text5 = Notes.getText().toString();
        if (!text.equals("")){
            QRContent += text+"\n";
        }
        if (!text1.equals("")){
            QRContent += text1+"\n";
        }
        if (!text2.equals("")){
            QRContent += text2+"\n";
        }
        if (!text3.equals("")){
            QRContent += text3+"\n";
        }
        if (!text4.equals("")){
            QRContent += text4+"\n";
        }
        if (!text5.equals("")){
            QRContent += text5;
        }

    }
}