import java.rmi.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RMIinterface extends Remote {

   public boolean verify_user(String a,String b) throws RemoteException;
   public boolean signup(String a,String b) throws RemoteException;
   public boolean change_password(String uname, String pd_old, String pd_new) throws RemoteException;
   public boolean accept_request(String receiver, String sender) throws RemoteException;
   public boolean reject_request(String rejecter,String sender) throws RemoteException;
   public boolean insert_request(String sender,String send_to) throws RemoteException;
   public boolean remove_friend(String remover,String other) throws RemoteException;
   public int Total_Friends(String friend_name) throws  RemoteException;
   public String Friend_Name(String friend_name, int i)  throws  RemoteException;


}