package net.free.supertool.useful.scancode.fragment;


import static net.free.supertool.useful.scancode.util.MyUtil.bitmapToByteArray;
import static net.free.supertool.useful.scancode.util.MyUtil.decodeBitmapFromResource;

import android.Manifest;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.tencent.mmkv.MMKV;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.bean.QRCodeBean;
import net.free.supertool.useful.scancode.db.QRCodeDao;
import net.free.supertool.useful.scancode.ui.ResultAct;
import net.free.supertool.useful.scancode.util.MyUtil;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CameraFragment extends Fragment implements View.OnClickListener{
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    // 定义请求相机权限的请求码
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private Activity activity;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private ImageView imageView,ic_close,ic_photo;
    private boolean isFirst = true;
    private boolean isTorchOn = false;

    public CameraFragment(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // Fragment 不可见
            // 在这里执行你需要的操作
            System.out.println("关闭相机");
            barcodeView.pause();
        } else {
            // Fragment 可见
            // 在这里执行你需要的操作
            checkCameraPermission();
            if (!isFirst) {
                MyUtil.myLog("打开相机");

            }
        }
    }
    // 在需要使用相机的地方调用此方法来检查和请求相机权限
    private void checkCameraPermission() {
        // 检查相机权限是否已授权
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // 已授权，可以使用相机
            openCamera();
        } else {
            // 未授权，向用户请求相机权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
    // 打开相机的方法
    private void openCamera() {
        // 在此处执行打开相机的逻辑
        // 可以使用 Intent 调用相机应用或使用 Camera API 进行相机操作
        barcodeView.resume();
    }
    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授权了相机权限
                openCamera();
            } else {
                // 用户拒绝了相机权限
                Toast.makeText(activity, "Camera permissions are required to use camera features", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            barcodeView.pause();

            beepManager.playBeepSoundAndVibrate();

            Bitmap bitmap = result.getBitmapWithResultPoints(Color.YELLOW);
            String qrCodeContent = result.getText();
            String format = result.getBarcodeFormat().toString();
            String timestamp = String.valueOf(result.getTimestamp());
            String code_type = "TxT";
            Bitmap my_bitmap = decodeBitmapFromResource( activity,R.drawable.ic_textsvg);
            if (my_bitmap==null){
                System.out.println("my_bitmap为空");
                return;
            }
            QRCodeDao qrCodeDao = new QRCodeDao(activity);
            QRCodeBean qrCodeBean = new QRCodeBean(bitmapToByteArray(bitmap), bitmapToByteArray(my_bitmap), qrCodeContent, format, timestamp, code_type);
            qrCodeDao.add(qrCodeBean);

            startResult(qrCodeBean);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        initView(rootView);
        initDate();
        initListener();
        return rootView;
    }

    public void initView(View rootView){

        imageView = rootView.findViewById(R.id.image);
        ic_close = rootView.findViewById(R.id.ic_close);
        ic_photo = rootView.findViewById(R.id.ic_photo);

        barcodeView = rootView.findViewById(R.id.dbv_custom);
        barcodeView.setStatusText("Please scan the bar code in the frame");
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(activity.getIntent());
        barcodeView.decodeContinuous(callback);


        beepManager = new BeepManager(activity);
    }

    public void initDate(){


    }

    @SuppressLint("ClickableViewAccessibility")
    public void initListener(){
        barcodeView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                isTorchOn = true;
            }

            @Override
            public void onTorchOff() {
                isTorchOn = false;
            }
        });
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTorchOn){
                    barcodeView.setTorchOff();
                    ic_close.setImageResource(R.drawable.ic_close);
                }else {
                    barcodeView.setTorchOn();
                    ic_close.setImageResource(R.drawable.ic_open);
                }

            }
        });
        ic_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        MyUtil.myLog("onResume");
        checkCameraPermission();
       /* if (isFirst) {

            barcodeView.resume();
            isFirst = false;
        }*/
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {


    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                scanQRImage(imageUri);
            }
        }
    }

    private void scanQRImage(Uri imageUri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            //bitmap.recycle();
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(binaryBitmap);
                // 扫描到了二维码
                handleResult(result,bitmap);
            } catch (NotFoundException e) {
                // 未扫描到二维码
                handleNoQRCodeFound();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 处理其他错误
        }
    }

    private void handleResult(Result result,Bitmap bitmap) {
        // 在这里处理扫描到的二维码内容
        String qrCodeContent = result.getText();
        String format = result.getBarcodeFormat().toString();
        String timestamp = String.valueOf(result.getTimestamp());
        String code_type = "TxT";
        // 显示或使用扫描到的内容
        System.out.println("使用相册handleResult扫描到的结果：getResultPoints"+ Arrays.toString(result.getResultPoints()));
        System.out.println("使用相册handleResult扫描到的结果：getRawBytes"+ result.getRawBytes().toString());
        System.out.println("使用相册handleResult扫描到的结果：getResultMetadata"+ result.getResultMetadata());
        System.out.println("使用相册handleResult扫描到的结果：getBarcodeFormat"+ result.getBarcodeFormat());
        System.out.println("使用相册handleResult扫描到的结果：getNumBits"+ result.getNumBits());
        System.out.println("使用相册handleResult扫描到的结果：getText"+ result.getTimestamp());

        Bitmap my_bitmap = decodeBitmapFromResource(activity, R.drawable.ic_textsvg);

        QRCodeDao qrCodeDao = new QRCodeDao(activity);
        byte[] code_image = bitmapToByteArray(bitmap);
        byte[] code_icon = bitmapToByteArray(my_bitmap);
        QRCodeBean qrCodeBean = new QRCodeBean(code_image, code_icon, qrCodeContent, format, timestamp, code_type);
        qrCodeDao.add(qrCodeBean);

        startResult(qrCodeBean);

    }
    private void handleNoQRCodeFound() {
        Toast.makeText(activity,"Not a valid QR code!",Toast.LENGTH_LONG).show();
        // 未扫描到二维码时的处理逻辑
        // 例如显示一个提示消息
    }

    public void startResult(QRCodeBean qrCodeBean){
        String JsQR = new Gson().toJson(qrCodeBean);
        MMKV.defaultMMKV().encode("qrCodeBean",JsQR);
        Intent intent = new Intent(activity, ResultAct.class);
        startActivity(intent);
    }
}