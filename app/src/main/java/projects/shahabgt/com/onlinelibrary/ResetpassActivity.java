package projects.shahabgt.com.onlinelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Map;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class ResetpassActivity extends AppCompatActivity {
    ImageView back;
    EditText number;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);
        builder = new AlertDialog.Builder(ResetpassActivity.this);

        back = findViewById(R.id.resetpass_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        number = findViewById(R.id.resetpass_number);

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
                if(number.getText().toString().isEmpty()||number.length()<11){
                    Toast.makeText(ResetpassActivity.this,"لطفا موارد خواسته شده را پر کنید!",Toast.LENGTH_LONG).show();
                }else {
                    if (checknet(ResetpassActivity.this)) {
                        progressDialog= new ProgressDialog(ResetpassActivity.this);
                        progressDialog.setMessage("لطفا منتظر بمانید...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        final String url=getResources().getString(R.string.url)+"resetpass.php";
                        StringRequest resetpass = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("notok")||response.equals("nonumber")){
                                    progressDialog.dismiss();
                                    DisplayMassage("خطا!","لطفا دوباره امتحان کنید!",0);
                                }else if(response.equals("notexist")){
                                    progressDialog.dismiss();
                                    DisplayMassage("خطا!","حساب کاربری با شماره وارد شده موجود نیست!",0);
                                }else if(response.equals("ok")){
                                    progressDialog.dismiss();
                                    DisplayMassage("اطلاع!","رمز عبور به شماره شما فرستاده شد!",1);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(ResetpassActivity.this,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                            }
                        }){

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String,String> params = new HashMap<>();
                                params.put("number",number.getText().toString());
                                return params;
                            }
                        };
                        Mysingleton.getmInstance(ResetpassActivity.this).addToRequestque(resetpass);
                    } else {
                        Toast.makeText(ResetpassActivity.this, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void DisplayMassage(String title, String message,final int stat){
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(stat==1) {
                    ResetpassActivity.this.finish();
                }else {
                    number.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
