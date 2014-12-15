package gra;

import java.applet.Applet;  
import java.awt.*;  
import java.io.*;  
import java.net.*;  
import javax.swing.*;  
import java.awt.event.*;  
import java.awt.TextField;  
import java.lang.*;  
public class logowanie extends Applet implements ActionListener{  
    Socket localSocket;  
    PrintWriter out;  
    BufferedReader in;  
    String s;  
    private Button b;  
    private TextField nick,password;  
    public void init(){  
        setLayout(new FlowLayout());  
        nick = new TextField(20);   
        add(nick);  
        password = new TextField(20);  
        add(password);  
        b = new Button("Connect");  
        b.addActionListener(this);  
        add(b);  
    }  
    public void actionPerformed(ActionEvent e)  
    {  
        if(e.getSource() == b)  
        //Create a socket  
        try  
        {  
            localSocket = new Socket("192.168.0.130", 8205);  
            //Setup data stream in and out of socket and from KeyBoard  
            
           
            in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));  
            out= new PrintWriter(localSocket.getOutputStream());  
 
            //flush the buffer if not full!  
            out.println("rejestracja");  
            out.flush();
            out.println(nick.getText());  
            out.flush();
            out.println(password.getText());  
            out.flush();
            System.out.println("Wyslane");
            // read incoming string from socket  
            //System.out.println(in.readLine());  
            String line = in.readLine();
            password.setText(line);  
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
    public static void main( String[] args )  
    {  
    	logowanie   applet = new logowanie();  
        JFrame  frame = new JFrame();  
        frame.setTitle( "Socket Test" );  
        frame.getContentPane().add( applet , BorderLayout.CENTER );  
        applet.init();  
        applet.start();  
        frame.setSize( 460 , 360 );  
        Dimension   d = Toolkit.getDefaultToolkit().getScreenSize();  
        frame.setLocation( ( d.width - frame.getSize().width ) / 2 ,  
        ( d.height - frame.getSize().height ) / 2 );  
        frame.setVisible( true );  
    }  
}  