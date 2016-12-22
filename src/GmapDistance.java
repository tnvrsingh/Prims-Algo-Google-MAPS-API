import com.google.gson.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/*import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;*/
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class GmapDistance {

    public static void main(String args[]) throws Exception,java.io.FileNotFoundException{

        int arr[][] = new int [5][5];
        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=Bengaluru|Chennai|Mumbai|Nagpur|Varanasi&destinations=Bengaluru|Chennai|Mumbai|Nagpur|Varanasi&key=%20AIzaSyBqoFhZNCPauh37CHnUk4zv0gyY6XypTV4");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        String line, outputString = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        while ((line = reader.readLine()) != null) {
            outputString += line;
        }
        System.out.print(outputString); //json not formatted

        // readable json conversion
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(outputString);
        String prettyOutputString = gson.toJson(je); //json readable

        //   System.out.print(prettyOutputString);

        String jsonstr = outputString;

        try(  PrintWriter out = new PrintWriter( "response.json" )  ) {
            out.println(prettyOutputString);
        }

        org.json.JSONObject outerObject = new org.json.JSONObject(jsonstr);
        org.json.JSONArray innerObject = outerObject.getJSONArray("rows");

        for (int o=0, size = innerObject.length(); o < size; o++){
            org.json.JSONObject obj = innerObject.getJSONObject(o);
            org.json.JSONArray jsonArray = obj.getJSONArray("elements");
            for (int i=0, s = jsonArray.length(); i < s; i++){
                org.json.JSONObject json = jsonArray.getJSONObject(i);
                org.json.JSONObject jsonArray1 = json.getJSONObject("distance");
                arr[o][i] = jsonArray1.getInt("value")/1000;
            }

        }

        //push array of distances of cities to the array
        prims primsObject = new prims();
        primsObject.primMST(arr);
    }

}
