package projects.shahabgt.com.onlinelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import projects.shahabgt.com.onlinelibrary.adapters.SubjectsAdapter;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;
import projects.shahabgt.com.onlinelibrary.models.SubjectsModel;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class SubjectsActivity extends AppCompatActivity {
    ImageView back;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubjectsAdapter adapter;
    LinearLayout loadinglayout;
    SimpleDraweeView loadingimg;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(SubjectsActivity.this);
        setContentView(R.layout.activity_subjects);
        back= findViewById(R.id.subjects_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadinglayout = findViewById(R.id.subjects_loadinglayout);
        loadingimg = findViewById(R.id.subjects_loading);
        Uri loadinguri= Uri.parse("asset:///loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(loadinguri)
                .setAutoPlayAnimations(true)
                .build();
        loadingimg.setController(controller);
        loadinglayout.setVisibility(View.VISIBLE);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        int stat=sp.getInt("stat",0);
        if(stat==0){
            startActivity(new Intent(SubjectsActivity.this,LoginActivity.class));
            SubjectsActivity.this.finish();
        }else {
            getsubjects();
        }





    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getsubjects(){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet(SubjectsActivity.this)) {
            final String url=getResources().getString(R.string.url)+"getsubjects.php";
            StringRequest subjectsrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("nothing")){
                        Toast.makeText(SubjectsActivity.this,"چیزی برای نمایش وجود ندارد", Toast.LENGTH_LONG).show();
                        onBackPressed();

                    }else
                    {
                        recyclerView = findViewById(R.id.subjects_recylcer);
                        ArrayList<SubjectsModel> subjectsModels = new ArrayList<SubjectsModel>();
                        subjectsModels.clear();
                        try{
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                SubjectsModel subjectsModel = new SubjectsModel();
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                subjectsModel.set_id(jsonobject.getString("id"));
                                subjectsModel.set_name(jsonobject.getString("name"));
                                subjectsModel.set_price(jsonobject.getString("price"));
                                subjectsModel.set_count(jsonobject.getString("count"));
                                subjectsModels.add(subjectsModel);

                            }


                        }catch (Exception e){
                            Toast.makeText(SubjectsActivity.this,"لطفا دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        layoutManager = new LinearLayoutManager(SubjectsActivity.this);
                        adapter = new SubjectsAdapter(SubjectsActivity.this,getApplicationContext(), subjectsModels);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        loadinglayout.setVisibility(View.GONE);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SubjectsActivity.this,"لطفا دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("number",sp.getString("number",""));
                    return params;
                }

            };
            Mysingleton.getmInstance(SubjectsActivity.this).addToRequestque(subjectsrequest);

        }else{
            Toast.makeText(SubjectsActivity.this,"اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }
}
