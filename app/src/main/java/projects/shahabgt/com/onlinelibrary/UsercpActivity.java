package projects.shahabgt.com.onlinelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import projects.shahabgt.com.onlinelibrary.classes.EmailValidation;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class UsercpActivity extends AppCompatActivity {
    ImageView back;
    TextView name,number;
    EditText email,oldpass,newpass,newrepass;
    Button update,signout;
    SharedPreferences sp;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercp);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        builder = new AlertDialog.Builder(UsercpActivity.this);

        back= findViewById(R.id.usercp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        name= findViewById(R.id.usercp_name);
        number= findViewById(R.id.usercp_number);
        email= findViewById(R.id.usercp_email);
        oldpass= findViewById(R.id.usercp_oldpass);
        newpass= findViewById(R.id.usercp_newpass);
        newrepass= findViewById(R.id.usercp_newrepass);
        update= findViewById(R.id.usercp_register);
        signout= findViewById(R.id.usercp_signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update(email.getText().toString(),oldpass.getText().toString(),newpass.getText().toString(),newrepass.getText().toString());
            }
        });
        Getdata();
    }
    private void Getdata(){
        name.setText(sp.getString("name",""));
        number.setText(sp.getString("number",""));
        email.setText(sp.getString("email",""));
    }
    private void Logout(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
        onBackPressed();
    }
    private void DisplayMassage(String title, String message,final int stat){
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(stat!=1) {
                    oldpass.setText("");
                    newpass.setText("");
                    newrepass.setText("");
                }else{
                    Toast.makeText(UsercpActivity.this,"تغییرات با موفقیت ذخیره شد!",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void Update(String email,String pass,String npass,String nrpass){
        String SPpass = sp.getString("password","");
        String SPemail = sp.getString("email","");
        String SPnumber = sp.getString("number","");
        if(SPpass.equals(pass)){
            if(npass.isEmpty()&& SPemail.equals(email)){
                Toast.makeText(UsercpActivity.this,"تغییرات با موفقیت ذخیره شد!",Toast.LENGTH_LONG).show();
                onBackPressed();
            }else if(npass.isEmpty()&& !SPemail.equals(email)){
                if(!EmailValidation.isValid(email)) {
                    Toast.makeText(UsercpActivity.this,"ایمیل وارد شده معتبر نیست!",Toast.LENGTH_LONG).show();
                }else{
                    Updatedb(SPnumber,SPpass,email);
                }

            }else if(!npass.isEmpty()&& !SPemail.equals(email)){
                if(!npass.equals(nrpass)){
                    Toast.makeText(UsercpActivity.this,"رمز جدید و تکرار آن یکسان نیست!",Toast.LENGTH_LONG).show();
                }else if(npass.length()<6){
                    Toast.makeText(UsercpActivity.this,"رمز جدید انتخابی باید بیشتر از 6 حرف باشد!",Toast.LENGTH_LONG).show();
                }else if(!EmailValidation.isValid(email)) {
                    Toast.makeText(UsercpActivity.this,"ایمیل وارد شده معتبر نیست!",Toast.LENGTH_LONG).show();
                }else{
                    Updatedb(SPnumber,npass,email);
                }

            }else if(!npass.isEmpty()&& SPemail.equals(email)){
                if(!(npass.equals(nrpass))){
                    Toast.makeText(UsercpActivity.this,"رمز جدید و تکرار آن یکسان نیست!",Toast.LENGTH_LONG).show();
                }else if(npass.length()<6){
                    Toast.makeText(UsercpActivity.this,"رمز جدید انتخابی باید بیشتر از 6 حرف باشد!",Toast.LENGTH_LONG).show();
                }else{
                    Updatedb(SPnumber,npass,SPemail);
                }
            }

        }else{
            DisplayMassage("خطا!","رمز وارد شده صحیح نمی باشد!",0);
        }


    }
    private void Updatedb(final String number,final String password, final String email){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet(UsercpActivity.this)){
            progressDialog= new ProgressDialog(UsercpActivity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"update.php";
            StringRequest updaterequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("notok")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","لطفا دوباره امتحان کنید!",0);
                    } else if(response.equals("ok")){
                        progressDialog.dismiss();
                        SharedPreferences.Editor e=sp.edit();
                        e.putString("email",email);
                        e.putString("password",password);
                        e.apply();
                        Toast.makeText(UsercpActivity.this,"تغییرات با موفقیت ذخیره شد!",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(UsercpActivity.this,"لطفا برنامه را ببندید و دوباره امتحان کنید.",Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("number",number);
                    params.put("password",password);
                    params.put("email",email);
                    return params;
                }
            };
            Mysingleton.getmInstance(UsercpActivity.this).addToRequestque(updaterequest);

        }else{
            Toast.makeText(UsercpActivity.this,"اتصال به اینترنت را بررسی کنید!",Toast.LENGTH_LONG).show();

        }
    }
}
