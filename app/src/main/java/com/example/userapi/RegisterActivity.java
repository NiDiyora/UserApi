package com.example.userapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText uname,uemail,upassword;
    TextView alreadyaccount;
    Button regiter;
    private String URL ="http://192.168.43.124/UserApi/register.php";
    CircleImageView circleImageView;
    private int SELECT_IMAGE = 101;
    Bitmap bitmap = null;
    String mediaPath;
    String username,email,password;
    String path;
    String imageURI;
    File file;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        uname = findViewById(R.id.uname);
        uemail = findViewById(R.id.uemail);
        upassword = findViewById(R.id.upassword);
        regiter = findViewById(R.id.btnregister);
        alreadyaccount = findViewById(R.id.signin);
        circleImageView = findViewById(R.id.profile);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(RegisterActivity.this, "already granted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);

                } else {
                        requestStroragepermmission();
                }

            }
        });
        regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = uname.getText().toString().trim();
                email = uemail.getText().toString().trim();
                password = upassword.getText().toString().trim();
                if(username.isEmpty() && email.isEmpty() && password.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                   file = new File(imageURI);
                  //  Model model = new Model(username, email, password);
                 //   Toast.makeText(RegisterActivity.this, file.getPath(), Toast.LENGTH_SHORT).show();
                    ApiInterface apiInterface = RetrofitInstance.getRetrofit().create(ApiInterface.class);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("img",
                            file.getName(), reqFile);
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"),username);
                    RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"),email);
                    RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"),password);

                    Call<ResponsPojo> responsPojoCall = apiInterface.databody(body,name,email1,password1);
                    responsPojoCall.enqueue(new Callback<ResponsPojo>() {
                        @Override
                        public void onResponse(Call<ResponsPojo> call, Response<ResponsPojo> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponsPojo> call, Throwable t) {
                            //Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("jsonereader",t.getMessage());

                        }
                    });
                }
            }
        });
        alreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    private void requestStroragepermmission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
            .setTitle("Permmission needed")
            .setMessage("this permmision is needed")
            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                }
            }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        }
        else
        {
            ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permmission granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Permmission Denied", Toast.LENGTH_SHORT).show();
            }
                
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
           if(data == null) {
                Toast.makeText(this, "unable to choose file", Toast.LENGTH_SHORT).show();
            }
            Uri mImageUri = data.getData();
           imageURI = getRealPath(mImageUri);
           circleImageView.setImageURI(Uri.parse(imageURI));
        }
    }
    private String getRealPath(Uri mImageUri) {
        String docId = DocumentsContract.getDocumentId(mImageUri);
        String[] split = docId.split(":");
        String type = split[0];
        Uri contentUri;
        switch (type) {
            case "image":
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                break;
            default:
                contentUri = MediaStore.Files.getContentUri("external");
        }
        String selection = "_id=?";
        String[] selectionArgs = new String[]{
                split[1]
        };

        return getDataColumn(getApplicationContext(), contentUri, selection, selectionArgs);
    }
    private String getDataColumn(Context context, Uri contentUri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(column);
                String value = cursor.getString(column_index);
                if (value.startsWith("content://") || !value.startsWith("/") && !value.startsWith("file://")) {
                    return null;
                }
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

}