package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

/**
 * Created by RAJ on 1/22/2018.
 */

public class MyTextWatcher implements TextWatcher {
    private Context context;
    private int i;

    public MyTextWatcher(Context context, int i) {
        this.context = context;
        this.i = i;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        MainActivity mainActivity = (MainActivity) this.context;
        mainActivity.item = mainActivity.getDataFromDB(userInput.toString());
        mainActivity.myAdapter = new ArrayAdapter<>(mainActivity, R.layout.list_item_name, mainActivity.item);
        mainActivity.myAdapter.notifyDataSetChanged();
        if (this.i == 1) {
            mainActivity.Source.setAdapter(mainActivity.myAdapter);
        } else {
            mainActivity.Dest.setAdapter(mainActivity.myAdapter);
        }
    }

    public void afterTextChanged(Editable s) {
    }
}
