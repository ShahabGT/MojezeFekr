package projects.shahabgt.com.onlinelibrary.style;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Jonathan on 9/13/2017.
 */

public class EnEditText extends android.support.v7.widget.AppCompatEditText {
    public static Typeface FONT_NAME;


    public EnEditText(Context context) {
        super(context);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "font/vazir.ttf");
        this.setTypeface(FONT_NAME);
    }
    public EnEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "font/vazir.ttf");
        this.setTypeface(FONT_NAME);
    }
    public EnEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "font/vazir.ttf");
        this.setTypeface(FONT_NAME);
    }
}