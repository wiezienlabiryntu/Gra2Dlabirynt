package gra;

import java.io.*;  
import java.net.*;  
import java.util.ArrayList;


public class sklep{  
    Socket localSocket;  
    PrintWriter out;  
    BufferedReader in;  
    
    String s;  
    String user;
    String password;
    
   int[] staty=new int[5];
    
    public sklep(Socket localSocket2, BufferedReader in2, PrintWriter out2) {
		// TODO Auto-generated constructor stub
    	this.localSocket=localSocket2;
    	this.in=in2;
    	this.out=out2;
	}

    //akcje przyciskow kup
  
    
    
	public ArrayList staty()  
    {  
		ArrayList<String> listaPrzedmiotow = new ArrayList<String>();
        try  
        {  
            out.println("sklep"); 
            out.flush();
            System.out.println("Wyslana prosba o przedmioty w sklepie");
            String dane="start";
            
            while (!dane.equals("koniec"))
            {
            	dane=in.readLine();
            	if (dane.equals("koniec"))
            		break;
            	
            	listaPrzedmiotow.add(dane);
            	System.out.println(dane);
            }
            System.out.println("Ilosc przedmiotow:"+listaPrzedmiotow.size());
            System.out.println(listaPrzedmiotow);
        }  
        catch(UnknownHostException unc)  
        {  
            System.out.println("Connection why not connected");  
        }  
        catch(IOException ioe)  
        {  
            System.out.println(ioe.getMessage());  
        }
		return listaPrzedmiotow;  
    }
    
	void kupno(int id) throws IOException
	{
		  System.out.println("Proba kupna przedmiotu z id:"+id);
		  out.println("kupno");
		  out.println(id);
          out.flush();
          System.out.println(in.readLine());
	}
	
	String statystykiL()
	{	
		return "<html>Zycie: "+staty[0]+"<br>Pancerz: "+staty[1]+"<br>Atak: "+staty[2]+"<br>Poziom: "+staty[3]+"<br>Pieniadze: "+staty[4]+"</html>";
	}
    void info(String inf)
    {
    	System.out.println(inf);
    }
 
}  
