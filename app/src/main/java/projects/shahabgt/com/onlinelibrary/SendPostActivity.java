package projects.shahabgt.com.onlinelibrary;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;
import projects.shahabgt.com.onlinelibrary.style.FontEditText;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class SendPostActivity extends AppCompatActivity {
    static final int gallery =20;
    static final int camera =50;
    Bitmap bitmap=null;
    Uri uri;
    Uri fileUri;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    ImageView image,back;
    LinearLayout layout;
    Button add,delete,send;
    int viewCount=0;
    ProgressDialog progressDialog;
    ArrayList<String> array;
    EditText subjectname,price;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);
        builder = new AlertDialog.Builder(this);
        add = findViewById(R.id.sendpost_addview);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();

            }
        });
        delete = findViewById(R.id.sendpost_deleteview);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteView();
            }
        });
        back = findViewById(R.id.sendpost_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        image = findViewById(R.id.sendpost_pic);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(SendPostActivity.this);
                builder.setTitle("انتخاب عکس");
                builder.setCancelable(true);
                builder.setMessage("لطفا عکس دوره را از طریق دوربین یا گالری انتخاب کنید");
                builder.setPositiveButton("گالری", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gallerySelect();
                    }
                });
                builder.setNegativeButton("دوربین", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        captureImage();
                    }
                });
                builder.create();
                builder.show();
            }
        });
        subjectname = findViewById(R.id.sendpost_subjectname);
        price = findViewById(R.id.sendpost_price);
        send=findViewById(R.id.sendpost_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array = new ArrayList<>();

                final String cprice= price.getText().toString();
                final String csubject = subjectname.getText().toString();

                for(int i=0;i<viewCount;i++){
                    FontEditText fontEditText = findViewById(i);
                    if(!fontEditText.getText().toString().isEmpty()){
                        array.add(fontEditText.getText().toString());
                    }
                }
                if(cprice.isEmpty()||csubject.isEmpty()||array.size()==0){
                    Toast.makeText(SendPostActivity.this,"لطفا فرم را پر کنید!",Toast.LENGTH_LONG).show();
                }else{
                    builder.setTitle("اطلاع");
                    builder.setMessage("آیا از صحت اطلاعات دوره تعریف شده اطمینان دارید؟");
                    builder.setPositiveButton("ارسال دوره", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pic="";
                            if(bitmap!=null) pic = imgToString(bitmap);
                            String res=array.toString();
                            send(csubject,array,cprice,pic);
                            alertDialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

    }

    private void deleteView(){
        if(viewCount>0) {
            layout = findViewById(R.id.sendpost_linear);
            FontEditText fontEditText = findViewById(--viewCount);
            layout.removeViewInLayout(fontEditText);
        }
    }

    private void addView(){
        if(viewCount<16) {
            layout = findViewById(R.id.sendpost_linear);
            FontEditText fontEditText = new FontEditText(SendPostActivity.this);

            fontEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            fontEditText.setMaxLines(1);
            fontEditText.setLines(1);
            fontEditText.setId(viewCount++);
            fontEditText.setHint("عنوان درس "+viewCount);
            layout.addView(fontEditText);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            switch (requestCode){
                case gallery:
                    uri = data.getData();
                    CropImage.activity(uri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setActivityTitle("ادیت عکس")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000,1000)
                            .start(this);
                    break;
                case camera:
                    CropImage.activity(fileUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setActivityTitle("ادیت عکس")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000,1000)
                            .start(this);
                    break;
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try{

                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                            image.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private String imgToString (Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, camera);
    }
    private void gallerySelect(){
        Intent galleryPickerIntent = new Intent();
        galleryPickerIntent.setType("image/*");
        galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryPickerIntent, "Select An Image"), gallery);
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {
        String IMAGE_DIRECTORY_NAME ="MojezeFekr";

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void send(final String subject,final ArrayList<String> subset,final String price, final String pic){
        if (checknet(SendPostActivity.this)) {
            progressDialog= new ProgressDialog(SendPostActivity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"sendpost.php";

            StringRequest sendpost = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if(response.equals("ok")){
                        Toast.makeText(SendPostActivity.this,"دوره جدید با موفقیت ثبت شد!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SendPostActivity.this,SendPost2Activity.class);
                        intent.putExtra("array",array);
                        startActivity(intent);
                        SendPostActivity.this.finish();
                    }else{
                        Toast.makeText(SendPostActivity.this,"خطایی رخ داده لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(SendPostActivity.this,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("subjectname",subject);
                    params.put("price",price);
                    if(!pic.isEmpty())
                        params.put("image",pic);

                    String res = subset.toString();
                    res = res.replace("[", "");
                    res = res.replace("]", "");
                    res = res.replace(",", ":::");
                    params.put("subsetname",res);

                    params.put("count",subset.size()+"");
                    return params;
                }
            };

            sendpost.setRetryPolicy(new DefaultRetryPolicy(30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Mysingleton.getmInstance(SendPostActivity.this).addToRequestque(sendpost);
        } else {
            Toast.makeText(SendPostActivity.this, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
        }
    }

}

