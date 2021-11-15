package com.example.userapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnlogin;
    TextView signup;
    EditText uemail, upassword;
    private String URL = "http://192.168.29.57/UserApi/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uemail = findViewById(R.id.editTextEmail);
        upassword = findViewById(R.id.editTextPassword);
        signup = findViewById(R.id.signup1);
        btnlogin = findViewById(R.id.LoginButton);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = uemail.getText().toString().trim();
                password = upassword.getText().toString().trim();
                login(email, password);
              /*if(!email.equals("") && !password.equals(""))
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("login success")) {
                                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,user_data.class));
                            }
                            else if(response.equals("login Faild"))
                            {
                                Toast.makeText(LoginActivity.this, response+"!=emaiid or password not match", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();

                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> data = new HashMap<>();
                            data.put("email",email);
                            data.put("password",password);
                            return data;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }else{
                    Toast.makeText(LoginActivity.this, "Fields Can Not be empty", Toast.LENGTH_SHORT).show();
                }
*/
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, user_data.class));
            }
        });
    }

    private void login(String email, String password) {
        if (email.isEmpty()) {
            uemail.requestFocus();
            uemail.setError("please Enter Your Email");

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            uemail.requestFocus();
            uemail.setError("please Enter Correct Email");
        }
        if (password.isEmpty()) {
            upassword.requestFocus();
            upassword.setError("please Enter Your Password");

        }
        ApiInterface apiInterface = RetrofitInstance.getRetrofit().create(ApiInterface.class);
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);
        Call<LoginResponse> responsPojoCall = apiInterface.login(data);
        responsPojoCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

    public void emailValidator(EditText uemail) {
        String emailToText = uemail.getText().toString();
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            Toast.makeText(this, "Email Verified !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
        }
    }
}