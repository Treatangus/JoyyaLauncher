package com.treatangus.joyya.mcgui;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.treatangus.joyya.R;

import java.io.IOException;

public class MinecraftSpinner extends androidx.appcompat.widget.AppCompatButton {
    private ColorDrawable stroke = new ColorDrawable(Color.parseColor("#1e1e1e"));
    private ColorDrawable shadow = new ColorDrawable(Color.parseColor("#58585a"));
    private ColorDrawable bgNormal = new ColorDrawable(Color.parseColor("#d0d1d5"));
    private ColorDrawable left = new ColorDrawable(Color.parseColor("#b2ffffff"));
    private ColorDrawable top = new ColorDrawable(Color.parseColor("#b2ffffff"));
    private ColorDrawable right = new ColorDrawable(Color.parseColor("#a5ffffff"));
    private ColorDrawable bottom = new ColorDrawable(Color.parseColor("#a5ffffff"));

    private ColorDrawable strokeFocus = new ColorDrawable(Color.parseColor("#1e1e1e"));
    private ColorDrawable bgFocus = new ColorDrawable(Color.parseColor("#b1b2b5"));
    private ColorDrawable leftFocus = new ColorDrawable(Color.parseColor("#b2ffffff"));
    private ColorDrawable topFocus = new ColorDrawable(Color.parseColor("#b2ffffff"));
    private ColorDrawable rightFocus = new ColorDrawable(Color.parseColor("#a5ffffff"));
    private ColorDrawable bottomFocus = new ColorDrawable(Color.parseColor("#a5ffffff"));
    private AssetManager assetManager;
    private SoundPool MinecraftButtonSound;
    private AssetFileDescriptor fileDescriptor;
    private int soundId;
    private int streamId;
    private boolean isUp = true;

    private Drawable[] DrawableArray = new Drawable[]{
            stroke,
            shadow,
            bgNormal,
            left,
            top,
            right,
            bottom
    };

    private Drawable[] DrawableArrayFocus = new Drawable[]{
            strokeFocus,
            bgFocus,
            topFocus,
            leftFocus,
            rightFocus,
            bottomFocus
    };

    private LayerDrawable layerdrawable, layerdrawablefocus;

    public MinecraftSpinner(Context ctx) {
        this(ctx, null);
    }

    public MinecraftSpinner(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initButtonSound(); //因为SoundPool需要预加载声音资源，否则会出现第一次按下按钮没有声音的问题
        setArrowImage();
        init();
    }

    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layerdrawable = new LayerDrawable(DrawableArray);
                layerdrawable.setLayerInset(0, 0, 0, 0, 0); // stroke
                layerdrawable.setLayerInset(1, 4, 4, 4, 4);// shadow
                layerdrawable.setLayerInset(2, 4, 4, 4, 12); // bg
                layerdrawable.setLayerInset(3, 8, 4, getWidth() - 4, 12); // left
                layerdrawable.setLayerInset(4, 8, 8, 4, getHeight() - 4); // top
                layerdrawable.setLayerInset(5, getWidth() - 4,4, 8, 12); // right
                layerdrawable.setLayerInset(6, 4, getHeight() - 12, 8, 16); // bottom

                layerdrawablefocus = new LayerDrawable(DrawableArrayFocus);
                layerdrawablefocus.setLayerInset(0, 0, 0, 0, 0); // strokeFocus
                layerdrawablefocus.setLayerInset(1, 4, 4, 4, 4); // bgFocus
                layerdrawablefocus.setLayerInset(2, 8, 4, getWidth() - 4, 4); // leftFocus
                layerdrawablefocus.setLayerInset(3, 8, 8, 4, getHeight() - 4); // topFocus
                layerdrawablefocus.setLayerInset(4, getWidth() - 4,4, 8, 4); // rightFocus
                layerdrawablefocus.setLayerInset(5, 4, getHeight() - 8, 8, 4); // bottomFocus

                setBackgroundDrawable(layerdrawable);
                setTextColor(Color.BLACK);
                setPadding(10, 10, 40, 10);
                //setOnTouchListener(null);
            }
        });
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/minecraft_seven.ttf"));

    }
    private void setArrowImage() {
        Drawable arrow = getResources().getDrawable(R.drawable.icon_arrow_down_black);
        arrow.setBounds(0,0,30,20); //设置箭头大小
        setCompoundDrawables(null,null,arrow,null); //设置箭头显示的位置
    }
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setBackgroundDrawable(enabled ? layerdrawable : layerdrawablefocus);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() == event.ACTION_UP) && !isUp && isEnabled()) {
            setBackgroundDrawable(layerdrawable);
            isUp = true;
        } else if (event.getAction() == event.ACTION_DOWN && isUp) {
            setBackgroundDrawable(layerdrawablefocus);
            if (MinecraftButtonSound == null) {
                streamId = MinecraftButtonSound.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            } else if (MinecraftButtonSound.unload(streamId)) {
                MinecraftButtonSound.stop(streamId);
            } else {
                MinecraftButtonSound.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            }
            isUp = false;
        }

        return super.onTouchEvent(event);
    }

    private void initButtonSound() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MinecraftButtonSound = new SoundPool.Builder()
                        .setMaxStreams(1)
                        .build();
            } else {
                MinecraftButtonSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            }
            assetManager = getContext().getAssets();
            fileDescriptor = assetManager.openFd("sounds/click.ogg");
            soundId = MinecraftButtonSound.load(fileDescriptor, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (MinecraftButtonSound.unload(streamId)) {
            MinecraftButtonSound.stop(streamId);
        }
        MinecraftButtonSound.release();
        super.onDetachedFromWindow();
    }
}
