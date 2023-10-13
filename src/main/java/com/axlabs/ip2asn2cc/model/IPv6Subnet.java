package com.axlabs.ip2asn2cc.model;

import java.util.Objects;

public class IPv6Subnet extends IPSubnet {

    // for IPv6::
    // * address: address (e.g., 2001:618::)
    // * networkMask: the ipv6 network mask on the CIDR format (e.g., 32 -- which means /32)

    private final Integer networkMask;
    private final String countryCode;

    public IPv6Subnet(String address, Integer networkMask, String countryCode) {
        super(address);
        this.networkMask = networkMask;
        this.countryCode = countryCode;
    }

    public Integer getNetworkMask() {
        return networkMask;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IPv6Subnet iPv6Subnet && (super.equals(iPv6Subnet))) {
            return this.networkMask.equals(iPv6Subnet.getNetworkMask()) && this.countryCode.equals(iPv6Subnet.getCountryCode());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + ((networkMask != null ? Objects.hash(networkMask) : 0)
                + (countryCode != null ? Objects.hash(countryCode) : 0));
    }

}
