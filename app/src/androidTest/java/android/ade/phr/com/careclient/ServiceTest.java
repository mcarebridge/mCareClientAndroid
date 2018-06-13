package android.ade.phr.com.careclient;

import android.app.Service;
import android.test.ServiceTestCase;

import com.phr.ade.connector.CareXMLReader;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ServiceTest extends ServiceTestCase<Service> {
    public ServiceTest() {
        super(Service.class);
        try {
            CareXMLReader.bindXML("");
            assertEquals(true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}