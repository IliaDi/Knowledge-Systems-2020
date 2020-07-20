package main.java.model;

import org.apache.jena.rdf.model.*;

import java.io.*;

public class Service {
    private final String PATH_TO_INPUT = "src/main/resources/";
    private final String PATH_TO_ONTOLOGY_BASE = "http://www.semanticweb.org/transportationOntology";

    private String inFile;
    private PrintWriter outputFilePrintWriter;

    public Service(String inFile) throws FileNotFoundException {
        this.inFile = inFile;
        this.outputFilePrintWriter = new PrintWriter("src/main/resources/calendar.trig");
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
            String service_id = parts[0];
            int monday=Integer.parseInt(parts[1]);
            int tuesday=Integer.parseInt(parts[2]);
            int wednesday= Integer.parseInt(parts[3]);
            int thursday= Integer.parseInt(parts[4]);
            int friday= Integer.parseInt(parts[5]);
            int saturday= Integer.parseInt(parts[6]);
            int sunday= Integer.parseInt(parts[7]);
            String start_date = parts[8];
            String end_date = parts[9];

            //Create a default model
            Model model = ModelFactory.createDefaultModel();

            //Create data & object properties
            Property Schedule_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#Service");
            Property startDate_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceStartDate");
            Property endDate_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceEndDate");
            Property monday_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceMonday");
            Property tuesday_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceTuesday");
            Property wednesday_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceWednesday");
            Property thursday_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceThursday");
            Property friday_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceFriday");
            Property saturday_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceSaturday");
            Property sunday_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#serviceSunday");

            //Create resource
            Resource resource = model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + service_id + "/", Schedule_p);
            //Add data properties to resource
            Statement statement = model.createStatement(resource, startDate_p, start_date);
            model.add(statement);
            statement = model.createStatement(resource, endDate_p, end_date);
            model.add(statement);
            model.addLiteral(resource, monday_p, monday);
            model.addLiteral(resource, tuesday_p, tuesday);
            model.addLiteral(resource, wednesday_p, wednesday);
            model.addLiteral(resource, thursday_p, thursday);
            model.addLiteral(resource, friday_p, friday);
            model.addLiteral(resource, saturday_p, saturday);
            model.addLiteral(resource, sunday_p, sunday);

            //Export current model to TRIG
            model.write(outputFilePrintWriter, "TRIG");
            //Close current model
            model.close();
        }
        br.close();

        outputFilePrintWriter.close();

    }

}
