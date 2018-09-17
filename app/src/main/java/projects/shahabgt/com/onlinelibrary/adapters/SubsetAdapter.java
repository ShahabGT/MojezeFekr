package projects.shahabgt.com.onlinelibrary.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import projects.shahabgt.com.onlinelibrary.R;
import projects.shahabgt.com.onlinelibrary.SubsetActivity;
import projects.shahabgt.com.onlinelibrary.models.SubsetModel;

public class SubsetAdapter extends RecyclerView.Adapter<SubsetAdapter.SubsetViewHolder>  {

    private Uri uri;
    private Context ctx;
    private Activity activity;
    private ArrayList<SubsetModel> arrayList;
    int selectedPosition=-1;
    String xs_id,xid;

    public SubsetAdapter(Activity activity, Context ctx, ArrayList<SubsetModel> arrayList){
        this.arrayList=arrayList;
        this.ctx=ctx;
        this.activity =activity;
    }


    @Override
    public SubsetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subset_items,parent,false);
        SubsetViewHolder subsetViewHolder = new SubsetViewHolder(view,ctx,activity);
        return subsetViewHolder;


    }

    @Override
    public void onBindViewHolder(SubsetViewHolder holder, int position) {
        final int pp=position;
        SubsetModel subsetModelModel = arrayList.get(position);
        holder.id.setText(subsetModelModel.get_id());
        xid=subsetModelModel.get_id();
        holder.s_id.setText(subsetModelModel.get_s_id());
        xs_id=subsetModelModel.get_s_id();
        holder.subject.setText(subsetModelModel.get_subject());
        holder.subset.setText(subsetModelModel.get_title());
        if(selectedPosition==position){
            holder.card.setBackgroundColor(Color.parseColor("#E8F5E9"));}
        else
        { holder.card.setBackgroundColor(Color.parseColor("#ffffff"));}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=pp;
                Uri mUri= Uri.parse(ctx.getResources().getString(R.string.sounds_url)+xs_id+"/"+(pp+1)+".mp3");
                SubsetActivity.play(activity,mUri);
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class SubsetViewHolder extends RecyclerView.ViewHolder {

        TextView s_id,id,subject,subset;
        LinearLayout card;
        Context ctx;
        Activity act;

        public SubsetViewHolder(final View view, Context context,Activity activity){
            super(view);
            this.ctx=context;
            this.act=activity;
            id= view.findViewById(R.id.subset_items_id);
            s_id= view.findViewById(R.id.subset_items_s_id);
            subject= view.findViewById(R.id.subset_items_subject);
            subset= view.findViewById(R.id.subset_items_subset);
            card= view.findViewById(R.id.subset_items_card);

        }
    }

}