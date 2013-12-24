/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiacopilot;

import coPilotUI.coPilotMainUI;
import java.awt.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.*;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;

/**
 * @author mohadib A simple example that demonsrates how to use JerkLib
 */
public class UtopiaCoPilot implements IRCEventListener {

    private ConnectionManager manager;
    
    
    public static void main(String[] args) {
        coPilotMainUI.launchUI();
    }

    public UtopiaCoPilot() {
        /*
         * ConnectionManager takes a Profile to use for new connections.
         */
        manager = new ConnectionManager(new Profile("UtoCoPilotBot"));

        /*
         * One instance of ConnectionManager can connect to many IRC networks.
         * ConnectionManager#requestConnection(String) will return a Session object.
         * The Session is the main way users will interact with this library and IRC
         * networks
         */
        serverConnect = "irc.geekshed.net";
        Session session = manager.requestConnection(serverConnect);

        /*
         * JerkLib fires IRCEvents to notify users of the lib of incoming events
         * from a connected IRC server.
         */
        session.addIRCEventListener(this);
        

    }

    /*
     * This method is for implementing an IRCEventListener. This method will be
     * called anytime Jerklib parses an event from the Session its attached to.
     * All events are sent as IRCEvents. You can check its actual type and cast it
     * to a more specific type.
     */
    public void receiveEvent(IRCEvent e) {
        if (!tabList.containsKey(e.getSession().toString())) {
            serverName = e.getSession().toString();
            jPaneladd = new javax.swing.JPanel();
            jPaneladd.setName(serverName);
            ircServerUI = new coPilotUI.IRCServerUI();
            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPaneladd);
            jPaneladd.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ircServerUI, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE));
            jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ircServerUI, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE));
            Component[] list = jPaneladd.getComponents();
            for (Component comp : list) {
                comp.setName(serverName);
            }
            tabList.put(serverName, jPaneladd);
            sessions.put(serverName,e.getSession());
            coPilotMainUI.irc.addTab(serverConnect, jPaneladd);
        }
        if (e.getType() == Type.CONNECT_COMPLETE) {
            e.getSession().join("#UtoCoPilot");
            JPanel tab = tabList.get(e.getSession().toString());
            //e.getSession().join("#free");
            //e.getSession().join("#tactics");
        } else if (e.getType() == Type.CHANNEL_MESSAGE) {
            MessageEvent me = (MessageEvent) e;
            JTextArea jta = channelList.get(me.getChannel().getName());
            jta.append("<" + me.getNick() + ">" + me.getMessage() + "\n");
            jta.setCaretPosition(jta.getDocument().getLength());
        } else if (e.getType() == Type.JOIN) {
            JoinEvent join = (JoinEvent) e;
            DefaultListModel nicklist = new DefaultListModel();
            int i = 0;
            for (String nick : join.getChannel().getNicks()) {
                nicklist.addElement(nick);
                i++;
            }
            JList check = nickLists.get(join.getChannel().getName());
            check.setModel(nicklist);
            String channel = join.getChannelName();
            JTextArea jta = channelList.get(channel);
            jta.append(join.getNick() + " has joined " +channel+ "\n");
            jta.setCaretPosition(jta.getDocument().getLength());
        } else if (e.getType() == Type.PART) {
            PartEvent part = (PartEvent) e;
            DefaultListModel nicklist = new DefaultListModel();
            int i = 0;
            for (String nick : part.getChannel().getNicks()) {
                nicklist.addElement(nick);
                i++;
            }
            String channel = part.getChannelName();
            JList check = nickLists.get(channel);
            check.setModel(nicklist);
            JTextArea jta = channelList.get(channel);
            jta.append(part.getWho() + "(" + part.getHostName() + ") has left " + part.getChannelName() + "\n");
            if (part.getPartMessage().length() > 0) {
                jta.append("(" + part.getPartMessage() + ")" + "\n");
            }
            jta.setCaretPosition(jta.getDocument().getLength());
        } else if (e.getType() == Type.NICK_LIST_EVENT) {
            NickListEvent nicks = (NickListEvent) e;

            DefaultListModel nicklist = new DefaultListModel();
            int i = 0;
            for (String nick : nicks.getNicks()) {
                nicklist.addElement(nick);
                i++;
            }
            JList check = nickLists.get(nicks.getChannel().getName());
            check.setModel(nicklist);
        } else if (e.getType() == Type.JOIN_COMPLETE) {

            JoinCompleteEvent jce = (JoinCompleteEvent) e;
            channelName = jce.getChannel().getName();
            channels.put(channelName, jce.getChannel());

            jPaneladd = new javax.swing.JPanel();
            jPaneladd.setName(channelName);
            ircChannelUI2 = new coPilotUI.ircChannelUI();
            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPaneladd);
            jPaneladd.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ircChannelUI2, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE));
            jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ircChannelUI2, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE));
            Component[] list = jPaneladd.getComponents();
            for (Component comp : list) {
                comp.setName(channelName);
            }
            tabList.put(channelName, jPaneladd);
            coPilotMainUI.irc.addTab(channelName, jPaneladd);

            String server = e.getSession().getServerInformation().getServerName();

           } else if (e.getType() == Type.NICK_CHANGE) {
            NickChangeEvent nickChange = (NickChangeEvent) e;
            String channels = "";
            for(String key:nickLists.keySet()) {
                JList iterate = nickLists.get(key);
                for (int j=0; j < iterate.getModel().getSize(); j++) {
                    if (iterate.getModel().getElementAt(j).toString().equals(nickChange.getOldNick())){
                        channels += key;
                    }
                    if (j != iterate.getModel().getSize()-1) {
                        channels += ",";
                    }
                }
            }
            for (String channel:channels.split(",")) {
                JTextArea jta = channelList.get(channel);
                jta.append(nickChange.getOldNick() + " is now known as " +nickChange.getNewNick() + "\n");
                jta.setCaretPosition(jta.getDocument().getLength());
            }
        } else {
            channelList.get(e.getSession().toString()).append(e.getType() + " : " + e.getRawEventData()+"\n");
        }
    }

    public static void Speak(String speak, String channel) {
        Channel check = channels.get(channel);
        if (speak.startsWith("/")) {
            String command = speak.split(" ")[0].trim().toLowerCase();
            if (command.matches("(/join|/part|/nick).*")) {
                if (command.matches("(/join).*")) {
                    for (String variables : speak.split(" ")[1].split(",")) {
                        if (command.matches("(/join).*")) {
                            check.getSession().join(variables);
                        }
                    }
                }
                if (command.matches("(/part).*")) {
                    check.part(speak.replace("/part", ""));
                    JPanel tab = tabList.get(channel);
                    coPilotMainUI.irc.remove(tab);
                }
                if (command.matches("(/nick).*")) {
                    check.getSession().changeNick(speak.split(" ")[1].trim());
                    JTextArea jta = channelList.get(channel);
                    jta.append(check.getSession().getNick() + " is now known as " + speak.split(" ")[1].trim() + "\n");
                    jta.setCaretPosition(jta.getDocument().getLength());
                }
            } else {
                check.getSession().sayPrivate(command.replaceFirst("/",""),speak.split(" ")[1]);
            }
        } else {
            check.say(speak);
            JTextArea jta = channelList.get(channel);
            jta.append("<" + check.getSession().getNick() + ">" + speak + "\n");
            jta.setCaretPosition(jta.getDocument().getLength());
        }
    }

    public void addScripter() {
            jPaneladd = new javax.swing.JPanel();
            jPaneladd.setName("Groovy");
            groovyScripting = new coPilotUI.groovyScripting();
            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPaneladd);
            jPaneladd.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groovyScripting, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE));
            jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groovyScripting, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE));
            Component[] list = jPaneladd.getComponents();
            for (Component comp : list) {
                comp.setName("groovy");
            }
            coPilotMainUI.mainTab.addTab("Groovy", jPaneladd);
    }

    public static void purgeLists(String channel) {
        sendButtons.remove(channel);
        sendTextFieldsList.remove(channel);
        channelList.remove(channel);
        nickLists.remove(channel);
        
    }
    public static HashMap<String, JButton> sendButtons = new HashMap<>();
    public static HashMap<String, JTextField> sendTextFieldsList = new HashMap<>();
    public static HashMap<String, JTextArea> channelList = new HashMap<>();
    public static HashMap<String, JList> nickLists = new HashMap<>();
    public static HashMap<String, JPanel> tabList = new HashMap<>();
    public static HashMap<String, Channel> channels = new HashMap<>();
    public static HashMap<String, Session> sessions = new HashMap<>();
    
    private coPilotUI.ircChannelUI ircChannelUI2;
    private coPilotUI.IRCServerUI ircServerUI;
    private coPilotUI.groovyScripting groovyScripting;
    private javax.swing.JPanel jPaneladd;
    public static String channelName;
    public static String serverName;
    public static String serverConnect;
    public static IRCEvent event;
    
   
    public static long hoursPassed(Long time, int hours) {
        time -= time % (60*60*1000);
        time += hours*60*60*1000;
        return time;
    } 
}