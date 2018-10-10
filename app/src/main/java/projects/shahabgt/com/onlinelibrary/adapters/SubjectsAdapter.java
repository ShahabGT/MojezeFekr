package projects.shahabgt.com.onlinelibrary.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import projects.shahabgt.com.onlinelibrary.R;
import projects.shahabgt.com.onlinelibrary.SendPostActivity;
import projects.shahabgt.com.onlinelibrary.SubsetActivity;
import projects.shahabgt.com.onlinelibrary.Dialogs.DiscountClass;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;
import projects.shahabgt.com.onlinelibrary.models.SubjectsModel;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder>  {
    private static ProgressDialog progressDialog;
    private Uri uri;
    private static Context ctx;
    private static Activity activity;
    private ArrayList<SubjectsModel> arrayList;
    private static SharedPreferences sp;
    private static AlertDialog.Builder builder;
    private static AlertDialog alertDialog;

    public SubjectsAdapter(Activity activity, Context ctx, ArrayList<SubjectsModel> arrayList){
        this.arrayList=arrayList;
        SubjectsAdapter.ctx =ctx;
        SubjectsAdapter.activity =activity;
        sp =activity.getSharedPreferences("logininfo",Context.MODE_PRIVATE);

    }


    @Override
    public SubjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects_items,parent,false);
        SubjectsViewHolder subjectsViewHolder = new SubjectsViewHolder(view,ctx,activity);
        return subjectsViewHolder;


    }

    @Override
    public void onBindViewHolder(SubjectsViewHolder holder, int position) {
        NumberFormat nf= NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);
        SubjectsModel subjectsModelModel = arrayList.get(position);
        uri = Uri.parse(ctx.getResources().getString(R.string.subject_img_url)+subjectsModelModel.get_id()+".jpg");
        holder.id.setText(subjectsModelModel.get_id());
        holder.name.setText(subjectsModelModel.get_name());
        if(subjectsModelModel.get_price().equals("خریداری شده")){
            holder.price.setText(subjectsModelModel.get_price());
            holder.tag.setVisibility(View.GONE);
        }else {
            holder.price.setText(nf.format(Integer.parseInt(subjectsModelModel.get_price())));
            holder.price2.setText(subjectsModelModel.get_price());

        }

        holder.count.setText(subjectsModelModel.get_count());
        holder.pic.setImageURI(uri);

    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class SubjectsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView id,name,count,price,price2,tag;
        SimpleDraweeView pic;
        Context ctx;
        Activity act;
        Button delete,edit;

        public SubjectsViewHolder(final View view, final Context context, final Activity activity){
            super(view);
            this.ctx=context;
            this.act=activity;
            view.setOnClickListener(this);
            id= view.findViewById(R.id.subjects_items_id);
            name= view.findViewById(R.id.subjects_items_name);
            count= view.findViewById(R.id.subjects_items_count);
            price= view.findViewById(R.id.subjects_items_price);
            price2= view.findViewById(R.id.subjects_items_price2);
            tag= view.findViewById(R.id.subjects_items_price_tag);
            pic= view.findViewById(R.id.subjects_items_pic);
            delete= view.findViewById(R.id.subjects_items_delete);
            edit= view.findViewById(R.id.subjects_items_edit);
            if(sp.getString("name","").equals("ADMIN")){
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }else {
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    builder = new android.app.AlertDialog.Builder(activity);
                    builder.setTitle("اطلاع");
                    builder.setMessage("دوره انتخاب شده حذف و قابل بازیابی نخواهد بود، مطمئن هستید؟");
                    builder.setPositiveButton("حذف", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Delete(id.getText().toString());
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
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder = new android.app.AlertDialog.Builder(activity);
                    builder.setTitle("اطلاع");
                    builder.setMessage("آیا مایل به ویرایش دوره مورد نظر هستید؟");
                    builder.setPositiveButton("ویرایش", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Edit(id.getText().toString(),name.getText().toString());
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
            });

        }

        @Override
        public void onClick(View v) {
            if(price.getText().toString().equals("خریداری شده")||price.getText().toString().equals("0") ){
                Intent intent= new Intent(act,SubsetActivity.class);
                intent.putExtra("s_id",id.getText().toString());
                intent.putExtra("subject",name.getText().toString());
                act.startActivity(intent);

            }else {

                DiscountClass cdd = new DiscountClass(act,ctx);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                DiscountClass.discountprice=price2.getText().toString();
                DiscountClass.s_id=id.getText().toString();
                DiscountClass.number=sp.getString("number","");
                cdd.show();
            }
        }
    }

    private static void Delete(final String sid){
        if (checknet(ctx)) {
            progressDialog= new ProgressDialog(activity);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=ctx.getResources().getString(R.string.url)+"delete.php";

            StringRequest sendpost = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if(response.equals("done")){
                        Toast.makeText(ctx,"دوره با موفقیت حذف شد!",Toast.LENGTH_LONG).show();
                        activity.finish();
                    }else{
                        Toast.makeText(ctx,"خطایی رخ داده لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(ctx,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("sid",sid);
                    return params;
                }
            };

      //      sendpost.setRetryPolicy(new DefaultRetryPolicy(30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Mysingleton.getmInstance(ctx).addToRequestque(sendpost);
        } else {
            Toast.makeText(ctx, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
        }

    }

    private static void Edit(final String id,final String name){
        Intent intent = new Intent(activity,SendPostActivity.class);
        intent.putExtra("where","edit");
        intent.putExtra("subjectid",id);

        intent.putExtra("name",name);
        activity.startActivity(intent);
        activity.finish();

    }

}