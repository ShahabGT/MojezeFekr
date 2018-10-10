package projects.shahabgt.com.onlinelibrary.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import projects.shahabgt.com.onlinelibrary.R;
import projects.shahabgt.com.onlinelibrary.WebActivity;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;


/**
 * Created by Jonathan on 12/27/2017.
 */

public class DiscountClass extends Dialog implements View.OnClickListener {
    public Activity c;
    public static String s_id="";
    public static String number="";
    public static String discountprice="";
    TextView discounttext,price;
    EditText discountnumber;
    Button discountbtn,cancel,buy;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    Context context;

    public DiscountClass(Activity a, Context context) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.discount_layout);


        builder = new AlertDialog.Builder(c.getApplicationContext());
        discountbtn = findViewById(R.id.discount_btn);
        cancel = findViewById(R.id.discount_cancel);
        buy = findViewById(R.id.discount_buy);
        price = findViewById(R.id.discount_price);
        discounttext = findViewById(R.id.discount_text);
        discountnumber = findViewById(R.id.discount_number);

        price.setText(discountprice);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.money = price.getText().toString();
                WebActivity.sid = s_id;
                WebActivity.number = number;
                c.startActivity(new Intent(c, WebActivity.class));
                c.finish();

            }
        });
        discountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount();
            }
        });

    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
     private void discount(){
         try {
             InputMethodManager inputManager = (InputMethodManager)
                     c.getSystemService(Context.INPUT_METHOD_SERVICE);
             inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                     InputMethodManager.HIDE_NOT_ALWAYS);
         }catch (Exception e){}
         if(discountnumber.getText().toString().isEmpty()){
             Toast.makeText(c.getApplicationContext(),"لطفا موارد خواسته شده را پر کنید!",Toast.LENGTH_LONG).show();
         }else {
             if (checknet(context)) {
                 progressDialog= new ProgressDialog(c);
                 progressDialog.setMessage("لطفا منتظر بمانید...");
                 progressDialog.setIndeterminate(true);
                 progressDialog.setCancelable(false);
                 progressDialog.show();
                 final String url=c.getResources().getString(R.string.url)+"checkdiscount.php";
                 StringRequest discountreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         if(response.equals("nothing")){
                             progressDialog.dismiss();
                             Toast.makeText(c.getApplicationContext(),"کد تخفیف وارد شده معتبر نمی باشد!",Toast.LENGTH_LONG).show();
                             discountnumber.setText("");
                         }else{
                             NumberFormat nf= NumberFormat.getNumberInstance();
                             nf.setMaximumFractionDigits(0);
                             progressDialog.dismiss();
                             discounttext.setText("مبلغ با "+response+"% تخفیف:");
                             discountnumber.setEnabled(false);
                             discountbtn.setEnabled(false);
                             int dis = ( Integer.parseInt(response)*Integer.parseInt(price.getText().toString()) ) /100;
                             int finalprice = Integer.parseInt(price.getText().toString())- dis;
                             price.setText(nf.format(finalprice));
                         }
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         progressDialog.dismiss();
                         Toast.makeText(c.getApplicationContext(),"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                     }
                 }){

                     @Override
                     protected Map<String, String> getParams() {
                         Map<String,String> params = new HashMap<String, String>();
                         params.put("s_id",s_id);
                         params.put("code",discountnumber.getText().toString());
                         return params;
                     }
                 };
                 Mysingleton.getmInstance(c).addToRequestque(discountreq);
             } else {
                 Toast.makeText(c.getApplicationContext(), "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
             }
         }
     }


}
