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

package org.citygml4j.cityjson.adapter.address;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.citygml4j.cityjson.builder.CityJSONBuildException;
import org.citygml4j.cityjson.builder.JsonObjectBuilder;
import org.citygml4j.cityjson.model.address.AddressType;
import org.citygml4j.cityjson.reader.Attributes;
import org.citygml4j.cityjson.reader.CityJSONBuilderHelper;
import org.citygml4j.cityjson.reader.CityJSONReadException;
import org.citygml4j.cityjson.serializer.CityJSONSerializeException;
import org.citygml4j.cityjson.serializer.JsonObjectSerializer;
import org.citygml4j.cityjson.writer.CityJSONSerializerHelper;
import org.citygml4j.cityjson.writer.CityJSONWriteException;
import org.xmlobjects.xal.model.RuralDelivery;
import org.xmlobjects.xal.model.types.Identifier;
import org.xmlobjects.xal.model.types.IdentifierElementType;

public class RuralDeliveryAdapter implements JsonObjectBuilder<RuralDelivery>, JsonObjectSerializer<RuralDelivery> {

    @Override
    public RuralDelivery createObject(JsonNode node, Object parent) throws CityJSONBuildException {
        return new RuralDelivery();
    }

    @Override
    public void buildObject(RuralDelivery object, Attributes attributes, JsonNode node, Object parent, CityJSONBuilderHelper helper) throws CityJSONBuildException, CityJSONReadException {
        String propertyName = AddressType.RURAL_DELIVERY.toTypeName();
        for (IdentifierElementType type : IdentifierElementType.values()) {
            JsonNode value = type == IdentifierElementType.NUMBER ?
                    node.path(propertyName) :
                    node.path(propertyName + type.toValue());
            if (value.isTextual()) {
                Identifier identifier = new Identifier(value.asText());
                identifier.setType(type == IdentifierElementType.NUMBER ? null : type);
                object.getIdentifiers().add(identifier);
            }
        }
    }

    @Override
    public void writeObject(RuralDelivery object, ObjectNode node, CityJSONSerializerHelper helper) throws CityJSONSerializeException, CityJSONWriteException {
        if (object.isSetIdentifiers()) {
            String propertyName = AddressType.RURAL_DELIVERY.toTypeName();
            for (Identifier identifier : object.getIdentifiers()) {
                if (identifier.getContent() != null) {
                    if (identifier.getType() == null || identifier.getType() == IdentifierElementType.NUMBER) {
                        node.put(propertyName, identifier.getContent());
                    } else {
                        node.put(propertyName + identifier.getType().toValue(), identifier.getContent());
                    }
                }
            }
        }
    }
}
