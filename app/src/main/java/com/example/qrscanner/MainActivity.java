package com.example.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    Button btn_scan;
    Button btn_generate;
    EditText edit_input;
    ImageView iv_qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_scan =findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });

        edit_input = findViewById(R.id.edit_input);
        btn_generate = findViewById(R.id.btn_generate);
        iv_qr = findViewById(R.id.iv_qr);

        btn_generate.setOnClickListener(c->{
            generateQR();
        });
    }

    private void generateQR() {
        String text = edit_input.getText().toString().trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400 ,400);

            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            iv_qr.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Flaşı açmak için sesi aç");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(),result ->{
       if (result.getContents() !=null){
           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
           builder.setTitle("Sonuçlar");
           builder.setMessage(result.getContents());
           builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();

               }

           }) .show();

       }
    });
}