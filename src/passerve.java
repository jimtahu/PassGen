import android.jimtahu.passgen.Generator;

import java.util.Calendar;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.OutputStreamWriter;

public class passerve {
    public static void main(String args[]) throws Exception{
        System.err.println("server starting");
        ServerSocket server = new ServerSocket(7310);
        while(true){
            Socket datacom = server.accept();
            Calendar now = Calendar.getInstance();
            Scanner input = new Scanner(datacom.getInputStream());
            OutputStreamWriter output = new OutputStreamWriter(datacom.getOutputStream());
            output.write("Authcode:");
            output.flush();
            Generator.seed(now.get(Calendar.HOUR));
            String real = Generator.passcode();
            System.err.println("Real "+real);
            String test = input.next();
            System.err.println("Test "+test);
            if(real.equals(test)){
                output.write("Passed\n");
            }else{
                output.write("Failed\n");
            }
            output.flush();
            datacom.close();
        }//end forever
    }//end main
}//end class passerve

