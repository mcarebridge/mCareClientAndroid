package com.phr.ade.util;

import android.telephony.SmsManager;
import android.util.Log;

import com.phr.ade.dto.RxLineDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

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
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS(String phoneNumber, String message)
    {
        Log.d("CareClientUtil", "--sendSMS --" + message);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
