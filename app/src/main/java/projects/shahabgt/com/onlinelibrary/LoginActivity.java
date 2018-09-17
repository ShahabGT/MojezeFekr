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

import java.util.HashMap;
import java.util.Map;

import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class LoginActivity extends AppCompatActivity {
    Button signup,resetpass,login;
    ImageView back;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    EditText number,pass;
    SharedPreferences sp;
    String token,deviceid;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        builder = new AlertDialog.Builder(LoginActivity.this);


        back= findViewById(R.id.login_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signup = findViewById(R.id.login_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                LoginActivity.this.finish();
            }
        });
        resetpass = findViewById(R.id.login_resetpass);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetpassActivity.class));
            }
        });

        number= findViewById(R.id.login_number);
        pass= findViewById(R.id.login_pass);


        login = findViewById(R.id.login_signon);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString().isEmpty()||pass.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"'شماره تلفن/رمز ورود' را وارد کنید", Toast.LENGTH_LONG).show();
                }else{
                    token = sp.getString("token","gg");
                    if(token.length()<5){
                        token= FirebaseInstanceId.getInstance().getToken();
                    }
                    FirebaseMessaging.getInstance().subscribeToTopic("ol_users");
                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    deviceid = telephonyManager.getDeviceId();
                    if(deviceid==null){
                        deviceid= Build.SERIAL;
                    }
                    logIn(number.getText().toString(), pass.getText().toString(),token,deviceid);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        int stat=sp.getInt("stat",0);
        if(stat==1){
            startActivity(new Intent(LoginActivity.this,UsercpActivity.class));
            LoginActivity.this.finish();
        }
    }

    public void DisplayMassage(String title, String message){
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pass.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void logIn(final String number,final String pass, final String token,final String deviceid){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet(LoginActivity.this)){
            progressDialog= new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"login.php";
            StringRequest loginrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("wrongpass")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","رمز ورود وارد شده اشتباه می باشد!");

                    }else if(response.equals("notexist")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","فردی با مشخصات وارد شده موجود نمی باشد!");
                    }else if(response.equals("notok")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","لطفا دوباره امتحان کنید!");
                    }else
                    {   String[] data= new String[2];
                        data=response.split(":::");
                        progressDialog.dismiss();
                        SharedPreferences.Editor e=sp.edit();
                        e.clear().apply();
                        e.putString("number",number);
                        e.putString("password",pass);
                        e.putString("name",data[0]);
                        e.putString("email",data[1]);
                        e.putInt("stat",1);
                        e.apply();
                        Toast.makeText(LoginActivity.this,data[0]+" خوش آمدی!",Toast.LENGTH_LONG).show();
                        LoginActivity.this.finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"لطفا برنامه را ببندید و دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("number",number);
                    params.put("password",pass);
                    params.put("token",token);
                    params.put("deviceid",deviceid);
                    return params;
                }
            };
            Mysingleton.getmInstance(LoginActivity.this).addToRequestque(loginrequest);

        }else{
            Toast.makeText(LoginActivity.this,"اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
        }
    }
}
