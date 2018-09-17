package projects.shahabgt.com.onlinelibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.changer.audiowife.AudioWife;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;
import projects.shahabgt.com.onlinelibrary.classes.UploadFileAsync;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class SendPost2Activity extends AppCompatActivity {

    Uri uri;
    static final int file =70;
    LinearLayout layout;
    int viewCount=1;
    Bundle bundle;
    ArrayList<String> array;
    Button upload;
    String which;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post2);
        builder = new AlertDialog.Builder(SendPost2Activity.this);
        upload = findViewById(R.id.sendpost2_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               sendMessage("مدیر","دوره جدید","کاربران همیشگی معجره فکر، دوره ای جدید به برنامه اضافه شد. جهت مشاهده به قسمت دوره ها مراجعه کنید.");
            }
        });
        bundle =getIntent().getExtras();
        array = bundle.getStringArrayList("array");
        for(int i=0;i<array.size();i++){
            addView(array.get(i));
        }

    }
    public static void play(Activity activity, Uri uri){
        AudioWife.getInstance().pause();
        AudioWife.getInstance().release();
        SeekBar mMediaSeekBar;
        ImageView mPlayMedia,mPauseMedia;
        TextView mRunTime,mTotalTime;
        mPlayMedia = activity.findViewById(R.id.sendpost2_player_play);
        mPauseMedia = activity.findViewById(R.id.sendpost2_player_pause);
        mMediaSeekBar = activity.findViewById(R.id.sendpost2_player_seekbar);
        mRunTime = activity.findViewById(R.id.sendpost2_player_runtime);
        mTotalTime = activity.findViewById(R.id.sendpost2_player_totaltime);
        AudioWife.getInstance()
                .init(activity.getApplicationContext(), uri)
                .setPlayView(mPlayMedia)
                .setPauseView(mPauseMedia)
                .setSeekBar(mMediaSeekBar)
                .setRuntimeView(mRunTime)
                .setTotalTimeView(mTotalTime)
                .play();
    }
    private String getFileName(Uri uri){
        String uriString = uri.toString();
        File myFile = new File(uriString);
        String displayName = null;

        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName();
        }

        return displayName;
    }
    public String getRealPathFromURI(Uri uri) {
        String a=uri.toString();
        String b= uri.getPath();
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = getContentResolver().query(uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void fileSelect(){
        Intent filePickerIntent = new Intent();
        filePickerIntent.setType("*/*");
        filePickerIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(filePickerIntent, "Select An Audio"), file);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case file:
                    uri = data.getData();
                    builder.setTitle("اطلاع");
                    builder.setCancelable(false);
                    builder.setMessage("آیا از صحت صدای انتخاب شده اطمینان دارید؟");
                    builder.setPositiveButton("ارسال صدا", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int whichbtn) {
                            AudioWife.getInstance().pause();
                            AudioWife.getInstance().release();
                            if (checknet(SendPost2Activity.this)) {
                                new UploadFileAsync(SendPost2Activity.this).execute(getRealPathFromURI(uri),which,getFileName(uri));
                            }else {
                                Toast.makeText(SendPost2Activity.this,"اتصال به اینترنت را بررسی کنید!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
//                    builder.setNeutralButton("پخش صدا", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            play(SendPost2Activity.this,uri);
//
//                        }
//                    });
                    builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AudioWife.getInstance().pause();
                            AudioWife.getInstance().release();

                        }
                    });
                    dialog = builder.create();
                    dialog.show();


                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(SendPost2Activity.this,"بازگشت در این مرحله امکان پذیر نمی باشد!",Toast.LENGTH_LONG).show();
    }
    private void addView(String text){

            layout = findViewById(R.id.sendpost2_linear);
            final Button button = new Button(SendPost2Activity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,25);
            button.setLayoutParams(params);

            button.setId(viewCount++);
            button.setText("انتخاب صدای "+text);

            button.setBackground(getResources().getDrawable(R.drawable.shape3));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    which= button.getId()+"";
                    fileSelect();
                }
            });
            layout.addView(button);

    }
    private void sendMessage(final String sender,final String title, final String message){
        if(checknet(SendPost2Activity.this)){
            progressDialog= new ProgressDialog(SendPost2Activity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"sendnotification.php";
            StringRequest loginrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    SendPost2Activity.this.finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(SendPost2Activity.this,"لطفا برنامه را ببندید و دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
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
            Mysingleton.getmInstance(SendPost2Activity.this).addToRequestque(loginrequest);

        }else{
            Toast.makeText(SendPost2Activity.this,"اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
        }
    }
}
