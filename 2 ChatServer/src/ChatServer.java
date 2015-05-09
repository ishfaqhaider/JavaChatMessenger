/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//import for rmi server
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import 
//import chatserver.Test;

/**
 *
 * @author Ishfaq Haider 1418-FBAS/BSSE/F11A
 */
public class ChatServer {

    String rmi_server_ip = new String("127.0.0.1");
    int rmi_server_port = 2222;
    RMIinterface rmi_fuctionCall;
    ////
    ArrayList<Clients> client_list = new ArrayList();
    Clients temp;
    private int total_clients = 0;
    ServerSocket server;
    Thread wait_Thread;
    Test test_obj = new Test();
    String messege = new String("");
    Boolean rmi_flag = new Boolean(false);
    
    //\-------Database Vars------\//
    Connection connection1;
    Statement statement1; // for add_messege
    ResultSet resultset1; // for add_messege

    Statement statement2; // for  Send_Hist
    ResultSet resultset2; // for  Send_Hist

    Statement statement3; // for Send_Undelivered
    ResultSet resultset3; // for Send_Undelivered
    
    Statement statement4; // for
    ResultSet resultset4; // for

    static Integer sms_id_last=new Integer(0);

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();

        // TODO code application logic here
    }

    public void connect_to_rmi()//for connection with rmi server
    {

        String name = "rmi:" + rmi_server_ip + ":" + rmi_server_port + "/MyRmiServer";
        try {
            Registry reg = LocateRegistry.getRegistry();
            rmi_fuctionCall = (RMIinterface) reg.lookup(name);
            System.out.println("RMI Successfuly connected"); //--
        } catch (java.rmi.ConnectException ex) {
            // ex.printStackTrace();
            System.out.println("Connection to server not responding(ConnectException)");
        } catch (java.rmi.NotBoundException ex) {
            System.out.println("RMI server not running on Port (ConnectException)");
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean RMI_check(String uname, String pd, Boolean rmi_flag) //for signup and signin
    {
        Boolean b = new Boolean(false);
        if (rmi_flag == true) {

            try {
                b = rmi_fuctionCall.verify_user(uname, pd);
            } catch (RemoteException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (b == true) {
                return true;
            } else {
                return false;
            }
        } else {
            try {
                b = rmi_fuctionCall.signup(uname, pd);
            } catch (RemoteException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }


            if (b == true) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void StartServer(int num) {
        try {
            server = new ServerSocket(12345, num);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Alert_User(final int list_id, final boolean status) 
    {
        /*new Thread() {
            public void run()*/ {
                // System.out.println("Alert_User()"); //--
                int num;
                int frnd = -1;
                Test test_send = new Test();
                String current;
/*                    if (client_list.size() > 0) {
                        for (int i = 0; i < client_list.size(); i++) {
                           //  System.out.println("Updating all clients friends's lists"); //--
                            if (client_list.get(i).is_login.equals(true)) { */
                              //  System.out.println("client_list.get(i).is_login.equals(true)"); //--
                                try {
                                    // rst=rmi_fuctionCall.Friend_List(client_list.get(i).client_id);
                                    num = rmi_fuctionCall.Total_Friends(client_list.get(list_id).client_id);
                                     // System.out.println("Total Friends"+num); //--
                                    for (int k = 1; k <= num; k++) {
                                       //  System.out.println("k="+k); //--
                                        current= rmi_fuctionCall.Friend_Name(client_list.get(list_id).client_id, k);
                                        // System.out.println("Friend "+k+":"+current); //--
                                        frnd = check_index(current);
                                        if (status==true){
                                            if (frnd == -1 || client_list.get(frnd).is_login==false ) {
                                         //   System.out.println("frnd == -1 || client_list.get(frnd).is_login==false"); //--
                                            test_send.operator = 15;
                                            test_send.string_a = current;
                                            test_send.string_b = "N";
                                            client_list.get(list_id).sendData(test_send.Encode());
                                        } else {
                                           // System.out.println("iner else friend "); //--
                                            test_send.operator = 15;
                                            test_send.string_b = "Y";
                                            test_send.string_a = current;
                                            client_list.get(list_id).sendData(test_send.Encode()); // telling coming client your current(friend name) is online
                                            if(client_list.get(frnd).is_login)
                                            {test_send.string_a = client_list.get(list_id).client_id;
                                            client_list.get(frnd).sendData(test_send.Encode()); // telling current(friend name) your friend(list_id) is online
                                            }
                                            }
                                        }
                                        else if(frnd!=-1 && client_list.get(frnd).is_login)
                                           { // System.out.println(" else if"); //-- 
                                            test_send.operator = 15;
                                            test_send.string_b = "N";
                                            test_send.string_a = client_list.get(list_id).client_id;
                                            client_list.get(frnd).sendData(test_send.Encode()); // telling current(friend name) your friend(list_id) is online
                                            }
                                    } // eof inner for                    
                                } // end of try
                                catch (RemoteException ex) {
                                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
/*
                            }
                        } // eof outer for
                    }
                    try {
                        // System.out.println("Alert_Users() thread sleeping"); //--
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }   */

            }
            
        /*}.start();*/

    }
    
    public void Alert_Users() 
    {
        new Thread() {

            public void run() {
                // System.out.println("Alert_Users()"); //--
                int num;
                int frnd = -1;
                Test test_send = new Test();
                String current;
                while (true) {
                    if (client_list.size() > 0) {
                        for (int i = 0; i < client_list.size(); i++) {
                           //  System.out.println("Updating all clients friends's lists"); //--
                            if (client_list.get(i).is_login.equals(true)) {
                              //  System.out.println("client_list.get(i).is_login.equals(true)"); //--
                                try {
                                    // rst=rmi_fuctionCall.Friend_List(client_list.get(i).client_id);
                                    num = rmi_fuctionCall.Total_Friends(client_list.get(i).client_id);
                                   //   System.out.println("Total Friends"+num); //--
                                    for (int k = 1; k <= num; k++) {
                                        // System.out.println("k="+k); //--
                                        current = rmi_fuctionCall.Friend_Name(client_list.get(i).client_id, k);
                                        // System.out.println("Friend "+k+":"+current); //--
                                        frnd = check_index(current);
                                        if (frnd == -1) {
                                           // System.out.println("frnd == -1 && !current.equals"); //--
                                            test_send.operator = 15;
                                            test_send.string_a = current;
                                            test_send.string_b = "N";
                                            client_list.get(i).sendData(test_send.Encode());
                                        } else {
                                           // System.out.println("else friend "); //--
                                            test_send.operator = 15;
                                            test_send.string_a = current;
                                            if (client_list.get(frnd).is_login == true) {
                                                test_send.string_b = "Y";
                                            } else {
                                                test_send.string_b = "N";
                                            }
                                            client_list.get(i).sendData(test_send.Encode());
                                        }
                                    } // eof inner for                    
                                } // end of try
                                catch (RemoteException ex) {
                                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        } // eof outer for
                    }
                    try {
                        // System.out.println("Alert_Users() thread sleeping"); //--
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();

    }

    private void waitForConnection() //throws IOException
    {
        wait_Thread = new Thread() {
            public void run() {
                // ur code
                int i = 0;
                while (true) {
                    temp = new Clients();
                    try {
                        System.out.println("Listening for : " + (total_clients));
                        temp.client_socket = server.accept();
                        /*
                         * JOptionPane.showMessageDialog(null, (total_clients +
                         * 1) + ")Name: " +
                         * temp.client_socket.getInetAddress().getHostName() +
                         * ", Port: " + temp.client_socket.getPort());
                         */
                        System.out.println((total_clients + 1) + ")Name: "
                                + temp.client_socket.getInetAddress()
                                + ", Port: " + temp.client_socket.getPort());

                        temp.output = new ObjectOutputStream(temp.client_socket.getOutputStream());
                        temp.output.flush();

                        temp.input = new ObjectInputStream(temp.client_socket.getInputStream());
                        System.out.println("OP_IP for :" + temp.client_socket.getInetAddress()); //--
                        client_list.add(temp);
                        /*
                         * if (total_clients == 1) // link input output when two
                         * connection made { Handle_Output_A();
                         * Handle_Output_B(); }
                         */
                        Handle_Client(total_clients);
                        total_clients++;
                    } catch (IOException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    i++;
                }// eof while
            }// eof run
        };// eof Thread
        wait_Thread.start();
    } // end method waitForConnection

    public void Handle_Client(final int client_id) {

        //  client_list.get(client_id).Array_index=client_id;
        client_list.get(client_id).client_thread = new Thread() {

            public void run() {
                Test test_input = new Test();
                Test test_output = new Test();
                Test test_invalid = new Test();
                Boolean loop_breaker = false;
                while (!loop_breaker) {
                    try {
                        test_input.Decode((String) client_list.get(client_id).input.readObject());
                    System.out.println(client_id+")"+client_list.get(client_id).client_id+" : "+test_input.operator+" | "+
                    test_input.string_a+" | "+test_input.string_b+" | "+test_input.string_c+" | "+test_input.string_d);
                        switch (test_input.operator) {
                            case 0: { /*
                                 * String return_input=new String("");
                                 * test_invalid.operator=18;
                                 * test_invalid.string_a="";
                                 * test_invalid.string_b="";
                                 * return_input=test_invalid.Encode();
                                 * client_list.get(client_id).sendData(return_input);
                                 */
                                break;
                            }
                               //--***************for signin***************--//
                            case 1: {
                               // test_input.show(); //--
                                rmi_flag = true;
                                if (RMI_check(test_input.string_a, test_input.string_b, rmi_flag) == false) {
                                    test_output.Reset();
                                    test_output.operator = 9;
                                    client_list.get(client_id).sendData(test_output.Encode());
                                    close_up(client_id);
                                    client_list.get(client_id).is_login = false;
                                    loop_breaker = true;
                                } else {
                                    System.out.println("Case 1:  else  ");
                                    test_output.Reset();
                                    test_output.operator = 3;
                                    test_output.string_a = test_input.string_a;
                                    client_list.get(client_id).client_id=test_input.string_a;// storing succcessful client info in arry list
                                    client_list.get(client_id).client_pwd=test_input.string_b;// storing succcessful client info in arry list
                                    client_list.get(client_id).is_login = true;
                                    client_list.get(client_id).sendData(test_output.Encode());
                                    Alert_User(client_id,true);
                                    Send_Hist(client_id);
                                    Send_Undelivered(client_id);
                                }


                                break;
                            }
                                //--***************for signup***************--//
                            case 2: {
                                rmi_flag = false;
                                if (RMI_check(test_input.string_a, test_input.string_b, rmi_flag) == false) {
                                    test_output.Reset();
                                    test_output.operator = 10;
                                    test_output.string_a = test_input.string_a;
                                    client_list.get(client_id).sendData(test_output.Encode());
                                    close_up(client_id);
                                    client_list.get(client_id).is_login = false;
                                    loop_breaker = true;
                                } else {
                                    test_output.Reset();
                                    test_output.operator = 2;
                                    test_output.string_a = test_input.string_a;
                                    client_list.get(client_id).client_id=test_input.string_a; // storing succcessful client info in arry list 
                                    client_list.get(client_id).client_pwd=test_input.string_b;// storing succcessful client info in arry list 
                                    client_list.get(client_id).is_login = true;

                                    client_list.get(client_id).sendData(test_output.Encode());
                                }


                                break;
                            }
                                //--***************for sending messages***************--//
                            case 3: {
                                test_output.operator = 1;
                                test_output.string_a =client_list.get(client_id).client_id;
                                test_output.string_b = test_input.string_b;
                                int a = check_index(test_input.string_a);
                                if (a != -1) {
                                client_list.get(a).sendData(test_output.Encode());
                                add_messege(client_list.get(client_id).client_id, test_input.string_a, test_input.string_b,true);
                                }
                                else 
                                add_messege(client_list.get(client_id).client_id, test_input.string_a, test_input.string_b,false);
                                break;
                            }
                                //--***************friend request received***************--//
                            case 4: {
                                test_output.Reset();
                                test_output.operator = 5;
                                test_output.string_a =client_list.get(client_id).client_id;
                                if(rmi_fuctionCall.insert_request(client_list.get(client_id).client_id,test_input.string_a)==true)
                                System.out.println("Request Successful Inserted: "+client_list.get(client_id).client_id+" to "+test_input.string_a);
                                int a = check_index(test_input.string_a);
                                if (a != -1) {
                                    client_list.get(a).sendData(test_output.Encode());
                                } else {
                                    test_output.Reset();
                                    test_output.operator=17;
                                    test_output.string_a=test_input.string_a;
                                    client_list.get(client_id).sendData(test_output.Encode());
                                }
                                break;
                            }
                                //--***************accept friend request***************--//
                            case 5: {
                                test_output.operator = 6; // for client friend request accepted
                                test_output.string_a = client_list.get(client_id).client_id;
                                test_output.string_b = "";
                                rmi_fuctionCall.accept_request(client_list.get(client_id).client_id,test_input.string_a);
                                int a = check_index(test_input.string_a);
                                if (a != -1) 
                                {
                                client_list.get(a).sendData(test_output.Encode());
                                }
                                break;
                            }
                                //--***************friend request rejected***************--//
                            case 6: {
                                test_output.operator = 20; // for client friend request rejected
                                test_output.string_a = client_list.get(client_id).client_id;
                                test_output.string_b = "";
                                rmi_fuctionCall.reject_request(client_list.get(client_id).client_id,test_input.string_a);
                                int a = check_index(test_input.string_a);
                                if (a != -1) {
                                  client_list.get(a).sendData(test_output.Encode());
                                }
                                break;
                            }
                                //--***************for removing friend***************--//
                            case 7: {
                                test_output.Reset();
                                test_output.operator = 21; // client_list.get(client_id).client_id remove you from friend list
                                test_output.string_a = client_list.get(client_id).client_id;
                                int a = check_index(test_input.string_a);
                                if(client_list.get(a).is_login)
                                client_list.get(a).sendData(test_output.Encode());
                                if(rmi_fuctionCall.remove_friend(client_list.get(client_id).client_id,test_input.string_a))
                                {
                                    System.out.println(client_list.get(client_id).client_id+" Removed "+test_input.string_a);
                                test_output.Reset();
                                test_output.operator = 22; // successfully remove test_input.string_a from you friends
                                test_output.string_a=test_input.string_a;
                                client_list.get(client_id).sendData(test_output.Encode());
                                }
                                
                                
                                break;
                            }
                                //--***************logout acknowledgement***************--//
                            case 8: {
                                test_output.Reset();
                                test_output.operator = 4;
                                client_list.get(client_id).is_login = false;
                                client_list.get(client_id).sendData(test_output.Encode());
                                close_up(client_id);
                                loop_breaker = true;
                                break;
                            }
                                //--***************password change request***************--//
                            case 9: {
                                
if(rmi_fuctionCall.change_password(client_list.get(client_id).client_id,test_input.string_a,test_input.string_b) == false) {
                                    test_output.operator = 19;
                                    test_output.string_a = "";
                                    test_output.string_b = "";
                                    client_list.get(client_id).sendData(test_output.Encode());
                                }
                                break;
                            }
                            case 10: {
                                
                                break;
                            }
                            case 11: {
                                //test_input.show();//--
                                
                                test_output.Reset();
                                test_output.operator=25;
                                test_output.string_a=client_list.get(client_id).client_id;
                                test_output.string_b=client_list.get(client_id).client_socket.getInetAddress().getHostAddress();
                                test_output.string_c=test_input.string_c;
                                test_output.string_d=test_input.string_d;
                                
                               int a = check_index(test_input.string_a);
                                if (a != -1) {
                                    if(client_list.get(a).is_login)
                                    {
                                   client_list.get(a).sendData(test_output.Encode());
                                    }
                                else
                                    {
                                test_output.Reset();
                                test_output.operator=26;
                                test_output.string_a=test_input.string_a;
                            //    test_output.string_b=client_list.get(client_id).client_socket.getInetAddress().getHostAddress();
                            //    test_output.string_c=test_input.string_c;
                                test_output.string_d=test_input.string_d;
                                client_list.get(client_id).sendData(test_output.Encode());
                                        
                                    }
                                }
                                
                                break;
                            }
                            case 12: {
                                test_output.Reset();
                                test_output.operator=26;
                                test_output.string_a=client_list.get(client_id).client_id;
                                test_output.string_c=test_input.string_c;
                                test_output.string_d=test_input.string_d;
                                int a = check_index(test_input.string_a);
                                client_list.get(a).sendData(test_output.Encode());

                                break;
                            }
                            case 13:
                            {
                                
                            }



                        }




                    } catch (NotSerializableException ex) {
                        JOptionPane.showMessageDialog(null, "NotSerializableException(ChatServer:Handle_Output(0))");
                    } catch (SocketException ex) {
                        System.out.println("SocketException");
                        client_list.get(client_id).is_login = false;
                        close_up(client_id);
                        loop_breaker = true;

                    } catch (EOFException ex) {
                        System.out.println("EOFException");
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                };

            }
        };
        client_list.get(client_id).client_thread.start();

    }

//--***************for closing socket,thread,input & output***************--//
    public void close_up(int id) // throws IOException {
    {
        try // throws IOException {
        {   Alert_User(id,false);
            client_list.get(id).input.close();
            client_list.get(id).output.close();
            client_list.get(id).client_socket.close();
            //Thread.currentThread().destroy();
        } catch (SocketException ex) {
            System.out.println("SocketException (close_up())");
        } catch (IOException ex) {
            //Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IOException (close_up())");
        }
    }

//--***************for searching id index***************--//

    public int check_index(String u_id) {
        int i = 0;
        while (i < client_list.size()) {
            if(client_list.get(i).is_login.equals(true)){
            if (client_list.get(i).client_id.equals(u_id)) {
                return i;
                
            }}
            i++;
        } // eof while
        return -1;
    }

    ChatServer() {
        connect_to_rmi();
        DBon();
        System.out.println("Seting messege id");
        while(!Set_sms_id_last()) // it will set Set_sms_id_last and proceed
        System.out.print(".");
        Set_sms_id_last();
        System.out.println("sms_id_last="+sms_id_last);
        StartServer(100);
        waitForConnection();
    }

    void DBon() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); //dynamic loading of driver
            connection1 = DriverManager.getConnection("jdbc:odbc:MDB", "scott", "default");
            statement1 =connection1.createStatement();
            statement2 =connection1.createStatement();
            statement3 =connection1.createStatement();
            System.out.println("DB Connected"); //--
            //JOptionPane.showMessageDialog(null,"DB Connected"); //--
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(HRM.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, "driver not found");
        } catch (SQLException ex) {
            //Logger.getLogger(HRM.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, "data base connectivity problem" + ex.getMessage());
        }
    }
    //--***************for setting db off****************--//

    public void DBoff() {
        try {
            statement1.close();
            statement2.close();
            statement3.close();
            connection1.close();
            JOptionPane.showMessageDialog(null, "data base connention close"); //--
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "data base connention cannot close properly" + ex.getMessage());
        }
    }

    boolean Set_sms_id_last(){
      try {
                sms_id_last=0;
    Statement stt=connection1.createStatement();
        ResultSet rst;
              stt.execute("select msgid from messege order by msgid desc;");
              rst=stt.getResultSet();
            if(rst.next())
            sms_id_last=Integer.parseInt(rst.getString(1));
            stt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
        return false;
        }
    }
    
    boolean add_messege(String sender, String receiver, String messege, boolean delivered) 
    {
        String del=new String("n");
        if(delivered)
            del="y";
    try {
        Statement stt=connection1.createStatement();
    synchronized(this)
    {stt.execute("insert into messege(msgid,username1,username2,msg,send) values ("+(++sms_id_last)+",'"+sender+"','"+receiver+"','"+messege+"','"+del+"');");
    }
    stt.close();
    return true;
        }
    catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
}    

    void Send_Hist(int idd)
    { 
        String name=new String(""+client_list.get(idd).client_id);
        String temp=new String("");
        Test t_op=new Test();
try {
    Statement stt=connection1.createStatement();
        ResultSet rst;
    //    "select * from messege where (username1='"+name+"' or username2='"+name+"') and send='y' order by msgid;"
stt.execute("select * from messege where (username1='"+name+
"' or username2='"+name+"') and send='y' order by msgid;");
rst=stt.getResultSet();
while(rst.next())
{
t_op.Reset();
temp=rst.getString(2);
if(!temp.equals(name)) // messege from friend
{
t_op.operator=31;    
t_op.string_a=temp; // geting friend name
}
else
{
t_op.operator=30;    
t_op.string_a=rst.getString(3); // geting friend name
}
t_op.string_b=rst.getString(4); // messege

client_list.get(idd).sendData(t_op.Encode()); // sending messege from history
}
stt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    
    void Send_Undelivered(int idd)
    { 
        
        String name=new String(""+client_list.get(idd).client_id);
        String temp=new String("");
        Test t_op=new Test();
try {
    Statement stt=connection1.createStatement();
        ResultSet rst;
            //    "select * from messege where (username1='"+name+"' or username2='"+name+"') and send='y' order by msgid;"
stt.execute("select * from messege where username2='"+name+"' and send='n' order by msgid;");
rst=stt.getResultSet();
while(rst.next())
{
t_op.Reset();
t_op.operator=32;
t_op.string_a=rst.getString(2); // geting friend name
t_op.string_b=rst.getString(4); // messege
client_list.get(idd).sendData(t_op.Encode()); // sending messege from history
 // now updte messege status
Update_Messege_Status_DB(Integer.parseInt(rst.getString(1)),true);
}
stt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    
    void Update_Messege_Status_DB(Integer upd, boolean state) // call it in Send_Undelivered to set msg is delivered after sending
    {  // updte messege status
    String state_s=new String("n");
    if(state)
        state_s="y";
try {
Statement stt=connection1.createStatement();
// "update messege set send='"+state_s+"' where msgid="+upd+";"
stt.execute("update messege set send='"+state_s+"' where msgid="+upd+";");
stt.close();
} catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    
    public class Clients {
        // public int Array_index;
        public String client_id;
        public String client_pwd;
        public Boolean is_login;

        public Socket client_socket;
        public ObjectOutputStream output; // output stream to client
        public ObjectInputStream input; // input stream from client
        public Thread client_thread;
        
        Clients()
        {
        client_id=new String("");
        client_pwd=new String("");
        is_login = new Boolean(false);
        }
        public synchronized void sendData(String a) //throws IOException
        {
             System.out.println(client_id+")))"+a); //--
            try {

                if (client_socket.isConnected()) {
                    output.writeObject(a);
                    output.flush();

                } else if (is_login == true) {
                    output.close();
                    input.close();
                    client_socket.close();
                    is_login = false;
                }
            } // eof try
            catch (SocketException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void close_input_output() throws IOException //closing thread,input,output,socket
        {
            this.input.close();
            this.output.close();

        }

        public void close_socket() throws IOException //closing socket
        {
            this.client_socket.close();
        }
    }
} // eofs class ChatServer

