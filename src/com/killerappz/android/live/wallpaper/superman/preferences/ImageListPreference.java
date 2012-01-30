package com.killerappz.android.live.wallpaper.superman.preferences;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.killerappz.android.live.wallpaper.superman.R;

/**
 * This is a Preference widget offering 
 * a List selection of Image items
 * along with textual descriptions
 * 
 * @author florin, adapted from Casper Wakkers
 * @see http://www.cmwmobile.com/index.php?option=com_content&view=article&id=4&Itemid=12
 *
 */
public class ImageListPreference extends ListPreference {
	private final int[] resourceIds;
	// the default selected value. I'm cheating, it is stored in the summary var
	private final String defaultValue;
	
	/**
	 * Constructor of the ImageListPreference. Initializes the custom images.
	 * @param context application context.
	 * @param attrs custom xml attributes.
	 */
	public ImageListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		defaultValue = getContext().getString(R.string.logo_texture_defaultValue);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
			R.styleable.ImageListPreference);

		String[] imagePaths = context.getResources().getStringArray(
			typedArray.getResourceId(typedArray.getIndexCount()-1, -1));

		resourceIds = new int[imagePaths.length];

		for (int i=0;i<imagePaths.length;i++) {
			String imageName = imagePaths[i].substring(
				imagePaths[i].indexOf('/') + 1,
				imagePaths[i].lastIndexOf('.'));

			resourceIds[i] = context.getResources().getIdentifier(imageName,
				null, context.getPackageName());
		}

		typedArray.recycle();
	}
	/**
	 * {@inheritDoc}
	 */
	protected void onPrepareDialogBuilder(Builder builder) {
		int index = findIndexOfValue(getSharedPreferences().
				getString(getKey(), defaultValue));

		ListAdapter listAdapter = new ImageArrayAdapter(getContext(),
			R.layout.listitem, getEntries(), resourceIds, index);

		// Order matters.
		builder.setAdapter(listAdapter, this);
		super.onPrepareDialogBuilder(builder);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		// Return if change was cancelled
		if (!positiveResult) {
			return;
		}

		// Notify activity about changes (to update preference summary line)
		notifyChanged();
	}
	
	@Override
	public CharSequence getSummary() {
		int index = findIndexOfValue(getSharedPreferences().getString(
				getKey(), defaultValue));
		return getEntries()[index];
	}
	
	/**
	 * The ImageArrayAdapter is the array adapter used for displaying an additional
	 * image to a list preference item.
	 * The ImageListPreference clas was just plumbing.
	 * It is here where the heavy lifting is performed
	 */
	class ImageArrayAdapter extends ArrayAdapter<CharSequence> {
		private int index = 0;
		private int[] resourceIds = null;

		/**
		 * ImageArrayAdapter constructor.
		 * @param context the context.
		 * @param textViewResourceId resource id of the text view.
		 * @param objects to be displayed.
		 * @param ids resource id of the images to be displayed.
		 * @param i index of the previous selected item.
		 */
		public ImageArrayAdapter(Context context, int textViewResourceId,
				CharSequence[] objects, int[] ids, int i) {
			super(context, textViewResourceId, objects);

			index = i;
			resourceIds = ids;
		}
		/**
		 * {@inheritDoc}
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.listitem, parent, false);

			ImageView imageView = (ImageView)row.findViewById(R.id.image);
			imageView.setImageResource(resourceIds[position]);

			CheckedTextView checkedTextView = (CheckedTextView)row.findViewById(
				R.id.check);

			checkedTextView.setText(getItem(position));

			if (position == index) {
				checkedTextView.setChecked(true);
			}

			return row;
		}
	}
}
