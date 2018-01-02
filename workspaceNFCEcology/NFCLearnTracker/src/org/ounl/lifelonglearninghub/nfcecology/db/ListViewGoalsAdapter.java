 /*******************************************************************************
 * Copyright (C) 2015 Open University of The Netherlands
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
package org.ounl.lifelonglearninghub.nfcecology.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.nfcecology.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Tag;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewGoalsAdapter extends BaseAdapter {
	public ArrayList<HashMap> list;
	Activity activity;

	public ListViewGoalsAdapter(Activity activity, ArrayList<HashMap> list) {
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

	private class GoalRow {
		ImageView iv;
		TextView tvName;
		TextView tvDesc;
		
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		GoalRow gr;

		LayoutInflater inflater = activity.getLayoutInflater();
		if (v == null) {
			v = inflater.inflate(R.layout.listview_row_goal, null);

			gr = new GoalRow();
			gr.iv = (ImageView) v.findViewById(R.id.ivGoalIcon);		
			gr.tvName = (TextView) v.findViewById(R.id.tvGoalName);
			gr.tvDesc = (TextView) v.findViewById(R.id.tvGoaldDesc);

			v.setTag(gr);
			v.setOnClickListener(new OnClickListenerGoalRow(position));
			
			
		} else {
			gr = (GoalRow) v.getTag();
		}
		
		
		HashMap map = list.get(position);
//		holder.iv.setText("" + map.get(Constants.FIRST));
//		holder.iv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		gr.tvName.setText("" + map.get(Goal.KEY_NAME));
		//gr.tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		gr.tvDesc.setText("" + map.get(Goal.KEY_DESC));
		//gr.tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		


		return v;
	}
}
