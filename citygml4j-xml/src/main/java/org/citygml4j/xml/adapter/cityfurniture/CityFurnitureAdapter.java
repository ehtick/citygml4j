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

package org.citygml4j.xml.adapter.cityfurniture;

import org.citygml4j.core.model.ade.generic.GenericADEOfCityFurniture;
import org.citygml4j.core.model.cityfurniture.ADEOfCityFurniture;
import org.citygml4j.core.model.cityfurniture.CityFurniture;
import org.citygml4j.core.model.deprecated.cityfurniture.DeprecatedPropertiesOfCityFurniture;
import org.citygml4j.core.util.CityGMLConstants;
import org.citygml4j.xml.adapter.CityGMLBuilderHelper;
import org.citygml4j.xml.adapter.CityGMLSerializerHelper;
import org.citygml4j.xml.adapter.ade.ADEBuilderHelper;
import org.citygml4j.xml.adapter.ade.ADESerializerHelper;
import org.citygml4j.xml.adapter.core.AbstractOccupiedSpaceAdapter;
import org.citygml4j.xml.adapter.core.ImplicitGeometryPropertyAdapter;
import org.xmlobjects.annotation.XMLElement;
import org.xmlobjects.annotation.XMLElements;
import org.xmlobjects.builder.ObjectBuildException;
import org.xmlobjects.gml.adapter.geometry.GeometryPropertyAdapter;
import org.xmlobjects.gml.adapter.geometry.aggregates.MultiCurvePropertyAdapter;
import org.xmlobjects.gml.model.geometry.GeometryProperty;
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
        @XMLElement(name = "CityFurniture", namespaceURI = CityGMLConstants.CITYGML_3_0_CITYFURNITURE_NAMESPACE),
        @XMLElement(name = "CityFurniture", namespaceURI = CityGMLConstants.CITYGML_2_0_CITYFURNITURE_NAMESPACE),
        @XMLElement(name = "CityFurniture", namespaceURI = CityGMLConstants.CITYGML_1_0_CITYFURNITURE_NAMESPACE)
})
public class CityFurnitureAdapter extends AbstractOccupiedSpaceAdapter<CityFurniture> {
    private final QName[] substitutionGroups = new QName[]{
            new QName(CityGMLConstants.CITYGML_2_0_CITYFURNITURE_NAMESPACE, "_GenericApplicationPropertyOfCityFurniture"),
            new QName(CityGMLConstants.CITYGML_1_0_CITYFURNITURE_NAMESPACE, "_GenericApplicationPropertyOfCityFurniture")
    };

    @Override
    public CityFurniture createObject(QName name, Object parent) throws ObjectBuildException {
        return new CityFurniture();
    }

    @Override
    public void buildChildObject(CityFurniture object, QName name, Attributes attributes, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (CityGMLBuilderHelper.isCityFurnitureNamespace(name.getNamespaceURI())) {
            if (CityGMLBuilderHelper.buildStandardObjectClassifier(object, name.getLocalPart(), reader))
                return;

            switch (name.getLocalPart()) {
                case "lod1Geometry":
                    GeometryProperty<?> lod1Geometry = reader.getObjectUsingBuilder(GeometryPropertyAdapter.class);
                    if (!CityGMLBuilderHelper.assignDefaultGeometry(object, 1, lod1Geometry))
                        object.getDeprecatedProperties().setLod1Geometry(lod1Geometry);
                    return;
                case "lod2Geometry":
                    GeometryProperty<?> lod2Geometry = reader.getObjectUsingBuilder(GeometryPropertyAdapter.class);
                    if (!CityGMLBuilderHelper.assignDefaultGeometry(object, 2, lod2Geometry))
                        object.getDeprecatedProperties().setLod2Geometry(lod2Geometry);
                    return;
                case "lod3Geometry":
                    GeometryProperty<?> lod3Geometry = reader.getObjectUsingBuilder(GeometryPropertyAdapter.class);
                    if (!CityGMLBuilderHelper.assignDefaultGeometry(object, 3, lod3Geometry))
                        object.getDeprecatedProperties().setLod3Geometry(lod3Geometry);
                    return;
                case "lod4Geometry":
                    object.getDeprecatedProperties().setLod4Geometry(reader.getObjectUsingBuilder(GeometryPropertyAdapter.class));
                    return;
                case "lod1TerrainIntersection":
                    object.setLod1TerrainIntersectionCurve(reader.getObjectUsingBuilder(MultiCurvePropertyAdapter.class));
                    return;
                case "lod2TerrainIntersection":
                    object.setLod2TerrainIntersectionCurve(reader.getObjectUsingBuilder(MultiCurvePropertyAdapter.class));
                    return;
                case "lod3TerrainIntersection":
                    object.setLod3TerrainIntersectionCurve(reader.getObjectUsingBuilder(MultiCurvePropertyAdapter.class));
                    return;
                case "lod4TerrainIntersection":
                    object.getDeprecatedProperties().setLod4TerrainIntersectionCurve(reader.getObjectUsingBuilder(MultiCurvePropertyAdapter.class));
                    return;
                case "lod1ImplicitRepresentation":
                    object.setLod1ImplicitRepresentation(reader.getObjectUsingBuilder(ImplicitGeometryPropertyAdapter.class));
                    return;
                case "lod2ImplicitRepresentation":
                    object.setLod2ImplicitRepresentation(reader.getObjectUsingBuilder(ImplicitGeometryPropertyAdapter.class));
                    return;
                case "lod3ImplicitRepresentation":
                    object.setLod3ImplicitRepresentation(reader.getObjectUsingBuilder(ImplicitGeometryPropertyAdapter.class));
                    return;
                case "lod4ImplicitRepresentation":
                    object.getDeprecatedProperties().setLod4ImplicitRepresentation(reader.getObjectUsingBuilder(ImplicitGeometryPropertyAdapter.class));
                    return;
                case "adeOfCityFurniture":
                    ADEBuilderHelper.addADEProperty(object, GenericADEOfCityFurniture::of, reader);
                    return;
            }
        } else if (CityGMLBuilderHelper.isADENamespace(name.getNamespaceURI())) {
            buildADEProperty(object, name, reader);
            return;
        }

        super.buildChildObject(object, name, attributes, reader);
    }

    @Override
    public void buildADEProperty(CityFurniture object, QName name, XMLReader reader) throws ObjectBuildException, XMLReadException {
        if (!ADEBuilderHelper.addADEProperty(object, name, GenericADEOfCityFurniture::of, reader, substitutionGroups))
            super.buildADEProperty(object, name, reader);
    }

    @Override
    public Element createElement(CityFurniture object, Namespaces namespaces) throws ObjectSerializeException {
        return Element.of(CityGMLSerializerHelper.getCityFurnitureNamespace(namespaces), "CityFurniture");
    }

    @Override
    public void writeChildElements(CityFurniture object, Namespaces namespaces, XMLWriter writer) throws ObjectSerializeException, XMLWriteException {
        super.writeChildElements(object, namespaces, writer);
        String cityFurnitureNamespace = CityGMLSerializerHelper.getCityFurnitureNamespace(namespaces);
        boolean isCityGML3 = CityGMLConstants.CITYGML_3_0_CITYFURNITURE_NAMESPACE.equals(cityFurnitureNamespace);

        CityGMLSerializerHelper.writeStandardObjectClassifier(object, cityFurnitureNamespace, namespaces, writer);

        if (!isCityGML3) {
            DeprecatedPropertiesOfCityFurniture properties = object.hasDeprecatedProperties() ?
                    object.getDeprecatedProperties() :
                    null;

            if (properties != null && properties.getLod1Geometry() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod1Geometry"), properties.getLod1Geometry(), GeometryPropertyAdapter.class, namespaces);
            else
                CityGMLSerializerHelper.writeDefaultGeometry(object, 1, "lod1Geometry", cityFurnitureNamespace, namespaces, writer);

            if (properties != null && properties.getLod2Geometry() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod2Geometry"), properties.getLod2Geometry(), GeometryPropertyAdapter.class, namespaces);
            else
                CityGMLSerializerHelper.writeDefaultGeometry(object, 2, "lod2Geometry", cityFurnitureNamespace, namespaces, writer);

            if (properties != null && properties.getLod3Geometry() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod3Geometry"), properties.getLod3Geometry(), GeometryPropertyAdapter.class, namespaces);
            else
                CityGMLSerializerHelper.writeDefaultGeometry(object, 3, "lod3Geometry", cityFurnitureNamespace, namespaces, writer);

            if (properties != null && properties.getLod4Geometry() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod4Geometry"), properties.getLod4Geometry(), GeometryPropertyAdapter.class, namespaces);

            if (object.getLod1TerrainIntersectionCurve() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod1TerrainIntersection"), object.getLod1TerrainIntersectionCurve(), MultiCurvePropertyAdapter.class, namespaces);

            if (object.getLod2TerrainIntersectionCurve() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod2TerrainIntersection"), object.getLod2TerrainIntersectionCurve(), MultiCurvePropertyAdapter.class, namespaces);

            if (object.getLod3TerrainIntersectionCurve() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod3TerrainIntersection"), object.getLod3TerrainIntersectionCurve(), MultiCurvePropertyAdapter.class, namespaces);

            if (properties != null && properties.getLod4TerrainIntersectionCurve() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod4TerrainIntersection"), properties.getLod4TerrainIntersectionCurve(), MultiCurvePropertyAdapter.class, namespaces);

            if (object.getLod1ImplicitRepresentation() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod1ImplicitRepresentation"), object.getLod1ImplicitRepresentation(), ImplicitGeometryPropertyAdapter.class, namespaces);

            if (object.getLod2ImplicitRepresentation() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod2ImplicitRepresentation"), object.getLod2ImplicitRepresentation(), ImplicitGeometryPropertyAdapter.class, namespaces);

            if (object.getLod3ImplicitRepresentation() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod3ImplicitRepresentation"), object.getLod3ImplicitRepresentation(), ImplicitGeometryPropertyAdapter.class, namespaces);

            if (properties != null && properties.getLod4ImplicitRepresentation() != null)
                writer.writeElementUsingSerializer(Element.of(cityFurnitureNamespace, "lod4ImplicitRepresentation"), properties.getLod4ImplicitRepresentation(), ImplicitGeometryPropertyAdapter.class, namespaces);
        }

        for (ADEOfCityFurniture property : object.getADEProperties(ADEOfCityFurniture.class))
            ADESerializerHelper.writeADEProperty(isCityGML3 ? Element.of(cityFurnitureNamespace, "adeOfCityFurniture") : null, property, namespaces, writer);
    }
}
