package com.example.piceditisimple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    ImageButton btpic,cameraButton;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btpic=findViewById(R.id.pic_imageButton);
        imageView=findViewById(R.id.image_result);
        cameraButton=findViewById(R.id.camera_button);



        btpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.this.checkPermission();
            }
        });

//        if (imageView !=  null){
//            Intent intent = new Intent(this,MainActivity2.class);
//                intent.setData();
//                startActivity(intent);
//        }

        // practic for camera get image

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,3000);

            }
        });



        //end

    }




    public void checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(Build.VERSION.SDK_INT >= 29){
            
            pickImage();
        }else{
            if(permission != 0){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},100);
            }else{
                pickImage();
            }
        }
    }

    private void pickImage() {
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/Download/");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(uri, "image/*");
//        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     if(requestCode==100 && grantResults.length>0 && grantResults[0]
     == PackageManager.PERMISSION_GRANTED){
         pickImage();
     }else{
         Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
     }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==RESULT_OK){
//
//            Uri uri =data.getData();
//            switch (requestCode){
//                case 100:
//                    Intent intent = new Intent(MainActivity.this, DsPhotoEditorActivity.class);
//                    intent.setData(uri);
//                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,"image");
//                    intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));
//                    intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,Color.parseColor("#FFFFFF"));
//                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, new int[]{DsPhotoEditorActivity.TOOL_WARMTH,DsPhotoEditorActivity.TOOL_PIXELATE});
//                    startActivityForResult(intent,101);
//                              break;
//
//
//                case 101:
//                    imageView.setImageURI(uri);
//                    Toast.makeText(getApplicationContext(), "Photo saved", Toast.LENGTH_SHORT).show();
//                      break;
//            }
//        }
        if (resultCode == -1) {
            Uri uri = data.getData();
            if (requestCode == 100) {
                Intent intent = new Intent(this, DsPhotoEditorActivity.class);
                intent.setData(uri);
                intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Images");
                intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));
                intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FFFFFF"));
                intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, new int[]{10, 11});
                startActivityForResult(intent, 101);
            } else if (requestCode == 101) {

//                Intent intent = new Intent(this,MainActivity2.class);
//                intent.setData(uri);
//                startActivity(intent);
                this.imageView.setImageURI(uri);
                Toast.makeText(getApplicationContext(), "Photo saved", Toast.LENGTH_SHORT).show();
            }
        }


        // get image in a camera demo
        if (requestCode == 3000) {
            Bundle extras =data.getExtras();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri =getImageUri(photo);
            Intent intent = new Intent(this, DsPhotoEditorActivity.class);
            intent.setData(uri);
            intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Images");
            intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));
            intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FFFFFF"));
            intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, new int[]{10, 11});
            startActivityForResult(intent, 101);

            //imageView.setImageBitmap(photo);
//

        }

    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,arrayOutputStream);
        String path =MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Titles","");
        return Uri.parse(path);
    }


}