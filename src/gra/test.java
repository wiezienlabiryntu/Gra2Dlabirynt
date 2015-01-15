package gra;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class test extends Applet
   implements KeyListener {

   int width, height;
   int x=0, y=0;
   String s = "";

   public void init() {
	  
      

      addKeyListener( this );
   }

   public void keyPressed(KeyEvent e) {
	    int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	           System.out.println("Nacisniety przycisk gory");
	           y--;
	           repaint();
	            break;
	        case KeyEvent.VK_DOWN:
	        	
	        	System.out.println("Nacisniety przycisk dolu");
	        	y++;
	        	repaint();
	            break;
	        case KeyEvent.VK_LEFT:
	        	System.out.println("Nacisniety przycisk lewo");
	        	x--;
	        	repaint();
	            break;
	        case KeyEvent.VK_RIGHT :
	        	System.out.println("Nacisniety przycisk prawo");
	        	x++;
	        	repaint();
	            break;
	     }
	} 
   
   
   public void keyReleased( KeyEvent e ) { }
   public void keyTyped( KeyEvent e ) {
   }


   public void paint( Graphics g ) {
      g.setColor( Color.gray );
      g.drawLine( x, y, x, y-10 );
      g.drawLine( x, y, x+10, y );
      g.setColor( Color.green );
      g.drawString( s, x, y );
      
      
      char[][] a = new char[30][30];
  
      
      for (int i=0; i<30; i++)
    	  for (int j=0; j<30; j++)
    	  {
    		  if(i==x && j==y )
    		  {
    			  System.out.println("Gracz");
    			  g.setColor( Color.black ); 
    		  }
    		  else if (Math.random()>0.8)
    		  {
    			  g.setColor( Color.green ); 
    		  }
    		  else
    		  {
    			  g.setColor( Color.gray );
    		  }
    		  g.fillRect(20*i, 20*j, 20, 20); 
    	  }
     
   }
   
 

	  
}
