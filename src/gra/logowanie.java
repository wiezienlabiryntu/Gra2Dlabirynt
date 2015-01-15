package gra;

import java.applet.Applet;  
import java.awt.*;  
import java.io.*;  
import java.net.*;  
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.*;  

import java.awt.event.*;  
import java.awt.TextField;   

public class logowanie extends Applet implements ActionListener{ 
	
	sklep sklepL;
	
    Socket localSocket;  
    PrintWriter out;  
    BufferedReader in;  
    String s;  
    
    //przyciski
    
    //po zalogowaniu
    Button granieB = new Button("Graj");
    Button shopB = new Button("Sklep");  
    Button  statystykiB = new Button("Statystyki");  
    Button  wylogowanieB = new Button("Wylogowanie");
    
    //glowne menu
    Button  rejestracjaB = new Button("Rejestracja"); 
    Button zarejestrujB = new Button("Zarejestruj");
    
    Button logowanieB = new Button("Zaloguj"); 
    
    //przycisk powrotu
    Button menuB = new Button("Menu");
    
    Label nickL=new Label("Login:      ");
    JPasswordField password = new JPasswordField(10); 
    Label passL=new Label("Password:");
    
    
		 
    private TextField nick;  
   
    void logowanie()
    {
    	
    	// czyszcze ekran
    	removeAll();
        password = new JPasswordField(10); 
        nick = new TextField(13);  
        
        //dodanie obiektow
        add(nickL);
        add(nick); 
        add(passL);
        add(password);
        add(rejestracjaB); 
        add(logowanieB);
        validate();
    }
    
    //inicjacja
    public void init(){ 
    	granieB.addActionListener(this);  
		shopB.addActionListener(this);  
		statystykiB.addActionListener(this);  
		wylogowanieB.addActionListener(this);  
		menuB.addActionListener(this);
		zarejestrujB.addActionListener(this); 
		
		logowanieB.addActionListener(this);  
        rejestracjaB.addActionListener(this);
		  
        logowanie();
    }  
    public void actionPerformed(ActionEvent e)  
    {  
        if(e.getSource() == logowanieB)  
        try  
        {  
            localSocket = new Socket("192.168.0.130", 8205);  
            in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));  
            out= new PrintWriter(localSocket.getOutputStream());  
 
            out.println("logowanie");
            out.println(nick.getText()); 
            out.println(password.getPassword());  
            out.flush();
            
            System.out.println("Dane wyslane");
            String zwrot=in.readLine();
            
            info(zwrot);
     
            if (zwrot.equals("Zalogowano"))
            {
             menu();
            }
            	
            
        }  
        catch(UnknownHostException unc)  
        {  
            System.out.println("Connection why not connected");  
        }  
        catch(IOException ioe)  
        {  
            System.out.println(ioe.getMessage());  
        }  
        
        
        //wylogowanie
        if(e.getSource() == wylogowanieB)  
        {
        	info("Wylogowywuje");
           logowanie();
        }
        
        if(e.getSource() == rejestracjaB)  
        {
        	removeAll();
           info("Rejestracja");
           Label LoginL=new Label("Wpisz wymarzony login/haslo:");
           add(LoginL);
           add(nickL);
           add(nick); 
           add(passL);
           add(password);
           add(zarejestrujB); 
           
         
          validate();
          
        }
        
        //nacisnieta rejestacja
        if(e.getSource() == zarejestrujB)
        {
        	rejestracja rejestracjaO=new rejestracja(nick.getText(), String.valueOf(password.getPassword()));
        	rejestracjaO.rejestracja();
        	logowanie();
        }
        
        //nacisniete granie
        if(e.getSource() == granieB)
        {
        	graj();
        }
        
        //przycisk statystyk
        if(e.getSource() == statystykiB)
        {
        	statystyki();
        }
        
        
      //przycisk sklepu
        if(e.getSource() == shopB)
        {
        	sklep();
        }
        
        
        //przejscie do menu
        if(e.getSource() == menuB)
        {
        menu();
        }
        
        
    }  
 
    void info(String inf)
    {
    	System.out.println(inf);
    }
    
    void menu()
    {
    	
    	  removeAll();
    	  
  		  add(granieB);
  		  add(shopB);
  		  add(statystykiB);
  		  add(wylogowanieB);
 
  		revalidate();
        repaint();
    }
    
    
    void statystyki()
    	{
    	removeAll();
    	statystyki statyO=new statystyki(localSocket, in, out);
    	statyO.staty();
    	JLabel statyL=new JLabel(statyO.statystykiL());
    	add(statyL);
    	
    	//przedmioty gracza
    	ArrayList<String> przedmioty=statyO.przedmioty();

    	
    	StringTokenizer st;
    	String[] temp=new String[8];
    	
    	System.out.println("Ilosc przedmiotow:"+przedmioty.size());
    	
    	for (int i = 0; i < przedmioty.size(); i++)
    	{
    		st = new StringTokenizer(przedmioty.get(i), "|");
    		
    		//przycisk
    		Button sprzedajB = new Button("Sprzedaj");	
    		Button zalozB = new Button("Zaloz");	
    		Button sciagnijB=new Button("Sciagnij");
 
    		int j=0;
    		while (st.hasMoreTokens())
    		{
    			temp[j]=st.nextToken();
    			j++;
    		}
    		
            Label label= new Label("Id:"+temp[0]+" Atak:"+temp[1]+" Obrona:"+temp[2]+" Zycie:"+temp[3]+" Rodzaj:"+temp[4]+" Nazwa:"+temp[5]+" Koszt:"+temp[6]);
            final int idButton=Integer.parseInt(temp[0]);
            
            add(label);
            add(sprzedajB);
            
            //jak jest nieubrane
            if(temp[7].equals("nie"))
            {
            add(zalozB);
            //nacisniecie
            zalozB.addActionListener(new ActionListener() {
          	   public void actionPerformed(ActionEvent ae2) {
          	     try {
					statyO.ubierz(idButton);
					menu();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          	   }
          	});
            }
            else
            {
            	//jak jest ubrane
            	  add(sciagnijB);
                  //nacisniecie
            	  sciagnijB.addActionListener(new ActionListener() {
                	   public void actionPerformed(ActionEvent ae2) {
                	     try {
      					statyO.sciagnij(idButton);
      					menu();
      				} catch (IOException e) {
      					// TODO Auto-generated catch block
      					e.printStackTrace();
      				}
                	   }
                	});
            }
            
            //nacisniecie
            sprzedajB.addActionListener(new ActionListener() {
          	   public void actionPerformed(ActionEvent ae2) {
          	     try {
					statyO.sprzedajB(idButton);
					menu();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          	   }
          	});
    	}
    	
    	//menu powrotne
    	add(menuB);
    	
    	validate();
    	repaint();
		
    	}


    void sklep()
    {
    	removeAll();
    	sklepL=new sklep(localSocket, in, out);
    	ArrayList<String> przedmioty=sklepL.staty();
    	
    	StringTokenizer st;
    	String[] temp=new String[7];
    	
    	System.out.println("Ilosc przedmiotow:"+przedmioty.size());
    	
    	for (int i = 0; i < przedmioty.size(); i++)
    	{
    		st = new StringTokenizer(przedmioty.get(i), "|");
    		
    		//przycisk
    		Button kup = new Button("Kup");	
 
    		int j=0;
    		while (st.hasMoreTokens())
    		{
    			temp[j]=st.nextToken();
    			j++;
    		}
    		
            Label label= new Label("Id:"+temp[0]+" Atak:"+temp[1]+" Obrona:"+temp[2]+"Zycie:"+temp[3]+" Rodzaj:"+temp[4]+" Nazwa:"+temp[5]+" Koszt:"+temp[6]);
            final int idButton=Integer.parseInt(temp[0]);
            
            add(label);
            add(kup);
            
            //nacisniecie
            kup.addActionListener(new ActionListener() {
          	   public void actionPerformed(ActionEvent ae2) {
          	     try {
					sklepL.kupno(idButton);
					menu();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          	   }
          	});
          	
    	}
    	
    	add(menuB);  
		
		validate();
    }
    
  
    void graj()
    {
    	System.out.println("Wczytuje plansze");
    	removeAll();
    	
    	add(menuB);
    	validate();
    	
    	
    }

}