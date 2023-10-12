package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv6Subnet;

public interface IPv6CheckerInterface {

    String getCountryCodeInRange(String ipAddress);

    boolean checkIfIsInRange(String ipAddress);

    void addSubnet(IPv6Subnet ipSubnet);

}
