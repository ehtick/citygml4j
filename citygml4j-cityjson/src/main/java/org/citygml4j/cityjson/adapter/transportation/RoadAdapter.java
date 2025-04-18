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
import org.citygml4j.cityjson.builder.TypeMapper;
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
        @CityJSONElement(name = "Road", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v2_0),
        @CityJSONElement(name = "Road", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v1_1),
        @CityJSONElement(name = "Road", schema = CityJSONConstants.CORE_SCHEMA, version = CityJSONVersion.v1_0)
})
public class RoadAdapter extends AbstractTransportationSpaceAdapter<Road> implements TypeMapper {

    @Override
    public Road createObject(JsonNode node, Object parent) throws CityJSONBuildException {
        return new Road();
    }

    @Override
    public void buildObject(Road object, Attributes attributes, JsonNode node, Object parent, CityJSONBuilderHelper helper) throws CityJSONBuildException, CityJSONReadException {
        super.buildObject(object, attributes, node, parent, helper);

        helper.buildStandardObjectClassifier(object, attributes);

        Iterator<JsonNode> children = node.path(Fields.CHILDREN).elements();
        while (children.hasNext()) {
            String child = children.next().asText();
            ObjectNode childNode = helper.getCityObjectNode(child);
            if ("Road".equals(childNode.path(Fields.TYPE).asText())) {
                String classifier = childNode.path(Fields.ATTRIBUTES).path("class").asText();
                if ("Section".equalsIgnoreCase(classifier)) {
                    Road section = helper.getCityObject(child, Road.class);
                    object.getSections().add(new SectionProperty(shallowCopy(section, new Section())));
                } else if ("Intersection".equalsIgnoreCase(classifier)) {
                    Road intersection = helper.getCityObject(child, Road.class);
                    object.getIntersections().add(new IntersectionProperty(shallowCopy(intersection, new Intersection())));
                } else {
                    helper.buildAsTopLevelObject(child);
                }

                children.remove();
            }
        }
    }

    @Override
    public String mapType(JsonNode node, Class<?> type) {
        String classifier = node.path(Fields.ATTRIBUTES).path("class").asText();
        return "Track".equalsIgnoreCase(classifier) && type.isAssignableFrom(Track.class) ? "Track" : "Road";
    }

    @Override
    public String createType(Road object, CityJSONVersion version) throws CityJSONSerializeException {
        return "Road";
    }

    @Override
    public void writeObject(Road object, ObjectNode node, CityJSONSerializerHelper helper) throws CityJSONSerializeException, CityJSONWriteException {
        super.writeObject(object, node, helper);
        ObjectNode attributes = helper.getOrPutObject(Fields.ATTRIBUTES, node);

        helper.writeStandardObjectClassifier(object, attributes);
    }
}
