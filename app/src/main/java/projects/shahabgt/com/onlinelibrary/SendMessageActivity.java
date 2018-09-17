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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class SendMessageActivity extends AppCompatActivity {

    EditText sender,title,message;
    Button send;
    ImageView back;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        builder = new AlertDialog.Builder(SendMessageActivity.this);
        back = findViewById(R.id.sendmessage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send =findViewById(R.id.sendmessage_send);
        sender =findViewById(R.id.sendmessage_sender);
        title =findViewById(R.id.sendmessage_title);
        message =findViewById(R.id.sendmessage_message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sender.getText().toString().isEmpty()||title.getText().toString().isEmpty()||message.getText().toString().isEmpty())
                {
                    Toast.makeText(SendMessageActivity.this,"لطفا فرم را تکمیل کنید!", Toast.LENGTH_LONG).show();
                }else{
                    sendMessage(sender.getText().toString(),title.getText().toString(),message.getText().toString());
                }
            }
        });


    }
    public void DisplayMassage(String mtitle, String mmessage){
        builder.setTitle(mtitle);
        builder.setMessage(mmessage);
        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                sender.setText("");
                title.setText("");
                message.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void sendMessage(final String sender,final String title, final String message){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet(SendMessageActivity.this)){
            progressDialog= new ProgressDialog(SendMessageActivity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"sendnotification.php";
            StringRequest loginrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("ok")){
                        progressDialog.dismiss();
                        DisplayMassage("اطلاع!","پیام با موفیقت ارسال شد!");

                    }else {
                        progressDialog.dismiss();
                        DisplayMassage("اطلاع!","خطایی رخ داده دوباره امتحان کنید!");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(SendMessageActivity.this,"لطفا برنامه را ببندید و دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("sender",sender);
                    params.put("message",message);
                    params.put("title",title);
                    return params;
                }
            };
            Mysingleton.getmInstance(SendMessageActivity.this).addToRequestque(loginrequest);

        }else{
            Toast.makeText(SendMessageActivity.this,"اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
        }
    }
}
