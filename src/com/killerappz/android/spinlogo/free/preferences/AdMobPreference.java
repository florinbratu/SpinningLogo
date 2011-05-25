package com.killerappz.android.spinlogo.free.preferences;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.killerappz.android.spinlogo.free.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Ads page in the preferences screen
 * 
 * @author florin
 *
 */
public class AdMobPreference extends Preference
{

    public AdMobPreference(Context context) {
        super(context, null);
    }

    public AdMobPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ad, null);
        
        // load that fwking ad!
        AdView adView = (AdView) view.findViewById(R.id.ad);
        adView.loadAd(new AdRequest());
        
        return view;
    }

}