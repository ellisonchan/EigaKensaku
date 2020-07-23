package com.ellison.eigakensaku.ui.touch;

public interface ITouchDataSync {
    void onItemRemoved(int pos);
    void onItemSwap(int before, int after);
}