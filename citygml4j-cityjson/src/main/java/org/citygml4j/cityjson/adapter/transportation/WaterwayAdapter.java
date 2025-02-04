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

package org.citygml4j.cityjson.adapter.transportation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.citygml4j.cityjson.adapter.Fields;
import org.citygml4j.cityjson.annotation.CityJSONElement;
import org.citygml4j.cityjson.annotation.CityJSONElements;
import org.citygml4j.cityjson.builder.CityJSONBuildException;
import org.citygml4j.cityjson.model.CityJSONVersion;
import org.citygml4j.cityjson.reader.Attributes;
import org.citygml4j.cityjson.reader.CityJSONBuilderHelper;
import org.citygml4j.cityjson.reader.CityJSONReadException;
import org.citygml4j.cityjson.serializer.CityJSONSerializeException;
import org.citygml4j.cityjson.util.CityJSONConstants;
import org.citygml4j.cityjson.writer.CityJSONSerializerHelper;
import org.citygml4j.cityjson.writer.CityJSONWriteException;
import org.citygml4j.core.model.transportation.*;
import org.xmlobjects.gml.model.basictypes.Code;

import java.util.Iterator;

@CityJSONElements({
        @CityJSONElement(name = "Waterway", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v2_0),
        @CityJSONElement(name = "Waterway", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v1_1)
})
public class WaterwayAdapter extends AbstractTransportationSpaceAdapter<Waterway> {

    @Override
    public Waterway createObject(JsonNode node, Object parent) throws CityJSONBuildException {
        return new Waterway();
    }

    @Override
    public void buildObject(Waterway object, Attributes attributes, JsonNode node, Object parent, CityJSONBuilderHelper helper) throws CityJSONBuildException, CityJSONReadException {
        super.buildObject(object, attributes, node, parent, helper);

        helper.buildStandardObjectClassifier(object, attributes);

        Iterator<JsonNode> children = node.path(Fields.CHILDREN).elements();
        while (children.hasNext()) {
            String child = children.next().asText();
            ObjectNode childNode = helper.getCityObjectNode(child);
            if ("Waterway".equals(childNode.path(Fields.TYPE).asText())) {
                String classifier = childNode.path(Fields.ATTRIBUTES).path("class").asText();
                if ("Section".equalsIgnoreCase(classifier)) {
                    Waterway section = helper.getCityObject(child, Waterway.class);
                    object.getSections().add(new SectionProperty(shallowCopy(section, new Section())));
                } else if ("Intersection".equalsIgnoreCase(classifier)) {
                    Waterway intersection = helper.getCityObject(child, Waterway.class);
                    object.getIntersections().add(new IntersectionProperty(shallowCopy(intersection, new Intersection())));
                } else {
                    helper.buildAsTopLevelObject(child);
                }

                children.remove();
            }
        }
    }

    @Override
    public String createType(Waterway object, CityJSONVersion version) throws CityJSONSerializeException {
        return "Waterway";
    }

    @Override
    public void writeObject(Waterway object, ObjectNode node, CityJSONSerializerHelper helper) throws CityJSONSerializeException, CityJSONWriteException {
        super.writeObject(object, node, helper);
        ObjectNode attributes = helper.getOrPutObject(Fields.ATTRIBUTES, node);

        helper.writeStandardObjectClassifier(object, attributes);

        if (object.isSetSections()) {
            for (SectionProperty property : object.getSections()) {
                if (property.getObject() != null) {
                    Waterway section = shallowCopy(property.getObject(), new Waterway());
                    section.setClassifier(new Code("Section"));
                    helper.writeChildObject(section, object, node);
                }
            }
        }

        if (object.isSetIntersections()) {
            for (IntersectionProperty property : object.getIntersections()) {
                if (property.getObject() != null) {
                    Waterway section = shallowCopy(property.getObject(), new Waterway());
                    section.setClassifier(new Code("Intersection"));
                    helper.writeChildObject(section, object, node);
                }
            }
        }
    }
}
