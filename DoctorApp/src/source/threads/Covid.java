package source.threads;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;


/**  */
public class Covid extends Thread{
    private String dateTime;
    private Integer casesWorld;
    private Integer casesCroatia;
    private Integer curedWorld;
    private Integer curedCroatia;


    @Override
	public void run() {
        try {

            URL url = new URL("https://www.koronavirus.hr/json/?action=podaci");

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

                JSONArray data = new JSONArray(jsonString);
                JSONObject latest = data.getJSONObject(0);
                
                dateTime = latest.getString("Datum");
                casesWorld = latest.getInt("SlucajeviSvijet");
                casesCroatia = latest.getInt("SlucajeviHrvatska");
                curedWorld = latest.getInt("IzlijeceniSvijet");
                curedCroatia = latest.getInt("IzlijeceniHrvatska");
                
               /* java.lang.IllegalStateException: Not on FX application thread;
                dateTimeLabel.setText(dateTimeLabel.getText() + ": " + dateTime);
                casesWorldLabel.setText(casesWorldLabel.getText() + " " + casesWorld.toString());
                casesCroatiaLabel.setText(casesCroatiaLabel.getText() + " " + casesCroatia.toString());
                curedWorldLabel.setText(curedWorldLabel.getText() + " " + curedWorld.toString());
                curedCroatiaLabel.setText(curedCroatiaLabel.getText() + " " + curedCroatia.toString());*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	public String getDateTime() {
		return dateTime;
	}

	public Integer getCasesWorld() {
		return casesWorld;
	}

	public Integer getCasesCroatia() {
		return casesCroatia;
	}

	public Integer getCuredWorld() {
		return curedWorld;
	}

	public Integer getCuredCroatia() {
		return curedCroatia;
	}


}
