package com.iriartesoft.dfgi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class donotforgetitActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button currentlistbutton = (Button)findViewById(R.id.b_currentlist);
        currentlistbutton.setOnClickListener(new OnClickListener() {
        	 
        	public void onClick(View v) {
        		Intent CurrentListIntent = new Intent(donotforgetitActivity.this, CurrentList.class);
        		startActivity(CurrentListIntent);
        	}
        });
        
        Button newlistbutton = (Button)findViewById(R.id.b_newlist);
        newlistbutton.setOnClickListener(new OnClickListener() {
        	 
        	public void onClick(View v) {
        		Intent NewListIntent = new Intent(donotforgetitActivity.this, NewList.class);
        		startActivity(NewListIntent);
        	}
        });
                        
        
    }
}