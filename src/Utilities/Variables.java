/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import coPilotUI.IRCServerUI;
import coPilotUI.coPilotMainUI;
import coPilotUI.groovyScripting;
import coPilotUI.ircChannelUI;
import java.awt.Frame;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import jerklib.Channel;
import jerklib.Session;
import utopiacopilot.UtopiaCoPilot;

/**
 *
 * @author paul
 */
public class Variables {
//MainUI Variables
    public static JTabbedPane ircTabVariable = coPilotMainUI.irc;
    public static JTabbedPane mainTabVariable = coPilotMainUI.mainTab;
    public static Frame[] framesVariable = coPilotMainUI.getFrames();
    public static JPanel mainPanelVariable = coPilotMainUI.jPanel1;
    public static JPanel tabPanelVariable = coPilotMainUI.jPanel2;
//Groovy Scripter Variables
    public static JPanel groovyPanelVariable = groovyScripting.jPanel2;
    public static JLabel scriptNameLabelVariable = groovyScripting.jLabel1;
    public static JLabel triggerLabelVariable = groovyScripting.triggerLabel;
    public static JScrollPane scriptScrollPaneVariable = groovyScripting.jScrollPane1;
    public static JScrollPane logScrollPaneVariable = groovyScripting.jScrollPane2;
    public static JButton saveScriptButtonVariable = groovyScripting.saveScript;
    public static JButton testScriptButtonVariable = groovyScripting.testScript;
    public static JTextArea logTextAreaVariable = groovyScripting.log;
    public static JTextArea scriptTextAreaVariable = groovyScripting.scriptTA;
    public static JTextField scriptNameTextFieldVariable = groovyScripting.name;
    public static JTextField scriptTriggerTextFieldVariable = groovyScripting.trigger;
//UtopiaCoPilot variables
    public static HashMap<String,JTextArea> channelsTextAreaHashMapVariable = UtopiaCoPilot.channelList;
    public static HashMap<String,Channel> channelHashMapVariable = UtopiaCoPilot.channels;
    public static HashMap<String,JList> nickJListHashMapVariable = UtopiaCoPilot.nickLists;
    public static HashMap<String,JButton> sendJButtonHashMapVariable = UtopiaCoPilot.sendButtons;
    public static HashMap<String,JTextField> sendJTextFieldHashMapVariable = UtopiaCoPilot.sendTextFieldsList;
    public static HashMap<String,Session> sessionsHashMapVariable = UtopiaCoPilot.sessions;
    public static HashMap<String,JPanel> channelTabsHashMapVariable = UtopiaCoPilot.tabList;
//IRCServerUI variables    
    public static JTextField serverJTextFieldVariable = IRCServerUI.serverSend;
    public static JTextArea serverJTextAreaVariable = IRCServerUI.serverText;
    public static JPanel serverPanelVariable = IRCServerUI.jPanel3;
    public static JScrollPane serverJScrollPaneVariable = IRCServerUI.jScrollPane1;
    public static JButton serverSendButtonVariable = IRCServerUI.serverSendButton;
//ircChannelUI variables
    public static JTextArea channelJTextAreaVariable = ircChannelUI.channelMessage;
    public static JTextField channelJTextFieldVariable = ircChannelUI.channelText;
    public static JList channelJListVariable = ircChannelUI.jList1;
    public static JPanel channelJPanelVariable = ircChannelUI.jPanel3;
    public static JScrollPane channelMessageJScrollPaneVariable = ircChannelUI.jScrollPane2;
    public static JScrollPane channelNicksJScrollPaneVariable = ircChannelUI.jScrollPane3;
    public static JButton channelSendJButtonVariable = ircChannelUI.sendButton;
    
}
