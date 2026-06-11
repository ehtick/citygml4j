/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Claus Nagel <claus.nagel@gmail.com>
 */

package org.citygml4j.cityjson.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum CityJSONVersion {
    v2_0(2, 0),
    v1_1(1, 1),
    v1_0(1, 0);

    private static final Pattern versionPattern = Pattern.compile("^(\\d+)\\.(\\d+).*");
    private final static Map<String, CityJSONVersion> versions = Arrays.stream(values())
            .collect(Collectors.toMap(
                    CityJSONVersion::toValue,
                    Function.identity()));

    private final int major;
    private final int minor;

    CityJSONVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public boolean isLessThanOrEqual(CityJSONVersion version) {
        return compare(this, version) <= 0;
    }

    public boolean isGreaterThanOrEqual(CityJSONVersion version) {
        return compare(this, version) >= 0;
    }

    public String toValue() {
        return major + "." + minor;
    }

    public static CityJSONVersion min(CityJSONVersion a, CityJSONVersion b) {
        return compare(a, b) < 0 ? a : b;
    }

    public static CityJSONVersion max(CityJSONVersion a, CityJSONVersion b) {
        return compare(a, b) > 0 ? a : b;
    }

    public static int compare(CityJSONVersion a, CityJSONVersion b) {
        return a.major != b.major ?
                Integer.compare(a.major, b.major) :
                Integer.compare(a.minor, b.minor);
    }

    public static CityJSONVersion fromValue(String value) {
        if (value == null) {
            return null;
        }

        Matcher matcher = versionPattern.matcher(value);
        if (!matcher.matches()) {
            return null;
        }

        return versions.get(matcher.group(1) + "." + matcher.group(2));
    }

    @Override
    public String toString() {
        return toValue();
    }
}
