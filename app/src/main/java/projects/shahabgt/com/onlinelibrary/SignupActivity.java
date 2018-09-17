package projects.shahabgt.com.onlinelibrary;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import projects.shahabgt.com.onlinelibrary.classes.EmailValidation;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class SignupActivity extends AppCompatActivity {
    ImageView back;
    EditText name,number,email,pass,repass;
    Button reg;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        builder = new AlertDialog.Builder(SignupActivity.this);
        back = findViewById(R.id.signup_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        name = findViewById(R.id.signup_name);
        number = findViewById(R.id.signup_number);
        email = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_pass);
        repass = findViewById(R.id.signup_repass);
        reg = findViewById(R.id.signup_register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){}
                if(number.getText().toString().isEmpty()||name.getText().toString().isEmpty()||email.getText().toString().isEmpty()||pass.getText().toString().isEmpty()){
                    Toast.makeText(SignupActivity.this,"لطفا موارد خواسته شده را پر کنید!",Toast.LENGTH_LONG).show();
                }else if(!(pass.getText().toString().equals(repass.getText().toString()))){
                    Toast.makeText(SignupActivity.this,"رمز و تکرار آن یکسان نیست!",Toast.LENGTH_LONG).show();
                }else if(!EmailValidation.isValid(email.getText().toString())){
                    Toast.makeText(SignupActivity.this,"ایمیل وارد شده معتبر نیست!",Toast.LENGTH_LONG).show();
                }else if(pass.length()<6){
                    Toast.makeText(SignupActivity.this,"رمز انتخابی باید بیشتر از 6 حرف باشد!",Toast.LENGTH_LONG).show();
                }else if(number.length()<11){
                    Toast.makeText(SignupActivity.this,"شماره تلفن خود را چک کنید!",Toast.LENGTH_LONG).show();
                }else {
                    if (checknet(SignupActivity.this)) {
                        progressDialog= new ProgressDialog(SignupActivity.this);
                        progressDialog.setMessage("لطفا منتظر بمانید...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        final String token= FirebaseInstanceId.getInstance().getToken();
                        FirebaseMessaging.getInstance().subscribeToTopic("ol_users");
                        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                        String id = telephonyManager.getDeviceId();
                        if(id==null){
                            id= Build.SERIAL;
                        }
                        final String deviceid = id;
                        final String url=getResources().getString(R.string.url)+"checknum.php";

                        StringRequest numcheckreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("nonumber")||response.equals("notok")){
                                    progressDialog.dismiss();
                                    DisplayMassage("خطا!","لطفا دوباره امتحان کنید!");
                                }else if(response.equals("exist")){
                                    progressDialog.dismiss();
                                    DisplayMassage("خطا!","حساب کاربری با شماره وارد شده موجود است!");
                                }else if(response.equals("ok")){
                                    progressDialog.dismiss();
                                    Intent intent;
                                    intent=new Intent(SignupActivity.this,Signup2Activity.class);
                                    intent.putExtra("password",pass.getText().toString());
                                    intent.putExtra("name",name.getText().toString());
                                    intent.putExtra("number",number.getText().toString());
                                    intent.putExtra("email",email.getText().toString());
                                    startActivity(intent);
                                    SignupActivity.this.finish();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                            }
                        }){

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("number",number.getText().toString());
                                params.put("name",name.getText().toString());
                                params.put("email",email.getText().toString());
                                params.put("password",pass.getText().toString());
                                params.put("token",token);
                                params.put("deviceid",deviceid);
                                params.put("resend","0");
                                return params;
                            }
                        };
                        Mysingleton.getmInstance(SignupActivity.this).addToRequestque(numcheckreq);
                    } else {
                        Toast.makeText(SignupActivity.this, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
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
                pass.setText("");
                repass.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }
}
