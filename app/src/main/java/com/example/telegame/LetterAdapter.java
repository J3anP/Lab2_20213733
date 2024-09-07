package com.example.telegame;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public class LetterAdapter extends BaseAdapter {
    private String[]letters;
    private LayoutInflater letterInf;

    public LetterAdapter(Context context){
        letters = new String[26];
        for(int i=0; i < letters.length;i++){
            letters[i]=""+(char)(i+'A');
        }
        letterInf=LayoutInflater.from(context);
    }
    public int getCount(){
        return letters.length;
    }
    @Override
    public Object getItem(int position){
        return null;
    }
    @Override
    public long getItemId(int position){
        return 0;
    }
}
