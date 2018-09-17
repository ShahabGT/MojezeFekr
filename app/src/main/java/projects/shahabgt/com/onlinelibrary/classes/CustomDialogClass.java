package projects.shahabgt.com.onlinelibrary.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import projects.shahabgt.com.onlinelibrary.R;


/**
 * Created by Jonathan on 12/27/2017.
 */

public class CustomDialogClass extends Dialog implements View.OnClickListener {
    public Activity c;
    TextView versionname;
    String versionName="";
    ImageView instagram,telegram,website;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customdialog_layout);
        versionname = findViewById(R.id.customdialog_versionname);
        try {
            versionName = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
            versionname.setText(" نسخه "+versionName);
        }catch (Exception e){}

        instagram = findViewById(R.id.instagram);
        telegram = findViewById(R.id.telegram);
        website = findViewById(R.id.website);

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/joinchat/AAAAAEJHl6EuzZj4L406Cg"));
                c.startActivity(telegram);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instagram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://www.instagram.com/Mojezeyefekr.ir/"));
                c.startActivity(instagram);

            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent website = new Intent(Intent.ACTION_VIEW , Uri.parse("http://www.mojezeyefekr.ir"));
                c.startActivity(website);

            }
        });

    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
}
