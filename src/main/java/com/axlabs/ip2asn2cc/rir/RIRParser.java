package com.axlabs.ip2asn2cc.rir;

import com.axlabs.ip2asn2cc.checker.ASNChecker;
import com.axlabs.ip2asn2cc.checker.IPv4Checker;
import com.axlabs.ip2asn2cc.checker.IPv6Checker;
import com.axlabs.ip2asn2cc.model.IPv4Subnet;
import com.axlabs.ip2asn2cc.model.IPv6Subnet;
import com.axlabs.ip2asn2cc.model.Ip2Asn2CcEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RIRParser implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RIRParser.class);

    private final IPv4Checker ipv4Checker;
    private final IPv6Checker ipv6Checker;
    private final ASNChecker asnChecker;
    private final File fileToParse;
    private final List<String> listCountryCodeToLookFor;

    public RIRParser(IPv4Checker ipv4Checker,
                     IPv6Checker ipv6Checker,
                     ASNChecker asnChecker,
                     File fileToParse,
                     List<String> listCountryCodeToLookFor) {
        this.ipv4Checker = ipv4Checker;
        this.ipv6Checker = ipv6Checker;
        this.asnChecker = asnChecker;
        this.fileToParse = fileToParse;
        this.listCountryCodeToLookFor = listCountryCodeToLookFor;
    }

    @Override
    public void run() {
        LOG.debug("Started parsing RIR file: " + this.fileToParse.getAbsolutePath());

        List<Pattern> listPatternsToWatch = this.listCountryCodeToLookFor.stream()
                .map(this::getPatternByCountry)
                .collect(Collectors.toList());

        try (Stream<String> stream = Files.lines(Paths.get(this.fileToParse.toURI()))) {

            stream.map((line) -> {
                return listPatternsToWatch.stream()
                        .filter((pattern) -> pattern.matcher(line).matches())
                        .map((pattern) -> pattern.matcher(line))
                        .collect(Collectors.toList());
            }).flatMap((matchers) -> {
                return matchers.stream()
                        .filter(Matcher::find)
                        .filter(matcher -> matcher.groupCount() == 7)
                        .map((matcher) -> {
                            return new Ip2Asn2CcEntry(
                                    matcher.group(1), // registry
                                    matcher.group(2), // inet family (ipv4 or ipv6)
                                    matcher.group(3), // address or asn number
                                    matcher.group(0).split("\\|")[1], // country code
                                    Integer.parseInt(matcher.group(4)), // addresses
                                    matcher.group(5)); // date
                        });
            }).forEach((ip2Asn2CcEntry) -> {
                if (ip2Asn2CcEntry.getInetFamily().equals("ipv4")) {
                    IPv4Subnet ipv4Subnet = new IPv4Subnet(ip2Asn2CcEntry.getAddress(), ip2Asn2CcEntry.getAddresses(), ip2Asn2CcEntry.getCountryCode());
                    this.ipv4Checker.addSubnet(ipv4Subnet);
                }
                if (ip2Asn2CcEntry.getInetFamily().equals("ipv6")) {
                    IPv6Subnet ipv6Subnet = new IPv6Subnet(ip2Asn2CcEntry.getAddress(), ip2Asn2CcEntry.getAddresses(), ip2Asn2CcEntry.getCountryCode());
                    this.ipv6Checker.addSubnet(ipv6Subnet);
                }
                if (ip2Asn2CcEntry.getInetFamily().equals("asn")) {
                    this.asnChecker.addASN(ip2Asn2CcEntry.getAddress());
                }
            });

        } catch (IOException e) {
            LOG.error("Error reading file ({}): ", this.fileToParse.getAbsolutePath(), e);
        }

        LOG.debug("Finished parsing RIR file: " + this.fileToParse.getAbsolutePath());
    }

    private Pattern getPatternByCountry(String countryCode) {
        // perform a pattern matching based on the following format:
        // https://www.apnic.net/about-APNIC/corporate-documents/documents/resource-guidelines/rir-statistics-exchange-format
        // http://www.regexplanet.com/advanced/java/index.html (tested with this)
        return Pattern.compile("^(\\w+)\\|" + countryCode.toUpperCase() +
                "\\|(ipv4|ipv6|asn)\\|(.+)\\|(.+)\\|(.+)\\|(allocated|assigned)(.*)$");
    }

}
