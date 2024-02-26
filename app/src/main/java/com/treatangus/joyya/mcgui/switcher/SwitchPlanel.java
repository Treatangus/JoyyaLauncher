package com.treatangus.joyya.mcgui.switcher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.treatangus.joyya.R;

public class SwitchPlanel extends androidx.appcompat.widget.LinearLayoutCompat {
    private ColorDrawable stroke = new ColorDrawable(Color.parseColor("#1e1e1e"));
    private ColorDrawable leftbg = new ColorDrawable(Color.parseColor("#3c8526"));
    private ColorDrawable rightbg = new ColorDrawable(Color.parseColor("#8c8d8f"));

    private Drawable[] DrawableArray = new Drawable[]{
            stroke,
            leftbg,
            rightbg
    };

    private LayerDrawable layerdrawable;

    public SwitchPlanel(@NonNull Context context) {
        super(context);
    }
    public SwitchPlanel(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
        initWidgit(ctx);
    }

    public void initWidgit(Context context) {
        ImageView leftiv = new ImageView(context);
        ImageView rightiv = new ImageView(context);

        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        layoutParams.weight = 2;
        leftiv.setLayoutParams(layoutParams);
        rightiv.setLayoutParams(layoutParams);

        leftiv.setPadding(10,10,10,10);
        rightiv.setPadding(10,10,10,10);

        Drawable left = getResources().getDrawable(R.drawable.icon_arrow_down_black);
        left.setBounds(20,20,0,0); //设置箭头大小
        Drawable right = getResources().getDrawable(R.drawable.icon_arrow_down_black);
        right.setBounds(0,0,20,20); //设置箭头大小

        leftiv.setImageDrawable(left);
        rightiv.setImageDrawable(right);

        //leftiv.setBackgroundResource(R.drawable.icon_check_white);
        //rightiv.setBackgroundResource(R.drawable.icon_check_black);

        addView(leftiv);
        addView(rightiv);
    }
    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layerdrawable = new LayerDrawable(DrawableArray);
                layerdrawable.setLayerInset(0, 0, 0, 0, 0); // stroke
                layerdrawable.setLayerInset(1, 4, 4, getWidth()/2, 4); // leftbg
                layerdrawable.setLayerInset(2, getWidth()/2, 4, 4, 4); // rightbg

                setBackgroundDrawable(layerdrawable);
                setPadding(10, 10, 10, 10);
                setOrientation(HORIZONTAL);
            }
        });
    }
}
