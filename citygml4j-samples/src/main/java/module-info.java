module org.citygml4j.samples {
    requires org.citygml4j.core;
    requires org.citygml4j.xml;
    requires org.citygml4j.cityjson;

    exports implementing_ades;
    exports implementing_ades.model;
    exports implementing_ades.adapter to org.xmlobjects;
    exports implementing_ades.walker to org.citygml4j.core;

    opens implementing_ades.model to org.citygml4j.core, org.xmlobjects.gml, org.xmlobjects;
    opens processing_citygml to org.citygml4j.core;
}