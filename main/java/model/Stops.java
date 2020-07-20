package main.java.model;

import org.apache.jena.rdf.model.*;

import java.io.*;

public class Stops {
    private final String PATH_TO_INPUT = "src/main/resources/";
    private final String PATH_TO_ONTOLOGY_BASE = "http://www.semanticweb.org/transportationOntology";

    private String inFile;
    private PrintWriter outputFilePrintWriter;

    public Stops(String inFile) throws FileNotFoundException {
        this.inFile = inFile;
        this.outputFilePrintWriter = new PrintWriter("src/main/resources/stops.trig");
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
            String stop_id = parts[0];
            Float stop_lat = Float.parseFloat(parts[4]);
            Float stop_lon = Float.parseFloat(parts[5]);

            //Create a default model
            Model model = ModelFactory.createDefaultModel();

            //Create data & object properties
            Property Stop_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#Stop");
            Property stopLon_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#stopLon");
            Property stopLat_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#stopLat");

            //Create resource
            Resource resource = model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + stop_id + "/", Stop_p);
            //Add data properties to resource
            model.addLiteral(resource,stopLat_p, stop_lat);
            model.addLiteral(resource, stopLon_p, stop_lon);

            //Export current model to TRIG
            model.write(outputFilePrintWriter, "TRIG");
            //Close current model
            model.close();
        }
        br.close();

        outputFilePrintWriter.close();

    }

}