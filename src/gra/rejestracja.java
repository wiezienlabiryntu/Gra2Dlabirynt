package gra;
 
import java.io.*;  
import java.net.*;  


public class rejestracja{  
    Socket localSocket;  
    PrintWriter out;  
    BufferedReader in;  
    String s;  
    String user;
    String password;
    
    rejestracja(String user, String password)
    {
    	this.user=user;
    	this.password=password;
    }
    
    public void rejestracja()  
    {  
        try  
        {  
            localSocket = new Socket("192.168.0.130", 8205);  
        
            in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));  
            out= new PrintWriter(localSocket.getOutputStream());  
 
            out.println("rejestracja");
            
            out.println(user);           
            out.println(password);  
            out.flush();
            System.out.println("Dane wyslane");
            info(in.readLine());
 
        }  
        catch(UnknownHostException unc)  
        {  
            System.out.println("Connection why not connected");  
        }  
        catch(IOException ioe)  
        {  
            System.out.println(ioe.getMessage());  
        }  
    }
    
    void info(String inf)
    {
    	System.out.println(inf);
    }
 
}  
