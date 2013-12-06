package com.deftech.viewtils.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public final class SimpleActivity extends Activity {
    private boolean viewClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }
    
    public void viewClicked(View view){
        viewClicked = true;
    }

    public boolean isViewClicked() { return viewClicked; }
}
