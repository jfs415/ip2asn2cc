package com.axlabs.ip2asn2cc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.axlabs.ip2asn2cc.exception.RIRNotDownloadedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Ip2Asn2CcIncludeFilterPolicyTest {

    private static Ip2Asn2Cc ip2Asn2Cc;

    @BeforeEach
    void initialize() throws RIRNotDownloadedException {
        List<String> listCountryCode = new ArrayList<>();
        listCountryCode.add("US");
        ip2Asn2Cc = new Ip2Asn2Cc(listCountryCode);
    }

    @Test
    void getCountryNameTest() throws RIRNotDownloadedException {
        ip2Asn2Cc = new Ip2Asn2Cc(Collections.singletonList("US"));
        System.out.println("Localhost Country: " + ip2Asn2Cc.getRIRCountryCode("127.0.0.1")); //Is hard coded to US

        String cc = ip2Asn2Cc.getRIRCountryCode("8.8.8.8");
        boolean status = cc.equals("US");
        System.out.println("Google DNS Country: " + cc); //Should be US
        System.out.println("Test Status: " + (status ? "PASSED" : "FAILED"));

        assertTrue(status);
    }

    @Test
    void testIPv4() {
        // it should return true since the Ip2Asn2Cc class was
        // initialized with "US" country code and the option to "include" such
        // country codes in the check
        assertTrue(ip2Asn2Cc.checkIP("8.8.8.8"));

        // It should return false, since it's an IP address from Switzerland
        assertFalse(ip2Asn2Cc.checkIP("77.109.144.219"));
    }

    @Test
    void testIPv6() {
        assertTrue(ip2Asn2Cc.checkIP("2600:1f18:1f:db01:11af:58af:ae11:f645"));
        assertFalse(ip2Asn2Cc.checkIP("2001:1620:2777:23::2"));
    }

    @Test
    void testASN() {
        assertTrue(ip2Asn2Cc.checkASN("3356"));
        assertFalse(ip2Asn2Cc.checkASN("13030"));
    }

}
