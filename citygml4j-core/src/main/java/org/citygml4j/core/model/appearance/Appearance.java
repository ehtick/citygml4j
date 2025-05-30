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

package org.citygml4j.core.model.appearance;

import org.citygml4j.core.model.core.AbstractAppearance;
import org.citygml4j.core.visitor.ObjectVisitor;
import org.xmlobjects.model.ChildList;

import java.util.List;

public class Appearance extends AbstractAppearance {
    private String theme;
    private List<AbstractSurfaceDataProperty> surfaceData;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<AbstractSurfaceDataProperty> getSurfaceData() {
        if (surfaceData == null)
            surfaceData = new ChildList<>(this);

        return surfaceData;
    }

    public boolean isSetSurfaceData() {
        return surfaceData != null && !surfaceData.isEmpty();
    }

    public void setSurfaceData(List<AbstractSurfaceDataProperty> surfaceData) {
        this.surfaceData = asChild(surfaceData);
    }

    @Override
    public void accept(ObjectVisitor visitor) {
        visitor.visit(this);
    }
}
