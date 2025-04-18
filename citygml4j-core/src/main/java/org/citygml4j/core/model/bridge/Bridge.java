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

import org.citygml4j.core.model.common.TopLevelFeature;
import org.citygml4j.core.visitor.ObjectVisitor;
import org.xmlobjects.gml.model.geometry.Envelope;
import org.xmlobjects.gml.util.EnvelopeOptions;
import org.xmlobjects.model.ChildList;

import java.util.List;

public class Bridge extends AbstractBridge implements TopLevelFeature {
    private List<BridgePartProperty> bridgeParts;

    public List<BridgePartProperty> getBridgeParts() {
        if (bridgeParts == null)
            bridgeParts = new ChildList<>(this);

        return bridgeParts;
    }

    public boolean isSetBridgeParts() {
        return bridgeParts != null && !bridgeParts.isEmpty();
    }

    public void setBridgeParts(List<BridgePartProperty> bridgeParts) {
        this.bridgeParts = asChild(bridgeParts);
    }

    @Override
    protected void updateEnvelope(Envelope envelope, EnvelopeOptions options) {
        super.updateEnvelope(envelope, options);

        if (bridgeParts != null) {
            for (BridgePartProperty property : bridgeParts) {
                if (property.getObject() != null)
                    envelope.include(property.getObject().computeEnvelope(options));
            }
        }
    }

    @Override
    public void accept(ObjectVisitor visitor) {
        visitor.visit(this);
    }
}
