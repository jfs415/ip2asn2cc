package com.axlabs.ip2asn2cc.model;

import java.util.Objects;

public class IPv4Subnet extends IPSubnet {

    // for IPv4:
    // * address: address (e.g., 192.168.0.0)
    // * amountOfAddresses: amount of addresses (e.g., 524288 -- which means /13)

    private final Integer amountOfAddresses;

    private final String countryCode;

    public IPv4Subnet(String address, Integer amountOfAddresses, String countryCode) {
        super(address);
        this.amountOfAddresses = amountOfAddresses;
        this.countryCode = countryCode;
    }

    public Integer getAmountOfAddresses() {
        return amountOfAddresses;
    }

    public String getCIDR() {
        if (this.address != null && !this.address.isEmpty() && this.amountOfAddresses != null && this.amountOfAddresses != 0) {
            // calculate the IPv4 CIDR based on the amount of hosts:
            // http://www.cisco.com/c/en/us/support/docs/ip/routing-information-protocol-rip/13790-8.html
            // http://networkengineering.stackexchange.com/questions/7106/how-do-you-calculate-the-prefix-network-subnet-and-host-numbers
            return this.address + "/" + (32 - (int) Math.ceil(Math.log(this.amountOfAddresses) / Math.log(2)));
        }
        return null;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IPv4Subnet iPv4Subnet && (super.equals(iPv4Subnet))) {
            return this.amountOfAddresses.equals(iPv4Subnet.getAmountOfAddresses()) && this.countryCode.equals(iPv4Subnet.getCountryCode());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + ((amountOfAddresses != null ? Objects.hash(amountOfAddresses) : 0)
                + (countryCode != null ? Objects.hash(countryCode) : 0));
    }

}
