package main.java.model;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        String filename = "routes.txt";
        try {
            Routes r = new Routes(filename);
            r.exportToTRIG();
            filename = "agency.txt";
            Agency a = new Agency(filename);
            a.exportToTRIG();
            filename = "trips.txt";
            Trips t = new Trips(filename);
            t.exportToTRIG();
            filename = "stops.txt";
            Stops s = new Stops(filename);
            s.exportToTRIG();
            filename = "stop_times.txt";
            StopTimes st = new StopTimes(filename);
            st.exportToTRIG();
            filename = "calendar.txt";
            Service d = new Service(filename);
            d.exportToTRIG();
        } catch (IOException e) {
            System.out.print("oops!");
        }
    }
}
