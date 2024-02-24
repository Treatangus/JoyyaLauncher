package com.treatangus.joyya.bean;

public class LocalVersionListBean {

    private String Text;
    private boolean isSelected;

    public void setText(String text) {
        Text = text;
    }
    public String getText() {
        return Text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
