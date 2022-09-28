package com.axlabs.ip2asn2cc.model;

public class Ip2Asn2CcEntry {

    private final String registry;

    private final String inetFamily;

    private final String address;

    private final String countryCode;

    private final int addresses;

    private final String date;

    public Ip2Asn2CcEntry(String registry, String inetFamily, String address, String countryCode, int addresses, String date) {
        this.registry = registry;
        this.inetFamily = inetFamily;
        this.address = address;
        this.countryCode = countryCode;
        this.addresses = addresses;
        this.date = date;
    }

    public String getRegistry() {
        return registry;
    }

    public String getInetFamily() {
        return inetFamily;
    }

    public String getAddress() {
        return address;
    }

    public int getAddresses() {
        return addresses;
    }

    public String getDate() {
        return date;
    }

    public String getCountryCode() {
        return countryCode;
    }

}
