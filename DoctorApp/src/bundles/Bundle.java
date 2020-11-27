package bundles;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import main.Main;

abstract public class Bundle {

    public static String getProperty(String propertyName){
        Properties langProp = new Properties();

        try {
            langProp.load(new FileReader("src\\bundles\\Bundle_" +  Main.getChosenLanguage().toString() + ".properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return langProp.getProperty(propertyName);   
    }
}
