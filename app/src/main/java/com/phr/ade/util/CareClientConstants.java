package com.phr.ade.util;

/**
 * Created by dheerajs on 1/22/2017.
 */

public interface CareClientConstants {

    //Message for submitting or skipping Rx
    int RXTAKEN_SUCCESS = 0;
    int RXTAKEN_CONN_ERROR = -1;
    int RXTAKEN_ERROR = -99;

    //Message for Phone error
    int SYNC_SUCCESSFUL = 0;
    int CONNECTION_ERR = 1;
    int TIMEOUT_ERR = 2;

}
