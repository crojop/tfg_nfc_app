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

import org.ounl.lifelonglearninghub.nfcecology.db.tables.Tagctivity;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
        

public class ListViewActivitiesAdapter extends BaseAdapter
{
    private ArrayList<HashMap<String, String>> list;
    Activity activity;

    public ListViewActivitiesAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                ActivityRow ar;
                
                LayoutInflater inflater =  activity.getLayoutInflater();
                 if (convertView == null){
                    convertView = inflater.inflate(R.layout.listview_row_activity, null);
                    ar = new ActivityRow();
                    ar.tvTagId = (TextView) convertView.findViewById(R.id.tvActivityTagId);
                    ar.tvDateCheckIn = (TextView) convertView.findViewById(R.id.tvActivityDateCheckIn);
                    ar.tvDateCheckOut = (TextView) convertView.findViewById(R.id.tvActivityDateCheckOut);
                    convertView.setTag(ar);
                } else {
                    ar = (ActivityRow) convertView.getTag();        
                }
                
                
                HashMap map = list.get(position);
                ar.tvTagId.setText(""+map.get(Tagctivity.KEY_ID_TAG));
                ar.tvDateCheckIn.setText(""+map.get(Tagctivity.KEY_DATE_CHECKIN));
                ar.tvDateCheckOut.setText(""+map.get(Tagctivity.KEY_DATE_CHECKOUT));
                
                
                return convertView;
    }
}
