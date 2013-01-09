package com.dropdownview.sample;

import com.dropdownview.widget.DropDownView;
import com.dropdownview.widget.DropDownView.OnDropDownViewDismissListener;

import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button mOpenButton;
	DropDownView mDropDownView;
	
	enum DropDownViewStyle {
		DropDownViewStyleDefault,
		DropDownViewStylePlainTextInput,
		DropDownViewStyleSecureTextInput,
		DropDownViewStyleLoginPasswordInput;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setStyle(DropDownViewStyle.DropDownViewStyleSecureTextInput);
        mDropDownView.setOnDropDownViewDismissListener(mDropDownViewDismissListener);
        mOpenButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick( View v )
			{
				if(!mDropDownView.isShowing()) {
					mDropDownView.animateDismiss();
					mOpenButton.setText("Close");
				} else {
					mDropDownView.animateShow();
					mOpenButton.setText("Open");
				}
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
    public void onContentChanged()
    {
    	super.onContentChanged();
    	mOpenButton = (Button) findViewById(R.id.button_open);
    	mDropDownView = (DropDownView) findViewById(R.id.drawer);
    	setupDropDownViewSubviews();
    }
    
    public void setStyle(DropDownViewStyle style) {
    	switch (style) {
    	case DropDownViewStyleDefault:
    		defaultTextViewContainer.setVisibility(View.GONE);
    		loginAndPasswordContainer.setVisibility(View.GONE);
    		break;
    	case DropDownViewStylePlainTextInput:
    	case DropDownViewStyleSecureTextInput:
    		messageLabel.setVisibility(View.GONE);
    		loginAndPasswordContainer.setVisibility(View.GONE);
    		if (style == DropDownViewStyle.DropDownViewStyleSecureTextInput) {
    			defaultEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    		}
    		break;
    	case DropDownViewStyleLoginPasswordInput:
    		messageLabel.setVisibility(View.GONE);
    		defaultTextViewContainer.setVisibility(View.GONE);
    		break;
    	}
    }
    
    private OnDropDownViewDismissListener mDropDownViewDismissListener = new OnDropDownViewDismissListener() {
		@Override
		public void onDropDownViewDismissed() {
			// TODO Auto-generated method stub
			
		}
	};
    
    TextView titleLabel;
    TextView messageLabel;
    EditText defaultEditText;
    EditText loginEditText;
    EditText passwordEditText;
    LinearLayout defaultTextViewContainer;
    LinearLayout loginAndPasswordContainer;
    LinearLayout buttonContainer;
    public void setupDropDownViewSubviews() {
    	titleLabel = (TextView)findViewById(R.id.dropDownView_title);
    	messageLabel = (TextView)findViewById(R.id.dropDownView_message);
    	defaultEditText = (EditText)findViewById(R.id.dropDownView_defaultEditText);
    	loginEditText = (EditText)findViewById(R.id.dropDownView_loginEditText);
    	passwordEditText = (EditText)findViewById(R.id.dropDownView_passEditText);
    	defaultTextViewContainer = (LinearLayout)findViewById(R.id.dropDownView_defaultTextViewContainer);
    	loginAndPasswordContainer = (LinearLayout)findViewById(R.id.dropDownView_loginAndPasswordTextViewContainer);
    	buttonContainer = (LinearLayout)findViewById(R.id.dropDowView_buttonContainer);
    }
}
