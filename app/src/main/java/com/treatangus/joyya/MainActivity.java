package com.treatangus.joyya;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.treatangus.joyya.databinding.ActivityMainBinding;
import com.treatangus.joyya.mcgui.MinecraftButtonGreen;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MinecraftButtonGreen start;
    private Timer mTimer;
    private TimerTask systemUiTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏系统状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        //窗口
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                hideSystemUI(getWindow().getDecorView());
                mTimer = new Timer();
            }
        });

    }
    private void initUI() {
        setSupportActionBar(binding.toolbar);
        start = binding.start;
    }

        @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(null);
            hideSystemUI(decorView);
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(null);
            if (systemUiTimerTask != null) systemUiTimerTask.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void hideSystemUI(View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}