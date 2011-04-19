package com.killerappz.android.lwp.xmen.preferences;

import com.killerappz.android.lwp.xmen.Constants;
import com.killerappz.android.lwp.xmen.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.preference.DialogPreference;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Class implementing a sliding seek bar. Used for selecting the rotation speed
 * Roughly inspired from the code at http://www.codeproject.com/KB/android/seekbar_preference.aspx
 * @author florin
 *
 */
public final class SeekBarPreference extends DialogPreference implements OnSeekBarChangeListener {

	private final int mDefaultValue;
	private final int mMaxValue;
	private final int mMinValue;

	// Current value
	private int mCurrentValue;

	// View elements
	private SeekBar mSeekBar;
	private TextView mValueText;

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Read parameters from attributes
		mMinValue = 0;
		// TODO bug; actually the calls below will always fall back to the values from Constants. need to investigate why.
		mMaxValue = attrs.getAttributeIntValue(Constants.ANDROID_NS, Constants.ATTR_MAX_VALUE, Constants.ROTATION_MAX_SPEED);
		mDefaultValue = attrs.getAttributeIntValue(Constants.ANDROID_NS, Constants.ATTR_DEFAULT_VALUE, Constants.DEFAULT_ROTATION_SPEED);
	}

	@Override
	protected View onCreateDialogView() {
		// Get current value from preferences
		mCurrentValue = getPersistedInt(mDefaultValue);

		// Inflate layout
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_slider, null);

		// Setup minimum and maximum text labels
		((TextView) view.findViewById(R.id.min_value)).setText(Integer.toString(mMinValue));
		((TextView) view.findViewById(R.id.max_value)).setText(Integer.toString(mMaxValue));

		// Setup SeekBar
		mSeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
		mSeekBar.setMax(mMaxValue - mMinValue);
		mSeekBar.setProgress(mCurrentValue - mMinValue);
		mSeekBar.setOnSeekBarChangeListener(this);

		// Setup text label for current value
		mValueText = (TextView) view.findViewById(R.id.current_value);
		mValueText.setText(Integer.toString(mCurrentValue));

		return view;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		// Return if change was cancelled
		if (!positiveResult) {
			return;
		}

		// Persist current value if needed
		if (shouldPersist()) {
			persistInt(mCurrentValue);
		}

		// Notify activity about changes (to update preference summary line)
		notifyChanged();
	}

	@Override
	public CharSequence getSummary() {
		// Format summary string with current value
		String summary = super.getSummary().toString();
		int value = getPersistedInt(mCurrentValue);
		return String.format(summary, value * Constants.ROTATION_SPEED_UNIT);
	}

	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
		// Update current value
		mCurrentValue = value + mMinValue;
		// Update label with current value
		mValueText.setText(Integer.toString(mCurrentValue));
	}

	public void onStartTrackingTouch(SeekBar seek) {
		// Not used
	}

	public void onStopTrackingTouch(SeekBar seek) {
		// Not used
	}
}