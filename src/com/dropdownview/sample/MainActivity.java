package com.dropdownview.sample;

import com.dropdownview.widget.DropDownViewHelper;
import com.dropdownview.widget.DropDownViewHelper.DropDownButtonClickedListener;
import com.dropdownview.widget.DropDownViewHelper.DropDownShowDismissListener;
import com.dropdownview.widget.DropDownViewHelper.DropDownViewStyle;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button mOpenButton;
	DropDownViewHelper mDropDownViewHelper;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mOpenButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				mDropDownViewHelper.showDropDownView();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onContentChanged() {
    	super.onContentChanged();
    	mOpenButton = (Button) findViewById(R.id.button_open);
    	setupDropDownView();
    }
    
    public void setupDropDownView() {
    	mDropDownViewHelper = new DropDownViewHelper();
		mDropDownViewHelper.initWithTitle("Enter Email", this, R.id.drawer, 
				DropDownViewStyle.PlainTextInput, false, 0);
		
		String[] buttonTitles = {"Dismiss", "Submit"};
		mDropDownViewHelper.setButtons(buttonTitles);
		mDropDownViewHelper.getEditTextAtIndex(0).setHint("Enter email address");
		mDropDownViewHelper.setOnClickListenerForDismissal();
		mDropDownViewHelper.setDropDownButtonClickedListener(new DropDownButtonClickedListener() {
			@Override
			public void clickedButtonAtIndex(int index) {
				Log.e("Clicked button!!!!", "At Index: " + index);
				if (index == 1 && mDropDownViewHelper.isValidEmailAddress()) {
					mDropDownViewHelper.setErrorMessageWithVisibility(View.GONE, null);
					mDropDownViewHelper.dismissDropDownView();
				} else if (index == 0) {
					mDropDownViewHelper.dismissDropDownView();
				} else {
					mDropDownViewHelper.setErrorMessageWithVisibility(View.VISIBLE,
							"Please enter a valid email address");
				}
			}
		});
		mDropDownViewHelper.setDropDownShowOrHideListener(new DropDownShowDismissListener() {
			@Override
			public void didShowOrDismiss(boolean didShow, boolean didDismiss) {
				if (didDismiss) {
					
				}
			}
		});
    }
}
