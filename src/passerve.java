import android.jimtahu.passgen.Generator;

import java.util.Calendar;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Random;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class passerve extends Thread {
	private static long secret;
	private static Properties settings;
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
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("Interupted while waiting for unlock to compleate.");
			e.printStackTrace();
		}
    }//end run_unlock
    
    /**
     * locks the computer (also setup for gnome3 atm)
     */
    private void run_lock(){	
    	try {
    		ProcessBuilder locker = new ProcessBuilder("/usr/bin/gnome-screensaver-command","-l");
        	locker.environment().put("DISPLAY", ":0.0");
			Process screen_run = locker.start();
			screen_run.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("Interupted while waiting for lock to compleate.");
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
                Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE)*passerve.secret);
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
                    run_lock();
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
    public static void main(String args[]){
    	settings = new Properties();
    	settings.setProperty("portnum", Integer.toString(7310));
    	settings.setProperty("secret", Long.toString((new Random()).nextLong()));
    	try {
			settings.loadFromXML(new FileInputStream(new File("serversettings.xml")));
		} catch (InvalidPropertiesFormatException e) {
			System.err.println("Settings file invalid, ignoring");
			System.err.println(e);
		} catch (FileNotFoundException e) {
			System.err.println("No settings file found, using defualts");
			System.err.println(e);
		} catch (IOException e) {
			System.err.println("Unable to read settings file, using defualts");
			System.err.println(e);
		}
    	if(args.length>0){
    		passerve.secret = Long.parseLong(args[0]);
    		settings.setProperty("secret",args[0]);
    	}else{
    		passerve.secret = Long.parseLong(settings.getProperty("secret"));
    	}
        System.err.print("The key is ");
        System.err.println(passerve.secret);
        System.err.println("saving settings");
        try {
			settings.storeToXML(new FileOutputStream(new File("serversettings.xml")),"");
		} catch (Exception e1) {
			System.err.println("Error saving settings");
			e1.printStackTrace();
		}
        System.err.println("server starting\n\n");
        ServerSocket server;
		try {
			server = new ServerSocket(Integer.parseInt(settings.getProperty("portnum")));
			while(true){
	            Socket datacom = server.accept();
	            new passerve(datacom).start();
	        }//end forever
		} catch (IOException e) {
			System.err.println("Error while listening for clients");
			e.printStackTrace();
		}      
    }//end main
}//end class passerve

