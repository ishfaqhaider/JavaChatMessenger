/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ishfaq Haider 1418-FBAS/BSSE/F11A
 */
public class Friends {
    public String fr_u_id;
    public Boolean is_login; 
    public String chat_history;
   public Friends()
    {
    fr_u_id=new String("");
    is_login=new Boolean(false);
    chat_history=new String("");
    }
   public Friends(String name)
    {
    fr_u_id=name;
    is_login=new Boolean(false);
    chat_history=new String("");
    }
}
