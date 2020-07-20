package main.java.model;
import org.apache.jena.rdf.model.*;

import java.io.*;


public class Routes {
    private final String PATH_TO_ROUTES_FILE = "src/main/resources/";
    private final String PATH_TO_ONTOLOGY_BASE = "http://www.semanticweb.org/transportationOntology";

    private String routesFile;
    private PrintWriter outputFilePrintWriter;

    public Routes(String routesFile) throws FileNotFoundException {
        this.routesFile = routesFile;
        this.outputFilePrintWriter = new PrintWriter( "src/main/resources/routes.trig");
    }

    public void exportToTRIG() throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(PATH_TO_ROUTES_FILE + routesFile));
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
        br.readLine(); //First line is skipped

        String row;
        while ((row = br.readLine()) != null) {
            //Parse data
            String[] parts = row.split(",");
            String route_id = parts[0];
            String route_short_name = parts[1];
            String route_long_name = parts[2];
            int route_type =Integer.parseInt(parts[4]);
            //Create a default model
            Model model = ModelFactory.createDefaultModel();
            //Create data & object properties
            Property Route_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#Route");
            Property routeShortName_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#routeShortName");
            Property routeLongName_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#routeLongName");
            Property routeType_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#routeType");
            //Create resource
            Resource resource = model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + route_id + "/", Route_p);
            //Add data properties to resource
            Statement statement = model.createStatement(resource, routeShortName_p, route_short_name);
            model.add(statement);
            statement = model.createStatement(resource, routeLongName_p, route_long_name);
            model.add(statement);
            model.addLiteral(resource, routeType_p, route_type);

            //Export current model to TRIG
            model.write(outputFilePrintWriter, "TRIG");
            //Close current model
            model.close();
        }
        br.close();

        outputFilePrintWriter.close();

    }
}