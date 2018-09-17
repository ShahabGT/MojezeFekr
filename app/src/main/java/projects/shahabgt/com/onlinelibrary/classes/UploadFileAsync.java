package projects.shahabgt.com.onlinelibrary.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import projects.shahabgt.com.onlinelibrary.R;

public class UploadFileAsync extends AsyncTask<String, Void, String> {
    private Context context;
    private ProgressDialog dialog;
    private String res;
    private String subset;
    private String filename;
    public UploadFileAsync(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        subset=params[1];
        filename=params[2];

        try {
            String sourceFileUri = params[0];
            String upLoadServerUri = context.getString(R.string.url)+"upload.php";
            HttpURLConnection conn;
            DataOutputStream dos;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (sourceFile.isFile()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", sourceFileUri);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);



                  int  serverResponseCode = conn.getResponseCode();
                    if (serverResponseCode == 200) {
                        res="ok";
                    }
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {
                    res="error";
                }
            }

        } catch (Exception ex) {
            res="error";
        }
        return res;
    }

    @Override
    protected void onPostExecute(String result) {

        if(result.equals("ok")){
            rename(subset,filename);
        }else {
            Toast.makeText(context,"خطا! لطفا دوباره امتحان کنید",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("در حال آپلود فایل لطفا منتظر بمانید...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void rename( final String subset, final String filename){
         final String url= context.getString(R.string.url)+"rename.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if(response.equals("ok")){
                    Toast.makeText(context,"فایل با موفقیت آپلود شد!",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"خطا! لطفا دوباره امتحان کنید",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context,"خطا! لطفا دوباره امتحان کنید",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("subset",subset);
                params.put("filename",filename);

                return params;

            }
        };
        Mysingleton.getmInstance(context).addToRequestque(request);

    }


}