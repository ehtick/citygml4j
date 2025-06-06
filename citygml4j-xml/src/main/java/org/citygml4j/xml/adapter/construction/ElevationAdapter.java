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

package org.citygml4j.xml.adapter.construction;

import org.citygml4j.core.model.construction.Elevation;
import org.citygml4j.core.util.CityGMLConstants;
import org.xmlobjects.annotation.XMLElement;
import org.xmlobjects.builder.ObjectBuildException;
import org.xmlobjects.builder.ObjectBuilder;
import org.xmlobjects.gml.adapter.basictypes.CodeAdapter;
import org.xmlobjects.gml.adapter.geometry.DirectPositionAdapter;
import org.xmlobjects.serializer.ObjectSerializeException;
import org.xmlobjects.serializer.ObjectSerializer;
import org.xmlobjects.stream.XMLReadException;
import org.xmlobjects.stream.XMLReader;
import org.xmlobjects.stream.XMLWriteException;
import org.xmlobjects.stream.XMLWriter;
import org.xmlobjects.xml.Attributes;
import org.xmlobjects.xml.Element;
import org.xmlobjects.xml.Namespaces;

import javax.xml.namespace.QName;

@XMLElement(name = "Elevation", namespaceURI = CityGMLConstants.CITYGML_3_0_CONSTRUCTION_NAMESPACE)
public class ElevationAdapter implements ObjectBuilder<Elevation>, ObjectSerializer<Elevation> {

    @Override
    public Elevation createObject(QName name, Object parent) throws ObjectBuildException {
        return new Elevation();
    }

    @Override
    public void buildChildObject(Elevation object, QName name, Attributes attributes, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (CityGMLConstants.CITYGML_3_0_CONSTRUCTION_NAMESPACE.equals(name.getNamespaceURI())) {
            switch (name.getLocalPart()) {
                case "elevationReference":
                    object.setElevationReference(reader.getObjectUsingBuilder(CodeAdapter.class));
                    break;
                case "elevationValue":
                    object.setElevationValue(reader.getObjectUsingBuilder(DirectPositionAdapter.class));
                    break;
            }
        }
    }

    @Override
    public Element createElement(Elevation object, Namespaces namespaces) throws ObjectSerializeException {
        return Element.of(CityGMLConstants.CITYGML_3_0_CONSTRUCTION_NAMESPACE, "Elevation");
    }

    @Override
    public void writeChildElements(Elevation object, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        if (object.getElevationReference() != null)
            writer.writeElementUsingSerializer(Element.of(CityGMLConstants.CITYGML_3_0_CONSTRUCTION_NAMESPACE, "elevationReference"), object.getElevationReference(), CodeAdapter.class, namespaces);

        if (object.getElevationValue() != null)
            writer.writeElementUsingSerializer(Element.of(CityGMLConstants.CITYGML_3_0_CONSTRUCTION_NAMESPACE, "elevationValue"), object.getElevationValue(), DirectPositionAdapter.class, namespaces);
    }
}
