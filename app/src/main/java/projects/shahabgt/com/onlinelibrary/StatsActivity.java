package projects.shahabgt.com.onlinelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class StatsActivity extends AppCompatActivity {
    private TextView yearcount,yearprice,totalcount,totalprice,countyeartitle,counttotalyeartitle,sumtotalyeartitle;
    private Button selectyear;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private ImageView back;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        back = findViewById(R.id.stats_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        yearcount = findViewById(R.id.stats_totalyearcount);
        yearprice = findViewById(R.id.stats_totalyearprice);
        countyeartitle = findViewById(R.id.stats_countyeartitle);
        totalcount = findViewById(R.id.stats_totalcount);
        totalprice = findViewById(R.id.stats_totalprice);
        selectyear = findViewById(R.id.stats_yearselector);
        counttotalyeartitle = findViewById(R.id.stats_counttotalyeartitle);
        sumtotalyeartitle = findViewById(R.id.stats_sumtotalyeartitle);

        mBarChart =  findViewById(R.id.barchart);
        mPieChart = findViewById(R.id.piechart);
        getStats("2018");

        selectyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(StatsActivity.this)
                        .minValue(1397)
                        .maxValue(1499)
                        .defaultValue(1397)
                        .backgroundColor(Color.WHITE)
                        .separatorColor(Color.TRANSPARENT)
                        .textColor(Color.BLACK)
                        .textSize(20)
                        .enableFocusability(false)
                        .wrapSelectorWheel(true)
                        .build();
                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(StatsActivity.this);

                builder
                        .setTitle("انتخاب سال")
                        .setView(numberPicker)
                        .setCancelable(true)
                        .setPositiveButton("انتخاب", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String a = String.valueOf(numberPicker.getValue()+621);
                                getStats(a);
                            }
                        })
                        .setNegativeButton("بستن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


    }

    private void getStats(final String year){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet(StatsActivity.this)){
            progressDialog= new ProgressDialog(StatsActivity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"stats.php";
            StringRequest loginrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        float[] countargs = new float[12];
                        float[] priceargs = new float[12];
                        for(int i=0; i<12;i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("count").equals("null")){
                                countargs[i]= 0f;
                            }else {
                                countargs[i]= Float.valueOf(jsonObject.getString("count"));
                            }
                            if(jsonObject.getString("sum").equals("null")){
                                priceargs[i]= 0f;
                            }else {
                                priceargs[i]= Float.valueOf(jsonObject.getString("sum"));
                            }

                        }
                        NumberFormat nf= NumberFormat.getNumberInstance();
                        nf.setMaximumFractionDigits(0);
                        JSONObject jsonObject = jsonArray.getJSONObject(12);
                        yearcount.setText( jsonObject.getString("count"));
                        if(jsonObject.getString("sum").equals("null")) {
                            yearprice.setText( "0"+" ریال");
                        }else {
                            yearprice.setText( nf.format(Integer.parseInt(jsonObject.getString("sum")))+" ریال");
                        }

                        jsonObject = jsonArray.getJSONObject(13);
                        totalcount.setText( jsonObject.getString("count"));
                        if(jsonObject.getString("sum").equals("null")) {
                            totalprice.setText( "0"+" ریال");
                        }else {
                            totalprice.setText( nf.format(Integer.parseInt(jsonObject.getString("sum")))+" ریال");
                        }
                        Barchart(countargs);
                        PieChart(priceargs);


                        String a =(Integer.parseInt(year)-621)+"";
                        countyeartitle.setText(getString(R.string.countyeartitle,a));
                        counttotalyeartitle.setText(getString(R.string.counttotalyeartitle,a));
                        sumtotalyeartitle.setText(getString(R.string.sumtotalyeartitle,a));



                    }catch (Exception e){
                        Toast.makeText(StatsActivity.this,e.getMessage() + " "+e.toString(), Toast.LENGTH_LONG).show();
                        onBackPressed();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(StatsActivity.this,"لطفا برنامه را ببندید و دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("year",year);
                    return params;
                }
            };
            Mysingleton.getmInstance(StatsActivity.this).addToRequestque(loginrequest);

        }else{
            Toast.makeText(StatsActivity.this,"اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }



    }

    private void Barchart(float[] args){
        mBarChart.clearChart();
        mBarChart.addBar(new BarModel("فروردین",args[0], 0xFF123456));
        mBarChart.addBar(new BarModel("اردیبهشت",args[1],  0xFF343456));
        mBarChart.addBar(new BarModel("خرداد",args[2], 0xFF563456));
        mBarChart.addBar(new BarModel("تیر",args[3], 0xFF873F56));
        mBarChart.addBar(new BarModel("مرداد",args[4], 0xFF56B7F1));
        mBarChart.addBar(new BarModel("شهریور",args[5],  0xFF343456));
        mBarChart.addBar(new BarModel("مهر",args[6], 0xFF1FF4AC));
        mBarChart.addBar(new BarModel("آبان",args[7],  0xFF1BA4E6));
        mBarChart.addBar(new BarModel("آذر",args[8],  0xFF1BA4E6));
        mBarChart.addBar(new BarModel("دی",args[9],  0xFF1BA4E6));
        mBarChart.addBar(new BarModel("بهمن",args[10],  0xFF1BA4E6));
        mBarChart.addBar(new BarModel("اسفند",args[11],  0xFF1BA4E6));
        mBarChart.update();
        mBarChart.startAnimation();
    }
    private void PieChart(float[] args){
        mPieChart.clearChart();
        mPieChart.addPieSlice(new PieModel("فروردین", args[0], Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("اردیبهشت", args[1], Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("خرداد", args[2], Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("تیر", args[3], Color.parseColor("#FED70E")));
        mPieChart.addPieSlice(new PieModel("مرداد", args[4], Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("شهریور", args[5], Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("مهر", args[6], Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("آبان", args[7], Color.parseColor("#FED70E")));
        mPieChart.addPieSlice(new PieModel("آذر", args[8], Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("دی", args[9], Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("بهمن", args[10], Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("اسفند", args[11], Color.parseColor("#FED70E")));
        mPieChart.update();
        mPieChart.startAnimation();
    }
}
