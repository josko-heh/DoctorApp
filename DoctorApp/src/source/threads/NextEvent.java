package source.threads;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;


public class NextEvent extends Thread {

    private static final String API_URL = "http://organizerrest.local/read.php";
    private JSONObject nextEvent;

    /**
     * Sets <code>this.nextEvent</code> to next event from http://organizerrest.local/index.php . 
     * If there are no events in future, latest past event is set.
     */
    @Override
    public void run() {
        try {
        
            URL url = new URL(API_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String jsonString = "";
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    jsonString += scanner.nextLine();
                }

                scanner.close();

                JSONArray eventsJsonArray = new JSONArray(jsonString);

                List<JSONObject> events = IntStream.range(0, eventsJsonArray.length())
                        .mapToObj(i -> eventsJsonArray.getJSONObject(i)).collect(Collectors.toList());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime now = LocalDateTime.now();

                nextEvent = events.stream().min((e1, e2) -> {
                    long e1diff = MINUTES.between(now, LocalDateTime.parse(e1.getString("DateTime"), formatter));
                    long e2diff = MINUTES.between(now, LocalDateTime.parse(e2.getString("DateTime"), formatter));
                    if (e1diff >= 0 && e2diff >= 0)
                        return (int) e1diff - (int) e2diff;
                    else if (e1diff < 0 && e2diff >= 0)
                        return 1;
                    else if (e1diff >= 0 && e2diff < 0)
                        return -1;
                    else
                        return e1diff < e2diff ? 1 : -1;
                }).get();
            }    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**@see #run() */
    public JSONObject getNextEvent() {
        return nextEvent;
    }

}
