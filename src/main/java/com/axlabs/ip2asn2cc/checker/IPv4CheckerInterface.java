package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv4Subnet;

public interface IPv4CheckerInterface {

    String getCountryCodeInRange(String ipAddress);

    boolean checkIfIsInRange(String ipAddress);

    void addSubnet(IPv4Subnet ipSubnet);

}
