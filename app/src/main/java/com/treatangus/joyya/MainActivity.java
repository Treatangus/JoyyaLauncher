package com.treatangus.joyya;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.ListPopupWindow;

import com.treatangus.joyya.databinding.ActivityMainBinding;
import com.treatangus.joyya.mcgui.MinecraftAdapter;
import com.treatangus.joyya.mcgui.MinecraftSpinner;
import com.treatangus.joyya.mcgui.button.MinecraftButton;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MinecraftButton start;
    private MinecraftSpinner msp_version;
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
        msp_version = binding.mspVersion;

    }
    public void onClick(View v) {
        showListPopulWindow();
    }

    private void showListPopulWindow() {
        //String[] item = getResources().getStringArray(R.array.minecraft_spinner);

        List<String> itemList = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4");
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(this);

       //listPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明解决白色边框问题
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.adapter_minecraft,R.id.tv_version, item);

        listPopupWindow.setAdapter(adapter);//用android内置布局，或设计自己的样式)*/
        listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_minecraft_black));//设置背景
        MinecraftAdapter adapter = new MinecraftAdapter(this, itemList);
        listPopupWindow.setAdapter(adapter);//用android内置布局，或设计自己的样式)*/
        listPopupWindow.setAnchorView(msp_version);//以哪个控件为基准
        listPopupWindow.setVerticalOffset(-101);//相对控件竖轴偏移量
        listPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!((String)adapterView.getItemAtPosition(i)).equals("null")) {
                    //setVersion((String)adapterView.getItemAtPosition(i));
                    msp_version.setText(((String)adapterView.getItemAtPosition(i)));
                    listPopupWindow.dismiss();
                }
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
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