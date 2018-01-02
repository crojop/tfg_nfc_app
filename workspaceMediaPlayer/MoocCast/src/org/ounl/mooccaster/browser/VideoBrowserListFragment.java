/*
 * Copyright (C) 2013 Google Inc. All Rights Reserved. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.ounl.mooccaster.browser;

import org.ounl.mooccaster.R;
import org.ounl.mooccaster.CastApplication;
import org.ounl.mooccaster.mediaplayer.LocalPlayerActivity;

import com.google.android.gms.cast.MediaInfo;
// Commented by btb import com.google.sample.cast.refplayer.R;
import com.google.sample.castcompanionlibrary.utils.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

public class VideoBrowserListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<List<MediaInfo>> {

//    private static final String TAG = "VideoBrowserListFragment";
////    private static final String CATALOG_URL =
////            "https://dl.dropboxusercontent.com/u/49435539/cast/documentB5.json";
////    private static final String CATALOG_URL =
// //           "https://dl.dropboxusercontent.com/u/49435539/cast/document.json";    
////    private static final String CATALOG_URL =
////            "https://dl.dropboxusercontent.com/u/49435539/video_list_es.json";    
////    private static final String CATALOG_URL =
////            "https://dl.dropboxusercontent.com/u/49435539/video_list_es.json";    
////    private static final String CATALOG_URL =
////            "https://dl.dropboxusercontent.com/u/49435539/mobile_list_ou.json";    
    
    
    
    	
    private VideoListAdapter mAdapter;

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setFastScrollEnabled(true);
        mAdapter = new VideoListAdapter(getActivity());
        setEmptyText(getString(R.string.no_video_found));
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
        
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
     * .support.v4.content.Loader, java.lang.Object)
     */
    @Override
    public void onLoadFinished(Loader<List<MediaInfo>> arg0, List<MediaInfo> data) {
        mAdapter.setData(data);
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
     * .support.v4.content.Loader)
     */
    @Override
    public void onLoaderReset(Loader<List<MediaInfo>> arg0) {
        mAdapter.setData(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MediaInfo selectedMedia = mAdapter.getItem(position);        
        handleNavigation(selectedMedia, false);
        setFocus(l, position);
    }
    
    
    /**
     * Focus item for given position
     * 
     * @param l
     * @param position
     */
    private void setFocus(ListView l, int position){
//    	int iNumItem= l.getChildCount();
//    	
//    	
//    	
//    	LA MOVIDA AQUI ES QUE EL GETCHILDCOUNT DEVUELVE SOLO 8 ELEMENTOS
//    	mientras que l.getAdapter().getCount(); devuelve 27
//    	SI
//    	
//    	for (int i = 0; i < iNumItem; i++) {
//    		RelativeLayout vRow = (RelativeLayout)l.getChildAt(i);
//    		vRow.setBackgroundColor(Color.WHITE);
//		}
//    	
//    	l.getChildAt(position).setBackgroundColor(Color.parseColor(CastApplication.COLOR_ORANGE));
    }

    private void handleNavigation(MediaInfo info, boolean autoStart) {
        Intent intent = new Intent(getActivity(), LocalPlayerActivity.class);
        intent.putExtra("media", Utils.fromMediaInfo(info));
        intent.putExtra("shouldStart", autoStart);
        getActivity().startActivity(intent);
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
     * android.os.Bundle)
     */
    @Override
    public Loader<List<MediaInfo>> onCreateLoader(int arg0, Bundle arg1) {
        return new VideoItemLoader(getActivity(), CastApplication.DEFAUT_CATALOG_URL);
    }

    public static VideoBrowserListFragment newInstance() {
        VideoBrowserListFragment f = new VideoBrowserListFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    public static VideoBrowserListFragment newInstance(Bundle b) {
        VideoBrowserListFragment f = new VideoBrowserListFragment();
        f.setArguments(b);
        return f;
    }
}
