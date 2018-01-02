package org.upm.pregonacid.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.upm.pregonacid.R;

public class EventFragment extends Fragment {
	
	private String CLASSNAME = this.getClass().getName();

	public static final String ARG_POSITION = "position";
	
	public static final String ARG_TITLE = "title";
	public static final String ARG_SUBTITLE = "subtitle";
	public static final String ARG_SUBSUBTITLE = "subsubtitle";
	

	private View rootView;
	private int mPosition = 0;
	private String mTitle 		= "";
	private String mSubTitle 	= "";
	private String mSubSubTitle = "";
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
		
		Bundle args = getArguments();
		mPosition = args.getInt(ARG_POSITION);
		mTitle = (String)args.getString(ARG_TITLE);
		mSubTitle = (String)args.getString(ARG_SUBTITLE);
		mSubSubTitle = (String)args.getString(ARG_SUBSUBTITLE);

		inflateLayout();
				
		Log.d(CLASSNAME, "onCreateView EventFragment item position["+mPosition+"] title["+mTitle+"]  TopText["+ mSubTitle +"]  LongText["+ mSubSubTitle +"].");
		
		return rootView;
		
	}

	public void inflateLayout(){
		
		TextView tTop = (TextView) rootView.findViewById(R.id.tvTitle);
		tTop.setText(mSubTitle);

		TextView tMid = (TextView) rootView.findViewById(R.id.tvSubTitle);
		tMid.setText(mSubTitle);

		TextView tBottom = (TextView) rootView.findViewById(R.id.tvSubSubTitle);

		// Temporary commented
		// tMid.setText(mSubSubTitle);

		tBottom.setText("En  un lugar de la Mancha, de cuyo nombre no quiero acordarme, no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor. Una olla de algo más vaca que carnero, salpicón las más noches, duelos y quebrantos los sábados, lentejas los viernes, algún palomino de añadidura los domingos, consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda... consumían las tres partes de su hacienda...");

	}
	
	
	public int getPosition(){
		return mPosition;
	}
	
	

	
}