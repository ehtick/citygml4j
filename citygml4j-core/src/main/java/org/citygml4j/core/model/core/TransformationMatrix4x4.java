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

import org.xmlobjects.gml.util.matrix.Matrix;

import java.util.List;

public class TransformationMatrix4x4 extends AbstractMatrix {

    public TransformationMatrix4x4() {
        super(4, 4);
    }

    public TransformationMatrix4x4(Matrix matrix) {
        this();
        setValue(matrix);
    }

    public static TransformationMatrix4x4 ofRowMajor(List<Double> values) {
        return new TransformationMatrix4x4(new Matrix(values, 4));
    }
}
