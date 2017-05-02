package com.phr.ade.service;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.phr.ade.connector.CareXMLReader;
import com.phr.ade.connector.MCareBridgeConnector;
import com.phr.ade.dto.RxLineDTO;
import com.phr.ade.util.CareClientUtil;
import com.phr.ade.xmlbinding.CaredPerson;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class MCareHTTPService {

    private String rxSynchStatus;

    public MCareHTTPService() {
    }

    public HashMap onStart(Context context) {
        Log.d("MCareHTTPService", "--calling onStart -- ");
        String _responseData = null;
        String _rxConsumed = null;
        String _rxSynch = "FALSE";
        boolean _serviceCall = false;
        String rxSynchStatus = "NONE";
        String _caredPersonName = "-";

        HashMap<String, String> headerKeyValueList = new HashMap<String, String>();

        try {

            String imeiCode = readIMEICode(context);
            //_responseData = MCareBridgeConnector.synchMobileUsingIMEI("353322068558368");
            _responseData = MCareBridgeConnector.synchMobileUsingIMEI(imeiCode);
            _rxConsumed = MCareBridgeConnector.getRxConsumed();
            //Log.d("MCareHTTPService XML -- ", _responseData + "<<<<<<<");
            Log.d("MCareHTTPService -- RxConsumed -- ", _rxConsumed + "<<<<<<<");
            _rxSynch = "TRUE";
            _serviceCall = true;
            rxSynchStatus = "SUCCESS";

        } catch (SocketTimeoutException s) {
            Log.e("MCareHTTPService", s.getMessage(), s);
            s.printStackTrace();
            rxSynchStatus = "TIMEOUT";
        } catch (UnknownHostException u) {
            Log.e("MCareHTTPService", u.getMessage(), u);
            u.printStackTrace();
            //_rxSynchStatus = "TIMEOUT";
            rxSynchStatus = "HOST_NOT_FOUND";
        } catch (Exception e) {
            Log.e("MCareHTTPService", e.getMessage(), e);
            e.printStackTrace();
            //_rxSynchStatus = "ERROR";
            rxSynchStatus = e.getMessage();

        } finally {

            boolean _isRxReady = false;
            //Default failed message.
            headerKeyValueList.put("AUTH", "AUTH-FAILED");
            //intent.setAction(Intent.ACTION_MAIN);
            //intent.addCategory(Intent.CATEGORY_LAUNCHER); -- old code
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //This is the case where Auth is not failed and sync is successful
            if (_responseData != null) {
                if (rxSynchStatus.equals("SUCCESS") && !(_responseData.equalsIgnoreCase("AUTH-FAILED"))) {
                    //intent.putExtra("XML_DATA", _responseData.toCharArray());
                    //intent.putExtra("RX_CONSUMED", _rxConsumed.toCharArray());
                    //intent.putExtra("RX_ASYNC", _rxSynch.toCharArray());
                    //intent.putExtra("AUTH", new String("AUTH-PASSED").toCharArray());
                    headerKeyValueList.put("XML_DATA", _responseData);
                    headerKeyValueList.put("RX_CONSUMED", _rxConsumed);
                    headerKeyValueList.put("RX_ASYNC", _rxSynch);
                    headerKeyValueList.put("AUTH", "AUTH-PASSED");

                    CaredPerson _caredPerson = CareXMLReader.bindXML(_responseData);
                    _caredPersonName = _caredPerson.getName();
                    //intent.putExtra("CARED_PERSON", _caredPersonName.toCharArray());
                    headerKeyValueList.put("CARED_PERSON", _caredPersonName);
                    ArrayList<RxLineDTO> _rxLineDTOList = CareXMLReader.extractRxTime(_caredPerson);
                    Log.d("MCareHTTPService", "--size of List -- " + _rxLineDTOList.size());
                    _isRxReady = CareClientUtil.checkTimeToTriggerRx(_rxLineDTOList);
                } else {
                    //intent.putExtra("AUTH", new String("AUTH-FAILED").toCharArray());
                    headerKeyValueList.put("AUTH", "AUTH-FAILED");
                }
            }
            //intent.putExtra("RX_SYNCH_STATUS", _rxSynchStatus.toCharArray());
            //intent.putExtra("SERVICE_CALL", _serviceCall);
            //intent.putExtra("RX_SCHDL", _isRxReady);
            headerKeyValueList.put("RX_SYNCH_STATUS", rxSynchStatus);
            headerKeyValueList.put("SERVICE_CALL", new Boolean(_serviceCall).toString());
            headerKeyValueList.put("RX_SCHDL", new Boolean(_isRxReady).toString());


            Log.d("MCareHTTPService", "_rxSynchStatus = " + rxSynchStatus + " _isRxReady = " + _isRxReady);

            /**
            if (rxSynchStatus.equals("SUCCESS")) {
                if (_isRxReady) {
                    Log.d("MCareHTTPService", "-- start Activity Triggered Point 1--");
                    Toast.makeText(context, "Rx scheduled -- Loading Schedule", Toast.LENGTH_LONG)
                            .show();
                    //startActivity(intent);
                } else {
                    Toast.makeText(context, "Relax. No scheduled medication.", Toast.LENGTH_LONG)
                            .show();
                }
            } else if (rxSynchStatus.equals("TIMEOUT")) {
                Toast.makeText(context, "Error : Connection Timeout", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(context, "Error : Unexpected error. Please report admin@mcarebridge.com", Toast.LENGTH_LONG)
                        .show();

            }
             **/
            return  headerKeyValueList;
        }
    }


    private String readIMEICode(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String _deviceId = telephonyManager.getDeviceId();
        Log.i("MCareHTTPService", _deviceId);
        //Only for testing
        //_deviceId = "867124022666036";
        return _deviceId;
    }

}
