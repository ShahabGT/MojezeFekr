package projects.shahabgt.com.onlinelibrary;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import projects.shahabgt.com.onlinelibrary.adapters.MessagesAdapter;
import projects.shahabgt.com.onlinelibrary.classes.DatabaseOperations;

public class MessagesActivity extends AppCompatActivity {
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        if(loadData(MessagesActivity.this)==0){
            Toast.makeText(MessagesActivity.this,"پیامی برای نمایش وجود ندارد!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        back= findViewById(R.id.messages_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public static int loadData(Activity activity){
        DatabaseOperations db = new DatabaseOperations(activity);
        RecyclerView recyclerView = activity.findViewById(R.id.messages_recylcer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        MessagesAdapter adapter = new MessagesAdapter(activity,activity.getApplicationContext(),db.getData());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db.close();
        return adapter.getItemCount();


    }
}
