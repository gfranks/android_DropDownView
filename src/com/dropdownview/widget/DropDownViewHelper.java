package com.dropdownview.widget;

import com.dropdownview.sample.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DropDownViewHelper {
	
	public static enum DropDownViewStyle {
		Default,
		PlainTextInput,
		SecureTextInput,
		LoginPasswordInput;
	}
	
	DropDownView mDropDownView;
	DropDownViewStyle mStyle;
	Activity mActivity;
	DropDownButtonClickedListener mDropDownButtonClickedListener;
	
	TextView mTitleLabel;
    TextView mMessageLabel;
    EditText mDefaultEditText;
    EditText mLoginEditText;
    EditText mPasswordEditText;
    LinearLayout mDefaultTextViewContainer;
    LinearLayout mLoginAndPasswordContainer;
    LinearLayout mButtonContainer;
    
    public DropDownViewHelper(){}
    
    public void initWithTitle(String title, Activity activity, int dropDownViewResId, DropDownViewStyle style) {
    	mActivity = activity;
    	mDropDownView = (DropDownView)activity.findViewById(dropDownViewResId);
    	mTitleLabel = (TextView)activity.findViewById(R.id.dropDownView_title);
    	mMessageLabel = (TextView)activity.findViewById(R.id.dropDownView_message);
    	mDefaultEditText = (EditText)activity.findViewById(R.id.dropDownView_defaultEditText);
    	mLoginEditText = (EditText)activity.findViewById(R.id.dropDownView_loginEditText);
    	mPasswordEditText = (EditText)activity.findViewById(R.id.dropDownView_passEditText);
    	mDefaultTextViewContainer = (LinearLayout)activity.findViewById(R.id.dropDownView_defaultEditTextContainer);
    	mLoginAndPasswordContainer = (LinearLayout)activity.findViewById(R.id.dropDownView_loginAndPasswordEditTextContainer);
    	mButtonContainer = (LinearLayout)activity.findViewById(R.id.dropDowView_buttonContainer);
    	
    	setStyle(style);
    	setTitleLabelText(title);
    }
    
    public void initWithTitleAndMessage(String title, String message, Activity activity, int dropDownViewResId) {
    	mActivity = activity;
    	mDropDownView = (DropDownView)activity.findViewById(dropDownViewResId);
    	mTitleLabel = (TextView)activity.findViewById(R.id.dropDownView_title);
    	mMessageLabel = (TextView)activity.findViewById(R.id.dropDownView_message);
    	mDefaultEditText = (EditText)activity.findViewById(R.id.dropDownView_defaultEditText);
    	mLoginEditText = (EditText)activity.findViewById(R.id.dropDownView_loginEditText);
    	mPasswordEditText = (EditText)activity.findViewById(R.id.dropDownView_passEditText);
    	mDefaultTextViewContainer = (LinearLayout)activity.findViewById(R.id.dropDownView_defaultEditTextContainer);
    	mLoginAndPasswordContainer = (LinearLayout)activity.findViewById(R.id.dropDownView_loginAndPasswordEditTextContainer);
    	mButtonContainer = (LinearLayout)activity.findViewById(R.id.dropDowView_buttonContainer);
    	
    	setStyle(DropDownViewStyle.Default);
    	setTitleLabelText(title);
    	setMessageLabelText(message);
    }
    
    public void showDropDown() {
    	mDropDownView.animateShow();
    }
    
    public void dismissDropDown() {
    	mDropDownView.animateDismiss();
    }
    
    public void setStyle(DropDownViewStyle style) {
    	mStyle = style;
    	
    	switch (mStyle) {
    	case Default:
    		mDefaultTextViewContainer.setVisibility(View.GONE);
    		mLoginAndPasswordContainer.setVisibility(View.GONE);
    		break;
    	case PlainTextInput:
    	case SecureTextInput:
    		mMessageLabel.setVisibility(View.GONE);
    		mLoginAndPasswordContainer.setVisibility(View.GONE);
    		if (style == DropDownViewStyle.SecureTextInput) {
    			mDefaultEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    		}
    		break;
    	case LoginPasswordInput:
    		mMessageLabel.setVisibility(View.GONE);
    		mDefaultTextViewContainer.setVisibility(View.GONE);
    		break;
    	}
    }
    
    public void setTitleLabelText(String title) {
    	mTitleLabel.setText(title);
    }
    
    public void setMessageLabelText(String message) {
    	mMessageLabel.setText(message);
    }
    
    public EditText getEditTextAtIndex(int index) {
    	EditText editText = null;
    	switch (mStyle) {
    	case PlainTextInput:
    	case SecureTextInput:
    		if (index == 0) {
    			editText = mDefaultEditText;
    		}
    		break;
    	case LoginPasswordInput:
    		if (index == 0) {
    			editText = mLoginEditText;
    		}
    		if (index == 1) {
    			editText = mPasswordEditText;
    		}
    		break;
    	case Default:
    		break;
    	}
    	
    	return editText;
    }
    
    public void setButtons(String[] buttonTitles) {
    	for (int i=0; i<buttonTitles.length; i++) {
    		if (i < 3) {
    			Button btn = new Button(mActivity);
    			btn.setText(buttonTitles[i]);
    			btn.setTag(i);
    			btn.setOnClickListener(mButtonClickListener);
    			
    			Resources r = mActivity.getResources();
    			int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
    					calculateButtonWidth(buttonTitles.length), r.getDisplayMetrics());
    			mButtonContainer.addView(btn, new LayoutParams(
    					px, LayoutParams.WRAP_CONTENT));
    		}
    	}
    }
    
    public boolean isDropDownShowing() {
    	return mDropDownView.isShowing();
    }
    
	@SuppressLint("NewApi") 
    @SuppressWarnings("deprecation")
    private int calculateButtonWidth(int buttonCount) {
    	Display display = mActivity.getWindowManager().getDefaultDisplay();
    	
    	int width = 0;
    	try { 
    		Point size = new Point();
    		display.getSize(size); 
    		width = size.x; 
    	} catch (NoSuchMethodError e) { 
    		width = display.getWidth();
    	}
    	
    	return width/5;
    }
	
	private OnClickListener mButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mDropDownButtonClickedListener != null) {
				mDropDownButtonClickedListener.clickedButtonAtIndex((Integer) v.getTag());
			}
		}
    };
    
    public void setDropDownButtonClickedListener(DropDownButtonClickedListener dropDownButtonClickedListener) {
    	this.mDropDownButtonClickedListener = dropDownButtonClickedListener;
    }
    
    public static interface DropDownButtonClickedListener {
    	public void clickedButtonAtIndex(int index);
    }
}
