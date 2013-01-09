package com.dropdownview.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.dropdownview.sample.R;
import com.dropdownview.widget.DropDownView.OnDropDownViewShowListener;

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
import android.widget.FrameLayout;
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
    
    boolean mHasTimer;
    long mDurationForTimer;
    
    public DropDownViewHelper(){}
    
    public void initWithTitle(String title, Activity activity, int dropDownViewResId, 
    		DropDownViewStyle style, boolean withTimer, long durationInMilli) {
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
    	
    	mHasTimer = withTimer;
    	mDurationForTimer = durationInMilli;
    }
    
    public void initWithTitleAndMessage(String title, String message, Activity activity, int dropDownViewResId,
    		boolean withTimer, long durationInMilli) {
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
    	
    	mHasTimer = withTimer;
    	mDurationForTimer = durationInMilli;
    }
    
    public void showDropDownView() {
    	mDropDownView.setOnDropDownViewShowListener(mOnDropDownShowListener);
    	mDropDownView.animateShow();
    }
    
    public void dismissDropDownView() {
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
    			
    			btn.setBackgroundResource(R.drawable.btn_blue);
    			btn.setTextColor(mActivity.getResources().getColor(android.R.color.white));
    			btn.setOnClickListener(mButtonClickListener);
    			
    			Resources r = mActivity.getResources();
    			int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
    					calculateButtonWidth(buttonTitles.length), r.getDisplayMetrics());
    			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, LinearLayout.LayoutParams.WRAP_CONTENT);
    			params.leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
    					5, r.getDisplayMetrics());
    			params.rightMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
    					5, r.getDisplayMetrics());
    			mButtonContainer.addView(btn, params);
    		}
    	}
    }
    
    public void setOnClickListenerForDismissal() {
    	mDropDownView.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			dismissDropDownView();
    		}
    	});
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
    
    private OnDropDownViewShowListener mOnDropDownShowListener = new OnDropDownViewShowListener() {
		@Override
		public void onDropDownViewShown() {
			if (mHasTimer) {
				Timer timer = new Timer();
				DropDownViewTimerTask dropDownTimer = new DropDownViewTimerTask();
				timer.scheduleAtFixedRate(dropDownTimer, 0, mDurationForTimer);
			}
		}
	};
    
    public void setDropDownButtonClickedListener(DropDownButtonClickedListener dropDownButtonClickedListener) {
    	this.mDropDownButtonClickedListener = dropDownButtonClickedListener;
    }
    
    public static interface DropDownButtonClickedListener {
    	public void clickedButtonAtIndex(int index);
    }
    
    class DropDownViewTimerTask extends TimerTask {
    	public void run() {
    		dismissDropDownView();
    	}
    }
}
