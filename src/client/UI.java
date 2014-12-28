package client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import javax.swing.JFrame;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.ComponentOrientation;

import javax.swing.JSplitPane;

import java.awt.Component;
import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.SwingConstants;

import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument.Content;
import javax.swing.UIManager;

import java.awt.Cursor;

import org.json.JSONException;

import Common.src.Logger;

public class UI {
	private JFrame Minet;
    private static JSplitPane splitPane = new JSplitPane();
    private static JSplitPane splitPane_1 = new JSplitPane();
    private static JSplitPane splitPane_2 = new JSplitPane();
    private static JSplitPane splitPane_3 = new JSplitPane();
    private static JSplitPane splitPane_4 = new JSplitPane();
    private static JSplitPane splitPane_5 = new JSplitPane();
    private static JSplitPane splitPane_6 = new JSplitPane();
    private static JLabel chat = new JLabel("");
    public static JList list_usr = new JList();
    public static JList roomList = new JList();
    public static JList recordsList = new JList();
	public static UIUserModel userListModel = new UIUserModel();
	public static UIRoomModel roomListModel = new UIRoomModel();
	public static UIRecordModel recordModel = new UIRecordModel();
    public static JTextArea text_input = new JTextArea();

    public static void main(String[] args) throws IOException, JSONException {
        Thread connectionThread = new Thread(new Connetction());
        connectionThread.start();
        login();
    }

    public static void login() throws IOException, JSONException {
        String name = null;
        while(name == null) {
            name = JOptionPane.showInputDialog("请输入你的用户名：" + System.getProperty("file.encoding")); 
            Client.login(name);
        }
    }

    public static class Connetction implements Runnable {
        @Override
        public void run() {
            try {
                Client.init();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showMainLayout() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Minet window = new Minet();
					window.Minet.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }

    public static class Minet {
        private JFrame Minet;

        public Minet() {
            initialize();
        }

        private void initialize() {
            initLayout();
            initUserList();
            initRoomList();
            initChatRecord();
            initSendTextArea();
        }

        private void initSendTextArea() {
            // 输入文本框
            text_input.setSelectedTextColor(new Color(255, 255, 255));
            text_input.setSelectionColor(new Color(100, 149, 237));
            text_input.setTabSize(4);
            text_input.setText("请输入...");
            text_input.setForeground(new Color(0, 0, 0));
            text_input.setBorder(null);
            text_input.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
            text_input.setBackground(new Color(248, 248, 255));
            text_input.setAlignmentY(Component.TOP_ALIGNMENT);
            text_input.setAlignmentX(Component.LEFT_ALIGNMENT);
            splitPane_6.setLeftComponent(text_input);
            text_input.addKeyListener(new KeyListener() {
                boolean isEnter = false;
                
                @Override
                public void keyTyped(KeyEvent e) {}
                
                @Override
                public void keyReleased(KeyEvent e) {
                    if (isEnter) {
                        text_input.setText("");
                        isEnter = false;
                    }
                }
                
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        getContentAndSend();
                        isEnter = true;
                    }
                }

            });

            JButton button_send = new JButton("发射！");
            button_send.setIcon(new ImageIcon(getClass().getResource("/send.png")));
            button_send.setBackground(new Color(147, 112, 219));
            button_send.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            splitPane_6.setRightComponent(button_send);
            button_send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getContentAndSend();
                    text_input.setText("");
                }
            });
        }

        protected void sendContent(String content) {
            content = decompose(content);
            try {
                if (Client.currentChatType == Client.USER) {
                    Client.p2pChat(content);
                } else if (Client.currentChatType == Client.GROUP) {
                    Client.groupChat(content);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            updateRecordListHeight();
        }

        private void initChatRecord() {
            // 聊天纪录显示框的标题
            chat.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(255, 0, 0)));
            chat.setForeground(new Color(0, 0, 0));
            chat.setOpaque(true);
            chat.setIcon(new ImageIcon(getClass().getResource("/chat.png")));
            chat.setBackground(new Color(230, 230, 250));
            chat.setHorizontalAlignment(SwingConstants.CENTER);
            chat.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            splitPane_5.setLeftComponent(chat);

            JScrollPane record = new JScrollPane();
            record.setBackground(new Color(248, 248, 255));
            record.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
            record.setBorder(null);
            splitPane_5.setRightComponent(record);

            recordsList.setVisibleRowCount(1);
            recordsList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            recordsList.setModel(recordModel);
            recordsList.setVerifyInputWhenFocusTarget(false);
            recordsList.setValueIsAdjusting(true);
            recordsList.setName("");
            recordsList.disable();
            recordsList.setLayoutOrientation(JList.VERTICAL_WRAP);
            recordsList.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
            record.setViewportView(recordsList);
        }

        private void initLayout() {
            Minet = new JFrame();
            Minet.setBackground(new Color(255, 255, 255));
            Minet.getContentPane().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
            Minet.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
            Minet.getContentPane().add(splitPane);
            Minet.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
            Minet.setIconImage(Toolkit.getDefaultToolkit().getImage("logo.jpg"));

            Minet.setResizable(false);
            Minet.setTitle("Minet : " + Client.username);
            Minet.setBounds(100, 100, 560, 400);
            Minet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            splitPane.setBorder(null);
            splitPane.setDividerSize(0);
            splitPane.setBackground(new Color(255, 255, 255));

            splitPane_1.setBorder(null);
            splitPane_1.setDividerSize(0);
            splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane.setLeftComponent(splitPane_1);

            splitPane_2.setBorder(null);
            splitPane_2.setDividerSize(0);
            splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane_1.setLeftComponent(splitPane_2);

            splitPane_4.setBorder(null);
            splitPane_4.setDividerSize(0);
            splitPane_4.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane.setRightComponent(splitPane_4);

            splitPane_5.setDividerSize(0);
            splitPane_5.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane_4.setLeftComponent(splitPane_5);

            splitPane_2.setDividerLocation(20);
            splitPane_3.setBorder(null);
            splitPane_3.setDividerSize(0);
            splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane_1.setRightComponent(splitPane_3);

            splitPane_6.setDividerSize(0);
            splitPane_6.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane_4.setRightComponent(splitPane_6);
            splitPane_6.setDividerLocation(104);
            splitPane_4.setDividerLocation(233);
            splitPane.setDividerLocation(135);

        }

        private void initUserList() {
            // 用户列表标题
            JLabel UsrLabel = new JLabel("在线用户");
            UsrLabel.setDisplayedMnemonic(KeyEvent.VK_CONTROL);
            UsrLabel.setOpaque(true);
            UsrLabel.setIcon(new ImageIcon(getClass().getResource("/usr.png")));
            UsrLabel.setBackground(new Color(173, 216, 230));
            UsrLabel.setForeground(new Color(0, 0, 0));
            UsrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            UsrLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
            splitPane_2.setLeftComponent(UsrLabel);

            JScrollPane scrollPane_usr = new JScrollPane();
            scrollPane_usr.setForeground(new Color(255, 255, 255));
            scrollPane_usr.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
            scrollPane_usr.setAutoscrolls(true);
            scrollPane_usr.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            scrollPane_usr.setBackground(new Color(255, 255, 255));
            scrollPane_usr.setBorder(new LineBorder(new Color(130, 135, 144)));
            scrollPane_usr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            splitPane_2.setRightComponent(scrollPane_usr);

            // 用户列表
            list_usr.setVisibleRowCount(Client.users.length());
            list_usr.setAutoscrolls(true);
            list_usr.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            list_usr.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            list_usr.setModel(userListModel);
            list_usr.setVerifyInputWhenFocusTarget(false);
            list_usr.setValueIsAdjusting(true);
            list_usr.setSelectionForeground(Color.BLACK);
            list_usr.setSelectionBackground(new Color(240, 248, 255));
            list_usr.setName("");
            list_usr.setLayoutOrientation(JList.VERTICAL_WRAP);
            list_usr.setForeground(Color.WHITE);
            list_usr.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            list_usr.setFixedCellWidth(100);
            list_usr.setFixedCellHeight(20);
            list_usr.setBackground(new Color(25, 25, 112));
            list_usr.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    String userName = (String) list_usr.getSelectedValue();
                    if (userName == null) return;
                    roomList.clearSelection();
                    Logger.log(userName);
                    Client.currentChatType = Client.USER;
                    Client.currentTargetName = userName;
                    refreshChat();
                }
            });
            scrollPane_usr.setViewportView(list_usr);

        }

        public static void refreshChat() {
            Client.updateCurrentRecordsCreateRecordsIfNotExist();
            chat.setText(Client.currentTargetName);
            updateRecordListHeight();
        }

        private void getContentAndSend() {
            String content = text_input.getText();
            if (content.length() == 0) return;
            sendContent(content);
        }

        private void initRoomList() {
            // 聊天室列表标题
            JLabel GroupLabel = new JLabel(decompose("群组列表"));
            GroupLabel.setOpaque(true);
            GroupLabel.setIcon(new ImageIcon(getClass().getResource("/group.png")));
            GroupLabel.setForeground(new Color(0, 0, 0));
            GroupLabel.setDisplayedMnemonic(KeyEvent.VK_CONTROL);
            GroupLabel.setBackground(new Color(173, 216, 230));
            GroupLabel.setHorizontalAlignment(SwingConstants.CENTER);
            GroupLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
            splitPane_3.setLeftComponent(GroupLabel);

            JSplitPane splitPane_7 = new JSplitPane();
            splitPane_7.setBorder(null);
            splitPane_7.setBackground(new Color(245, 245, 245));
            splitPane_7.setDividerSize(0);
            splitPane_7.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane_3.setRightComponent(splitPane_7);

            // 新建聊天室
            JButton button_new = new JButton("创建新群组");
            button_new.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    String roomName = JOptionPane.showInputDialog("请输入新建群组名称");
                    if (roomName != null) {
                        try {
                            Sender.createGroupChat(roomName);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }	
                }
            });
            button_new.setIcon(new ImageIcon(getClass().getResource("/new.png")));
            button_new.setForeground(new Color(0, 0, 0));
            button_new.setBackground(new Color(255, 255, 255));
            button_new.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
            splitPane_7.setRightComponent(button_new);

            JScrollPane scrollPane_group = new JScrollPane();
            scrollPane_group.setBorder(new LineBorder(new Color(130, 135, 144)));
            scrollPane_group.setForeground(new Color(255, 255, 255));
            scrollPane_group.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
            scrollPane_group.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            scrollPane_group.setBackground(new Color(255, 255, 255));
            scrollPane_group.setAutoscrolls(true);
            scrollPane_group.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            splitPane_7.setLeftComponent(scrollPane_group);

            roomList.setVisibleRowCount(Client.allRooms.length());
            roomList.setValueIsAdjusting(true);
            roomList.setSelectionForeground(Color.BLACK);
            roomList.setSelectionBackground(new Color(240, 248, 255));
            roomList.setModel(roomListModel);
            roomList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    String roomName = (String) roomList.getSelectedValue();
                    if (roomName == null) return;
                    try {
                        Client.enterGroupChat(roomName);
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    list_usr.clearSelection();
                    Client.currentChatType = Client.GROUP;
                    Client.currentTargetName = roomName;
                    refreshChat();
                    Logger.log(roomName);
                }
            });

            roomList.setSelectedIndex(1);
            roomList.setLayoutOrientation(JList.VERTICAL_WRAP);
            roomList.setForeground(Color.WHITE);
            roomList.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            roomList.setFixedCellWidth(100);
            roomList.setFixedCellHeight(20);
            roomList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            roomList.setBackground(new Color(25, 25, 112));
            scrollPane_group.setViewportView(roomList);
            splitPane_7.setDividerLocation(173);
            splitPane_3.setDividerLocation(20);
            splitPane_1.setDividerLocation(145);
        }
    }

    public static void updateRecordListHeight() {
        recordModel.refresh();
        int size = Client.currentRecords.size();
        recordsList.setVisibleRowCount(size);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                recordsList.ensureIndexIsVisible(size - 1);
            }
        });
    }

	public static String decompose(String s) { 
//	    String normalized = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
//	    String accentsgone = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
//	    return accentsgone;
	    return s;
	}
}
