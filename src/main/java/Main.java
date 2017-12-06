import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.OperatorImportFromWkt;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.WktImportFlags;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.core.geometry.ogc.OGCMultiPolygon;
import com.esri.core.geometry.ogc.OGCPolygon;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.*;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import service.LoadData;

import java.io.InputStream;
import java.util.Properties;


public class Main {

    public static void main(String ...args) {

//        load ontology model
        String filename = "/Users/apple/projects/KDE/test.owl";
        Model model = RDFDataMgr.loadModel(filename);
        OntModel ontoModel = ModelFactory.createOntologyModel();
        InputStream in = FileManager.get().open(filename);
        ontoModel.read(in, null);

        LoadData loadData = LoadData.getInstance();
        loadData.loadCountyData(ontoModel);


    }

}
