package com.treatangus.joyya.mcgui.button;

import android.content.*;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.*;
import android.view.*;

import com.treatangus.joyya.R;

import java.io.IOException;

public class MinecraftButton extends androidx.appcompat.widget.AppCompatButton {
    public SwitchButtonStyle switchButtonStyle;
    private String[] minecrftButtonColor = new String[13];

    private ColorDrawable stroke;
    private ColorDrawable shadow;
    private ColorDrawable bgNormal;
    private ColorDrawable left;
    private ColorDrawable top;
    private ColorDrawable right;
    private ColorDrawable bottom;
    private ColorDrawable strokeFocus;
    private ColorDrawable bgFocus;
    private ColorDrawable leftFocus;
    private ColorDrawable topFocus;
    private ColorDrawable rightFocus;
    private ColorDrawable bottomFocus;

    private AssetManager assetManager;
    private SoundPool MinecraftButtonSound;
    private AssetFileDescriptor fileDescriptor;
    private int soundId;
    private int streamId;
    private boolean isUp = true;

    private Drawable[] DrawableArray = new Drawable[7];
    private Drawable[] DrawableArrayFocus = new Drawable[6];
    private LayerDrawable layerdrawable, layerdrawablefocus;

    public MinecraftButton(Context ctx) {
        this(ctx, null);
    }

    public MinecraftButton(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initAttribute(ctx,attrs);
        SwitchStyle();
        initButtonSound(); //因为SoundPool需要预加载声音资源，否则会出现第一次按下按钮没有声音的问题
        init();
    }

    private void initAttribute(Context context,AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MinecraftButton);
        //mSwitchButtonStyle = getSwitchButtonStyle(typedArray.getInt(R.styleable.MinecraftButton_MinecraftButtonStyle,0));
        switchButtonStyle = SwitchButtonStyle.values()[typedArray.getInt(R.styleable.MinecraftButton_MinecraftButtonStyle, 0)];
        typedArray.recycle();
    }

    public enum SwitchButtonStyle {
        GREEN,
        GRAY
    }

    /**
     * Switch Style
     */
    private void SwitchStyle() {
        switch (switchButtonStyle) {
            case GREEN:
               GREENSTYLE();
                break;
            case GRAY:
               GRAYSTYLE();
                break;
        }
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
                if (switchButtonStyle == SwitchButtonStyle.GRAY) {
                    setTextColor(Color.BLACK);
                } else {
                    setTextColor(Color.WHITE);
                }
                setPadding(10, 10, 10, 10);
            }
        });
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/minecraft_seven.ttf"));

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
            if (switchButtonStyle == SwitchButtonStyle.GREEN) {
                fileDescriptor = assetManager.openFd("sounds/button.ogg");
            } else {
                fileDescriptor = assetManager.openFd("sounds/click.ogg");
            }
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

    /**
     * Setting GREEN Style
     */
    private void GREENSTYLE() {

        minecrftButtonColor[0] = "#1e1e1e";
        minecrftButtonColor[1] = "#1d4d12";
        minecrftButtonColor[2] = "#3c8526";
        minecrftButtonColor[3] = "#33ffffff";
        minecrftButtonColor[4] = "#33ffffff";
        minecrftButtonColor[5] = "#19ffffff";
        minecrftButtonColor[6] = "#19ffffff";
        minecrftButtonColor[7] = "#1e1e1e";
        minecrftButtonColor[8] = "#1d4d13";
        minecrftButtonColor[9] = "#4cffffff";
        minecrftButtonColor[10] = "#4cffffff";
        minecrftButtonColor[11] = "#33ffffff";
        minecrftButtonColor[12] = "#33ffffff";

        stroke = new ColorDrawable(Color.parseColor(minecrftButtonColor[0]));
        shadow = new ColorDrawable(Color.parseColor(minecrftButtonColor[1]));
        bgNormal = new ColorDrawable(Color.parseColor(minecrftButtonColor[2]));
        left = new ColorDrawable(Color.parseColor(minecrftButtonColor[3]));
        top = new ColorDrawable(Color.parseColor(minecrftButtonColor[4]));
        right = new ColorDrawable(Color.parseColor(minecrftButtonColor[5]));
        bottom = new ColorDrawable(Color.parseColor(minecrftButtonColor[6]));
        strokeFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[7]));
        bgFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[8]));
        leftFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[9]));
        topFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[10]));
        rightFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[11]));
        bottomFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[12]));

        DrawableArray[0] = stroke;
        DrawableArray[1] = shadow;
        DrawableArray[2] = bgNormal;
        DrawableArray[3] = left;
        DrawableArray[4] = top;
        DrawableArray[5] = right;
        DrawableArray[6] = bottom;
        DrawableArrayFocus[0] = strokeFocus;
        DrawableArrayFocus[1] = bgFocus;
        DrawableArrayFocus[2] = leftFocus;
        DrawableArrayFocus[3] = topFocus;
        DrawableArrayFocus[4] = rightFocus;
        DrawableArrayFocus[5] = bottomFocus;
    }

    /**
     * Setting GRAY Style
     */
    private void GRAYSTYLE() {

        minecrftButtonColor[0] = "#1e1e1e";
        minecrftButtonColor[1] = "#58585a";
        minecrftButtonColor[2] = "#d0d1d5";
        minecrftButtonColor[3] = "#b2ffffff";
        minecrftButtonColor[4] = "#b2ffffff";
        minecrftButtonColor[5] = "#a5ffffff";
        minecrftButtonColor[6] = "#a5ffffff";
        minecrftButtonColor[7] = "#1e1e1e";
        minecrftButtonColor[8] = "#b1b2b5";
        minecrftButtonColor[9] = "#b2ffffff";
        minecrftButtonColor[10] = "#b2ffffff";
        minecrftButtonColor[11] = "#a5ffffff";
        minecrftButtonColor[12] = "#a5ffffff";

        stroke = new ColorDrawable(Color.parseColor(minecrftButtonColor[0]));
        shadow = new ColorDrawable(Color.parseColor(minecrftButtonColor[1]));
        bgNormal = new ColorDrawable(Color.parseColor(minecrftButtonColor[2]));
        left = new ColorDrawable(Color.parseColor(minecrftButtonColor[3]));
        top = new ColorDrawable(Color.parseColor(minecrftButtonColor[4]));
        right = new ColorDrawable(Color.parseColor(minecrftButtonColor[5]));
        bottom = new ColorDrawable(Color.parseColor(minecrftButtonColor[6]));
        strokeFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[7]));
        bgFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[8]));
        leftFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[9]));
        topFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[10]));
        rightFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[11]));
        bottomFocus = new ColorDrawable(Color.parseColor(minecrftButtonColor[12]));

        DrawableArray[0] = stroke;
        DrawableArray[1] = shadow;
        DrawableArray[2] = bgNormal;
        DrawableArray[3] = left;
        DrawableArray[4] = top;
        DrawableArray[5] = right;
        DrawableArray[6] = bottom;
        DrawableArrayFocus[0] = strokeFocus;
        DrawableArrayFocus[1] = bgFocus;
        DrawableArrayFocus[2] = leftFocus;
        DrawableArrayFocus[3] = topFocus;
        DrawableArrayFocus[4] = rightFocus;
        DrawableArrayFocus[5] = bottomFocus;
    }
}
