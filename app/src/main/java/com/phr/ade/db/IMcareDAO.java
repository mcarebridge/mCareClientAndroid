package com.phr.ade.db;


public interface IMcareDAO {

    int addMcareRequest();

    void readRequestByRequestId();

    void readAllUnsentRequestByAge();

    void readNotRespondedRequestById();

    int addMcareResponse();

    void readMcareResponseByResponseId();

}
