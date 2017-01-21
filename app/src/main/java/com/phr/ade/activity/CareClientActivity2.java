package com.phr.ade.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
        char[] _auth = _intent.getCharArrayExtra("AUTH");

        boolean _rxSchdl = _intent.getBooleanExtra("RX_SCHDL", false);

        String rxSynchStatus = _rxSynchStatus != null ? new String(_rxSynchStatus) : "";
        String caredPersonName = _caredPersonName != null ? new String(_caredPersonName) : "-";
        String auth = _auth != null ? new String(_auth) : "";

        Log.d("CareClientActivity2", "-- rxSynchStatus --" + rxSynchStatus);
        Log.d("CareClientActivity2", "-- AUTH --" + auth);

        TextView _rxMsgWindow = (TextView) findViewById(R.id.msgwindow);
        _rxMsgWindow.setTextColor(Color.BLACK);

        String _messageToDisplay = selectDisplayMessage(rxSynchStatus, caredPersonName, _rxSchdl, auth);

        _rxMsgWindow.setText(_messageToDisplay);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onClick(View v) {

    }

    /**
     * @param v
     */
    public void onSynch(View v) {
        Log.d("CareClientActivity2", "--calling onSynch --");
        char[] _auth = getIntent().getCharArrayExtra("AUTH");
        boolean _rxSchdl = getIntent().getBooleanExtra("RX_SCHDL", false);
        String auth = _auth != null ? new String(_auth) : "";
        Log.d("CareClientActivity2", "-- onSynch.AUTH --" + auth);

        if (auth.equals("AUTH-PASSED") && _rxSchdl) {
            Intent intent = new Intent(this, com.phr.ade.activity.CareClientActivity3.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("SERVICE_CALL", false);
            startActivity(intent);
        } else {
//            Intent _intent = new Intent(this, com.phr.ade.service.ClientService1.class);
//            PendingIntent pi = PendingIntent.getService(this, 0, _intent, 0);
//            startService(_intent);
            new LoginAsyncTask(this).execute();
        }

        //startActivity(_intent);
        //this.finish(); //

        //new LoginAsyncTask().execute();
    }

    /**
     * @param v
     */
    public void onQuit(View v) {
        Log.d("CareClient", "-- onQuit --");

        Button _rxClose = (Button) findViewById(R.id.closeBtn);
        _rxClose.setClickable(false);

        Button _synchButton = (Button) findViewById(R.id.synchbtn);
        _synchButton.setClickable(false);
        Context _context = getApplicationContext();
        Drawable _d = _context.getResources().getDrawable(R.drawable.synchbtndisabled);
        _synchButton.setBackground(_d);

        _rxClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });
    }


    private String selectDisplayMessage(String rxSynchStatus, String caredPersonName, boolean rxSchdl, String auth) {

        String _messageToDisplay = "Welcome to mCareBridge. Data Sync-up in process. Please wait.";

        Log.d("selectDisplayMessage ", rxSynchStatus + "-" + caredPersonName);
        Button _synchButton = (Button) findViewById(R.id.synchbtn);
        Button _closeButton = (Button) findViewById(R.id.closeBtn);

        if (rxSynchStatus.equals("SUCCESS")) {
            _messageToDisplay = "Welcome " + caredPersonName;
            if (auth.equals("AUTH-PASSED")) {
                if (rxSchdl) {
                    _messageToDisplay += '\n' + "Rx scheduled. Please click on Synch to Proceed ";
                } else {
                    _messageToDisplay += '\n' + "No Rx scheduled.";
                }

                _synchButton.setClickable(true);
                Context _context = getApplicationContext();
                Drawable _d = _context.getResources().getDrawable(R.drawable.synchbtn);
                _synchButton.setBackground(_d);
                _synchButton.setText("Synch Rx");
                _closeButton.setClickable(true);

            }
            // auth.equals("AUTH-FAILED")
            else {
                Log.d("selectDisplayMessage : AuthStatus - ", auth + "-");
                _messageToDisplay = " Welcome to mCareBridge.";
                _synchButton.setClickable(true);
                _closeButton.setClickable(true);

                Context _context = getApplicationContext();
                Drawable _d = _context.getResources().getDrawable(R.drawable.synchbtn);
                _synchButton.setBackground(_d);
                _synchButton.setText("Retry");
                _messageToDisplay += '\n' + "You are not registered with mCareBridge. Please contact your Care Provider.";
            }
        } else if (rxSynchStatus.equals("TIMEOUT")) {
            _messageToDisplay = "Connection Timeout. Please try again";
            _synchButton.setClickable(true);
            _closeButton.setClickable(true);
        }
        else if (rxSynchStatus.equals("ERROR")) {
            _messageToDisplay = "Error in Data Synch. Please report to admin@mcarebridge.com.";
            _synchButton.setClickable(false);
            _synchButton.setText("Synch Err");
            _closeButton.setClickable(true);
        }

        return _messageToDisplay;
    }

    ProgressDialog progressDialog;

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        public CareClientActivity2 ccActivity2;

        public LoginAsyncTask(CareClientActivity2 ccActivity2)
        {
            this.ccActivity2 = ccActivity2;
        }

        @Override
        protected void onPreExecute() {

            Log.d("LoginAsyncTask.onPreExecute", "----");

            progressDialog = new ProgressDialog(CareClientActivity2.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
            // Parsse response data
            Log.d("LoginAsyncTask.doInBackground", "----");
            Intent _intent = new Intent(this.ccActivity2, com.phr.ade.service.ClientService1.class);
            PendingIntent pi = PendingIntent.getService(this.ccActivity2, 0, _intent, 0);
            startService(_intent);
            return null;
        }

        protected void onPostExecute(Void result) {

            Log.d("LoginAsyncTask.onPostExecute", "----");

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        //Not sure why a sleep for 2 secs is needed. To be removed if hampering performance
                        Thread.sleep(2000);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

//            if (progressDialog.isShowing())
//                progressDialog.dismiss();
            //move activity
            super.onPostExecute(result);
        }
    }


}

