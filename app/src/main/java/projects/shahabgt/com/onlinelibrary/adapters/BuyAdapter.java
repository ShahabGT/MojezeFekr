package projects.shahabgt.com.onlinelibrary.adapters;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import projects.shahabgt.com.onlinelibrary.R;
import projects.shahabgt.com.onlinelibrary.classes.DateParser;
import projects.shahabgt.com.onlinelibrary.models.BuyModel;
import projects.shahabgt.com.onlinelibrary.models.DateModel;


public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.BuyViewHolder> {

    private Uri uri;
    private Context ctx;
    private Activity activity;
    private ArrayList<BuyModel> arrayList;


    public BuyAdapter(Activity activity, Context ctx, ArrayList<BuyModel> arrayList){
        this.arrayList=arrayList;
        this.ctx=ctx;
        this.activity =activity;
    }


    @Override
    public BuyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_items,parent,false);
        BuyViewHolder buyViewHolder = new BuyViewHolder(view,ctx,activity);
        return buyViewHolder;


    }

    @Override
    public void onBindViewHolder(BuyViewHolder holder, int position) {
        NumberFormat nf= NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);
        BuyModel buyModelModel = arrayList.get(position);
        holder.message.setText(buyModelModel.get_subject());
        holder.price.setText(nf.format(Integer.parseInt(buyModelModel.get_price()))+" ریال");
        DateParser dp = new DateParser(buyModelModel.get_date());
        DateModel dm = DateParser.dateAndTimeParser();
        String temp = dm.toString();
        holder.date.setText(temp);
        holder.rescode.setText(buyModelModel.get_rescode());
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class BuyViewHolder extends RecyclerView.ViewHolder
    {

        TextView message,date,price,rescode;
        Context ctx;
        Activity act;

        public BuyViewHolder(final View view, Context context,Activity activity){
            super(view);
            this.ctx=context;
            this.act=activity;
            message= view.findViewById(R.id.buy_items_message);
            date= view.findViewById(R.id.buy_items_date);
            price= view.findViewById(R.id.buy_items_price);
            rescode= view.findViewById(R.id.buy_items_rescode);


        }
    }
}