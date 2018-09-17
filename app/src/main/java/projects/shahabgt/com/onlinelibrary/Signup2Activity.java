package projects.shahabgt.com.onlinelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import projects.shahabgt.com.onlinelibrary.classes.EmailValidation;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class Signup2Activity extends AppCompatActivity {
    ImageView back;
    Button reg,resend;
    Bundle bundle;
    EditText signup2_number;
    String number,name,password,code,email;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        builder = new AlertDialog.Builder(Signup2Activity.this);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        bundle= getIntent().getExtras();
        number=bundle.getString("number");
        name=bundle.getString("name");
        password=bundle.getString("password");
        email=bundle.getString("email");

        signup2_number = findViewById(R.id.signup2_number);
        back = findViewById(R.id.signup2_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        reg = findViewById(R.id.signup2_register);
        resend = findViewById(R.id.signup2_resend);
        resend.setEnabled(false);
        resend.setBackground(getResources().getDrawable(R.drawable.shapedisabled));
        resend.setText("ارسال مجدد کد تایید(بعد از یک دقیقه)");
        resend.setTextColor(getResources().getColor(R.color.black));

        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        resend.setEnabled(true);
                        resend.setBackground(getResources().getDrawable(R.drawable.shape3));
                        resend.setText("ارسال مجدد کد تایید");
                        resend.setTextColor(getResources().getColor(R.color.white));
                    }
                });
            }
        }, 60000);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend.setEnabled(false);
                resend.setBackground(getResources().getDrawable(R.drawable.shapedisabled));
                resend.setText("ارسال مجدد کد تایید(بعد از یک دقیقه)");
                resend.setTextColor(getResources().getColor(R.color.black));

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                resend.setEnabled(true);
                                resend.setBackground(getResources().getDrawable(R.drawable.shape3));
                                resend.setText("ارسال مجدد کد تایید");
                                resend.setTextColor(getResources().getColor(R.color.white));
                            }
                        });
                    }
                }, 60000);
                if (checknet(Signup2Activity.this)) {
                    progressDialog = new ProgressDialog(Signup2Activity.this);
                    progressDialog.setMessage("لطفا منتظر بمانید...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    final String url = getResources().getString(R.string.url) + "checknum.php";
                    StringRequest resendreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("notok")) {
                                progressDialog.dismiss();
                                Toast.makeText(Signup2Activity.this, "خطا لطفا دوباره امتحان کنید!", Toast.LENGTH_LONG).show();
                            }else if (response.equals("ok")) {
                                progressDialog.dismiss();
                                Toast.makeText(Signup2Activity.this, "کد جدید با موفقیت ارسال شد!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup2Activity.this, "خطا لطفا دوباره امتحان کنید!", Toast.LENGTH_LONG).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("number", number);
                            params.put("resend", "1");
                            return params;
                        }
                    };
                    Mysingleton.getmInstance(Signup2Activity.this).addToRequestque(resendreq);
                }else {
                    Toast.makeText(Signup2Activity.this, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){}
                if(signup2_number.getText().toString().isEmpty()){
                    Toast.makeText(Signup2Activity.this,"لطفا کد را وارد کنید!",Toast.LENGTH_LONG).show();
                }else {
                    if (checknet(Signup2Activity.this)) {
                        progressDialog= new ProgressDialog(Signup2Activity.this);
                        progressDialog.setMessage("لطفا منتظر بمانید...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        final String url=getResources().getString(R.string.url)+"signup.php";
                        StringRequest signup = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("notok")){
                                    progressDialog.dismiss();
                                    DisplayMassage("خطا!","لطفا دوباره امتحان کنید!");
                                }else if(response.equals("ok")){
                                    progressDialog.dismiss();
                                    sp.edit().putString("number",number).apply();
                                    sp.edit().putString("password",password).apply();
                                    sp.edit().putString("name",name).apply();
                                    sp.edit().putString("email",email).apply();
                                    sp.edit().putInt("stat",1).apply();
                                    Toast.makeText(Signup2Activity.this,"ثبت نام با موفقیت انجام شد.", Toast.LENGTH_LONG).show();
                                   Signup2Activity.this.finish();
                                }else if(response.equals("wrong")){
                                    progressDialog.dismiss();
                                    DisplayMassage("خطا!","کد وارد شده صحیح نمی باشد!");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Signup2Activity.this,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                            }
                        }){

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("number",number);
                                params.put("code",signup2_number.getText().toString());
                                return params;
                            }
                        };
                        Mysingleton.getmInstance(Signup2Activity.this).addToRequestque(signup);
                    } else {
                        Toast.makeText(Signup2Activity.this, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void DisplayMassage(String title, String message){
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                pass.setText("");
//                repass.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }
}
