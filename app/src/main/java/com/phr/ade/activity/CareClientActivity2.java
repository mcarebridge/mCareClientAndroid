package com.phr.ade.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


public class CareClientActivity2 extends Activity implements View.OnClickListener {

    private static boolean alarmSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CareClientActivity2", "--calling onCreate --");
        setContentView(R.layout.activity_care_client2);


        if (!alarmSet) {
            Calendar cur_cal = Calendar.getInstance();
            cur_cal.setTimeInMillis(System.currentTimeMillis());
            cur_cal.set(Calendar.HOUR_OF_DAY, 00);
            cur_cal.set(Calendar.MINUTE, 00);
            cur_cal.set(Calendar.SECOND, 0);

            Log.d("CareClientActivity2", "Calender Set time:" + cur_cal.getTime());
            Intent intent = new Intent(this, com.phr.ade.service.ClientService1.class);
            Log.d("CareClientActivity2", "Intent created");

            PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);

            AlarmManager alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //Wake up after every 60 mins
            alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP,
                    cur_cal.getTimeInMillis(), 3600 * 1000, pi);
            //alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cur_cal.getTimeInMillis(), 3600 * 1000, pi);
            Log.d("CareClientActivity2", "alarm manager set");
            alarmSet = true;
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("CareClientActivity2", "--calling onStart --");
        Intent _intent = getIntent();
        char[] _rxSynchStatus = _intent.getCharArrayExtra("RX_SYNCH_STATUS");
        char[] _caredPersonName = _intent.getCharArrayExtra("CARED_PERSON");
        boolean _rxSchdl = _intent.getBooleanExtra("RX_SCHDL", false);


        String rxSynchStatus = _rxSynchStatus != null ? new String(_rxSynchStatus) : "";
        String caredPersonName = _caredPersonName != null ? new String(_caredPersonName) : "-";

        Log.d("CareClientActivity2", "-- rxSynchStatus --" + rxSynchStatus);


        TextView _rxMsgWindow = (TextView) findViewById(R.id.msgwindow);
        _rxMsgWindow.setTextColor(Color.BLACK);

        String _messageToDisplay = selectDisplayMessage(rxSynchStatus, caredPersonName, _rxSchdl);

        _rxMsgWindow.setText(_messageToDisplay);

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    public void onClick(View v) {

    }

    /**
     *
     * @param v
     */
    public void onSynch(View v) {
        Log.d("CareClientActivity2", "--calling onSynch --");
        Intent intent = new Intent(this, com.phr.ade.activity.CareClientActivity3.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SERVICE_CALL", false);
        startActivity(intent);
    }

    /**
     *
     * @param v
     */
    public void onQuit(View v) {
        Log.d("CareClient", "-- onSkipClick --");

        Button _rxClose = (Button) findViewById(R.id.closeBtn);
        _rxClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });
    }


    private String selectDisplayMessage(String rxSynchStatus, String caredPersonName, boolean rxSchdl) {

        String _messageToDisplay = "Welcome to mCareBridge. Please click on Synch to Proceed";

        Log.d("selectDisplayMessage ", rxSynchStatus + "-" + caredPersonName);

        if (rxSynchStatus.equals("SUCCESS")) {
            _messageToDisplay = "Welcome " + caredPersonName;
            if (rxSchdl) {
                _messageToDisplay += '\n' + "Rx scheduled. Please click on Synch to Proceed ";
            } else {
                _messageToDisplay += '\n' + "No Rx scheduled.";
            }

        } else if (rxSynchStatus.equals("TIMEOUT")) {
            _messageToDisplay = "Connection Timeout. Please try again";
        } else if (rxSynchStatus.equals("ERROR")) {
            _messageToDisplay = "Error in Data Synch. Please try again.";
        }

        return _messageToDisplay;
    }


}
