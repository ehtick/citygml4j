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

package org.citygml4j.xml.writer;

import org.citygml4j.core.model.CityGMLVersion;
import org.citygml4j.core.model.core.*;
import org.citygml4j.xml.module.citygml.AppearanceModule;
import org.citygml4j.xml.module.citygml.CoreModule;
import org.citygml4j.xml.module.gml.GMLCoreModule;
import org.citygml4j.xml.reader.FeatureInfo;
import org.xml.sax.ContentHandler;
import org.xmlobjects.serializer.ObjectSerializeException;
import org.xmlobjects.stream.XMLWriteException;
import org.xmlobjects.stream.XMLWriter;
import org.xmlobjects.stream.XMLWriterFactory;
import org.xmlobjects.util.copy.CopyBuilder;
import org.xmlobjects.xml.Element;

import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;

public class CityGMLChunkWriter extends AbstractCityGMLWriter<CityGMLChunkWriter> {
    private CityModelInfo cityModelInfo;
    private State state = State.INITIAL;

    private enum State {
        INITIAL,
        DOCUMENT_STARTED,
        CLOSED
    }

    public CityGMLChunkWriter(XMLWriter writer, CityGMLVersion version, XMLWriterFactory factory) {
        super(writer, version, factory);
    }

    public CityModelInfo getCityModelInfo() {
        if (cityModelInfo == null)
            cityModelInfo = new CityModelInfo();

        return cityModelInfo;
    }

    public CityGMLChunkWriter withCityModelInfo(CityModel cityModel) {
        if (cityModel != null)
            cityModelInfo = new CityModelInfo(cityModel);

        return this;
    }

    public CityGMLChunkWriter withCityModelInfo(FeatureInfo featureInfo) {
        if (featureInfo != null) {
            cityModelInfo = new CityModelInfo();
            cityModelInfo.setId(featureInfo.getId());
            cityModelInfo.setDescription(featureInfo.getDescription());
            cityModelInfo.setDescriptionReference(featureInfo.getDescriptionReference());
            cityModelInfo.setIdentifier(featureInfo.getIdentifier());
            cityModelInfo.setBoundedBy(featureInfo.getBoundedBy());
            cityModelInfo.setEngineeringCRS(featureInfo.getEngineeringCRS());

            if (featureInfo.isSetMetaDataProperties()) {
                cityModelInfo.setMetaDataProperties(featureInfo.getMetaDataProperties());
            }

            if (featureInfo.isSetNames()) {
                cityModelInfo.setNames(featureInfo.getNames());
            }
        }

        return this;
    }

    public void writeMember(AbstractFeature feature) throws CityGMLWriteException {
        if (feature instanceof AbstractCityObject) {
            writeMember(feature, CoreModule.of(version).getNamespaceURI(), "cityObjectMember");
        } else if (feature instanceof AbstractAppearance) {
            writeMember(feature, version != CityGMLVersion.v3_0 ?
                    AppearanceModule.of(version).getNamespaceURI() :
                    CoreModule.of(version).getNamespaceURI(), "appearanceMember");
        } else if (version == CityGMLVersion.v3_0) {
            if (feature instanceof AbstractVersion) {
                writeMember(feature, CoreModule.v3_0.getNamespaceURI(), "versionMember");
            } else if (feature instanceof AbstractVersionTransition) {
                writeMember(feature, CoreModule.v3_0.getNamespaceURI(), "versionTransitionMember");
            } else {
                writeMember(feature, CoreModule.v3_0.getNamespaceURI(), "featureMember");
            }
        } else {
            writeMember(feature, GMLCoreModule.v3_1.getNamespaceURI(), "featureMember");
        }
    }

    private void writeMember(AbstractFeature feature, String namespaceURI, String propertyName) throws CityGMLWriteException {
        switch (state) {
            case CLOSED:
                throw new CityGMLWriteException("Illegal to write features after writer has been closed.");
            case INITIAL:
                writeHeader();
        }

        try {
            XMLWriter writer = getWriter(this.writer.getContentHandler(true));
            writer.writeStartDocument();
            writer.writeStartElement(Element.of(namespaceURI, propertyName));
            writer.writeObject(feature, namespaces);
            writer.writeEndElement();
            writer.writeEndDocument();
            resetTransformer();
        } catch (XMLWriteException | ObjectSerializeException | TransformerException e) {
            throw new CityGMLWriteException("Caused by:", e);
        }
    }

    private void writeHeader() throws CityGMLWriteException {
        if (state != State.INITIAL)
            throw new CityGMLWriteException("The document has already been started.");

        try {
            CityModel cityModel;
            if (cityModelInfo != null) {
                cityModel = (CityModel) cityModelInfo.getCityModel().shallowCopy(new CopyBuilder());
                cityModel.setADEProperties(null);
            } else
                cityModel = new CityModel();

            SAXFragmentHandler fragmentHandler = new SAXFragmentHandler(
                    writer.getContentHandler(),
                    SAXFragmentHandler.Mode.HEADER);

            XMLWriter writer = getWriter(fragmentHandler);
            writer.writeStartDocument();
            writer.writeObject(cityModel, namespaces);
            writer.writeEndDocument();
            resetTransformer();
        } catch (XMLWriteException | ObjectSerializeException | TransformerException e) {
            throw new CityGMLWriteException("Caused by:", e);
        } finally {
            state = State.DOCUMENT_STARTED;
        }
    }

    private void writeFooter() throws CityGMLWriteException {
        if (state == State.INITIAL)
            writeHeader();

        try {
            CityModel cityModel = new CityModel();
            if (cityModelInfo != null) {
                cityModel.setADEProperties(cityModelInfo.getADEProperties());
            }

            SAXFragmentHandler fragmentHandler = new SAXFragmentHandler(
                    writer.getContentHandler(),
                    SAXFragmentHandler.Mode.FOOTER);

            XMLWriter writer = getWriter(fragmentHandler);
            writer.writeStartDocument();
            writer.writeObject(cityModel, namespaces);
            writer.writeEndDocument();
        } catch (XMLWriteException | ObjectSerializeException e) {
            throw new CityGMLWriteException("Caused by:", e);
        }
    }

    private XMLWriter getWriter(ContentHandler handler) {
        if (transformer == null)
            return factory.createWriter(handler);
        else {
            XMLWriter writer = factory.createWriter(transformer.getRootHandler());
            transformer.setResult(new SAXResult(handler));
            return writer;
        }
    }

    private void resetTransformer() throws TransformerException {
        if (transformer != null)
            transformer.reset();
    }

    @Override
    public void close() throws CityGMLWriteException {
        if (state == State.CLOSED)
            throw new CityGMLWriteException("The writer has already been closed.");

        try {
            writeFooter();
            super.close();
        } finally {
            state = State.CLOSED;
        }
    }
}
