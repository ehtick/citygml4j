package org.citygml4j.builder.json.objects.geometry;

import java.util.ArrayList;
import java.util.List;

public class MultiLineStringType extends AbstractGeometryType {
	private final GeometryTypeName type = GeometryTypeName.MULTI_LINE_STRING;
	private List<List<Integer>> geometry = new ArrayList<>();
	
	@Override
	public GeometryTypeName getType() {
		return type;
	}
	
	public void addLineString(List<Integer> lineString) {
		if (lineString != null && lineString.size() > 0)
			geometry.add(lineString);
	}

	public List<List<Integer>> getLineStrings() {
		return geometry;
	}

	public void setLineStrings(List<List<Integer>> lineStrings) {
		if (lineStrings != null)
			geometry = lineStrings;
	}
	
}