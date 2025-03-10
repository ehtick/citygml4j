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

package org.citygml4j.core.model.core;

import org.citygml4j.core.model.ade.ADEObject;
import org.xmlobjects.gml.model.GMLObject;
import org.xmlobjects.gml.model.base.AbstractArrayProperty;
import org.xmlobjects.gml.model.base.AbstractInlineOrByReferenceProperty;
import org.xmlobjects.gml.model.base.AbstractInlineProperty;
import org.xmlobjects.gml.model.geometry.AbstractGeometry;
import org.xmlobjects.gml.model.geometry.Envelope;
import org.xmlobjects.gml.util.EnvelopeOptions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ADEEnvelopeBuilder {

    private ADEEnvelopeBuilder() {
    }

    static void updateEnvelope(GMLObject object, Envelope envelope, EnvelopeOptions options, Set<Object> visited) {
        Class<?> type = object.getClass();

        do {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value != null)
                        updateEnvelope(value, envelope, options, visited);
                } catch (Throwable e) {
                    throw new RuntimeException("Failed to update envelope for " + object + ".", e);
                }
            }
        } while ((type = type.getSuperclass()) != Object.class && ADEObject.class.isAssignableFrom(type));
    }

    static void updateEnvelope(Object object, Envelope envelope, EnvelopeOptions options, Set<Object> visited) {
        if (!visited.add(object))
            return;

        if (object instanceof AbstractGeometry geometry) {
            envelope.include(geometry.computeEnvelope());
        } else if (object instanceof org.xmlobjects.gml.model.feature.AbstractFeature feature) {
            envelope.include(feature.computeEnvelope(options));
        } else if (object instanceof AbstractArrayProperty<?> property) {
            if (property.isSetObjects()) {
                property.getObjects().forEach(v -> updateEnvelope(v, envelope, options, visited));
            }
        } else if (object instanceof AbstractInlineOrByReferenceProperty<?> property) {
            updateEnvelope(property.getObject(), envelope, options, visited);
        } else if (object instanceof AbstractInlineProperty<?> property) {
            updateEnvelope(property.getObject(), envelope, options, visited);
        } else if (object instanceof GMLObject gmlObject && object instanceof ADEObject) {
            updateEnvelope(gmlObject, envelope, options, visited);
        } else if (object instanceof Collection<?> collection) {
            collection.forEach(v -> updateEnvelope(v, envelope, options, visited));
        } else if (object instanceof Object[] array) {
            Arrays.stream(array).forEach(v -> updateEnvelope(v, envelope, options, visited));
        } else if (object instanceof Map<?, ?> map) {
            map.values().forEach(v -> updateEnvelope(v, envelope, options, visited));
        }
    }
}
