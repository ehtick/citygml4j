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

import java.util.Iterator;

@CityJSONElements({
        @CityJSONElement(name = "Railway", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v2_0),
        @CityJSONElement(name = "Railway", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v1_1),
        @CityJSONElement(name = "Railway", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v1_0)
})
public class RailwayAdapter extends AbstractTransportationSpaceAdapter<Railway> {

    @Override
    public Railway createObject(JsonNode node, Object parent) throws CityJSONBuildException {
        return new Railway();
    }

    @Override
    public void buildObject(Railway object, Attributes attributes, JsonNode node, Object parent, CityJSONBuilderHelper helper) throws CityJSONBuildException, CityJSONReadException {
        super.buildObject(object, attributes, node, parent, helper);

        helper.buildStandardObjectClassifier(object, attributes);

        Iterator<JsonNode> children = node.path(Fields.CHILDREN).elements();
        while (children.hasNext()) {
            String child = children.next().asText();
            ObjectNode childNode = helper.getCityObjectNode(child);
            if ("Railway".equals(childNode.path(Fields.TYPE).asText())) {
                String classifier = childNode.path(Fields.ATTRIBUTES).path("class").asText();
                if ("Section".equalsIgnoreCase(classifier)) {
                    Railway section = helper.getCityObject(child, Railway.class);
                    object.getSections().add(new SectionProperty(shallowCopy(section, new Section())));
                } else if ("Intersection".equalsIgnoreCase(classifier)) {
                    Railway intersection = helper.getCityObject(child, Railway.class);
                    object.getIntersections().add(new IntersectionProperty(shallowCopy(intersection, new Intersection())));
                } else {
                    helper.buildAsTopLevelObject(child);
                }

                children.remove();
            }
        }
    }

    @Override
    public String createType(Railway object, CityJSONVersion version) throws CityJSONSerializeException {
        return "Railway";
    }

    @Override
    public void writeObject(Railway object, ObjectNode node, CityJSONSerializerHelper helper) throws CityJSONSerializeException, CityJSONWriteException {
        super.writeObject(object, node, helper);
        ObjectNode attributes = helper.getOrPutObject(Fields.ATTRIBUTES, node);

        helper.writeStandardObjectClassifier(object, attributes);
    }
}
