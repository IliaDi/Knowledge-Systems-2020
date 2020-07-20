package main.java.model;

import org.apache.jena.rdf.model.*;

import java.io.*;


public class Agency {
    private final String PATH_TO_INPUT = "src/main/resources/";
    private final String PATH_TO_ONTOLOGY_BASE = "http://www.semanticweb.org/transportationOntology";

    private String inFile;
    private PrintWriter outputFilePrintWriter;

    public Agency(String inFile) throws FileNotFoundException {
        this.inFile = inFile;
        this.outputFilePrintWriter = new PrintWriter("src/main/resources/agency.trig");
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
            String agency_name = parts[0];
            String agency_url = parts[1];

            //Create a default model
            Model model = ModelFactory.createDefaultModel();

            //Create data & object properties
            Property Agency_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#Agency");
            Property agencyName_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#agencyName");
            Property agencyUrl_p = model.createProperty(PATH_TO_ONTOLOGY_BASE + "#agencyUrl");

            //Create resource
            Resource resource = model.createResource(PATH_TO_ONTOLOGY_BASE + "/" + agency_name + "/", Agency_p);
            //Add data properties to resource
            Statement statement = model.createStatement(resource, agencyUrl_p, agency_url);
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