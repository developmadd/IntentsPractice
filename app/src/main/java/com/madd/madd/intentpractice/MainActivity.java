package com.madd.madd.intentpractice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.BTN_Phone)         Button buttonPhone;
    @BindView(R.id.BTN_Share_Text)    Button buttonTextShare;
    @BindView(R.id.BTN_Share_Text_WA) Button buttonTextShareWA;
    @BindView(R.id.BTN_Image_Share)   Button buttonImageShare;
    @BindView(R.id.BTN_Image_Share_WA)Button buttonImageShareWA;
    @BindView(R.id.TV_Phone)          TextView textViewPhone;
    @BindView(R.id.BTN_Contact)       ImageButton buttonContact;
    @BindView(R.id.ET_Share_Text)     EditText editTextShare;
    @BindView(R.id.IV_Image_Share)    ImageView imageViewShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);




        buttonContact.setOnClickListener(view -> {
            checkPermission(Manifest.permission.READ_CONTACTS, hasPermission -> {
                if(hasPermission){
                    Intent intent = new Intent(this,ContactsActivity.class);
                    startActivityForResult(intent,0);
                }
            });
        });
        buttonPhone.setOnClickListener(view -> {
            checkPermission(Manifest.permission.CALL_PHONE, hasPermission -> {
                if(hasPermission) {
                    phoneCall();
                }
            });
        });





        buttonTextShare.setOnClickListener(view -> {
            shareText();
        });
        buttonTextShareWA.setOnClickListener(view -> {
            shareTextWA();
        });





        imageViewShare.setOnClickListener(view -> {
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, hasWritePermission -> {
                if( hasWritePermission ) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, hasReadPermission -> {
                        if( hasReadPermission ) {
                            openCamera();
                        }
                    });
                }
            });
        });

        buttonImageShare.setOnClickListener(view -> {
            shareImage();
        });
        buttonImageShareWA.setOnClickListener(view -> {
            shareImageWA();
        });
    }


















    Uri photoUri;
    void openCamera(){
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if( intent.resolveActivity(getPackageManager()) != null ) {

                String fileName = "Image_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + "_";
                File file = File.createTempFile(fileName, ".jpg", getFilesDir());
                photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

                intent.putExtra("return-data", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, 1);
            }
        } catch (IOException ignored) {
            photoUri = null;
            Toast.makeText(this,"Error abriendo cámara",Toast.LENGTH_LONG).show();
        }
    }
















    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 0) {
                if( data != null ) {
                    try {
                        String phoneNumber = data.getExtras().getString("phone");
                        textViewPhone.setText(phoneNumber);
                    } catch (Exception ignored){
                        Toast.makeText(this, "Error cargando teléfono", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Error cargando teléfono", Toast.LENGTH_LONG).show();
                }

            } else if (requestCode == 1) {

                if (photoUri != null) {
                    Glide.with(this)
                            .load(photoUri)
                            .centerCrop()
                            .into(imageViewShare);
                } else {
                    Toast.makeText(this, "Error durante la fotografía", Toast.LENGTH_LONG).show();
                }

            }
        }

    }









    public void shareImageWA( ){
        if( photoUri != null ) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.setPackage("com.whatsapp");
            intent.putExtra(Intent.EXTRA_STREAM, photoUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(Intent.createChooser(intent, "Compartir fotografía"));
            } catch(Exception ignored){
                Toast.makeText(this,"WhatsApp no esta instalado en este dispositivo",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,"Tome una fotografía para compartir",Toast.LENGTH_LONG).show();
        }
    }
    public void shareImage( ){
        if( photoUri != null ) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, photoUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Compartir fotografía"));
        } else {
            Toast.makeText(this,"Tome una fotografía para compartir",Toast.LENGTH_LONG).show();
        }
    }


    public void shareTextWA(){
        if( !editTextShare.getText().toString().isEmpty() ) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, editTextShare.getText().toString());
            try{
                startActivity(Intent.createChooser(intent, "Compartir texto"));
            } catch(Exception ignored){
                Toast.makeText(this,"WhatsApp no esta instalado en este dispositivo",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,"Introduzca texto a compartir",Toast.LENGTH_LONG).show();
        }
    }
    public void shareText(){
        if( !editTextShare.getText().toString().isEmpty() ) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, editTextShare.getText().toString());
            startActivity(Intent.createChooser(intent, "Compartir texto"));
        } else {
            Toast.makeText(this,"Introduzca texto a compartir",Toast.LENGTH_LONG).show();
        }
    }

    public void phoneCall(){
        if ( !textViewPhone.getText().toString().isEmpty() ){
            Intent intent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:" + textViewPhone.getText().toString()));
            startActivity(intent);
        } else {
            Toast.makeText(this,"El número introducido tiene formato inválido",Toast.LENGTH_LONG).show();
        }
    }


















    void checkPermission( String permissionType, PermissionRequest permissionRequest ) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, permissionType);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.permissionRequest = permissionRequest;
                requestPermissions(new String[]{permissionType}, 1);
            }
        } else {
            permissionRequest.setPermission(true);
        }
    }

    PermissionRequest permissionRequest;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            permissionRequest.setPermission(true);
        } else {
            permissionRequest.setPermission(false);
        }
        permissionRequest = null;

    }

    interface PermissionRequest{
        void setPermission(boolean hasPermission);
    }

}
