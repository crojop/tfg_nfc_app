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

import org.ounl.lifelonglearninghub.nfclearntracker.R;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Notification;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
        

public class ListViewNotificationsAdapter extends BaseAdapter
{
    public ArrayList<HashMap> list;
    Activity activity;

    public ListViewNotificationsAdapter(Activity activity, ArrayList<HashMap> list) {
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
                NotificationRow nRow;
                LayoutInflater inflater =  activity.getLayoutInflater();
                if (convertView == null){
                    convertView = inflater.inflate(R.layout.listview_row_notification, null);
                    nRow = new NotificationRow();
                    nRow.ivNotificationGoal = (ImageView) convertView.findViewById(R.id.ivNotificationGoal);
                    nRow.tvNotificationGoalName = (TextView) convertView.findViewById(R.id.tvNotificationGoal);
                    nRow.tvNotificationDelay = (TextView) convertView.findViewById(R.id.tvNotificationDelay);
                    nRow.ivNotificationOnEventType = (ImageView) convertView.findViewById(R.id.ivNotificationOnEventType);
                    convertView.setTag(nRow);
                } else {
                    nRow = (NotificationRow) convertView.getTag();        
                }
                
                HashMap map = list.get(position);
                String sGoalId = (String)map.get(Notification.KEY_ID_GOAL);
                if(sGoalId.contains(Notification.ALL_GOALS_NOTIFICATION)){
                	nRow.ivNotificationGoal.setImageResource(R.drawable.goal_all_50x);
                }else{
                	nRow.ivNotificationGoal.setImageResource(R.drawable.goal_50x);
                }
                nRow.tvNotificationDelay.setText(""+map.get(Notification.KEY_NOTIF_DELAY_SECS));
                nRow.tvNotificationGoalName.setText(""+map.get(Notification.KEY_ID_GOAL));

                String sNotif = (String)map.get(Notification.KEY_NOTIF_TYPE);
                Integer iNotif = Integer.valueOf(sNotif);
                int iNotifType = iNotif.intValue();
                switch (iNotifType) {
				case Notification.NOTIFICATION_TYPE_ONCHECKIN:
					nRow.ivNotificationOnEventType.setImageResource(R.drawable.start_50x);
					break;
				case Notification.NOTIFICATION_TYPE_ONCHECKOUT:
					nRow.ivNotificationOnEventType.setImageResource(R.drawable.stop_50x);
					break;
				case Notification.NOTIFICATION_TYPE_SCHEDULED:
					nRow.ivNotificationOnEventType.setImageResource(R.drawable.start_50x);
					break;
				case Notification.NOTIFICATION_TYPE_RANDOMIZED:
					nRow.ivNotificationOnEventType.setImageResource(R.drawable.stop_50x);
					break;

				default:
					break;
				}

                
                return convertView;
    }
}
