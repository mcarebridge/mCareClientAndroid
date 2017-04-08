package com.phr.ade.util;

import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

import com.phr.ade.dto.RxLineDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import static android.R.id.icon;
import static android.R.id.title;

/**
 * Created by deejay on 11/26/2014.
 */
public class CareClientUtil {


    /**
     * @param rxLineDTOList
     * @return
     */
    public static boolean checkTimeToTriggerRx(ArrayList<RxLineDTO> rxLineDTOList) {

        Calendar _c = Calendar.getInstance();
        int _hour24 = _c.get(Calendar.HOUR_OF_DAY);
        boolean _rxFound = false;


        for (Iterator<RxLineDTO> iterator = rxLineDTOList.iterator(); iterator.hasNext(); ) {
            RxLineDTO _rxLine = iterator.next();

            if (_rxLine.getRxTime() == _hour24) {
                _rxFound = true;
            }
        }

        return _rxFound;
    }


    /**
     * Send short Messages
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS(String phoneNumber, String message) {
        Log.d("CareClientUtil", "--sendSMS --" + message);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }


    public static void triggerRxAlert(Context context){

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.play();
    }

}
