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

package implementing_ades.model;

import org.citygml4j.core.model.building.ADEOfAbstractBuilding;
import org.xmlobjects.gml.model.measures.Area;
import org.xmlobjects.model.ChildList;

import java.util.List;

public class BuildingProperties extends ADEOfAbstractBuilding {
    private String ownerName;
    private Area floorArea;
    private EnergyPerformanceCertificationProperty energyPerformanceCertification;
    private List<BuildingUndergroundProperty> buildingUndergrounds;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Area getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(Area floorArea) {
        this.floorArea = asChild(floorArea);
    }

    public EnergyPerformanceCertificationProperty getEnergyPerformanceCertification() {
        return energyPerformanceCertification;
    }

    public void setEnergyPerformanceCertification(EnergyPerformanceCertificationProperty energyPerformanceCertification) {
        this.energyPerformanceCertification = asChild(energyPerformanceCertification);
    }

    public List<BuildingUndergroundProperty> getBuildingUndergrounds() {
        if (buildingUndergrounds == null) {
            buildingUndergrounds = new ChildList<>(this);
        }

        return buildingUndergrounds;
    }

    public boolean isSetBuildingUndergrounds() {
        return buildingUndergrounds != null && !buildingUndergrounds.isEmpty();
    }

    public void setBuildingUndergrounds(List<BuildingUndergroundProperty> buildingUndergrounds) {
        this.buildingUndergrounds = asChild(buildingUndergrounds);
    }
}
