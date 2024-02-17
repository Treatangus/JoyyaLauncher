package com.treatangus.joyya.mcgui;

import android.content.*;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.*;
import android.view.*;

import java.io.IOException;

public class MinecraftLinearLayout extends androidx.appcompat.widget.LinearLayoutCompat {
    private ColorDrawable left = new ColorDrawable(Color.parseColor("#ecedee"));
    private ColorDrawable top = new ColorDrawable(Color.parseColor("#ecedee"));
    private ColorDrawable stroke = new ColorDrawable(Color.parseColor("#1e1e1e"));
    private ColorDrawable right = new ColorDrawable(Color.parseColor("#e3e3e5"));
    private ColorDrawable bottom = new ColorDrawable(Color.parseColor("#e3e3e5"));
    private ColorDrawable bgNormal = new ColorDrawable(Color.parseColor("#313233"));

    private Drawable[] DrawableArray = new Drawable[]{
            stroke,
            /*top,
            left,
            right,
            bottom, */
            bgNormal
    };

    private LayerDrawable layerdrawable, layerdrawablefocus;

    public MinecraftLinearLayout(Context ctx) {
        this(ctx, null);
    }

    public MinecraftLinearLayout(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
    }

    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layerdrawable = new LayerDrawable(DrawableArray);
                layerdrawable.setLayerInset(0, 0, 0, 0, 0); // stroke
                /*layerdrawable.setLayerInset(1, 4, 4, 4, 4); // top
                layerdrawable.setLayerInset(2, 4, 4, getWidth() - 4, 4); // left
                layerdrawable.setLayerInset(3, getWidth() - 4, 4, 4, 4); // right
                layerdrawable.setLayerInset(4, 4, getHeight() - 4, 4, 4);// bottom */
                layerdrawable.setLayerInset(1, 8, 8, 8, 8); // bg

                setBackgroundDrawable(layerdrawable);
                setPadding(10, 10, 10, 10);
                //setOnTouchListener(null);
            }
        });

    }
}
