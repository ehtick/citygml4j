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

package org.citygml4j.core.model.bridge;

import org.citygml4j.core.model.CityGMLObject;
import org.xmlobjects.gml.model.feature.FeatureProperty;

public class BridgeRoomProperty extends FeatureProperty<BridgeRoom> implements CityGMLObject {

    public BridgeRoomProperty() {
    }

    public BridgeRoomProperty(BridgeRoom object) {
        super(object);
    }

    public BridgeRoomProperty(String href) {
        super(href);
    }

    @Override
    public Class<BridgeRoom> getTargetType() {
        return BridgeRoom.class;
    }
}
