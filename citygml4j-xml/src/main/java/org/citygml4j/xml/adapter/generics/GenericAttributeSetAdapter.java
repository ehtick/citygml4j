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

package org.citygml4j.xml.adapter.generics;

import org.citygml4j.core.model.core.AbstractGenericAttribute;
import org.citygml4j.core.model.core.AbstractGenericAttributeProperty;
import org.citygml4j.core.model.generics.GenericAttributeSet;
import org.citygml4j.core.util.CityGMLConstants;
import org.citygml4j.xml.adapter.CityGMLSerializerHelper;
import org.citygml4j.xml.adapter.core.AbstractGenericAttributeAdapter;
import org.citygml4j.xml.adapter.core.AbstractGenericAttributePropertyAdapter;
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
        @XMLElement(name = "GenericAttributeSet", namespaceURI = CityGMLConstants.CITYGML_3_0_GENERICS_NAMESPACE),
        @XMLElement(name = "genericAttributeSet", namespaceURI = CityGMLConstants.CITYGML_2_0_GENERICS_NAMESPACE),
        @XMLElement(name = "genericAttributeSet", namespaceURI = CityGMLConstants.CITYGML_1_0_GENERICS_NAMESPACE)
})
public class GenericAttributeSetAdapter extends AbstractGenericAttributeAdapter<GenericAttributeSet> {

    @Override
    public GenericAttributeSet createObject(QName name, Object parent) throws ObjectBuildException {
        return new GenericAttributeSet();
    }

    @Override
    public void initializeObject(GenericAttributeSet object, QName name, Attributes attributes, XMLReader reader) throws ObjectBuildException, XMLReadException {
        super.initializeObject(object, name, attributes, reader);
        attributes.getValue("codeSpace").ifPresent(object::setCodeSpace);
    }

    @Override
    public void buildChildObject(GenericAttributeSet object, QName name, Attributes attributes, XMLReader reader) throws ObjectBuildException, XMLReadException {
        super.buildChildObject(object, name, attributes, reader);

        if (CityGMLConstants.CITYGML_3_0_GENERICS_NAMESPACE.equals(name.getNamespaceURI())) {
            switch (name.getLocalPart()) {
                case "codeSpace":
                    reader.getTextContent().ifPresent(object::setCodeSpace);
                    break;
                case "genericAttribute":
                    object.getValue().add(reader.getObjectUsingBuilder(AbstractGenericAttributePropertyAdapter.class));
                    break;
            }
        } else if (CityGMLConstants.CITYGML_2_0_GENERICS_NAMESPACE.equals(name.getNamespaceURI())
                || CityGMLConstants.CITYGML_1_0_GENERICS_NAMESPACE.equals(name.getNamespaceURI()))
            object.getValue().add(new AbstractGenericAttributeProperty(reader.getObject(AbstractGenericAttribute.class)));
    }

    @Override
    public Element createElement(GenericAttributeSet object, Namespaces namespaces) throws ObjectSerializeException {
        String genericsNamespace = CityGMLSerializerHelper.getGenericsNamespace(namespaces);
        return CityGMLConstants.CITYGML_3_0_GENERICS_NAMESPACE.equals(genericsNamespace) ?
                Element.of(genericsNamespace, "GenericAttributeSet") :
                Element.of(genericsNamespace, "genericAttributeSet");
    }

    @Override
    public void initializeElement(Element element, GenericAttributeSet object, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        super.initializeElement(element, object, namespaces, writer);
        if (object.getCodeSpace() != null && !namespaces.contains(CityGMLConstants.CITYGML_3_0_CORE_NAMESPACE))
            element.addAttribute("codeSpace", object.getName());
    }

    @Override
    public void writeChildElements(GenericAttributeSet object, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        super.writeChildElements(object, namespaces, writer);
        String genericsNamespace = CityGMLSerializerHelper.getGenericsNamespace(namespaces);
        boolean isCityGML3 = CityGMLConstants.CITYGML_3_0_GENERICS_NAMESPACE.equals(genericsNamespace);

        if (isCityGML3 && object.getCodeSpace() != null)
            writer.writeElement(Element.of(genericsNamespace, "codeSpace").addTextContent(object.getCodeSpace()));

        if (object.isSetValue()) {
            for (AbstractGenericAttributeProperty property : object.getValue()) {
                if (isCityGML3)
                    writer.writeElementUsingSerializer(Element.of(genericsNamespace, "genericAttribute"), property, AbstractGenericAttributePropertyAdapter.class, namespaces);
                else if (property.getObject() != null)
                    writer.writeObject(property.getObject(), namespaces);
            }
        }
    }
}
