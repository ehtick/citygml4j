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

package org.citygml4j.core.model.deprecated.cityobjectgroup;

import org.citygml4j.core.model.CityGMLObject;
import org.citygml4j.core.model.core.AbstractCityObject;
import org.xmlobjects.gml.model.common.GenericElement;
import org.xmlobjects.gml.model.feature.AbstractFeatureMember;

public class GroupMember extends AbstractFeatureMember<AbstractCityObject> implements CityGMLObject {
    private String role;

    public GroupMember() {
    }

    public GroupMember(AbstractCityObject object) {
        super(object);
    }

    public GroupMember(AbstractCityObject object, String role) {
        super(object);
        this.role = role;
    }

    public GroupMember(GenericElement element) {
        super(element);
    }

    public GroupMember(GenericElement element, String role) {
        super(element);
        this.role = role;
    }

    @Override
    public Class<AbstractCityObject> getTargetType() {
        return AbstractCityObject.class;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
