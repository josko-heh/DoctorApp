package source.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

public class AddEvent extends Thread {

    private static final String API_URL = "http://organizerrest.local/add.php";
    private String responseMsg;
    private boolean failed = false;
   
    private String notes;
    private String date;
    private String time;


    public AddEvent(String notes, String date, String time) {
        this.notes = notes;
        this.date = date;
        this.time = time;
    }


	@Override
    public void run() {
        try {
            URL url = new URL(API_URL);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("DateTime", date + " " + time);
            params.put("Notes", notes);

            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) 
                    postData.append('&');

                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);

            JSONObject responseJson = new JSONObject(sb.toString());
            responseMsg = responseJson.getString("message");
            
            failed = false;
        
        }catch(Exception e){
            failed = true;
            responseMsg = e.getMessage();
            e.printStackTrace();
        }
    }

    
    public String getResponseMsg() {
        return responseMsg;
    }

    public boolean isFailed() {
        return failed;
    }

}
