package projects.shahabgt.com.onlinelibrary.style;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class FontTextView extends android.support.v7.widget.AppCompatTextView {
    public static Typeface FONT_NAME;


    public FontTextView(Context context) {
        super(context);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "font/vazir.ttf");
        this.setTypeface(FONT_NAME);
    }
    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "font/vazir.ttf");
        this.setTypeface(FONT_NAME);
    }
    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "font/vazir.ttf");
        this.setTypeface(FONT_NAME);
    }
}