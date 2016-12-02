package com.crossover.trial.weather;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {

    /** end point to supply updates */
    private WebTarget collect;

    public AirportLoader() {
        Client client = ClientBuilder.newClient();
        collect = client.target("http://localhost:9090/collect"); //CR: fix port
    }

    public void upload(InputStream airportDataStream) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(airportDataStream));
        String l = null;
        try {
            while ( (l = br.readLine()) != null) {
                String[] split = l.split(",");

                String strPathTmp = "/airport/"+split[4]+"/"+split[6]+"/"+split[7];
                String strPath = strPathTmp.replace("\"", "");

                WebTarget path = collect.path(strPath);
                Response response = path.request().post(Entity.entity("", MediaType.TEXT_PLAIN));
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) throws IOException{
        File airportDataFile = new File(args[0]);
        if (!airportDataFile.exists() || airportDataFile.length() == 0) {
            System.err.println(airportDataFile + " is not a valid input");
            System.exit(1);
        }

        AirportLoader al = new AirportLoader();
        al.upload(new FileInputStream(airportDataFile));
        System.exit(0);
    }
}
