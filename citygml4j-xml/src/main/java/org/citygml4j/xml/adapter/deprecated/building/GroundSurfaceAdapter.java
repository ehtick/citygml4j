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

package org.citygml4j.xml.adapter.deprecated.building;

import org.citygml4j.core.model.ade.generic.GenericADEOfGroundSurface;
import org.citygml4j.core.model.construction.ADEOfGroundSurface;
import org.citygml4j.core.model.construction.GroundSurface;
import org.citygml4j.core.util.CityGMLConstants;
import org.citygml4j.xml.adapter.CityGMLBuilderHelper;
import org.citygml4j.xml.adapter.CityGMLSerializerHelper;
import org.citygml4j.xml.adapter.ade.ADEBuilderHelper;
import org.citygml4j.xml.adapter.ade.ADESerializerHelper;
import org.xmlobjects.annotation.XMLElement;
import org.xmlobjects.annotation.XMLElements;
import org.xmlobjects.builder.ObjectBuildException;
import org.xmlobjects.serializer.ObjectSerializeException;
import org.xmlobjects.stream.XMLReadException;
import org.xmlobjects.stream.XMLReader;
import org.xmlobjects.stream.XMLWriteException;
import org.xmlobjects.stream.XMLWriter;
import org.xmlobjects.xml.Attributes;
import org.xmlobjects.xml.Element;
import org.xmlobjects.xml.Namespaces;

import javax.xml.namespace.QName;

@XMLElements({
        @XMLElement(name = "GroundSurface", namespaceURI = CityGMLConstants.CITYGML_2_0_BUILDING_NAMESPACE),
        @XMLElement(name = "GroundSurface", namespaceURI = CityGMLConstants.CITYGML_1_0_BUILDING_NAMESPACE)
})
public class GroundSurfaceAdapter extends AbstractBoundarySurfaceAdapter<GroundSurface> {
    private final QName[] substitutionGroups = new QName[]{
            new QName(CityGMLConstants.CITYGML_2_0_BUILDING_NAMESPACE, "_GenericApplicationPropertyOfGroundSurface"),
            new QName(CityGMLConstants.CITYGML_1_0_BUILDING_NAMESPACE, "_GenericApplicationPropertyOfGroundSurface")
    };

    @Override
    public GroundSurface createObject(QName name, Object parent) throws ObjectBuildException {
        return new GroundSurface();
    }

    @Override
    public void buildChildObject(GroundSurface object, QName name, Attributes attributes, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (CityGMLBuilderHelper.isADENamespace(name.getNamespaceURI()))
            buildADEProperty(object, name, reader);
        else
            super.buildChildObject(object, name, attributes, reader);
    }

    @Override
    public void buildADEProperty(GroundSurface object, QName name, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (!ADEBuilderHelper.addADEProperty(object, name, GenericADEOfGroundSurface::of, reader, substitutionGroups))
            super.buildADEProperty(object, name, reader);
    }

    @Override
    public Element createElement(GroundSurface object, Namespaces namespaces) throws ObjectSerializeException {
        return Element.of(CityGMLSerializerHelper.getBuildingNamespace(namespaces), "GroundSurface");
    }

    @Override
    public void writeChildElements(GroundSurface object, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        super.writeChildElements(object, namespaces, writer);

        for (ADEOfGroundSurface property : object.getADEProperties(ADEOfGroundSurface.class))
            ADESerializerHelper.writeADEProperty(property, namespaces, writer);
    }
}
