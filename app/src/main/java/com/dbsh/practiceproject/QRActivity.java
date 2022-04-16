package com.dbsh.practiceproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Locale;

public class QRActivity extends AppCompatActivity {

    Button QRBtn;
    ImageView QR_CODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_form);

        Intent intent = getIntent();
        String id = ((userClass) getApplication()).getId();
        String date = "0000000022";

        QRBtn = (Button) findViewById(R.id.QRBtn);

        QRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                QR_CODE = (ImageView)findViewById(R.id.QRcode);
                String data = ("").
                        concat("7082").
                        concat("\n").
                        concat(id).
                        concat("\n").
                        concat("02").
                        concat("\n").
                        concat(date);
                System.out.println(data);

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE,400,400);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    QR_CODE.setImageBitmap(bitmap);
                }catch (Exception e){}
            }
        });
    }
}
