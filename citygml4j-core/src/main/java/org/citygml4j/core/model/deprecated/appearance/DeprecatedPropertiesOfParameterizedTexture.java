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

package org.citygml4j.core.model.deprecated.appearance;

import org.citygml4j.core.model.deprecated.core.DeprecatedPropertiesOfAbstractFeature;
import org.xmlobjects.model.ChildList;

import java.util.List;

public class DeprecatedPropertiesOfParameterizedTexture extends DeprecatedPropertiesOfAbstractFeature {
    private List<TextureAssociationReference> targets;

    public List<TextureAssociationReference> getTargets() {
        if (targets == null) {
            targets = new ChildList<>(this);
        }

        return targets;
    }

    public boolean isSetTargets() {
        return targets != null && !targets.isEmpty();
    }

    public void setTargets(List<TextureAssociationReference> targets) {
        this.targets = asChild(targets);
    }
}
