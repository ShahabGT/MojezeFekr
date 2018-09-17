package projects.shahabgt.com.onlinelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import projects.shahabgt.com.onlinelibrary.classes.CustomDialogClass;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce;
    ImageView menuOpener;
    SharedPreferences sp;
    LinearLayout courses,acount,cart,message,about,stats;
    Animation animation;
    String acountn="";
    TextView acount_text,stats_text,about_text,card_text;
    ImageView stats_img,about_img,card_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doubleBackToExitPressedOnce = false;
        sp= getApplicationContext().getSharedPreferences("logininfo",0);

        menuOpener = findViewById(R.id.main_drawer);
        animation = AnimationUtils.loadAnimation(this, R.anim.drawer_anim);
        courses = findViewById(R.id.main_courses);
        acount = findViewById(R.id.main_acount);
        acount_text = findViewById(R.id.main_acount_text);
        cart = findViewById(R.id.main_cart);
        message = findViewById(R.id.main_message);
        about = findViewById(R.id.main_about);
        stats = findViewById(R.id.main_stats);
        stats_text = findViewById(R.id.main_stats_text);
        stats_img = findViewById(R.id.main_stats_img);
        about_text = findViewById(R.id.main_about_text);
        about_img = findViewById(R.id.main_about_img);
        card_text = findViewById(R.id.main_card_text);
        card_img = findViewById(R.id.main_card_img);



        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SubjectsActivity.class));
            }
        });

        acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MessagesActivity.class));
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sp.getInt("stat",0)==1){
            acountn=sp.getString("name","");
        }else{
            acountn="حساب کاربری";
        }
        acount_text.setText(acountn);
        if(sp.getString("name","").equals("ADMIN")){
           stats_text.setText("آمار");
           about_text.setText("ارسال پیام");
           card_text.setText("دوره جدید");
           stats_img.setImageDrawable(getResources().getDrawable(R.drawable.statistics));
           about_img.setImageDrawable(getResources().getDrawable(R.drawable.sendmessage));
           card_img.setImageDrawable(getResources().getDrawable(R.drawable.newpost));
           stats.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this,StatsActivity.class));
               }
           });
           about.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this,SendMessageActivity.class));
               }
           });
           cart.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this,SendPostActivity.class));
               }
           });

        }else{
            stats_text.setText("خروج");
            about_text.setText("درباره");
            card_text.setText("خرید های من");
            stats_img.setImageDrawable(getResources().getDrawable(R.drawable.mexit));
            about_img.setImageDrawable(getResources().getDrawable(R.drawable.aboutus));
            card_img.setImageDrawable(getResources().getDrawable(R.drawable.cart));
            stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.this.finish();
                }
            });
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialogClass cdd = new CustomDialogClass(MainActivity.this);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }
            });
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,BuyActivity.class));
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Toast t;
        t =  Toast.makeText(MainActivity.this,"برای خروج دوباره کلید بازگشت را فشار دهید!",Toast.LENGTH_LONG);
            if (doubleBackToExitPressedOnce) {
                MainActivity.this.finish();
                t.cancel();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            t.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);}


}
