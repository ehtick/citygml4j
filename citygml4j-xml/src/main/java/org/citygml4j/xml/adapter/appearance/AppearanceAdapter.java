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

package org.citygml4j.xml.adapter.appearance;

import org.citygml4j.core.model.ade.generic.GenericADEOfAppearance;
import org.citygml4j.core.model.appearance.ADEOfAppearance;
import org.citygml4j.core.model.appearance.AbstractSurfaceDataProperty;
import org.citygml4j.core.model.appearance.Appearance;
import org.citygml4j.core.util.CityGMLConstants;
import org.citygml4j.xml.adapter.CityGMLBuilderHelper;
import org.citygml4j.xml.adapter.CityGMLSerializerHelper;
import org.citygml4j.xml.adapter.ade.ADEBuilderHelper;
import org.citygml4j.xml.adapter.ade.ADESerializerHelper;
import org.citygml4j.xml.adapter.core.AbstractAppearanceAdapter;
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
        @XMLElement(name = "Appearance", namespaceURI = CityGMLConstants.CITYGML_3_0_APPEARANCE_NAMESPACE),
        @XMLElement(name = "Appearance", namespaceURI = CityGMLConstants.CITYGML_2_0_APPEARANCE_NAMESPACE),
        @XMLElement(name = "Appearance", namespaceURI = CityGMLConstants.CITYGML_1_0_APPEARANCE_NAMESPACE)
})
public class AppearanceAdapter extends AbstractAppearanceAdapter<Appearance> {
    private final QName[] substitutionGroups = new QName[]{
            new QName(CityGMLConstants.CITYGML_2_0_APPEARANCE_NAMESPACE, "_GenericApplicationPropertyOfAppearance"),
            new QName(CityGMLConstants.CITYGML_1_0_APPEARANCE_NAMESPACE, "_GenericApplicationPropertyOfAppearance")
    };

    @Override
    public Appearance createObject(QName name, Object parent) throws ObjectBuildException {
        return new Appearance();
    }

    @Override
    public void buildChildObject(Appearance object, QName name, Attributes attributes, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (CityGMLBuilderHelper.isAppearanceNamespace(name.getNamespaceURI())) {
            switch (name.getLocalPart()) {
                case "theme":
                    reader.getTextContent().ifPresent(object::setTheme);
                    return;
                case "surfaceData":
                case "surfaceDataMember":
                    object.getSurfaceData().add(reader.getObjectUsingBuilder(AbstractSurfaceDataPropertyAdapter.class));
                    return;
                case "adeOfAppearance":
                    ADEBuilderHelper.addADEProperty(object, GenericADEOfAppearance::of, reader);
                    return;
            }
        } else if (CityGMLBuilderHelper.isADENamespace(name.getNamespaceURI())) {
            buildADEProperty(object, name, reader);
            return;
        }

        super.buildChildObject(object, name, attributes, reader);
    }

    @Override
    public void buildADEProperty(Appearance object, QName name, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (!ADEBuilderHelper.addADEProperty(object, name, GenericADEOfAppearance::of, reader, substitutionGroups))
            super.buildADEProperty(object, name, reader);
    }

    @Override
    public Element createElement(Appearance object, Namespaces namespaces) throws ObjectSerializeException {
        return Element.of(CityGMLSerializerHelper.getAppearanceNamespace(namespaces), "Appearance");
    }

    @Override
    public void writeChildElements(Appearance object, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        super.writeChildElements(object, namespaces, writer);
        String appearanceNamespace = CityGMLSerializerHelper.getAppearanceNamespace(namespaces);
        boolean isCityGML3 = CityGMLConstants.CITYGML_3_0_APPEARANCE_NAMESPACE.equalsIgnoreCase(appearanceNamespace);

        if (object.getTheme() != null)
            writer.writeElement(Element.of(appearanceNamespace, "theme").addTextContent(object.getTheme()));

        if (object.isSetSurfaceData()) {
            for (AbstractSurfaceDataProperty property : object.getSurfaceData())
                writer.writeElementUsingSerializer(
                        Element.of(appearanceNamespace, isCityGML3 ? "surfaceData" : "surfaceDataMember"),
                        property, AbstractSurfaceDataPropertyAdapter.class, namespaces);
        }

        for (ADEOfAppearance property : object.getADEProperties(ADEOfAppearance.class))
            ADESerializerHelper.writeADEProperty(isCityGML3 ? Element.of(appearanceNamespace, "adeOfAppearance") : null, property, namespaces, writer);
    }
}
