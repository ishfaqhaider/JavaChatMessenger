/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// 169.254.247.2
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//import cahtserver.Test;

/**
 *
 * @author Ishfaq Haider 1418-FBAS/BSSE/F11A
 */
public class ChatClient extends ChatFrame {

    /**
     * @param args the command line arguments
     */
/// Connection Related Information ///
    //  private String chatServer=new String("39.47.154.169"); // host server for this application
//    private String chatServer = new String("169.254.247.2"); // daniyal laptop
//    private String chatServer = new String("192.168.194.50"); // daniyal laptop conectify-ID
    
    
//    private String chatServer = new String("169.254.37.214"); // my laptop
   // private String chatServer = new String("192.168.194.1"); // my laptop conectify-ID
    
    // private String chatServer=new String("192.168.1.119"); 
     private String chatServer = new String("127.0.0.1"); // host server for this application
//    private String chatServer=new String("192.168.10.104"); // host server for this application
    private int port_num = 12345;
    private int connection_attemps = 25; // number of try to coonect with server
/// Other Data Members ///    
    SignInPanel sip = new SignInPanel();
    SignUpPanel sup = new SignUpPanel();
    MainChatPanel mcp = new MainChatPanel();
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private Socket client; // socket to communicate with server
    //SocketAddress socket_address=new SocketAddress() {};
    InetSocketAddress socket_address = new InetSocketAddress(chatServer, port_num);  // dont modify this line    
    private Boolean error = new Boolean(false);
    private String error_m = new String("");
    Test test_ip = new Test(); // for all other functions
    Test test_op = new Test();  // for all other functions
    Test get_input_testip = new Test(); // only use within get_input function
    Test get_input_testop = new Test(); // only use within get_input function
    Thread main_thread;
    String Curent_ID = new String("");
    String Curent_PWD = new String("");
    Test send_btn_strng = new Test();
    ArrayList<Friends> friends_list = new ArrayList();
    Boolean is_signin = new Boolean(false);
    Boolean wants_to_close = new Boolean(false);
    String previous_selected_friend = new String("");
    String curent_selected_friend = new String("");
    Boolean AddFriend_GUI_Flag = new Boolean(false);
    Boolean PasswordChange_GUI_Flag = new Boolean(false);
    Boolean Remove_Friend_Menue_Flag = new Boolean(false);
    Boolean Receive_File_GUI_Flag = new Boolean(false);
    Add_Friend adding_f = new Add_Friend(); // 
    // temprary variables for testing //
    int temp_friend = 0;
//-// File Sharing And Receiving Variables //-//
    Boolean Share_File_GUI_Flag = new Boolean(false);
    private File share_file;
    private final int share_file_length_mb = 1000; // max file length
    private final long share_file_length_b = 1024 * 1024 * share_file_length_mb; // converting in bytes
    byte[] share_bytes;
    FileInputStream share_fis;
    BufferedInputStream share_bis;
    BufferedOutputStream share_out;
    ServerSocket share_server;
    Socket share_socket;
    Thread share_thread;
//@@@@  END @@@@///    
//-// File Recieving Variables //-//
    Socket rec_socket;
    InputStream rec_is;
    FileOutputStream rec_fos;
    BufferedOutputStream rec_bos;
    byte[] rec_bytes;

//@@@@  END @@@@///    
// eof temprary variables for testing //
    public void Reset_ChatClient() // this function shows signin page
    {
        this.setVisible(false);
        is_signin = false;
        error = false;
        error_m = "";
        test_ip.Reset();
        test_op.Reset();
        send_btn_strng.Reset();
        get_input_testip.Reset();
        get_input_testop.Reset();
        Curent_ID = "";
        Curent_PWD = "";

        friends_list.clear();
        this.mcp.jList_Vector.removeAllElements();
        this.mcp.jListFriend.setListData(this.mcp.jList_Vector);
        this.mcp.jScrollPaneFriends.revalidate();
        this.mcp.jScrollPaneFriends.repaint();
        this.mcp.jTextAreaHistory.setText("");
        this.mcp.jTextAreaMessege.setText("");

        //previous_selected_friend=""; 
        //curent_selected_friend="";  // these four creating problem so coment these
        //AddFriend_GUI_Flag=false;
        //PasswordChange_GUI_Flag=false;

        this.setSize(465, 400);
        this.setLocation(500, 200);
        this.setTitle("Chat Messenger");
        this.setResizable(false);
        this.jMenuBar1.setVisible(false);
        this.jRadioButtonSignIn.setSelected(true);
        this.add(this.jPanel1, BorderLayout.NORTH);
        this.SetSIP();
        Visible_Frame();
    }

    public void Share_File_Func() {
        if (!Share_File_GUI_Flag) {
            share_thread = new Thread() {
                @Override
                public void run() {

                    {
                        Share_File_GUI_Flag = true;

                        final Share_File_GUI sfg = new Share_File_GUI();
                        sfg.jButtonCancel.setVisible(false);
                        sfg.jButtonShare.setEnabled(false);
                        sfg.jButtonSelect.setEnabled(false);
                        sfg.jLabelFilePath.setText("");
                        sfg.setVisible(false);
                        sfg.setTitle("Share File");
                        sfg.setResizable(false);
                        sfg.jListShare.setListData(mcp.jList_Vector);
                        sfg.jScrollPane1.revalidate();
                        sfg.jScrollPane1.repaint();
                        sfg.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                        sfg.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                Share_File_GUI_Flag = false;
                                sfg.dispose();
                            }
                        });
                        sfg.jListShare.addListSelectionListener(new ListSelectionListener() {
                            @Override
                            public void valueChanged(ListSelectionEvent e) {
                                if (Friend_Index((String) sfg.jListShare.getSelectedValue()) > -1 && friends_list.get(Friend_Index((String) sfg.jListShare.getSelectedValue())).is_login)
                                    sfg.jButtonSelect.setEnabled(true);
                                else
                                    sfg.jButtonSelect.setEnabled(false);
                            }
                        }); // enable select frile button on selecting a valid online friend
                        sfg.jButtonSelect.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sfg.jButtonShare.setEnabled(false);
                                JFileChooser fc = new JFileChooser();
                                if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(sfg)) {
                                    share_file = new File(fc.getSelectedFile().getAbsolutePath());
                                    if (share_file.length() > share_file_length_b) {
                                        sfg.jLabelFilePath.setText("Maximum file length: " + share_file_length_mb);
                                    } else {
                                        sfg.jLabelFilePath.setText("" + share_file.getAbsolutePath());
                                        sfg.jButtonShare.setEnabled(true);
                                    }
                                }
                            }
                        });
                        sfg.jButtonShare.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                boolean error = false;
                                Test obj = new Test();
                                obj.operator = 11;
                                obj.string_a = (String) sfg.jListShare.getSelectedValue();
                                sfg.setVisible(false);
                                share_bytes = new byte[(int) share_file.length()];
                                try {
                                    share_fis = new FileInputStream(share_file);
                                    share_bis = new BufferedInputStream(share_fis);
                                    share_server = new ServerSocket(0);
                                    obj.string_c = Integer.toString(share_server.getLocalPort());
                                    obj.string_d = share_file.getName();
                                    SendData(obj.Encode());
                                    share_socket = share_server.accept();
                                    share_out = new BufferedOutputStream(share_socket.getOutputStream());
                                    int count;
                                    while ((count = share_bis.read(share_bytes)) > 0)
                                        share_out.write(share_bytes, 0, count);
                                    share_out.flush();
                                    share_out.close();
                                    share_fis.close();
                                    share_bis.close();
                                    share_socket.close();
                                    share_server.close();
                                       // JOptionPane.showMessageDialog(null, "Yor file send "); //--
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                                    error = true;
                                } catch (IOException ex) {
                                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                                    error = true;
                                }
                                Share_File_GUI_Flag = false;
                                sfg.dispose();
                            }
                        });
                        sfg.setVisible(true);
                    }
                }
            };
//
            share_thread.start();
        } // eof main if
    }

    public void Remove_Friend_Menue() {
        if (!Remove_Friend_Menue_Flag) {
            Remove_Friend_Menue_Flag = true;
            final Remove_Friend_Window rfg = new Remove_Friend_Window();
            rfg.setVisible(false);
            rfg.setTitle("Remove Friend");
            rfg.setResizable(false);
            rfg.jListRemove.setListData(this.mcp.jList_Vector);
            rfg.jScrollPane1.revalidate();
            rfg.jScrollPane1.repaint();
            rfg.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            rfg.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Remove_Friend_Menue_Flag = false;
                    rfg.dispose();
                }
            });
            rfg.jButtonRemove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String frnd_name = new String("" + (String) rfg.jListRemove.getSelectedValue());
                    if (!frnd_name.equals("null") && (Friend_Index(frnd_name) > -1)) {
                        Test obj = new Test();
                        obj.operator = 7; // send_type_7 - Send Friend Removal ACK
                        obj.string_a = frnd_name;
                        try {
                            SendData(obj.Encode());
                            Remove_Friend_L_G(frnd_name);
                            //friends_list.remove(Friend_Index(frnd_name));
                            Remove_Friend_Menue_Flag = false;
                            rfg.dispose();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(rootPane, "Request can not be send IOException(AddFriend_GUI())");
                            adding_f.jTextField1.setText("");
                        }
                    }// eof if
                }
            });
            rfg.setVisible(true);
        }
    }

    public void Receive_Req(String frnd_name) {
        boolean ret = false;
        final Receive_Request receive = new Receive_Request();
        receive.setTitle("Friend Request");
        receive.setVisible(false);
        receive.jLabelFriend.setText(frnd_name);
        final Test ttt = new Test();
        ttt.string_a = frnd_name; // for replying server_inputs_types(5,6)
        receive.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        receive.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ttt.operator = 6;
                try {
                    SendData(ttt.Encode());
                } catch (IOException ex) {
                }
                receive.dispose();
            }
        });

        receive.jButtonAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ttt.operator = 5;
                try {
                    SendData(ttt.Encode());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Request Can Not Accepted (IOException)");
                    //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                receive.dispose();
            }
        });
        receive.jButtonReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ttt.operator = 6;
                try {
                    SendData(ttt.Encode());
                } catch (IOException ex) {
                }
                receive.dispose();
            }
        });
        receive.setVisible(true);
    }

    public void File_Receiving(final Test rec) {
        //if (!Receive_File_GUI_Flag) {
            Receive_File_GUI_Flag = true;
            final File_Receiving_GUI frg = new File_Receiving_GUI();
            frg.setVisible(false);
            frg.setVisible(false);
            frg.jLabelFriend.setText(rec.string_a);
            frg.jLabelFile.setText(rec.string_d);
            frg.setTitle("File Receive");
            frg.setResizable(false);
            frg.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // if u want to over write code of a JFrame closing button then must do this
            frg.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Receive_File_GUI_Flag = false;
                    frg.dispose();
                }
            });
            frg.jButtonReject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Test obj = new Test();
                    obj.operator = 12;
                    obj.string_a = rec.string_a;
                    obj.string_c = rec.string_c;
                    obj.string_d = rec.string_d;
                    try {
                        SendData(obj.Encode());
                    } catch (IOException ex) {
                        //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Receive_File_GUI_Flag = false;
                    frg.dispose();
                }
            });
            frg.jButtonAccept.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        frg.jButtonAccept.setEnabled(false);
                        frg.jButtonReject.setEnabled(false);
                        frg.jLabelFile.setText("");
                        frg.jLabelFriend.setText("");
                        frg.jLabel2.setText("please wait file receiving");
                        frg.jLabelFile.setText("");
                        Socket rec_socket;

                        InputStream rec_is;
                        FileOutputStream rec_fos;
                        BufferedOutputStream rec_bos;
                        int rec_size = 0;
                        rec.show();
                        Integer port=Integer.parseInt(rec.string_c);
                        InetSocketAddress skt_address = new InetSocketAddress(rec.string_b, port);  // dont modify this line    
                        int t=5;
                        frg.jLabelFile.setText(""+rec.string_b+port);

                        while(true)
                        {rec_socket = new Socket();
                            rec_socket.connect(skt_address, 1000*t);
                        if(rec_socket.isConnected())
                            break;
                        else
                            t++;
                        }

                        rec_is = rec_socket.getInputStream();
                        //frg.jLabelFile.setText("."); //--
                        rec_size = rec_socket.getReceiveBufferSize();
                        //System.out.println("rec_size"+rec_size); //--
                        //frg.jLabelFile.setText("3"); //--
                        JFileChooser fc = new JFileChooser();
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        fc.showOpenDialog(frg);
                        //frg.jLabelFile.setText("4"); //-

                        String path=fc.getSelectedFile().getAbsolutePath() + "\\" + rec.string_d;
                        rec_fos = new FileOutputStream(path);
                        //frg.jLabelFile.setText("5"); //-

                        rec_bos = new BufferedOutputStream(rec_fos);
                        byte[]  rec_bytes = new byte[rec_size];

                        int count;
                        //frg.jLabelFile.setText("...."); //-
                        while ((count = rec_is.read(rec_bytes)) > 0) {
                            rec_bos.write(rec_bytes, 0, count);
                        }
                        //JOptionPane.showMessageDialog(frg, rec.string_d + " successfully stored ");//--
                        rec_bos.flush();
                        rec_bos.close();
                        rec_is.close();
                        rec_socket.close();
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Receive_File_GUI_Flag = false;
                    frg.setVisible(false);
                    JOptionPane.showMessageDialog(frg, rec.string_d + " successfully stored ");
                    frg.dispose();
                }
            });
            frg.setVisible(true);
        //
    }

    public void PasswordChange() {
        if (PasswordChange_GUI_Flag == false) {
            PasswordChange_GUI_Flag = true;
//        new Thread(){
            //      public void run(){
            final Pasword_Change_GUI pcg = new Pasword_Change_GUI();
            pcg.setVisible(false);
            pcg.setTitle("Change Password");
            pcg.setResizable(false);
            pcg.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            pcg.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    PasswordChange_GUI_Flag = false;
                    pcg.jPasswordFieldCur.setText("");
                    pcg.jPasswordFieldNew.setText("");
                    pcg.jPasswordFieldCnfrm.setText("");
                    pcg.dispose();
                }
            });
            pcg.jButtonChange.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Test obj = new Test();
                    obj.Reset();
                    obj.operator = 9;
                    boolean error = false;
                    String error_m = "";
                    //error_m+="\n";
                    if (!pcg.jPasswordFieldCur.getText().equals(Curent_PWD)) {
                        error = true;
                        error_m += "\nWrong Current Password";
                    }
                    if (!pcg.jPasswordFieldNew.getText().equals(pcg.jPasswordFieldCnfrm.getText())) {
                        error = true;
                        error_m += "\nConfirm Password mismatch";
                    } else if (pcg.jPasswordFieldCur.getText().length() < 3 || pcg.jPasswordFieldCur.getText().length() > 20) {
                        error = true;
                        error_m += "\nPassword length must be 4 to 20";
                    }
                    if (error) {
                        JOptionPane.showMessageDialog(null, error_m);
                    } else {
                        obj.string_a = pcg.jPasswordFieldCur.getText();
                        obj.string_b = pcg.jPasswordFieldNew.getText();
                        try {
                            SendData(obj.Encode());
                            Curent_PWD = pcg.jPasswordFieldNew.getText();
                            PasswordChange_GUI_Flag = false;
                            pcg.jPasswordFieldCur.setText("");
                            pcg.jPasswordFieldNew.setText("");
                            pcg.jPasswordFieldCnfrm.setText("");
                            pcg.dispose();
                        } catch (IOException ex) {
                            //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Can Not Connect, Password Not Change");
                        }
                    }

                }
            });
            pcg.setVisible(true);
            //    }}.start();
        } // eof starter if
    }

    public void AddFriend_GUI() {
        if (AddFriend_GUI_Flag == false) {
            AddFriend_GUI_Flag = true;
            adding_f.setVisible(false);
            adding_f.setTitle("Adding Friend");
            adding_f.setResizable(false);
            adding_f.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // if u want to over write code of a JFrame closing button then must do this
            adding_f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AddFriend_GUI_Flag = false;
                    adding_f.jTextField1.setText("");
                    adding_f.dispose();
                }
            });
            adding_f.jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Test obj = new Test();
                    obj.Reset();
                    obj.operator = 4;
                    obj.string_a = adding_f.jTextField1.getText();
                    try {
                        SendData(obj.Encode());
                        adding_f.jTextField1.setText("");
                        AddFriend_GUI_Flag = false;
                        adding_f.dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(rootPane, "Request can not be send IOException(AddFriend_GUI())");
                        adding_f.jTextField1.setText("");
                    }
                }
            });
            adding_f.setVisible(true);
        }
    }

    public void Add_Friend_ToList(String frnd_name) {
        System.out.println("Ading new frnd "); //-- 
        if (Friend_In_List(frnd_name) == false) {
            String cur_selection = new String("" + (String) mcp.jListFriend.getSelectedValue());
            //String cur_writen = new String("" + mcp.jTextAreaMessege.getText());
            friends_list.add(new Friends(frnd_name));
            this.mcp.jList_Vector.addElement(frnd_name);
            this.mcp.jListFriend.setListData(this.mcp.jList_Vector);
            this.mcp.jScrollPaneFriends.revalidate();
            this.mcp.jScrollPaneFriends.repaint();
            if (!cur_selection.equals("null")) {
                //mcp.jListFriend.setSelectedValue(cur_selection, false); // list can not scrool
                //mcp.jTextAreaMessege.setText(cur_writen);
            }
        }
    }

    public void Remove_Friend_L_G(String frnd_name) // remove friend from friends list and user gui
    {
        if(this.mcp.jListFriend.getSelectedValue().equals(frnd_name))
        {
        friends_list.remove(Friend_Index(frnd_name));
        this.mcp.jListFriend.clearSelection();
        this.mcp.jTextAreaMessege.setText("");
        this.mcp.jTextAreaHistory.setText("");
        }
        this.mcp.jList_Vector.removeElement(frnd_name);
        this.mcp.jListFriend.setListData(this.mcp.jList_Vector);
        this.mcp.jScrollPaneFriends.revalidate();
        this.mcp.jScrollPaneFriends.repaint();
    }

    public Boolean Friend_In_List(String frnd_name) {
        Boolean found = new Boolean(false);
        int i = 0;
        if (friends_list.size() > 0) {
            while (i < friends_list.size()) {
                if (friends_list.get(i).fr_u_id.equals(frnd_name)) {
                    found = true;
                    break;
                }
                i++;
            }// eof while
        } // eof if
        return found;
    }

    public void Update_Friend_State(String frnd_name, Boolean a) {
        int i = Friend_Index(frnd_name);
        if (i > -1) {
            friends_list.get(Friend_Index(frnd_name)).is_login = a;
            if (frnd_name.equals((String) this.mcp.jListFriend.getSelectedValue())) {
                if (a.equals(true)) {
                    mcp.jTextAreaMessege.setEditable(true);
                    mcp.jButtonSend.setEnabled(true);
                    mcp.jLabel1.setText("  Online");
                    Set_Chat_History();
                } else {
                    mcp.jTextAreaMessege.setText("");
                    mcp.jLabel1.setText("  Offline");
                  //offline//  mcp.jTextAreaMessege.setEditable(false);
                  //offline//  mcp.jButtonSend.setEnabled(false);
                }
            }
        } // eof if
    }

    public void Update_Chat_History(int id, boolean frnd_send, String messege) // update in back end data
    {
        if (frnd_send == true) {
            friends_list.get(id).chat_history += "\n\n" + friends_list.get(id).fr_u_id + ": " + messege;
        } else {
            friends_list.get(id).chat_history += "\n\nYou" + ": " + messege;
        }
        Set_Chat_History();
    }

    public int Friend_Index(String frnd_name) // return friends_list index of frnd_name
    {
        int i = 0;
        if (friends_list.size() > 0) {
            while (i < friends_list.size()) {
                if (friends_list.get(i).fr_u_id.equals(frnd_name)) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public void Set_New_Messege_In_History() // update in user gui
    {
        int index = Friend_Index((String) mcp.jListFriend.getSelectedValue());
        if (index > -1) {
            this.mcp.jTextAreaHistory.setText(friends_list.get(index).chat_history);
        }
    }

    public void Set_Chat_History() // only cal it when selecting a member from friend list, mean it will call only if list value change
    {
        if (friends_list.size() > 0) {
            String frnd_name = new String("" + (String) this.mcp.jListFriend.getSelectedValue());
            int index = Friend_Index(frnd_name);
            boolean found = false;
            if (index > -1) {
                found = true;

                if (found == false && !frnd_name.equals("null")) // mean friend is showing in list to user,  but not existing in linklist
                {                                                // this case will never execute
                    this.mcp.jTextAreaHistory.setText("");
                    this.mcp.jTextAreaHistory.setEditable(false);
                    this.mcp.jTextAreaMessege.setText("Friend not found(error)");
                    this.mcp.jTextAreaMessege.setEditable(false);
                    this.mcp.jButtonSend.setEnabled(false);
                }
                if (found == true && !previous_selected_friend.equals(curent_selected_friend)) {
                    this.mcp.jTextAreaHistory.setText(friends_list.get(index).chat_history);
                    this.mcp.jTextAreaMessege.setText("");
                    if (friends_list.get(index).is_login == true) {
                       //offline// this.mcp.jButtonSend.setEnabled(true);
                        this.mcp.jLabel1.setText("  Online");
                       //offline// this.mcp.jTextAreaMessege.setEditable(true);
                    } else {
                       //offline// this.mcp.jButtonSend.setEnabled(false);
                        this.mcp.jLabel1.setText("  Offline");
                       //offline// this.mcp.jTextAreaMessege.setEditable(false);
                    }
                }
                /// write code to move scroolpane full down
            }
        } // eof if(friends_list.size()>0)
    }

    synchronized void SendData(String send) throws IOException {
        if (client.isConnected()) {
            output.writeObject(send);
            output.flush();
        }
    }

    void Get_Input() {
        this.mcp.jTextAreaHistory.setEditable(false);
        main_thread = new Thread() {
            public void run() {
                Boolean loop_Breaker = new Boolean(false);
                do {
                    try {
                        test_ip.Decode((String) input.readObject());
                        switch (test_ip.operator) {
                            case 1: // receive_type_1 - Messege Receive
                            case 31: // receive history messeg(from frend)
                            case 32: // receive ofline messeges
                            {
                                if (Friend_In_List(test_ip.string_a)) // Friend exist in friend list
                                {
                                    Update_Chat_History(Friend_Index(test_ip.string_a), true, test_ip.string_b);
                                    // System.out.println(test_ip.string_a+": "+test_ip.string_b); //--
                                    if (test_ip.string_a.equals((String) mcp.jListFriend.getSelectedValue())) {
                                        Set_New_Messege_In_History();
                                    }
                                }
                                break;
                            }
                            case 30: // receive history messeg(from you)
                            {
                                if (Friend_In_List(test_ip.string_a)) // Friend exist in friend list
                                {
                                    Update_Chat_History(Friend_Index(test_ip.string_a), false, test_ip.string_b);
                                    // System.out.println(test_ip.string_a+": "+test_ip.string_b); //--
                                    if (test_ip.string_a.equals((String) mcp.jListFriend.getSelectedValue())) {
                                        Set_New_Messege_In_History();
                                    }
                                }
                                break;
                            }
                            case 4: // receive_type_4 - Logout Successful
                            // when case 4 execute while loop will be terminate 
                            // when while loop terminate thread will be finish
                            {
                                loop_Breaker = true;
                                close_Output_Input();
                                disconnectToServer();
                                if (wants_to_close == true) // user press close button of msngr
                                {
                                    Dispose_It();
                                } else // User only logout, so show again sign in panel
                                {
                                    Reset_ChatClient();
                                }
                                break;
                            }
                            case 5: // Receive Friend Request
                            {
                                new Thread() {
                                    public void run() {
                                        Receive_Req(test_ip.string_a);
                                    }
                                }.start();
                                break;
                            }
                            case 6: {
                                new Thread() {
                                    public void run() {
                                        if (Friend_In_List(test_ip.string_a) == false) {
                                            Add_Friend_ToList(test_ip.string_a);
                                        }
                                        if (test_ip.string_b.equals("Y")) //&& friends_list.get(Friend_Index(test_ip.string_a)).is_login==false)
                                        {
                                            Update_Friend_State(test_ip.string_a, true);
                                        }
                                        if (test_ip.string_b.equals("N")) // && friends_list.get(Friend_Index(test_ip.string_a)).is_login==true)
                                        {
                                            Update_Friend_State(test_ip.string_a, false);
                                        }
                                        JOptionPane.showMessageDialog(null, test_ip.string_a + " accept your request");
                                    }
                                }.start();

                                break;
                            }
                            case 15: // receive_type_15 - String_a(Friend_Name) , String_a(Status)
                            {   // Status Y(online) ,  N(offline)

                                if (Friend_In_List(test_ip.string_a) == false) {
                                    Add_Friend_ToList(test_ip.string_a);
                                }
                                //if (Friend_In_List(test_ip.string_a) == true) 
                                {
                                    if (test_ip.string_b.equals("Y")) //&& friends_list.get(Friend_Index(test_ip.string_a)).is_login==false)
                                    {
                                        Update_Friend_State(test_ip.string_a, true);
                                    }
                                    if (test_ip.string_b.equals("N")) // && friends_list.get(Friend_Index(test_ip.string_a)).is_login==true)
                                    {
                                        Update_Friend_State(test_ip.string_a, false);
                                    }
                                }
                                break;
                            }
                            case 17: {
                                new Thread() {
                                    public void run() {
                                        JOptionPane.showMessageDialog(null, "Invalid Freind Request: " + test_ip.string_a + " username not exist");
                                    }
                                }.start();

                                break;
                            }
                            case 20: {
                                new Thread() {
                                    public void run() {
                                        JOptionPane.showMessageDialog(null, "" + test_ip.string_a + " frnd request rejeced");
                                    }
                                }.start();

                                break;
                            }
                            case 21: // friend test_ip.string_a remove you
                            {   
                                Remove_Friend_L_G(test_ip.string_a);
                                JOptionPane.showMessageDialog(null, test_ip.string_a+" remove you from friend list");
                                break;
                            }
                            case 22: // friend test_ip.string_a successfully remove by you
                            {
                                JOptionPane.showMessageDialog(null, test_ip.string_a+" successfully remove by you from friend list");
                                break;
                            }
                            case 25: // file receive request
                            { 
                                final Test ipa=new Test();
                                ipa.Decode(test_ip.Encode());
                                new Thread() {
                                public void run() {
                                File_Receiving(ipa);
                                    }
                                }.start();
                                break;
                            }
                            case 26: // test_ip.string_a reject your file sharing
                            {   
                                Share_File_GUI_Flag=false;
                                share_thread.destroy();
                                JOptionPane.showMessageDialog(null, test_ip.string_a+" reject sharing for "+ test_ip.string_d);
                                break;
                            }
                            case 0: { // Invalid input from server so send ACk of send again to server
                                // this is extar work so i coment it
                                //test_invalid.Reset();
                                //test_invalid.operator = 10; //10: send last messege again
                                //SendData(test_invalid.Encode());
                                break;
                            }
                        } // eof switch
                    } catch (java.net.SocketException ex) // Connection with server fail
                    {
                        //System.out.println(""+ex.getMessage());
                        loop_Breaker = true;
                        JOptionPane.showMessageDialog(null, "Server Down (Get_Input())", "Chat Messenger", JOptionPane.ERROR_MESSAGE);
                        Log_Out_Process();
                        Reset_ChatClient();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while (!loop_Breaker);
            }// eof run
        };

        main_thread.start();
    }

    private Boolean connectToServer() {
        int i = 0;
        while (i <= connection_attemps) {
            i++;
            try {
                //client.close();
                client = null;
                client = new Socket();
                client.connect(socket_address);
                if (client.isConnected()) {
                    // JOptionPane.showMessageDialog(null, "Server IP: "+client.getInetAddress()+"\nPort Num: "+client.getPort()); //--
                    break;
                }
                ///displayMessage("not Connected");
            } catch (IOException ex) {
                //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                //JOptionPane.showMessageDialog(null, "Cant Connect:"+i);
            }
            //System.out.print("."); //--
        }
        //JOptionPane.showMessageDialog(null,"client.isConnected()="+client.isConnected()); //--
        if (client.isConnected()) {
            return true;
        } else {
            return false;
        }
    } // end method connectToServer

    private void open_Output_Input() //throws IOException 
    {

        try //throws IOException
        {
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
        } // end method getStreams
        catch (java.net.SocketException ex) //if socket is close
        {
            ex.printStackTrace();
        } catch (IOException ex) {
            //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "IOException (open_Output_Input):" + ex.getMessage());
        }
    } // end method open_Output_Input

    public void runClient() {
        /*  try // connect to server, get streams, process connection
         {
         connectToServer(); // create a Socket to make connection
         // getStreams(); // get the input and output streams
         // processConnection(); // process connection
         } // end try
         catch (EOFException eofException) {
         // displayMessage("\nClient terminated connection");
         } // end catch
         catch (IOException ioException) {
         ioException.printStackTrace();
         } // end catch
         finally {
         //closeConnection(); // close connection
         } // end finally
         */
    } // end method runClient 

    public static void main(String[] args) {
        // TODO code application logic here

        ChatClient cc = new ChatClient();

    }

    public void Send_Button() {
        //    String send_it=new String();
        //JOptionPane.showMessageDialog(null, "send btn start"); //--

        send_btn_strng.string_b = ""+mcp.jTextAreaMessege.getText();
        send_btn_strng.operator = 3;
        send_btn_strng.string_a = (String) mcp.jListFriend.getSelectedValue();
        //// test_ip.string_b= get selected friend user name from list;
        // JOptionPane.showMessageDialog(null, "Try of send"); //--
        try {
            //JOptionPane.showMessageDialog(null, "To: " + test_ip.string_a + " ,SMS: " + test_ip.string_b); //--
            SendData(send_btn_strng.Encode());
            //JOptionPane.showMessageDialog(null, "Object write successfully"); //--
            //JOptionPane.showMessageDialog(null, "after flush"); //--
            Update_Chat_History(Friend_Index(send_btn_strng.string_a), false, send_btn_strng.string_b); // update in backend data
            Set_New_Messege_In_History(); // show for user
            mcp.jTextAreaMessege.setText(null);
        } catch (NotSerializableException ex) {
            JOptionPane.showMessageDialog(null, "NotSerializableException(ChatClient:JBUTTONSEND)");
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Messenge cannot send(mcp.jButtonSend.addActionListener)");
        }
    } //  eof Send_Button

    ChatClient() {
        socket_address = new InetSocketAddress(chatServer, port_num);  // dont modify this line    
        this.jRadioButtonSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                SetSIP();
            }
        });
        this.jRadioButtonSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                SetSUP();
            }
        });
        this.sip.jButtonSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                sip.jButtonSignIn.enable(false);
                Sign_In_Process();
                sip.jButtonSignIn.enable(true);
            }
        });
        this.sup.jButtonSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Sign_Up_Process();
            }
        });
        this.mcp.jButtonSend.removeActionListener(null);
        this.mcp.jListFriend.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                previous_selected_friend = curent_selected_friend;
                curent_selected_friend =""+(String) mcp.jListFriend.getSelectedValue();
                if(!curent_selected_friend.equals("null"))
                {
                mcp.jButtonSend.setEnabled(true);
                mcp.jTextAreaMessege.setEditable(true);
                Set_Chat_History();
                }
                else
                {
                mcp.jButtonSend.setEnabled(false);
                mcp.jTextAreaMessege.setEditable(false);
                mcp.jTextAreaHistory.setText("");
                }
            }
        });
        this.jMenuItemLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Log_Out_Process();
            }
        });
        this.mcp.jButtonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                Send_Button();
                //System.out.println(""+mcp.jListFriend.getSelectedValue());
            }
        });
        this.addWindowListener(new WindowAdapter() // closing code of main frame
        {
            public void windowClosing(WindowEvent e) {
                if (is_signin == true) {
                    Log_Out_Process();
                    wants_to_close = true;
                } else {
                    Dispose_It();
                }
            }
        });
        this.mcp.jTextAreaMessege.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if (e.getKeyChar() == '\n') {
                    Send_Button();
                    mcp.jTextAreaMessege.setText(null);
                }
            }
        });
        this.jMenuItemAddFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                AddFriend_GUI();
            }
        });
        this.jMenuItemChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordChange();
            }
        });
        this.jMenuItemRemoveFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Remove_Friend_Menue();
            }
        });
        this.jMenuItemSharing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Share_File_Func();
            }
        });
        Reset_ChatClient();

    }

    public void Sign_In_Process() {
        boolean error = false;
        String error_m = new String("");
        test_op.string_a = sip.jTextFieldSignInEmail.getText().toLowerCase();
        test_op.string_b = sip.jPasswordFieldSignIn.getText();
        if (test_op.string_a.length() < 3 || test_op.string_a.length() > 20) {
            error_m += "User Name length must between 3 to 20\n";
            error = true;
        }
        if (test_op.string_b.length() < 3 || test_op.string_b.length() > 20) {
            error_m += "Password length must between 3 to 20";
            error = true;
        }
        if (error == true) {
            JOptionPane.showMessageDialog(null, error_m, "Invalid Credentials", JOptionPane.ERROR_MESSAGE);
        } else if (connectToServer()) {
            Curent_ID = sip.jTextFieldSignInEmail.getText().toLowerCase();
            Curent_PWD = sip.jPasswordFieldSignIn.getText();
            test_op.operator = 1; // send_type_1 - Send Sign In Request
            String temp = new String("");

            try {
                open_Output_Input();
                SendData(test_op.Encode());
                //test_ip.Decode(
                temp = (String) input.readObject();
                System.out.println(temp);
                test_ip.Decode(temp);
                test_ip.show();
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (test_ip.operator == 3 && Curent_ID.equals(test_ip.string_a)) // receive_type_3 - Sign In Successful
            { // continue connection
                is_signin = true;
                SetMCP();
                Get_Input();
            } else if (test_ip.operator == 9) // receive_type_9 - Sign In Rejected
            {
                JOptionPane.showMessageDialog(null, "Signin Rejected, Invalid userid or password", "Invalid Credentials", JOptionPane.ERROR_MESSAGE);
                close_Output_Input();
                disconnectToServer();
            } else {
                JOptionPane.showMessageDialog(null, "Try Again");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Can not connectToServer (Sign_In_Process())");
        }
    }

    public void Sign_Up_Process() {
        boolean error = false;
        String error_m = new String("");
        test_op.string_a = sup.jTextFieldSignUpEmail.getText().toLowerCase();
        if (test_op.string_a.length() < 3 || test_op.string_a.length() > 20) {
            error_m += "User Name length must between 3 to 20\n";
            error = true;
        }
        if (sup.jPasswordFieldSignUp1.getText().equals(sup.jPasswordFieldSignUp2.getText())) {
            test_op.string_b = sup.jPasswordFieldSignUp1.getText();
            if (test_op.string_b.length() < 3 || test_op.string_b.length() > 20) {
                error_m += "Password length must between 3 to 20";
                error = true;
            }
        } else {
            error_m += "Password not matching in both fields";
            error = true;
        }

        if (error == true) {
            JOptionPane.showMessageDialog(null, error_m, "Invalid Credentials", JOptionPane.ERROR_MESSAGE);
        } else if (connectToServer()) {
            open_Output_Input();
            Curent_ID = sup.jTextFieldSignUpEmail.getText().toLowerCase();
            Curent_PWD = sup.jPasswordFieldSignUp1.getText();
            test_op.operator = 2; // send_type_2 - Send Sign Up Request
            try {
                SendData(test_op.Encode());
                test_ip.Decode((String) input.readObject());
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (test_ip.operator == 2 && Curent_ID.equals(test_ip.string_a)) // receive_type_2 - Sign Up Successful
            { // continue connection
                is_signin = true;
                SetMCP();
                Get_Input();
            } else if (test_ip.operator == 10 && test_ip.string_a.equals(Curent_ID)) // receive_type_10 - Sign Up Rejected
            {
                JOptionPane.showMessageDialog(null, "SignUp Rejected, userid already exist", "Invalid Credentials", JOptionPane.ERROR_MESSAGE);
                close_Output_Input();
                disconnectToServer();
            } else {
                JOptionPane.showMessageDialog(null, "Try Again");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Can not connectToServer (Sign_In_Process())");
        }
    }

    public void Log_Out_Process() // this method only logout user not dispose main frame
    {

        try {
            test_op.Reset();
            test_op.operator = 8; // send_type_2 - Send Sign Up Request
            test_op.string_a = Curent_ID;
            SendData(test_op.Encode());
            Invisible_Frame();
        } catch (IOException ex) {
            //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "IOException(Log_Out_Process)");
        }
    }

    public void Invisible_Frame() {
        this.setVisible(false);
    }

    public void Visible_Frame() {
        this.setVisible(true);
    }

    public void close_Output_Input() {
        try {
            output.close();
            input.close();
        } catch (java.net.SocketException ex) //if socket is close
        {
            JOptionPane.showMessageDialog(null, "IOException (SocketException)" + ex.getMessage());
            //output=null;
            //input=null;
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "IOException (Close_Output_Input)" + ex.getMessage());
        }
    }

    public void disconnectToServer() {
        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Coonection Can Not Close(Close_Client)");
        }

    }

    public void Dispose_It() {
        this.dispose();
    }

    public void SetSIP() {
        this.setResizable(true);
        this.remove(sup);
        this.remove(mcp);
        this.sip.jTextFieldSignInEmail.setText("");
        this.sip.jPasswordFieldSignIn.setText("");
        this.add(sip, BorderLayout.CENTER);
        this.setResizable(false);
    }

    public void SetSUP() {
        this.setResizable(true);
        this.remove(sip);
        this.sup.jTextFieldSignUpEmail.setText("");
        this.sup.jPasswordFieldSignUp1.setText("");
        this.sup.jPasswordFieldSignUp2.setText("");
        this.add(sup, BorderLayout.CENTER);
        this.setResizable(false);
    }

    public void SetMCP() {
        this.setResizable(true);
        this.remove(this.jPanel1);
        this.setSize(415, 430);
        this.remove(sip);
        this.remove(sup);
        this.mcp.jLabelYou.setText("("+Curent_ID+")");
        this.jMenuBar1.setVisible(true);
        this.add(mcp, BorderLayout.CENTER);
        this.setResizable(false);
        this.mcp.jButtonSend.setEnabled(false);
        this.mcp.jTextAreaMessege.setEditable(false);
    }
}
// send_type_2 - Send Sign Up Request
// receive_type_10 - Sign Up Rejected
// output.writeObject(test_op.Encode());
// test_ip.Decode((String) input.readObject());


// 15@2/A%h>hasan@2/A%h>Y@2/A%h>