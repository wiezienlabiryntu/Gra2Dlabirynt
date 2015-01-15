package server;

import java.sql.*;
import java.net.*;
import java.io.*;

public class watekSerwera extends Thread
{ 
	private Socket socket;
	String uzytkownik;
	String statystyki;
	int staty[]=new int[5];
	
public watekSerwera(Socket socket) 
{ 
	this.socket=socket;
	}

public void run()
{
try {
	System.out.println("Połączony z "+ socket.getRemoteSocketAddress()); 

	DataInputStream in = new DataInputStream(socket.getInputStream());
	PrintWriter out= new PrintWriter(socket.getOutputStream());  

do{
	System.out.println("Oczekiwanie na komende");
	String dzialanie=in.readLine();
	//co otrzymane
	System.out.println(dzialanie);

	//postepowanie
if(dzialanie.equals("rejestracja"))
{
	System.out.println("Proba rejestracji uzytkownika");
	
	Boolean rejestracja=rejestracj(in.readLine(), in.readLine());
	if (rejestracja==true)
	{
		System.out.println("Zarejestrowano");
		out.println("Zarejestrowano");
	}
	else
	{
		System.out.println("Zarejestrowano");
		out.println("Nie zarejestrowano");
	}
	
	out.flush();

}
else if ( dzialanie.equals("logowanie"))
{
	uzytkownik=in.readLine();
 	String haslo=in.readLine();
 	System.out.println("Proba zalogowania uzytkownika:"+uzytkownik+" Haslo:"+haslo);
	staty=sprawdz(uzytkownik, haslo);

if (staty[0]==0)
{
	System.out.println("Bledny login/haslo");
	out.println("Bledny login/haslo");
}
else
{
	System.out.println("Zalogowano");
	out.println("Zalogowano");
}
out.flush();
}
else if(dzialanie.equals("statystyki"))
{
	obliczenieStatystyk();
	System.out.println("Prosba o statystyki");
	out.println(statystyki);
	System.out.println("Statystyki wyslane: "+statystyki);
	out.flush();
	
	if (in.readLine().equals("przedmioty"))
	{
		System.out.println("Otrzymano prosbe o przedmioty");
		pobraniePrzedmiotowUz(out);
	}
	else
		System.out.println("Nie otrzymano prosby o przedmioty");
	
		
}
else if(dzialanie.equals("sklep"))
{
	
	System.out.println("Prosba o przedmioty");
	pobraniePrzedmiotow(out);
}
else if(dzialanie.equals("kupno"))
{
	
	System.out.println("Proba kupna przedmiotu");
	kupnoPrzedmiotu(out, in.readLine());
}
else if(dzialanie.equals("ubierz"))
{
	
	System.out.println("Proba ubrania przedmiotu");
	zalozeniePrzedmiotu(out, in.readLine());
}
else if(dzialanie.equals("sciagnij"))
{
	
	System.out.println("Proba sciagniecia przedmiotu");
	sciagnieciePrzedmiotu(out, in.readLine());
}
else if(dzialanie.equals("sprzedaz"))
		{
	System.out.println("Proba sprzedazy przedmiotu");
	sprzedazPrzedmiotu(out, in.readLine());
		}



}while (staty[0]!=0);

//zamkniecie socketu/buforow
in.close(); 
out.close(); 
socket.close();


}catch(SocketTimeoutException s)
{ System.out.println("Timed out!");
}catch(IOException e) 
{ e.printStackTrace();
}
}

void kupnoPrzedmiotu(PrintWriter out, String id)
{
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
  
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
          //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            
            String koszt = "select `koszt` from przedmiot where id_przedmiotu='"+id+"' and `id_gracza` is null";
            System.out.println(koszt);
            ResultSet rs = stmt.executeQuery(koszt);
            
            if (!rs.next())
            {
            	out.println("Przedmiot zostal juz kupiony");
        	 	out.flush();
            	throw new IllegalArgumentException("Przedmiot zostal juz kupiony");
            }
            	
            int kosztZ=rs.getInt(1);
            
            //ilosc pieniedzy
            String pieniadze = "select `Pieniadze` from gracz where `id_gracza`='"+staty[5]+"';";
            System.out.println(pieniadze);
            rs = stmt.executeQuery(pieniadze);
            rs.next();
            int pieniadzeZ=rs.getInt(1);
            
            //czy starczy pieniedzy
            if(pieniadzeZ-kosztZ>=0){
            	//przypisanie przedmiotu
            	String query = "update przedmiot set id_gracza='"+staty[5]+"' where id_przedmiotu='"+id+"';";
            	PreparedStatement preparedStmt = conn.prepareStatement(query);
            	preparedStmt.executeUpdate();
            	
            	//odjecie pieniedzy
            	 query = "UPDATE `gra`.`gracz` SET `Pieniadze` = Pieniadze-'"+kosztZ+"' WHERE id_gracza='"+staty[5]+"';";
            	 preparedStmt = conn.prepareStatement(query);
            	 preparedStmt.executeUpdate();
            	 out.println("Zakupiony");
            	 out.flush();
            	 System.out.println("Przedmiot przypisany do konta");
            }
            else
            {
            	 out.println("Za malo funduszy");
            	 	out.flush();
            	 	 System.out.println("Za malo funduszy");
            }

           
          
            conn.close();
    }
    //Wyrzuć wyjątki jężeli nastąpią błędy z podłączeniem do bazy danych lub blędy zapytania o dane
    catch(ClassNotFoundException wyjatek) {
            System.out.println("Przedmiot zostal juz kupiony");
            out.println("Przedmiot zostal juz kupiony");
    	 	out.flush();
            
    }
    
    catch(IllegalArgumentException wyjatek) {
        System.out.println("Problem ze sterownikiem");
}

    catch(SQLException wyjatek) {
        System.out.println("SQLException: " + wyjatek.getMessage());
        System.out.println("SQLState: " + wyjatek.getSQLState());
        System.out.println("VendorError: " + wyjatek.getErrorCode());
    }
}


void zalozeniePrzedmiotu(PrintWriter out, String id)
{
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
  
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            //sprawdzenie czy przedmiot nalezy do gracza
            String koszt = "select id_przedmiotu, rodzaj from przedmiot where id_przedmiotu='"+id+"' and `id_gracza`='"+staty[5]+"';";
            ResultSet rs = stmt.executeQuery(koszt);
            rs.next();
            
            
            if(rs.getString(1).equals(id))
            {
            	 //sciaganie innych przedmiotow tego typu
            	 String query = "UPDATE `gra`.`przedmiot` SET `zalozone` = 'nie' WHERE rodzaj='"+rs.getString(2)+"' and `przedmiot`.`id_gracza` ="+staty[5];
                 stmt = conn.prepareStatement(query);
                 stmt.executeUpdate(query);
                 
                 //zakladanie przedmiotu
           
                 query = "UPDATE `gra`.`przedmiot` SET `zalozone` = 'tak' WHERE `przedmiot`.`id_przedmiotu` ="+id;
                 stmt = conn.prepareStatement(query);
                 stmt.executeUpdate(query);
                 
                 //wysylanie logow
               	 out.println("Ubrany");
               	 out.flush();
               	 System.out.println("Przedmiot ubrany na postac");	
            }
            else
            {
            	out.println("Blad, przedmiot nie nalezy do gracza");
              	 out.flush();
              	 System.out.println("Blad, przedmiot nie nalezy do gracza");	
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
}


void sciagnieciePrzedmiotu(PrintWriter out, String id)
{
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
  
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            //sprawdzenie czy przedmiot nalezy do gracza
            String koszt = "select id_przedmiotu, rodzaj from przedmiot where id_przedmiotu='"+id+"' and `id_gracza`='"+staty[5]+"';";
            ResultSet rs = stmt.executeQuery(koszt);
            rs.next();
            
            
            if(rs.getString(1).equals(id))
            {
            	 //sciaganie przedmiotow tego typu
            	 String query = "UPDATE `gra`.`przedmiot` SET `zalozone` = 'nie' WHERE rodzaj='"+rs.getString(2)+"' and `przedmiot`.`id_gracza` ="+staty[5];
                 stmt = conn.prepareStatement(query);
                 stmt.executeUpdate(query);
                 
                 
                 //wysylanie logow
               	 out.println("Sciagniete");
               	 out.flush();
               	 System.out.println("Przedmiot Sciagniety");	
            }
            else
            {
            	out.println("Blad, przedmiot nie nalezy do gracza");
              	 out.flush();
              	 System.out.println("Blad, przedmiot nie nalezy do gracza");	
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
}


void sprzedazPrzedmiotu(PrintWriter out, String id)
{
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
  
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            
            String koszt = "select `koszt` from przedmiot where id_przedmiotu='"+id+"' and `id_gracza`='"+staty[5]+"';";
            ResultSet rs = stmt.executeQuery(koszt);
            
            if(rs.next()){
            	int kosztZ=rs.getInt(1);
            	//dodawanie pieniedzy
            	 String query = "update gracz set Pieniadze=Pieniadze+'"+kosztZ+"' where id_gracza='"+staty[5]+"';";
            	 PreparedStatement preparedStmt = conn.prepareStatement(query);
                 preparedStmt.executeUpdate();
                 //dodawanie przedmiotu do sklepu
                  query = "UPDATE `gra`.`przedmiot` SET `zalozone`='nie', `id_gracza` = NULL WHERE `przedmiot`.`id_przedmiotu` = "+id;
                 
                 preparedStmt = conn.prepareStatement(query);
                 preparedStmt.executeUpdate();
                 
                 System.out.println("Przedmiot sprzedany do konta");
                 
                 out.println("Sprzedany");
            	 	out.flush();
            }
            else
            {
            	//Błędne zapytanie
            	System.out.println("Przedmiot nie nalezy do gracza");
            	out.println("Przedmiot nie nalezy do mnie");
        	 	out.flush();
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
}


void pobraniePrzedmiotow(PrintWriter out)
{	
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
    String query = "select `id_przedmiotu`, `Atak`, `pancerz`, `zycie`, `Rodzaj`, `Nazwa`, `koszt` from przedmiot where `id_gracza` IS NULL";
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
            	String dane=rs.getString(1);
            	 for (int i=1; i<=6; i++)
                 {
                  	dane=dane+"|"+rs.getString(i+1);
                  }
            	 out.println(dane);
            	 out.flush();
            	 System.out.println("Wyslalem przedmiot:"+dane);
            }
            out.println("koniec");
       	 	out.flush();
           System.out.println("Przedmioty wyslane");
           
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

}


void pobraniePrzedmiotowUz(PrintWriter out)
{	
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
    String query = "select `id_przedmiotu`, `Atak`, `pancerz`,`zycie`, `Rodzaj`, `Nazwa`, `koszt`, `zalozone` from przedmiot where id_gracza='"+staty[5]+"';";
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
            	String dane=rs.getString(1);
            	 for (int i=1; i<=7; i++)
                 {
                  	dane=dane+"|"+rs.getString(i+1);
                  }
            	 out.println(dane);
            	 out.flush();
            	 System.out.println("Wyslalem przedmiot:"+dane);
            }
            out.println("koniec");
       	 	out.flush();
           System.out.println("Przedmioty wyslane");
           
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

}


void obliczenieStatystyk()
{
	
	
	
	
	
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
     Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            
            //statystyki bohatera
            staty=sprawdz(staty[5]);
        	
        	//sprawdzenie czy przedmiot nalezy do gracza
            String statystykiPrzedmiotow = "select `zycie`, `pancerz`, `atak` from przedmiot where zalozone='tak' and `id_gracza`='"+staty[5]+"';";
            ResultSet rs = stmt.executeQuery(statystykiPrzedmiotow);
            
            while( rs.next())
            {
            	for (int i=0; i<3; i++)
            		staty[i]+=rs.getInt(i+1);
            }
          
        	
        	statystyki=Integer.toString(staty[0]);
        	for (int i=1; i<=4; i++)
        	{
        		statystyki=statystyki+"|"+Integer.toString(staty[i]);
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
}


//logowanie
private int[] sprawdz(String username, String haslo)
{
	int[] dane = new int[6];
	for(int i=0; i<dane.length;i++)
		dane[i]=0;
	
	//Polaczenie z baza danych
    String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
    //Tworzymy proste zapytanie doa bazy danych
    String query = "select `zycie`, `pancerz`, `atak`, `poziom`, `Pieniadze`, `id_gracza` from gracz where nick='"+username+"' and haslo='"+haslo+"';";
    Connection conn = null;
   
    try {
            //Ustawiamy dane dotyczące podłączenia
            conn = DriverManager.getConnection(polaczenieURL);  
            //Ustawiamy sterownik MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Uruchamiamy zapytanie do bazy danych
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
        
           for (int i=0; i<=5; i++)
           {
            	dane[i]=rs.getInt(i+1);
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

//statystyki po id_gracza
private int[] sprawdz(int staty2)
{
	int[] dane = new int[6];
	for(int i=0; i<dane.length;i++)
		dane[i]=0;
	
	//Polaczenie z baza danych
  String polaczenieURL = "jdbc:mysql://127.0.0.1/gra?user=root&password=keep3class";
  //Tworzymy proste zapytanie doa bazy danych
  String query = "select `zycie`, `pancerz`, `atak`, `poziom`, `Pieniadze`, `id_gracza` from gracz where id_gracza='"+staty2+"';";
  Connection conn = null;
 
  try {
          //Ustawiamy dane dotyczące podłączenia
          conn = DriverManager.getConnection(polaczenieURL);  
          //Ustawiamy sterownik MySQL
          Class.forName("com.mysql.jdbc.Driver");
          //Uruchamiamy zapytanie do bazy danych
          Statement stmt = conn.createStatement();
          
          ResultSet rs = stmt.executeQuery(query);
          rs.next();
      
         for (int i=0; i<=5; i++)
         {
          	dane[i]=rs.getInt(i+1);
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
	System.out.println("Uzytkownik:"+uzytkownik+" Haslo:"+haslo);
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