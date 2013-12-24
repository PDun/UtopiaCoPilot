/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import coPilotUI.groovyScripting;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import static utopiacopilot.UtopiaCoPilot.*;

/**
 *
 * @author paul
 */
public class groovyConsole {

    public static void groovyConsole(String filename) {
        Binding binding = new Binding();
        binding.setVariable("foo", new Integer(2));
        GroovyShell shell = new GroovyShell(binding);
        try {
            Object value = shell.evaluate(new File(filename));
        } catch (CompilationFailedException ex) {
            groovyScripting.log.append(groovyConsole.class.getName().toString() + "\n" + ex + "\n");
        } catch (IOException ex) {
            groovyScripting.log.append(groovyConsole.class.getName() + "\n" + Level.SEVERE + ex);
        }
    }

    public static void groovyConsoleTest(String code) {
        Binding binding = new Binding();
        setVariables(binding);
        GroovyShell shell = new GroovyShell(binding);
        try {
            Object value = shell.evaluate(code);
        } catch (CompilationFailedException ex) {
            groovyScripting.log.append(groovyConsole.class.getName().toString() + "\n" + ex + "\n");
        }
    }

    public static void setVariables(Binding binding) {
        binding.setVariable("sendButtonsMap", sendButtons);
        binding.setVariable("channelTextFieldMap", sendTextFieldsList);
        binding.setVariable("channelTextAreaMap", channelList);
        binding.setVariable("nickListMap", nickLists);
        binding.setVariable("tabMap", tabList);
        binding.setVariable("channelMap", channels);
        binding.setVariable("sessions", sessions);
        binding.setVariable("log", groovyScripting.log);
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
            Logger.getLogger(groovyConsole.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(groovyConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static void dbInsert(String database, String query) {
        try {
            ResultSet rs = null;
            Class.forName("org.sqlite.JDBC");

            String dbURL = "jdbc:sqlite:" + database;

            conn = DriverManager.getConnection(dbURL);

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.executeUpdate();

            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(groovyConsole.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(groovyConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
