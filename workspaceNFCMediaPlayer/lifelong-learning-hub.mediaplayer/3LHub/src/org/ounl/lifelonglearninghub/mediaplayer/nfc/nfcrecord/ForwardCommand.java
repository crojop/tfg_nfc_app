/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.ounl.lifelonglearninghub.mediaplayer.nfc.nfcrecord;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;

public class ForwardCommand implements IParsedNdefCommand {

	public static final String RECORD_TYPE = IParsedNdefCommand.COMMAND_FORWARD;

	private final String mId;

	private final String mColor;

	public ForwardCommand(String id) {
		this.mId = Preconditions.checkNotNull(id);
		this.mColor = Preconditions.checkNotNull(id);
	}

	public View getView(Activity activity, LayoutInflater inflater,
			ViewGroup parent, int offset) {
		// Commented by btb
		// TextView text = (TextView) inflater.inflate(R.layout.tag_text,
		// parent, false);
		// text.setText(mId.toString());

		TextView text = null;

		return text;
	}

	public String getId() {
		return mId;
	}

	public String getColor() {
		return mColor;
	}

	@Override
	public String getCommand() {
		return RECORD_TYPE;
	}
}