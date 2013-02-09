import android.jimtahu.passgen.Generator;

import java.util.Calendar;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class passerve extends Thread {
    private Socket datacom;

    /**
     * Creates new thread for handeling a connection
     */
    public passerve(Socket d){
        datacom = d;
    }

    /**
     * unlocks the computer (setup for gnome3 atm)
     */
    private void run_unlock(){	
    	try {
    		ProcessBuilder unlocker = new ProcessBuilder("/usr/bin/gnome-screensaver-command","-d");
        	unlocker.environment().put("DISPLAY", ":0.0");
			Process screen_run = unlocker.start();
			screen_run.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("Interupted while waiting for unlock to compleate.");
			e.printStackTrace();
		}
    }//end run_unlock
    
    /**
     * Handles auth request
     */
    public void run(){
            try{
                Calendar now = Calendar.getInstance();
                Scanner input = new Scanner(datacom.getInputStream());
                OutputStreamWriter output = new OutputStreamWriter(datacom.getOutputStream());
                output.write("Authcode:");
                output.flush();
                Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE));
                String real = Generator.passcode();
                System.err.println("Real "+real);
                String test = input.next();
                System.err.println("Test "+test);
                if(real.equals(test)){
                    System.err.println("Passed\n");
                    output.write("Passed\n");
                    run_unlock();
                }else{
                    System.err.println("Failed\n");
                    output.write("Failed\n");
                }
                output.flush();
                datacom.close();
            }catch(IOException ex){
                System.err.println(ex);
                System.err.println("Connection dropped"); 
            }
    }//end run

    /**
     * Main (server thread)
     */
    public static void main(String args[]) throws Exception{
        System.err.println("server starting");
        ServerSocket server = new ServerSocket(7310);
        while(true){
            Socket datacom = server.accept();
            new passerve(datacom).start();
        }//end forever
    }//end main
}//end class passerve

