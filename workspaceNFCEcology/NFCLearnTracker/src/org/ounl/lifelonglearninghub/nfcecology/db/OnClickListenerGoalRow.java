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

import java.util.HashMap;

import org.ounl.lifelonglearninghub.nfcecology.db.tables.Goal;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class OnClickListenerGoalRow implements OnClickListener {

	int iSelected;

	public OnClickListenerGoalRow(int i) {
		this.iSelected = i;
	}

	@Override
	public void onClick(View v) {


		//Intent intent = new Intent(v.getContext(), TimeLineActivity.class);
		
		Intent intent = new Intent(v.getContext(), ViewGoalActivity.class);
		
		ListView l = (ListView)v.getParent();
		HashMap<String, String> o = (HashMap<String, String>)l.getItemAtPosition(iSelected);
		
		String oI = (String)o.get(Goal.KEY_NAME); // Get GoalId
		intent.putExtra(Goal.KEY_NAME, oI);
		v.getContext().startActivity(intent);
		

	}

};