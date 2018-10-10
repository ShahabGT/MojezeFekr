package projects.shahabgt.com.onlinelibrary.style;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class FontEditText extends android.support.v7.widget.AppCompatEditText {
    public static Typeface FONT_NAME;


    public FontEditText(Context context) {
        super(context);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), getFont(context));
        this.setTypeface(FONT_NAME);
        this.setTextColor(getColor(context));

    }
    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), getFont(context));
        this.setTypeface(FONT_NAME);
        this.setTextColor(getColor(context));

    }
    public FontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), getFont(context));
        this.setTypeface(FONT_NAME);
        this.setTextColor(getColor(context));
    }

    public String getFont(Context context){
        SharedPreferences sp = context.getSharedPreferences("settings",0);
        String font =  sp.getString("font","vazir");
        return "font/"+font+".ttf";
    }
    public int getColor(Context context){
        SharedPreferences sp = context.getSharedPreferences("settings",0);
        int color =  sp.getInt("color", Color.BLACK);
        return color;
    }
}