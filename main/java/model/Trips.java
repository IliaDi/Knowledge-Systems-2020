package main.java.model;

import org.apache.jena.rdf.model.*;

import java.io.*;

public class Trips {
    private final String PATH_TO_INPUT = "src/main/resources/";
    private final String PATH_TO_ONTOLOGY_BASE = "http://www.semanticweb.org/transportationOntology";

    private String inFile;
    private PrintWriter outputFilePrintWriter;

    public Trips(String inFile) throws FileNotFoundException {
        this.inFile = inFile;
        this.outputFilePrintWriter = new PrintWriter("src/main/resources/trips.trig");
    }
    public void exportToTRIG() throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(PATH_TO_INPUT + inFile));
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
        br.readLine(); //First line is skipped
        String row;
        while ((row = br.readLine()) != null) {
            //Parse data
            String[] parts = row.split(",");
            String trip_route_id = parts[0];
            String trip_service_id = parts[1];
            String trip_id = parts[2];
            String trip_direction = parts[3];

            //Create a default model
            Model model = ModelFactory.createDefaultModel();

            //Create data & object properties
            Property Trip_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#Trip");
            Property tripRouteId_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#onRoute");
            Property tripServiceId_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#hasService");
            Property tripDirection_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#tripDirection");

            //Create resource
            Resource resource = model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + trip_id + "/", Trip_p);
            //Add data properties to resource
            resource.addProperty(tripRouteId_p, model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + trip_route_id + "/"));
            resource.addProperty(tripServiceId_p, model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + trip_service_id + "/"));

            Statement statement = model.createStatement(resource, tripDirection_p, trip_direction);
            model.add(statement);

            //Export current model to TRIG
            model.write(outputFilePrintWriter, "TRIG");
            //Close current model
            model.close();
        }
        br.close();

        outputFilePrintWriter.close();

    }

}
