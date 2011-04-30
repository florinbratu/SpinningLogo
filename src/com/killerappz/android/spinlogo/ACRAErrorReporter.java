package com.killerappz.android.spinlogo;

import org.acra.ErrorReporter;

/**
 * For collecting user feedback I am (ab)using ACRA.
 * Unfortunately:
 * 	- acra latest release 3.1.2 is distributed as a jar
 *  - couldnt find any sources for 3.1.2
 *  SO - I resort to this hack to adapt ACRA's ErrorReporter to my needs
 * @author florin
 *
 */
public class ACRAErrorReporter extends ErrorReporter {

	private boolean includeLogcat = false;
	private String userComment;
	
	public void addUserComment(String comment) {
		
	}

	public void includeLogcat(boolean includeLogcat) {
		
	}

}
