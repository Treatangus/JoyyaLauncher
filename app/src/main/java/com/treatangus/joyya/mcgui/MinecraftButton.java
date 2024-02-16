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

public class MinecraftButton extends androidx.appcompat.widget.AppCompatButton
{
    private ColorDrawable left = new ColorDrawable(Color.parseColor("#4f9443"));
    private ColorDrawable top = new ColorDrawable(Color.parseColor("#1c4e13"));
    private ColorDrawable stroke = new ColorDrawable(Color.parseColor("#FF000000"));
    private ColorDrawable right = new ColorDrawable(Color.parseColor("#5e9f4d"));
    private ColorDrawable bottom = new ColorDrawable(Color.parseColor("#50913f"));
    private ColorDrawable topStroke = new ColorDrawable(Color.parseColor("#61a153"));
    private ColorDrawable bgNormal = new ColorDrawable(Color.parseColor("#3b8526"));

    private ColorDrawable strokeFocus = new ColorDrawable(Color.parseColor("#1e1c21"));
    private ColorDrawable leftFocus = new ColorDrawable(Color.parseColor("#4b6e46"));
    private ColorDrawable topFocus = new ColorDrawable(Color.parseColor("#4b7243"));
    private ColorDrawable rightFocus = new ColorDrawable(Color.parseColor("#3b6132"));
    private ColorDrawable bottomFocus = new ColorDrawable(Color.parseColor("#375c31"));
    private ColorDrawable bgFocus = new ColorDrawable(Color.parseColor("#1d4d13"));
    private AssetManager assetManager;
    private SoundPool MinecraftButtonSound;
    private  AssetFileDescriptor fileDescriptor;
    private int soundId;
    private int streamId;
    private MediaPlayer MinecraftButtonSound2;
    private boolean isUp = true;

    private Drawable[] DrawableArray = new Drawable[]{
            stroke,
            top,
            topStroke,
            left,
            right,
            bottom,
            bgNormal
    };

    private Drawable[] DrawableArrayFocus = new Drawable[]{
            strokeFocus,
            topFocus,
            leftFocus,
            rightFocus,
            bottomFocus,
            bgFocus
    };

    private LayerDrawable layerdrawable, layerdrawablefocus;

    public MinecraftButton(Context ctx) {
        this(ctx, null);
    }

    public MinecraftButton(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initButtonSound(); //因为SoundPool需要预加载声音资源，否则会出现第一次按下按钮没有声音的问题
        init();
    }
    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layerdrawable = new LayerDrawable(DrawableArray);
                layerdrawable.setLayerInset(0, 0, 0, 0, 0); // stroke
                layerdrawable.setLayerInset(1, 4, 4, 4, 4); // top
                layerdrawable.setLayerInset(2, 4, 8, 4, getHeight() - 4); // topStroke
                layerdrawable.setLayerInset(3, 4, 8, getWidth() - 12,16); // left
                layerdrawable.setLayerInset(4, getWidth() - 12, 8, 4, 16); // right
                layerdrawable.setLayerInset(5, 4, getHeight() - 12,4,16);// bottom
                layerdrawable.setLayerInset(6, 8, 8, 8, 16); // bg

                layerdrawablefocus = new LayerDrawable(DrawableArrayFocus);
                layerdrawablefocus.setLayerInset(0, 0, 0, 0, 0); // stroke
                layerdrawablefocus.setLayerInset(1, 4, 4, 4, 4); // top
                layerdrawablefocus.setLayerInset(2, 4, 4, getWidth() - 4,4); // left
                layerdrawablefocus.setLayerInset(3, getWidth() - 4, 4, 4, 4); // right
                layerdrawablefocus.setLayerInset(4, 4, getHeight() - 4,4,4);// bottom
                layerdrawablefocus.setLayerInset(5, 8, 8, 8, 8); // bg

                setBackgroundDrawable(layerdrawable);
                setTextColor(Color.WHITE);
                setPadding(10, 10, 10, 10);
                //setOnTouchListener(null);
            }
        });
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/minecraft_seven.ttf"));

    }


    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        setBackgroundDrawable(enabled ? layerdrawable : layerdrawablefocus);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if ((event.getAction() == event.ACTION_UP) && !isUp && isEnabled()) {
            setBackgroundDrawable(layerdrawable);
            setTranslationY(0f); //松手时还原
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
            setTranslationY(10f); //按下时向下移动10dp，看起来更加灵动
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
            fileDescriptor = assetManager.openFd("sounds/Minecraft_Button_Green_Sound.ogg");
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
        MinecraftButtonSound2.release();
        super.onDetachedFromWindow();
    }

   /* private void MediaPlayer用法() {
        try {
            if (MinecraftButtonSound2 == null) {

                assetManager = getContext().getAssets();
                AssetFileDescriptor fileDescriptor = assetManager.openFd("sounds/Minecraft_Button_Green_Sound.ogg");
                MinecraftButtonSound2 = new MediaPlayer();
                MinecraftButtonSound2.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                MinecraftButtonSound2.prepare();
                MinecraftButtonSound2.start();
            } else if (MinecraftButtonSound2.isPlaying()) {
                MinecraftButtonSound2.stop();
                MinecraftButtonSound2.prepare();
                MinecraftButtonSound2.start();
            } else {
                MinecraftButtonSound2.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } */
}