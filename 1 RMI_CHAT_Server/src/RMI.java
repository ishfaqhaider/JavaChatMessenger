// RMI Server is without start rmiregistry comand from cmd
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMI //extends UnicastRemoteObject //
        implements RMIinterface {

    Connection connection1;
    Statement statement1;
    ResultSet resultset1;

    public static void main(String[] argv) {
        RMI hinter2 = new RMI();
        final String HelloImpl_IP = new String("127.0.0.1"); //dont change it because this rmi server always run on this machine(localhost)
// final String HelloImpl_IP=new String("192.168.10.100"); //dont change it because this rmi server always run on this machine(localhost)
        int HelloImpl_Port = 2222; // the port that will listen for this rmi server

        try {
            RMIinterface stub2 = (RMIinterface) UnicastRemoteObject.exportObject((RMIinterface) hinter2, HelloImpl_Port);
            LocateRegistry.createRegistry(1099); // default port for rmiregistry, dont change this line in your program
            Registry reg = LocateRegistry.getRegistry();
            reg.rebind("rmi:" + HelloImpl_IP + ":" + HelloImpl_Port + "/MyRmiServer", (Remote) stub2);
            System.out.println("RMI_MAIN_SERVER is ready.");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.out.println("RMI_MAIN_SERVER failed: RemoteException" + ex.getMessage());
        } catch (java.lang.ClassCastException ex) {
            ex.printStackTrace();
            System.out.println("RMI_MAIN_SERVER failed: ClassCastException" + ex.getMessage());
        }
    } // eof main
    //--***************for signin**************--//
    RMI() {
        DBon();
    }

    public boolean verify_user(String uname, String pd) {
        String st = new String("");
        String it = new String("");
        try {
            // System.out.println("Verification for " + uname + "|" + pd); //--
            statement1.execute("select * from userinfo where USERNAME='" + uname + "';");
            resultset1 = statement1.getResultSet();

            if (resultset1.next()) {
                st = (String) resultset1.getString(1);
                it = (String) resultset1.getString(2);
                //System.out.println("Result: " + st + "|" + it);  //--

            } else {
                //System.out.println("no result found"); //--
            }
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (st.equals(uname) && it.equals(pd)) {
            //System.out.println("Verified: " + st + "|" + it);  //--
            return true;
        } else {
            //System.out.println("Not Verified: " + uname + "|" + pd);  //--

            return false;
        }

    }
    //--***************for signup***************--//

    public synchronized boolean signup(String uname, String pd) throws RemoteException
    {
        String st = new String("");
        try {
            statement1.execute("select * from userinfo where USERNAME='" + uname + "';");
            resultset1 = statement1.getResultSet();
            if (resultset1.next()) {
                st = resultset1.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (st.equals(uname)) {
            return false;
        } else {
            try {
                // System.out.println("SignUp for: " + uname + "|" + pd); //--
                statement1.execute("insert into userinfo (USERNAME,PWORD) VALUES ('" + uname + "','" + pd + "');");
                return true;
            } catch (SQLException ex) {
                //Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("" + ex.getMessage()); //--
                return false;
            }
        }



    }
    //--***************for changing password***************--//

    public boolean change_password(String uname, String pd_old, String pd_new) throws RemoteException
    {
        try {

            statement1.execute("update userinfo set pword='"+pd_new+"' where username='"+uname+"' and pword='"+pd_old+"';");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean accept_request(String receiver, String sender) throws RemoteException
    {
        String check = new String("");
        try {
            statement1.execute("update friends set accepted='y' where username1='"+sender+"' and username2='"+receiver+"';");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean reject_request(String rejecter,String sender) throws RemoteException
    {
        try {
           statement1.execute("delete friends where username1='"+sender+"' and username2 ='"+rejecter+"';");
           return true;
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean insert_request(String sender,String send_to) throws RemoteException
    {  boolean ret=false;
        System.out.println("F Request : "+sender+send_to); //--
        try {
            int last=0;
            statement1.execute("select fshipid from friends order by fshipid desc;");
            System.out.println("1"); //--
            resultset1=statement1.getResultSet();
            System.out.println("2"); //--
            System.out.println("3"); //--
            if(resultset1!=null)
            {resultset1.next();
            System.out.println("4 in if"); //--
                last=Integer.parseInt(resultset1.getString(1));
            }
            System.out.println("Last="+last); //--
            last++;
            System.out.println("Last++="+last); //--
    //insert into friends(fshipid,username1,username2,accepted) values (10,'arslan','daniyal','y');
            System.out.println("6"); //--
            System.out.println("insert into friends(fshipid,username1,username2,accepted) values ("+last+",'"+sender+"','"+send_to+"','n');"); //-- 
            statement1.execute("insert into friends(fshipid,username1,username2,accepted) values ("+last+",'"+sender+"','"+send_to+"','n');");
            System.out.println("7"); //--
           statement1.execute("commit;");
            System.out.println("8"); //--
           ret=true;
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            ret=false;
        }
        return ret;
    }
    public void DBon() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); //dynamic loading of driver
            connection1 = DriverManager.getConnection("jdbc:odbc:MDB", "scott", "default");
            statement1 = connection1.createStatement();
            stt=connection1.createStatement();
            System.out.println("DB Connected");
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
            connection1.close();
            JOptionPane.showMessageDialog(null, "data base connention close");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "data base connention cannot close properly" + ex.getMessage());
        }
    }
    
            Statement stt;
            ResultSet rst;
    
    @Override
       public int Total_Friends(String friend_name) throws RemoteException
    { 
            int ret=0;
        try {
                stt.execute("select * from friends where (username1='"+friend_name+"' or username2='"+friend_name+"') and accepted='y';");
                rst=stt.getResultSet();
               // if(rst.next())
               // System.out.println(""+rst.getFetchSize()); //--
                while(rst.next())
                { ret++; 
                //System.out.println("in while"); //--
                }
                
                //System.out.println("Returning t_f:"+ret); //--
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            ret=0;
        }
                return ret;
       }

    @Override
    public String Friend_Name(String friend_name, int i) throws RemoteException
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        String ret=new String(""); 
        int k=0;
        String a=new String("");
        String b=new String("");
            Statement stt;
            ResultSet rst;
   try {
     
            stt=connection1.createStatement();
                stt.execute("select * from friends where (username1='"+friend_name+"' or username2='"+friend_name+"') and accepted='y' order by fshipid;");
                rst=stt.getResultSet();
                while(rst.next() && k<(i-1))
                {k++;}
                a=rst.getString(2);
                b=rst.getString(3);
                // System.out.println("a="+a); //--
                // System.out.println("b="+b); //--
                
                if(friend_name.equals(a))
                    ret=b;
                else if(friend_name.equals(b))
                    ret=a;
            } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            ret="";
            }   //Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
    return ret;
    }

 
    public boolean remove_friend(String remover, String other) throws RemoteException {
        try {
            // "DELETE friends WHERE username1='"++"' AND username2 ='"++"';"
            // "DELETE friends WHERE username1='"++"' AND username2 ='"++"';"
            // "DELETE messege where username1='"+remover+"' AND username2='"+other+"';"
            // "DELETE messege where username1='"+other+"' AND username2='"+remover+"';"
            
            // first delete child records
            statement1.execute("DELETE messege where username1='"+remover+"' AND username2='"+other+"';");
            statement1.execute("DELETE messege where username1='"+other+"' AND username2='"+remover+"';");
            // now delete parent records
            statement1.execute("DELETE friends WHERE username1='"+remover+"' AND username2 ='"+other+"';");
            statement1.execute("DELETE friends WHERE username1='"+other+"' AND username2 ='"+remover+"';");
            
/*            statement1.execute("select * from friends where username1='"+remover+"' and username2='"+other+"';");
            resultset1=statement1.getResultSet();
            if(resultset1.next())
            {
             //   resultset1.next();
                statement1.execute("delete friends where username1='"+remover+"' and username2 ='"+other+"';");
                return true;
            }
            else
            {
                 statement1.execute("select * from friends where username1='"+other+"' and username2='"+remover+"';");
                   if(resultset1!=null)
            {
                resultset1.next();
                statement1.execute("delete friends where username1='"+other+"' and username2 ='"+remover+"';");
                return true;
            }
                   else 
                   {return false;}
            }
            */
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RMI.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }

   }
