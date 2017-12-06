package service;

import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.core.geometry.ogc.OGCMultiPolygon;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

import javax.jws.WebParam;
import java.io.InputStream;

public class LoadData {
    private static final LoadData instance = new LoadData();
    private final String NS = "http://www.kde5.com/ontology#";

    private LoadData() {
    }

    public static LoadData getInstance() {
        return instance;
    }

    public void loadCountyData(Model ontoModel) {
        try ( RDFConnection conn = RDFConnectionFactory.connect("http://localhost:3030/geohive/") ) {

            String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n" +
                    "PREFIX ov: <http://open.vocab.org/terms/>\n" +
                    "PREFIX ogs: <http://www.opengis.net/ont/geosparql#> \n" +
                    "PREFIX osi: <http://ontologies.geohive.ie/osi#> \n" +
                    "                             \n" +
                    "SELECT ?a ?geom ?dblink\n" +
                    "\tWHERE { \n" +
                    "\t\t?county ov:similarTo ?dblink .\n" +
                    "\t\t?county rdfs:label ?a . \n" +
                    "\t\t?county rdf:type osi:County . \n" +
                    "\t\t?county ogs:defaultGeometry ?geom . \n" +
                    "\t\t?geom ogs:asWKT ?coord \n" +
                    "\t\tFILTER LANGMATCHES(LANG(?a), \"EN\")}  ";


//            THIS IS IT
//            final String NS = "http://vivo.iu.edu/individual/";
//            Individual doc = model.getIndividual(NS+"n6356");
//            Individual ref = model.getIndividual(NS+"n6399");
//            ObjectProperty cites = model.getObjectProperty("http://purl.org/ontology/bibo/cites");
//            model.add(doc,cites,ref).write(new FileOutputStream(new File("rdf/myRDFFile.owl"));

            String osi = "http://www.co-ode.org/ontologies/pizza/pizza.owl#";

            OntClass domainConcept = ontoModel.getOntClass(osi + "DomainConcept");
            ObjectProperty objectProperty = ontoModel.getObjectProperty(osi + "hasIngredient");

            Individual ind0 = domainConcept.createIndividual( osi + "ind0");
            HasValueRestriction ukLocation =
                    ontoModel.createHasValueRestriction( null, objectProperty, ind0 );


//            Graph g = ontoModel.getGraph();

            conn.querySelect(query, (qs)-> {
                System.out.println(qs.toString());
                Literal l = qs.getLiteral("?coord");
//                Resource subject = qs.getResource("?coord") ;
                String wkt = l.getString();
//                String wkt = subject.toString();
//                Resource county = qs.getResource("county");


                OGCGeometry ogcMultiPolygon = OGCMultiPolygon.fromText(wkt);
                System.out.println(qs.getResource("?county").toString());
                System.out.println("Area is " +  ogcMultiPolygon.getEsriGeometry().calculateArea2D() * 7314);





            });

        }
    }
}
