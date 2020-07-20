package main.java.model;

import org.apache.jena.rdf.model.*;

import java.io.*;

public class StopTimes {
    private final String PATH_TO_INPUT = "src/main/resources/";
    private final String PATH_TO_ONTOLOGY_BASE = "http://www.semanticweb.org/transportationOntology";

    private String inFile;
    private PrintWriter outputFilePrintWriter;

    public StopTimes(String inFile) throws FileNotFoundException {
        this.inFile = inFile;
        this.outputFilePrintWriter = new PrintWriter("src/main/resources/stop_times.trig");
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
        int i=0;
        while ((row = br.readLine()) != null) {
            //Parse data
            String[] parts = row.split(",");
            String trip_id = parts[0];
            String arrival_time = parts[1];
            String departure_time = parts[2];
            String stop_id = parts[3];
            i++;

            //Create a default model
            Model model = ModelFactory.createDefaultModel();

            //Create data & object properties
            Property StopTimes_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#StopTimes");
            Property timeTripId_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#onTrip");
            Property arrivalTime_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#arrivalTime");
            Property departureTime_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#departureTime");
            Property timeStopId_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#onStop");

            //Create resource
            Resource resource = model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + i + "/", StopTimes_p);
            //Add data properties to resource
            resource.addProperty(timeTripId_p, model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + trip_id + "/"));
            resource.addProperty(timeStopId_p, model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + stop_id + "/"));

            Statement statement = model.createStatement(resource, arrivalTime_p, arrival_time);
            model.add(statement);
            statement = model.createStatement(resource, departureTime_p, departure_time);
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
