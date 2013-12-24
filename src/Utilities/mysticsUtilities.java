/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.File;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import static utopiacopilot.UtopianStuff.dbInsert;
import static utopiacopilot.UtopianStuff.timeElapsed;

/**
 *
 * @author paul
 */
public class mysticsUtilities {
    
    public static void Time() {
        
    }
    
    
    public static HashMap<String,String[]> councilMystic(String html) {
        Document doc = Jsoup.parse(html);
        Elements tableElements = doc.select("table");
        Elements tableEles = tableElements.get(1).select("th");
        Elements tableElesValues = tableElements.get(1).select("td");
        
        
        HashMap<String, String[]> values = new HashMap<String, String[]>();


        int k = 0;
        for (int i = 3; i < tableEles.size(); i++) {
            String[] hours = new String[2];
            int x = 0;
            while (x < 2) {
                hours[x] = tableElesValues.get(k).text();
                k++;
                x++;
            }
            values.put(tableEles.get(i).text(), hours);
        }
        if (timeElapsed("Council_Mystic", 30)) {
            for (String key : values.keySet()) {
                String query = "INSERT into Council_Mystic (spell,remain,description)"
                        + " VALUES (\"" + key + "\",\"" + values.get(key)[0].split(" ")[0] + "\",\"" + values.get(key)[1] + "\")";
                dbInsert(new File("").getAbsolutePath()+"/Data/Database.db", query);
            }
        }
        return values;
    }
}
