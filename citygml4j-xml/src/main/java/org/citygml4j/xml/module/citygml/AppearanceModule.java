/*
 * citygml4j - The Open Source Java API for CityGML
 * https://github.com/citygml4j
 *
 * Copyright 2013-2025 Claus Nagel <claus.nagel@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.citygml4j.xml.module.citygml;

import org.citygml4j.core.model.CityGMLVersion;
import org.citygml4j.core.util.CityGMLConstants;

public class AppearanceModule extends CityGMLModule {
    public static final AppearanceModule v3_0;
    public static final AppearanceModule v2_0;
    public static final AppearanceModule v1_0;

    private AppearanceModule(String namespaceURI, String namespacePrefix, String schemaLocation, CityGMLVersion version) {
        super(namespaceURI, namespacePrefix, schemaLocation, version);
    }

    static {
        v3_0 = new AppearanceModule(
                CityGMLConstants.CITYGML_3_0_APPEARANCE_NAMESPACE,
                "app",
                "http://schemas.opengis.net/citygml/appearance/3.0/appearance.xsd",
                CityGMLVersion.v3_0
        );

        v2_0 = new AppearanceModule(
                CityGMLConstants.CITYGML_2_0_APPEARANCE_NAMESPACE,
                "app",
                "http://schemas.opengis.net/citygml/appearance/2.0/appearance.xsd",
                CityGMLVersion.v2_0
        );

        v1_0 = new AppearanceModule(
                CityGMLConstants.CITYGML_1_0_APPEARANCE_NAMESPACE,
                "app",
                "http://schemas.opengis.net/citygml/appearance/1.0/appearance.xsd",
                CityGMLVersion.v1_0
        );
    }

    public static AppearanceModule of(CityGMLVersion version) {
        return switch (version) {
            case v2_0 -> v2_0;
            case v1_0 -> v1_0;
            default -> v3_0;
        };
    }
}
