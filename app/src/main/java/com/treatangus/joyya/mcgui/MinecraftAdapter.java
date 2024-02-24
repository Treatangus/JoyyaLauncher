package com.treatangus.joyya.mcgui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.treatangus.joyya.R;

import java.util.ArrayList;
import java.util.List;

public class MinecraftAdapter extends BaseAdapter {
    private Context context;
    private List<String> items;
    private int selectedItemPosition = -1; // 初始化为-1，表示没有选中项

    public MinecraftAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;

    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        setSelectedItemPosition(position);
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_minecraft, parent, false);
        }

        AppCompatTextView textView = convertView.findViewById(R.id.tv_version);
        AppCompatImageView imageView = convertView.findViewById(R.id.iv_check);

        textView.setText(items.get(position));

        // 根据选中状态设置图标的可见性
        if (position == selectedItemPosition) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    // 设置选中的项目位置
    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged(); // 通知数据已更改
    }
}
