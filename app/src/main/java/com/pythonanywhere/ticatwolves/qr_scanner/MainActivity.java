package com.pythonanywhere.ticatwolves.qr_scanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AdView mAdView;
    EditText inputs;
    ImageView qrimage;
    TextView data;
    Button readqr,snap;
    Bitmap bitmap;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputs = (EditText)findViewById(R.id.input);
        readqr = (Button) findViewById(R.id.add);
        qrimage = (ImageView)findViewById(R.id.qrimage);
        data = (TextView)findViewById(R.id.data);
        snap = (Button)findViewById(R.id.snap);
        //MobileAds.initialize(this, "ca-app-pub-5759830031625110~4451327785");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        inputs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = inputs.getText().toString();
                if(!(s.equals(""))) {
                    try {
                        data.setText("");
                        MultiFormatWriter ml = new MultiFormatWriter();
                        BitMatrix butmat = ml.encode(s, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        //Bitmap bitmap = barcodeEncoder.createBitmap(butmat);
                        bitmap = barcodeEncoder.createBitmap(butmat);
                        qrimage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    qrimage.setImageResource(R.drawable.white);
                }
            }
        });

        readqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this,readcode.class);
                startActivity(i1);
            }
        });
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveImage(bitmap);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            Bundle b1 = getIntent().getExtras();
            data.setText(b1.getString("data").toString());
        }
        catch (Exception e){

        }
    }
    public void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}

