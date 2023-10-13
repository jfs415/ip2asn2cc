package com.axlabs.ip2asn2cc;

import com.axlabs.ip2asn2cc.model.FilterPolicy;

public record Config(FilterPolicy filterPolicy, Boolean includeIpv4LocalAddresses, Boolean includeIpv6LocalAddresses) {

}
