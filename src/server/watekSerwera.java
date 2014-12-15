package server;

import java.sql.*;
import java.net.*;
import java.io.*;

public class watekSerwera extends Thread
{ private Socket socket;
private int staties[];
public watekSerwera(Socket socket) 
{ 
	this.socket=socket;
	}

public void run()
{
try {
	System.out.println("Połączony z "+ socket.getRemoteSocketAddress()); 

boolean zalogowany=false;
int staty[]=new int[5];
DataInputStream in = new DataInputStream(socket.getInputStream());
PrintWriter out= new PrintWriter(socket.getOutputStream());  

do{
String dzialanie=in.readLine();
if(dzialanie.equals("rejestracja"))
{
	System.out.println("Proba rejestracji uzytkownika");
	
	Boolean rejestracja=rejestracj(in.readLine(), in.readLine());
	if (rejestracja==true)
	out.write("Zarejestrowano");
	else
	out.write("Nie zarejestrowano");
	
}
else if ( dzialanie.equals("logowanie"))
{
String uzytkownik=in.readLine();
String haslo=in.readLine();
System.out.println("Proba zalogowania uzytkownika:"+uzytkownik+" Haslo:"+haslo);
staty=sprawdz(uzytkownik, haslo);
if (staty[0]==0)
{
	System.out.println("Bledny login/haslo");
//out.writeBoolean(false);
}
else
{
	System.out.println("Zalogowano");
	//out.writeBoolean(true);
}
}


}while (staty[0]==0);




//in.close(); 
//out.close(); 
//socket.close();


}catch(SocketTimeoutException s)
{ System.out.println("Timed out!");
}catch(IOException e) 
{ e.printStackTrace();
}
}


private int[] sprawdz(String username, String haslo)
{
	int[] dane = new int[5];
	for(int i=0; i<dane.length;i++)
		dane[i]=0;
	
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
    String query = "select `zycie`, `pancerz`, `atak`, `poziom` from gracz where nick='"+username+"' and haslo='"+haslo+"';";
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(query);
            int i=0;
          
            while(rs.next()) {
            	dane[i]=rs.getInt(i+1);
                    i=i+1;
            }
            conn.close();
            
    }
    //Wyrzuć wyjątki jężeli nastąpią błędy z podłączeniem do bazy danych lub blędy zapytania o dane
    catch(ClassNotFoundException wyjatek) {
            System.out.println("Problem ze sterownikiem");
    }
    catch(SQLException wyjatek) {
        System.out.println("SQLException: " + wyjatek.getMessage());
        System.out.println("SQLState: " + wyjatek.getSQLState());
        System.out.println("VendorError: " + wyjatek.getErrorCode());
    }
    return dane;

}

@SuppressWarnings("finally")
boolean rejestracj(String uzytkownik, String haslo) throws IOException
{
	boolean stworzony=false;
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie do bazy danych
    
    	    Connection conn = null;
   
    try {
    	
    	//Ustawiamy dane dotyczące podłączenia
        conn = DriverManager.getConnection(polaczenieURL); 
        //Ustawiamy sterownik MySQL
        Class.forName("com.mysql.jdbc.Driver");
        //Uruchamiamy zapytanie do bazy danych
        Statement stmt = conn.createStatement();
        
        
        ResultSet rs = stmt.executeQuery("Select * from gracz where nick='"+uzytkownik+"';");
        if(rs.next()){
        	return false;
        }
        
        rs = stmt.executeQuery("Select max(id_gracza) from gracz;");
        rs.next();
 
        String query = "INSERT INTO `Gra`.`gracz` (`id_gracza`, `nick`, `haslo`) VALUES ('"+(rs.getInt(1)+1)+"', '"+uzytkownik+"', '"+haslo+"');";
        stmt.executeUpdate(query);
           
           
            conn.close();
            stworzony=true;
    }
    //Wyrzuć wyjątki jężeli nastąpią błędy z podłączeniem do bazy danych lub blędy zapytania o dane
    catch(ClassNotFoundException wyjatek) {
            System.out.println("Problem ze sterownikiem");
            stworzony=false;
    }
    catch(SQLException wyjatek) {
        System.out.println("SQLException: " + wyjatek.getMessage());
        System.out.println("SQLState: " + wyjatek.getSQLState());
        System.out.println("VendorError: " + wyjatek.getErrorCode());
        stworzony=false;
    }
    finally {
    	 return stworzony;
    }
   
}
}
