

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ishfaq Haider 1418-FBAS/BSSE/F11A
 */
class Test {
   public Integer operator;
   public String string_a;
   public String string_b;
   public String string_c;
   public String string_d;
   
   public static final String decode_seprator="@2/A%h>";
   public static final int total_members=5;
public Test(){
operator=new Integer(0);
string_a=new String("");
string_b=new String("");
string_c=new String("");
string_d=new String("");

}
public String Encode(){
return operator+decode_seprator+string_a+decode_seprator+string_b+decode_seprator+string_c+decode_seprator+string_d+decode_seprator; 
}
public boolean Decode(String ab){
//String gap=new String("@#/&%");
String temp=new String("");
String last_five=new String("");

if(ab.length()<=(decode_seprator.length()*3))
{Reset();
return false;
}
int i=0;
for(int k=1;k<=total_members;k++) // fetching one operator, aad (total_members-1) strings 
{
    temp="";
    last_five="";
do{
 //   System.out.println("Char at "+i+"="+ab.charAt(i)); //--
    last_five+=ab.charAt(i);    
    temp+=ab.charAt(i);
    if(last_five.length()>decode_seprator.length())
{
        // trim last_five
    last_five=last_five.substring((last_five.length()-decode_seprator.length())); 
}
i++;
}while(!last_five.equals(decode_seprator) && i<ab.length());
if(last_five.equals(decode_seprator))
{
temp=temp.substring(0, (temp.length()-decode_seprator.length()));
 //System.out.println("gap="+gap); //--
}
else{ Reset(); return false; }
switch(k){
    case 1: // geting operator
    {    try{
operator=Integer.parseInt(temp);
// System.out.println("op="+operator); //--
}
catch(NumberFormatException ex)
{
Reset();
return false;}} // eof case 1
    case 2: // geting 1st string 
    {
    string_a=temp;
    break;}
    case 3: // geting 2nd string 
    {
    string_b=temp;    
    break;}
    case 4: // geting 3rd string 
    {
    string_c=temp;    
    break;}
    case 5: // geting 4th string 
    {
    string_d=temp;    
    break;}

}// eof switch
}// eof for loop

return true;
}
public void show(){
System.out.println("Operator: "+operator);
System.out.println("string_a: "+string_a);
System.out.println("string_b: "+string_b);
System.out.println("string_c: "+string_c);
System.out.println("string_d: "+string_d);

}
public void Reset(){
operator=0;
string_a="";
string_b="";
string_c="";
string_d="";
}
}
