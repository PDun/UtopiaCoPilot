/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 *
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package utopiacopilot;

import Utilities.mysticsUtilities;
import chrriis.common.*;
import chrriis.dj.nativeswing.swtimpl.*;
import chrriis.dj.nativeswing.swtimpl.components.*;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Christopher Deckers
 */
public class UtopianStuff extends JPanel {

    public static int throneCount = 0;

    public UtopianStuff() {
        super(new BorderLayout());
        JPanel webBrowserPanel = new JPanel(new BorderLayout());
        webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));
        final JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.navigate("http://utopia-game.com/shared/?next=/wol/game/throne");
        final String[] login = login("http://utopia-game.com/wol/game/throne");
        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
        add(webBrowserPanel, BorderLayout.CENTER);
        webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
            @Override public void loadingProgressChanged(WebBrowserEvent e) {
                if(e.getWebBrowser().getLoadingProgress()==100){
                    if (webBrowser.getResourceLocation().equalsIgnoreCase("http://utopia-game.com/shared/?next=/wol/game/throne")) {
                        webBrowser.executeJavascript("document.getElementById(\"id_username\").value='" + login[0] + "';\n"
                                + "document.getElementById(\"id_password\").value='" + login[1] + "';\n"
                                + "document.getElementsByClassName(\"button\")[0].click();");
                    }

                    if (webBrowser.getResourceLocation().equalsIgnoreCase("http://utopia-game.com/shared/lobby/")) {
                        if (throneCount == 0) {
                            webBrowser.navigate("http://utopia-game.com/wol/game/throne");
                        }
                    }
                    if (webBrowser.getResourceLocation().equalsIgnoreCase("http://utopia-game.com/wol/game/council_spells") ) {
                        
                        HashMap<String, String[]> values = new HashMap<String, String[]>(mysticsUtilities.councilMystic(webBrowser.getHTMLContent()));
                        
                        for (String key:values.keySet()) {
                            for(String value:values.get(key)) {
                                System.out.println(key+" "+value);
                            }
                            
                        }
                        
                    }
                    if (webBrowser.getResourceLocation().equalsIgnoreCase("http://utopia-game.com/wol/game/throne")) {
                        if (throneCount == 0 && timeElapsed("throne",30)) {
                            throneCount++;
                            String html = webBrowser.getHTMLContent();
                            Document doc = Jsoup.parse(html);
                            Elements tableElements = doc.select("table.two-column-stats");
                            Elements tableEles = tableElements.select("th");
                            Elements tableElesValues = tableElements.select("td");

                            ArrayList<String> values = new ArrayList<String>();

                            for (int i = 0; i < tableEles.size(); i++) {
                                System.out.println(tableEles.get(i).text());
                                System.out.println(tableElesValues.get(i).text());
                                values.add(tableElesValues.get(i).text().replace(",", ""));
                            }
                            String[] valueArray = values.toArray(new String[values.size()]);
                            throne(valueArray);

                        }
                    }
                    if (webBrowser.getResourceLocation().equalsIgnoreCase("http://utopia-game.com/wol/game/build")) {
                        String html = webBrowser.getHTMLContent();
                        HashMap<String, String[]> build = new HashMap<String, String[]>(build(html));
                    }
                }
            }
        });
    }

    /* Standard main method to try that test as a standalone application. */
    public static void main(String[] args) throws SQLException, ParseException {

        NativeInterface.open();
        UIUtils.setPreferredLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("DJ Native Swing Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new UtopianStuff(), BorderLayout.CENTER);
                frame.setSize(800, 600);
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
            }
        });
        NativeInterface.runEventPump();
    }

       
    
    public static boolean timeElapsed(String table, int time) {
        boolean value = false;
        java.util.Date current = new Date(System.currentTimeMillis() - time * 60 * 1000);
        ResultSet dateRS = dbRead(new File("").getAbsolutePath()+"/Data/Database.db", "Select max(date) from " + table + ";", null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String parse = null;
        java.util.Date parsed = null;
        try {
            dateRS.next();
            parse = dateRS.getString(1);
            parsed = sdf.parse(parse);
            conn.close();
            value = current.after(parsed);
        } catch (SQLException ex) {
            Logger.getLogger(UtopianStuff.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(UtopianStuff.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public static String[] login(String server) {
        ArrayList<String> login = new ArrayList<String>();
        try {
            ResultSet rs = dbRead(new File("").getAbsolutePath()+"/Data/Database.db", "Select * from userinformation where server = '" + server + "'", null);
            rs.next();
            login.add(rs.getString("username"));
            login.add(rs.getString("password"));
            login.add(rs.getString("server"));
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(UtopianStuff.class.getName()).log(Level.SEVERE, null, ex);
        }
        return login.toArray(new String[3]);
    }

    public static void throne(String[] values) {
        String query = "INSERT into throne "
                + "(race,soldiers,ruler,off,land,def,peasants,elites,"
                + "buildeff,thieves,stealth,money,wizards,mana,food,horses,runes"
                + ",prisoners,trade,offpoints,networth,defpoints) "
                + "VALUES ('" + values[0] + "','" + values[1] + "','" + values[2] + "','" + values[3] + "','" + values[4] + "','"
                + values[5] + "','" + values[6] + "','" + values[7] + "','" + values[8] + "','" + values[9].split("\\(")[0] + "','"
                + values[9].split("\\(")[1].substring(0, values[9].split("\\(")[1].length() - 1) + "','" + values[10] + "','" + values[11].split("\\(")[0] + "','"
                + values[11].split("\\(")[1].substring(0, values[11].split("\\(")[1].length() - 1) + "','" + values[12] + "','" + values[13] + "','" + values[14] + "','" + values[15]
                + "','" + values[16] + "','" + values[17] + "','" + values[18] + "','" + values[19] + "')";

        dbInsert(new File("").getAbsolutePath()+"/Data/Database.db",query);
    }
    
    public static HashMap<String,String[]> science(String html) {
        Document doc = Jsoup.parse(html);
        Elements tableElements = doc.select("table.two-column-stats");
        Elements tableEles = tableElements.get(0).select("th");
        Elements tableElesValues = tableElements.get(0).select("td");
        Elements tableElements2 = doc.select("tbody");
        Elements tableEles2 = tableElements2.get(3).select("th");
        Elements tableElesValues2 = tableElements2.get(3).select("td");
        Elements tableElements3 = doc.select("option[selected=selected]");
        
        HashMap<String, String[]> values = new HashMap<String, String[]>();

        for (int i = 0; i < tableEles.size() && i < 4; i++) {
            String value1 = tableElesValues.get(i).text();
            values.put(tableEles.get(i).text(), new String[]{value1});
        }
        int k = 0;
        for (int i = 0; i < tableEles2.size(); i++) {
            String value1 = tableElesValues2.get(k).text();
            k++;
            String value2 = tableElesValues2.get(k).text();
            k++;
            String value3 = tableElesValues2.get(k).text();
            values.put(tableEles2.get(i).text(), new String[]{value1, value2,value3});
            k++;
            k++;
        }
        values.put("ResearchRate",tableElements3.get(0).text().split("-"));
        return values;
    }
    
    public static HashMap<String,String[]> military(String html){
        Document doc = Jsoup.parse(html);
        Elements tableElements3 = doc.select("option[selected=selected]");
        Elements tableElements = doc.select("table");
        Elements tableEles = tableElements.get(1).select("th");
        Elements tableElesValues = tableElements.get(1).select("td");
        Elements tableElements2 = doc.select("tbody");
        Elements tableEles2 = tableElements2.get(3).select("th");
        Elements tableElesValues2 = tableElements2.get(3).select("td");
        Elements tableEles3 = tableElements.get(2).select("th");
        Elements tableElesValues3 = tableElements.get(2).select("input");
        Elements tableEles4 = tableElements.get(1).select("th");
        Elements tableElesValues4 = tableElements.get(1).select("td");
        
        HashMap<String, String[]> values = new HashMap<String, String[]>();

        for (int i = 0; i < tableEles.size() && i < 4; i++) {
            String value1 = tableElesValues.get(i).text();
            values.put(tableEles.get(i).text(), new String[]{value1});
        }
        for (int i = 0; i < tableEles3.size() && i < 4; i++) {
            String value1 = tableElesValues3.attr("value");
            values.put(tableEles3.get(i).text(), new String[]{value1});
        }
        for (int i = 0; i < tableEles.size() && i < 4; i++) {
            String value1 = tableElesValues.get(i).text();
            values.put(tableEles.get(i).text(), new String[]{value1});
        }
        int k = 0;
        for (int i = 0; i < tableEles2.size(); i++) {
            String value1 = tableElesValues2.get(k).text();
            k++;
            String value2 = tableElesValues2.get(k).text();
            k++;
            String value3 = tableElesValues2.get(k).text();
            k++;
            String value4 = tableElesValues2.get(k).text();
            values.put(tableEles2.get(i).text(), new String[]{value1, value2,value3,value4});
            k++;
            k++;
        }
        return values;
    }
    

    
    public static HashMap<String,String[]> councilMilitary(String html) {
        Document doc = Jsoup.parse(html);
        Elements tableElements = doc.select("table");
        Elements tableEles = tableElements.get(1).select("th");
        Elements tableElesValues = tableElements.get(1).select("td");
        Elements tableElements2 = doc.select("tbody");
        Elements tableElesValues2 = tableElements2.get(3).select("th");
        Elements tableEles2 = tableElements2.get(3).select("td");
        Elements tableEles3 = tableElements.get(2).select("th");
        Elements tableElesValues3 = tableElements.get(2).select("td");
        
        
        HashMap<String, String[]> values = new HashMap<String, String[]>();

        for (int i = 0; i < tableEles.size(); i++) {
            String value1 = tableElesValues.get(i).text();
            values.put(tableEles.get(i).text(), new String[]{value1});
        }
        int k = -1;
        for (int i = 5; i < tableEles3.size(); i++) {
            k++;
            String value1 = tableElesValues3.get(k).text();
            k++;
            String value2 = tableElesValues3.get(k).text();
            k++;
            String value3 = tableElesValues3.get(k).text();
            k++;
            String value4 = tableElesValues3.get(k).text();
            k++;
            String value5 = tableElesValues3.get(k).text();
            values.put("Army "+tableEles3.get(i).text(), new String[]{value1,value2,value3,value4,value5});
        }
        
        k = 0;
        for (int i = 0; i < tableElesValues2.size(); i++) {
            String[] hours = new String[24];
            int x = 0;
            while (x < 24) {
                hours[x] = tableEles2.get(k).text();
                k++;
                x++;
            }
            values.put(tableElesValues2.get(i).text(), hours);
        }
        return values;
    }
    
    public static HashMap<String,String[]> councilBuild(String html) {
        Document doc = Jsoup.parse(html);
        Elements tableElements = doc.select("table");
        Elements tableEles = tableElements.get(1).select("th");
        Elements tableElesValues = tableElements.get(1).select("td");
        Elements tableElements2 = doc.select("tbody");
        Elements tableElesValues2 = tableElements2.get(4).select("th");
        Elements tableEles2 = tableElements2.get(4).select("td");
        Elements tableEles3 = tableElements.get(2).select("th");
        Elements tableElesValues3 = tableElements.get(2).select("td");
        
        
        HashMap<String, String[]> values = new HashMap<String, String[]>();

        for (int i = 0; i < tableEles.size(); i++) {
            String value1 = tableElesValues.get(i).text();
            values.put(tableEles.get(i).text(), new String[]{value1});
        }
        int k = -1;
        for (int i = 4; i < tableEles3.size(); i++) {
            k++;
            String value1 = tableElesValues3.get(k).text();
            k++;
            String value2 = tableElesValues3.get(k).text();
            k++;
            String value3 = tableElesValues3.get(k).text();
            values.put("Effects "+tableEles3.get(i).text(), new String[]{value1,value2,value3});
        }
        
        k = 0;
        for (int i = 0; i < tableElesValues2.size(); i++) {
            String[] hours = new String[24];
            int x = 0;
            while (x < 24) {
                hours[x] = tableEles2.get(k).text();
                k++;
                x++;
            }
            for (String hour: hours ) {
                System.out.println(hour);
            }
            values.put(tableElesValues2.get(i).text(), hours);
        }
        return values;
    }
    
    public static HashMap<String,String[]> build(String html) {
        Document doc = Jsoup.parse(html);
        Elements tableElements = doc.select("tbody");
        System.out.println(tableElements.size());
        Elements tableEles = tableElements.get(3).select("th");
        Elements tableElesValues = tableElements.get(3).select("td");

        HashMap<String, String[]> values = new HashMap<String, String[]>();

        int k = 0;
        for (int i = 0; i < tableEles.size(); i++) {
            String value1 = tableElesValues.get(k).text();
            k++;
            String value2 = tableElesValues.get(k).text();
            values.put(tableEles.get(i).text(), new String[]{value1, value2});
            k++;
            k++;
        }
        return values;
    }
    
    
    public static Connection conn = null;

    public static ResultSet dbRead(String database, String query, String[] values) {

        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");

            String dbURL = "jdbc:sqlite:" + database;

            conn = DriverManager.getConnection(dbURL);

            stmt = conn.createStatement();

            if (stmt.execute(query)) {
                rs = stmt.getResultSet();
            } else {
                System.err.println("select failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtopianStuff.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UtopianStuff.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static void dbInsert(String database, String query) {

        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");

            String dbURL = "jdbc:sqlite:" + database;

            conn = DriverManager.getConnection(dbURL);

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.executeUpdate();

            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UtopianStuff.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UtopianStuff.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}