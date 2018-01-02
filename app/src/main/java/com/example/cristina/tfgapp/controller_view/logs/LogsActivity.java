package com.example.cristina.tfgapp.controller_view.logs;

import android.os.Bundle;
import android.widget.TableLayout;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.MyTerminal;


/**
 * Created by Cristina on 03/06/17.
 */

public class LogsActivity extends MyTerminal {
    /*
    Activity que muestra todos los logs de ese número de terminal (de todos los usuarios)
     */
    private Logs logs;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        TableLogs table = new TableLogs(LogsActivity.this, (TableLayout)findViewById(R.id.tabla),
                this);
        this.logs = new Logs(table, this, false);
    }
}
