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
package org.ounl.lifelonglearninghub.nfclearntracker.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tag;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ounl.lifelonglearninghub.nfclearntracker.R;

public class ListViewTagsAdapter extends BaseAdapter {
	public ArrayList<HashMap> list;
	Activity activity;

	public ListViewTagsAdapter(Activity activity, ArrayList<HashMap> list) {
		super();
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class TagRow {
		TextView tvTagId;
		TextView tvTagGoadId;
		TextView tvDateValidityBegin;
		TextView tvDateValidityEnd;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TagRow tr;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_row_tag, null);
			tr = new TagRow();
			tr.tvTagId = (TextView) convertView
					.findViewById(R.id.tvTagId);
			tr.tvTagGoadId = (TextView) convertView
					.findViewById(R.id.tvTagGoalId);
			tr.tvDateValidityBegin = (TextView) convertView
					.findViewById(R.id.tvTagDateValidityBegin);
			tr.tvDateValidityEnd = (TextView) convertView
					.findViewById(R.id.tvTagDateValidityEnd);

			convertView.setTag(tr);
			
			
			
		} else {
			tr = (TagRow) convertView.getTag();
		}
		HashMap map = list.get(position);
		tr.tvTagId.setText("" + map.get(Tag.KEY_ID_TAG));
		tr.tvTagId.setBackgroundColor(Color.parseColor("#"+map.get(Tag.KEY_COLOR)));
		tr.tvTagGoadId.setText("" + map.get(Tag.KEY_ID_GOAL));
		tr.tvDateValidityBegin.setText("" + map.get(Tag.KEY_VALIDITY_BEGIN));
		tr.tvDateValidityEnd.setText("" + map.get(Tag.KEY_VALIDITY_END));

		return convertView;
	}
}
