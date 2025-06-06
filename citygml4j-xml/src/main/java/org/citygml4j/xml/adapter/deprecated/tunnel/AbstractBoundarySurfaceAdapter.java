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

package org.citygml4j.xml.adapter.deprecated.tunnel;

import org.citygml4j.core.model.ade.generic.GenericADEOfAbstractThematicSurface;
import org.citygml4j.core.model.construction.ADEOfAbstractConstructionSurface;
import org.citygml4j.core.model.construction.AbstractConstructionSurface;
import org.citygml4j.core.model.construction.AbstractFillingSurfaceProperty;
import org.citygml4j.core.model.core.ADEOfAbstractThematicSurface;
import org.citygml4j.core.model.core.AbstractThematicSurface;
import org.citygml4j.core.util.CityGMLConstants;
import org.citygml4j.xml.adapter.CityGMLBuilderHelper;
import org.citygml4j.xml.adapter.CityGMLSerializerHelper;
import org.citygml4j.xml.adapter.ade.ADEBuilderHelper;
import org.citygml4j.xml.adapter.ade.ADESerializerHelper;
import org.citygml4j.xml.adapter.construction.AbstractFillingSurfacePropertyAdapter;
import org.citygml4j.xml.adapter.core.AbstractCityObjectAdapter;
import org.xmlobjects.builder.ObjectBuildException;
import org.xmlobjects.gml.adapter.geometry.aggregates.MultiSurfacePropertyAdapter;
import org.xmlobjects.serializer.ObjectSerializeException;
import org.xmlobjects.stream.XMLReadException;
import org.xmlobjects.stream.XMLReader;
import org.xmlobjects.stream.XMLWriteException;
import org.xmlobjects.stream.XMLWriter;
import org.xmlobjects.xml.Attributes;
import org.xmlobjects.xml.Element;
import org.xmlobjects.xml.Namespaces;

import javax.xml.namespace.QName;

public abstract class AbstractBoundarySurfaceAdapter<T extends AbstractThematicSurface> extends AbstractCityObjectAdapter<T> {
    private final QName substitutionGroup = new QName(CityGMLConstants.CITYGML_2_0_TUNNEL_NAMESPACE, "_GenericApplicationPropertyOfBoundarySurface");

    @Override
    public void buildChildObject(T object, QName name, Attributes attributes, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (CityGMLBuilderHelper.isTunnelNamespace(name.getNamespaceURI())) {
            switch (name.getLocalPart()) {
                case "lod2MultiSurface":
                    object.setLod2MultiSurface(reader.getObjectUsingBuilder(MultiSurfacePropertyAdapter.class));
                    return;
                case "lod3MultiSurface":
                    object.setLod3MultiSurface(reader.getObjectUsingBuilder(MultiSurfacePropertyAdapter.class));
                    return;
                case "lod4MultiSurface":
                    object.getDeprecatedProperties().setLod4MultiSurface(reader.getObjectUsingBuilder(MultiSurfacePropertyAdapter.class));
                    return;
                case "opening":
                    if (object instanceof AbstractConstructionSurface surface)
                        surface.getFillingSurfaces().add(reader.getObjectUsingBuilder(AbstractFillingSurfacePropertyAdapter.class));
                    return;
            }
        } else if (CityGMLBuilderHelper.isADENamespace(name.getNamespaceURI())) {
            buildADEProperty(object, name, reader);
            return;
        }

        super.buildChildObject(object, name, attributes, reader);
    }

    @Override
    public void buildADEProperty(T object, QName name, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (!ADEBuilderHelper.addADEProperty(object, name, GenericADEOfAbstractThematicSurface::of, reader, substitutionGroup))
            super.buildADEProperty(object, name, reader);
    }

    @Override
    public void writeChildElements(T object, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        super.writeChildElements(object, namespaces, writer);
        String tunnelNamespace = CityGMLSerializerHelper.getTunnelNamespace(namespaces);

        if (object.getLod2MultiSurface() != null)
            writer.writeElementUsingSerializer(Element.of(tunnelNamespace, "lod2MultiSurface"), object.getLod2MultiSurface(), MultiSurfacePropertyAdapter.class, namespaces);

        if (object.getLod3MultiSurface() != null)
            writer.writeElementUsingSerializer(Element.of(tunnelNamespace, "lod3MultiSurface"), object.getLod3MultiSurface(), MultiSurfacePropertyAdapter.class, namespaces);

        if (object.hasDeprecatedProperties() && object.getDeprecatedProperties().getLod4MultiSurface() != null)
            writer.writeElementUsingSerializer(Element.of(tunnelNamespace, "lod4MultiSurface"), object.getDeprecatedProperties().getLod4MultiSurface(), MultiSurfacePropertyAdapter.class, namespaces);

        if (object instanceof AbstractConstructionSurface surface) {
            if (surface.isSetFillingSurfaces()) {
                for (AbstractFillingSurfaceProperty property : surface.getFillingSurfaces())
                    writer.writeElementUsingSerializer(Element.of(tunnelNamespace, "opening"), property, AbstractFillingSurfacePropertyAdapter.class, namespaces);
            }

            for (ADEOfAbstractConstructionSurface property : object.getADEProperties(ADEOfAbstractConstructionSurface.class))
                ADESerializerHelper.writeADEProperty(property, namespaces, writer);
        }

        for (ADEOfAbstractThematicSurface property : object.getADEProperties(ADEOfAbstractThematicSurface.class))
            ADESerializerHelper.writeADEProperty(property, namespaces, writer);
    }
}
