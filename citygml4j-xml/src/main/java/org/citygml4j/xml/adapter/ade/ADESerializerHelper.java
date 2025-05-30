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

package org.citygml4j.xml.adapter.ade;

import org.citygml4j.core.model.ade.ADEProperty;
import org.citygml4j.core.model.ade.generic.ADEGenericProperty;
import org.xmlobjects.serializer.ObjectSerializeException;
import org.xmlobjects.stream.XMLWriteException;
import org.xmlobjects.stream.XMLWriter;
import org.xmlobjects.xml.Element;
import org.xmlobjects.xml.Namespaces;

public class ADESerializerHelper {

    public static void writeADEProperty(Element element, ADEProperty property, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        if (element != null) {
            writer.writeStartElement(element);
            writeADEProperty(property, namespaces, writer);
            writer.writeEndElement();
        } else {
            writeADEProperty(property, namespaces, writer);
        }
    }

    public static void writeADEProperty(ADEProperty property, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        if (property instanceof ADEGenericProperty generic) {
            writer.writeDOMElement(generic.getValue());
        } else {
            writer.writeObject(property, namespaces);
        }
    }
}
