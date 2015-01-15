package gra;

import java.io.*;  
import java.net.*;  
import java.util.ArrayList;
import java.util.StringTokenizer;


public class statystyki{  
    Socket localSocket;  
    PrintWriter out;  
    BufferedReader in;  
    
    String s;  
    String user;
    String password;
    
   int[] staty=new int[5];
    
    public statystyki(Socket localSocket2, BufferedReader in2, PrintWriter out2) {
		// TODO Auto-generated constructor stub
    	this.localSocket=localSocket2;
    	this.in=in2;
    	this.out=out2;
	}


	public void staty()  
    {  
        try  
        {  
            out.println("statystyki"); 
            out.flush();
            System.out.println("Wyslana prosba o statystyki");
            String dane=in.readLine();
            
            StringTokenizer st = new StringTokenizer(dane, "|");
             
            int i=0;
            while (st.hasMoreTokens())
            {
               staty[i]= Integer.parseInt(st.nextToken());
               i++;
            }
            
            System.out.println("Zycie: "+staty[0]+"\nPancerz: "+staty[1]+"\nAtak: "+staty[2]+"\nPoziom: "+staty[3]+"\nPieniadze: "+staty[4]);
            
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
	
	
	public ArrayList przedmioty()  
    {  
		ArrayList<String> listaPrzedmiotow = new ArrayList<String>();
        try  
        {  
            out.println("przedmioty"); 
            out.flush();
            
            System.out.println("Wyslana prosba o przedmioty gracza");
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
	
	
    //zwracanie wszystkich statystyk
	String statystykiL()
	{
		
		return "<html>Zycie: "+staty[0]+"<br>Pancerz: "+staty[1]+"<br>Atak: "+staty[2]+"<br>Poziom: "+staty[3]+"<br>Pieniadze: "+staty[4]+"</html>";
	}
    void info(String inf)
    {
    	System.out.println(inf);
    }


    //sprzedaz itemka
	public void sprzedajB(int id) throws IOException {
		// TODO Auto-generated method stub

			  System.out.println("Proba sprzedazy przedmiotu z id:"+id);
			  out.println("sprzedaz");
			  out.flush();
			  out.println(id);
	          out.flush();
	          System.out.println(in.readLine());
		
	}
	
    //ubranie itemka
	public void ubierz(int id) throws IOException {
		// TODO Auto-generated method stub

			  System.out.println("Proba ubioru przedmiotu z id:"+id);
			  out.println("ubierz");
			  out.flush();
			  out.println(id);
	          out.flush();
	          System.out.println(in.readLine());
		
	}
	
	public void sciagnij(int id) throws IOException {
		// TODO Auto-generated method stub

			  System.out.println("Proba sciagniecia przedmiotu z id:"+id);
			  out.println("sciagnij");
			  out.flush();
			  out.println(id);
	          out.flush();
	          System.out.println(in.readLine());
		
	}
 
}  
