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
package org.ounl.lifelonglearninghub.nfclearntracker.nfcrecord;

import java.nio.charset.Charset;

import org.ounl.lifelonglearninghub.nfclearntracker.R;

import android.app.Activity;
import android.nfc.NdefRecord;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;

public class MimeMediaRecord implements IParsedNdefRecord {

	public static final String RECORD_TYPE = "MimeMediaRecord";

	private final String mId;
	
	private final String mColor;

	private MimeMediaRecord(String id) {
		this.mId = Preconditions.checkNotNull(id);
		this.mColor = Preconditions.checkNotNull(id);
	}

	public View getView(Activity activity, LayoutInflater inflater,
			ViewGroup parent, int offset) {
		TextView text = (TextView) inflater.inflate(R.layout.tag_text, parent,
				false);
		text.setText(mId.toString());
		return text;
	}

	/**
	 * Setting a name with Trigger Tasker should have the following payload
	 * enZ:1:3lhub#COLORINHEX#COLORNAME;a:http&#58/www.a.es:0
	 * 
	 * Text within separator # will be taken as id 
	 * 
	 * COLORINHEX
	 * 
	 */
	public static MimeMediaRecord parse(NdefRecord record) {


		String sPayload = new String(record.getPayload(),
				Charset.forName("UTF-8"));
		int ini = sPayload.indexOf("#") + 1;
		int fin = sPayload.indexOf("#", ini);
		String sId = sPayload.substring(ini, fin);
		
		Log.d(RECORD_TYPE, "Reading NdefRecord "+ sId);

		return new MimeMediaRecord(sId);

	}

	public String getId() {
		return mId;
	}
	
	public String getColor() {
		return mColor;
	}	

	@Override
	public String getType() {
		return RECORD_TYPE;
	}
}