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
package org.ounl.lifelonglearninghub.learnbeacon;

import java.util.ArrayList;
import java.util.List;

import org.ounl.lifelonglearninghub.learnbeacon.R;
import org.ounl.lifelonglearninghub.learnbeacon.objects.Beacon;
import org.ounl.lifelonglearninghub.learnbeacon.objects.Layer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class BeaconEditorActivity extends Activity {



    private String CLASSNAME = this.getClass().getName();
    private Beacon beacon;

    TableLayout tl;
    TableRow tr_header;
    EditText etChoiceText;
    CheckBox cbChoiceCorrect;

    int iNumRows = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.beacon_editor_activity);

            final Bundle extras = getIntent().getExtras();

            if (extras != null) {
                    beacon = (Beacon) extras.get("generalItem");
            }

            tl = (TableLayout) findViewById(R.id.tableLayoutB);
            tr_header = (TableRow) findViewById(R.id.tableRowB1);
            etChoiceText = (EditText) findViewById(R.id.etChoiceText);
            cbChoiceCorrect = (CheckBox) findViewById(R.id.checkBoxA1);

            Button buttonCreateMC = (Button) findViewById(R.id.buttonCreateItem);
            buttonCreateMC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Toast.makeText(BeaconEditorActivity.this, "Clicked Add item button", Toast.LENGTH_SHORT).show();
                            userClickedAddMCButton();

                    }

            });

            Button buttonAddOption = (Button) findViewById(R.id.buttonAdd);
            buttonAddOption.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            // Toast.makeText(MultipleChoiceActivity.this, "Num of rows:" +
                            // iNumRows, Toast.LENGTH_SHORT).show();
                            iNumRows++;

                            TableRow tr = new TableRow(v.getContext());
                            tr.setId(iNumRows);

                            CheckBox cbCorrect = new CheckBox(v.getContext());
                            cbCorrect.setText(etChoiceText.getText());
                            cbCorrect.setTextColor(Color.GRAY);
                            cbCorrect.setChecked(cbChoiceCorrect.isChecked());
                            // android:layout_column="2"
                            tr.addView(cbCorrect);

                            TextView tvText = new TextView(v.getContext());
                            tvText.setId(iNumRows);
                            tvText.setTextColor(Color.GRAY);
                            // tvText.setPadding(5, 5, 5, 5);
                            tr.addView(tvText);// add the column to the table row here

                            ImageView img = new ImageView(v.getContext());
                            img.setId(iNumRows);
                            img.setImageResource(R.drawable.delete_48x);
                            img.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                            for (int i = 0; i < tl.getChildCount(); i++) {
                                                    View vChild = tl.getChildAt(i);
                                                    if (vChild.getId() == v.getId()) {
                                                            // Toast.makeText(MultipleChoiceActivity.this,
                                                            // "Clicked delete item " + i,
                                                            // Toast.LENGTH_SHORT).show();
                                                            tl.removeViewAt(i);
                                                    }
                                            }

                                    }
                            });
                            tr.addView(img);

                            tr_header.setVisibility(View.VISIBLE);
                            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    }
            });

    }

    private void userClickedAddMCButton() {

            // Capture values to be saved
            String sItemName = ((EditText) findViewById(R.id.etNewMultipleChoice)).getText() + "";
            String sDescription = ((EditText) findViewById(R.id.etGIDesc)).getText() + "";
            Log.d(CLASSNAME, "Creating multiple choice question with " + sItemName + " and " + sDescription);

            if ((sItemName.length() > 0) && (sDescription.length() > 0)) {
// Commented by btb
//                    beacon.setText(sItemName);
//                    beacon.setRichText(sDescription);
//                    beacon.setDeleted(false);
//                    beacon.setScope("user");
//                    beacon.setName(sItemName);
//                    beacon.setAutoLaunch(false);
//                    beacon.setType(Constants.GI_TYPE_MULTIPLE_CHOICE);

                    List<Layer> lmcai = new ArrayList<Layer>();

                    for (int i = 1; i < tl.getChildCount(); i++) {
                            TableRow trChild = (TableRow) tl.getChildAt(i);
                            CheckBox cbChild = (CheckBox) trChild.getChildAt(0);
                            // TextView tvChild = (TextView)trChild.getChildAt(1);

                            Layer mcai = new Layer();
// Commented by btb
//                            mcai.setAnswer(cbChild.getText().toString());
//                            mcai.setType(Constants.GI_TYPE_MULTIPLE_CHOICE_ANSWER);
//                            mcai.setIsCorrect(cbChild.isChecked());
                            lmcai.add(mcai);


                    }
                    // Commented by btb
                    // beacon.setAnswers(lmcai);

                    

                    // Commented by btb
                    // GeneralItemsDelegator.getInstance().createGeneralItem(this, beacon);
                    BeaconEditorActivity.this.finish();

            } else {
                    // TODO give alert in order to fill in the fields.
                    Log.e(CLASSNAME, "Fields were not filled in!!!");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Please fill in name and description fields.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                    // NewGeneralItemActivity.this.finish();
                            }
                    });

                    builder.create();
            }

    }	
	
}
