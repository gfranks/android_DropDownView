package com.dropdownview.widget;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dropdownview.sample.R;
import com.dropdownview.widget.DropDownView.OnDropDownViewShowListener;
import com.dropdownview.widget.DropDownView.OnDropDownViewDismissListener;

import android.app.Activity;
import android.content.res.Resources;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
    TextView mErrorLabel;
    EditText mDefaultEditText;
    EditText mLoginEditText;
    EditText mPasswordEditText;
    FrameLayout mDefaultTextViewContainer;
    LinearLayout mLoginAndPasswordContainer;
    LinearLayout mButtonContainer;
    DropDownShowDismissListener showDismissListener;
    
    boolean mHasTimer;
    long mDurationForTimer;
    
    public DropDownViewHelper(){}
    
    public void initWithTitle(String title, Activity activity, int dropDownViewResId, 
    		DropDownViewStyle style, boolean withTimer, long durationInMilli) {
    	mActivity = activity;
    	setupSubViews(dropDownViewResId);
    	
    	setStyle(style);
    	setTitleLabelText(title);
    	
    	mHasTimer = withTimer;
    	mDurationForTimer = durationInMilli;
    }
    
    public void initWithTitleAndMessage(String title, String message, Activity activity, int dropDownViewResId,
    		boolean withTimer, long durationInMilli) {
    	mActivity = activity;
    	setupSubViews(dropDownViewResId);
    	
    	setStyle(DropDownViewStyle.Default);
    	setTitleLabelText(title);
    	setMessageLabelText(message);
    	
    	mHasTimer = withTimer;
    	mDurationForTimer = durationInMilli;
    }
    
    public void setupSubViews(int dropDownViewResId) {
    	mDropDownView = (DropDownView)mActivity.findViewById(dropDownViewResId);
    	mTitleLabel = (TextView)mActivity.findViewById(R.id.dropDownView_title);
    	mMessageLabel = (TextView)mActivity.findViewById(R.id.dropDownView_message);
    	mErrorLabel = (TextView)mActivity.findViewById(R.id.dropDowView_errorTextView);
    	mDefaultEditText = (EditText)mActivity.findViewById(R.id.dropDownView_defaultEditText);
    	mLoginEditText = (EditText)mActivity.findViewById(R.id.dropDownView_loginEditText);
    	mPasswordEditText = (EditText)mActivity.findViewById(R.id.dropDownView_passEditText);
    	mDefaultTextViewContainer = (FrameLayout)mActivity.findViewById(R.id.dropDownView_defaultEditTextContainer);
    	mLoginAndPasswordContainer = (LinearLayout)mActivity.findViewById(R.id.dropDownView_loginAndPasswordEditTextContainer);
    	mButtonContainer = (LinearLayout)mActivity.findViewById(R.id.dropDowView_buttonContainer);
    	
    	((Button)mActivity.findViewById(R.id.dropDownView_defaultEditText_clear))
    		.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDefaultEditText.setText("");
				}
    	});
    	((Button)mActivity.findViewById(R.id.dropDownView_loginEditText_clear))
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mLoginEditText.setText("");
				}
		});
    	((Button)mActivity.findViewById(R.id.dropDownView_passEditText_clear))
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mPasswordEditText.setText("");
				}
		});
    }
    
    public void showDropDownView() {
    	mDropDownView.setOnDropDownViewShowListener(mOnDropDownShowListener);
    	mDropDownView.setOnDropDownViewDismissListener(mOnDropDownDismissListener);
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
    
    public void setErrorMessageWithVisibility(int visibility, String message) {
    	mErrorLabel.setVisibility(visibility);
    	mErrorLabel.setText(message);
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
    
    public boolean isValidEmailAddress() {
    	switch (mStyle) {
    	case PlainTextInput:
    	case SecureTextInput:
    		return isValidEmail(mDefaultEditText.getText().toString());
    	case LoginPasswordInput:
    		return isValidEmail(mLoginEditText.getText().toString());
    	case Default:
    		break;
    	}
    	
    	return false;
    }
    
    public boolean isValidEmail(String value){
		Pattern p = Pattern.compile("^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$");
		Matcher m = p.matcher(value);
		return m.find();
	}
    
    public void setButtons(String[] buttonTitles) {
    	for (int i=0; i<buttonTitles.length; i++) {
    		if (i < 3) {
    			Button btn = new Button(mActivity);
    			btn.setText(buttonTitles[i]);
    			btn.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
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
    			params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
    					35, r.getDisplayMetrics());
    			mButtonContainer.addView(btn, params);
    			btn.setGravity(Gravity.CENTER);
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
    
    public void setOnClickListenerForDismissal(OnClickListener listener) {
    	mDropDownView.setOnClickListener(listener);
    }
    
    public void setDropDownShowOrHideListener(DropDownShowDismissListener listener) {
    	showDismissListener = listener;
    }
    
    public boolean isDropDownShowing() {
    	return mDropDownView.isShowing();
    }
    
	private int calculateButtonWidth(int buttonCount) {
		DisplayMetrics metrics = mActivity.getBaseContext().getResources().getDisplayMetrics();
    	
    	int width =  metrics.widthPixels; 
    	
    	return width/6;
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
	
	private OnDropDownViewDismissListener mOnDropDownDismissListener = new OnDropDownViewDismissListener() {	
		@Override
		public void onDropDownViewDismissed() {
			if (showDismissListener != null) {
				showDismissListener.didShowOrDismiss(false, true);
			}
		}
	};
    
    public void setDropDownButtonClickedListener(DropDownButtonClickedListener dropDownButtonClickedListener) {
    	this.mDropDownButtonClickedListener = dropDownButtonClickedListener;
    }
    
    public static interface DropDownShowDismissListener {
    	public void didShowOrDismiss(boolean didShow, boolean didDismiss);
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
