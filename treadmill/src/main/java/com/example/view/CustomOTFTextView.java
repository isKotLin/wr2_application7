package com.example.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xrd_linux on 2017/2/17.
 */
public class CustomOTFTextView extends TextView {
    public CustomOTFTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomOTFTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomOTFTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
//        AssetManager assetManager= context.getAssets();
//        Typeface font=Typeface.createFromAsset(assetManager, "font/Fragma Light.ttf");
//        setTypeface(font);
        Typeface newFont = Typeface.createFromAsset(context.getAssets(), "font/UNIVERSLTSTD-THINULTRACN.OTF");
        setTypeface(newFont);
    }
}
