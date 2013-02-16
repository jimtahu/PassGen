package android.jimtahu.passgen;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;

import android.os.AsyncTask;

/** 'Thread' for doing actual code exchange */
class CodeExchange extends AsyncTask<Integer, String, String> {
    private PassGen screen;
    private String host;
    private String result;
    private long secret;

    /**
     * @param o Main app
     * @param h Host name of server
     */
    public CodeExchange(PassGen o, String h, long s){
        this.screen=o;
        this.host=h;
        this.secret=s;
    }//end constructor

    /** equivlant to run */
    @Override
    public String doInBackground(Integer ... opt){
        String msg = "fish";
        try{
            Calendar now = Calendar.getInstance();
            Socket datacom = new Socket(this.host, 7310);
            Scanner input = new Scanner(datacom.getInputStream());
            OutputStreamWriter host = new OutputStreamWriter(datacom.getOutputStream());
            Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE)*secret);
            if(opt[0]==0)
            	host.write(Generator.passcode()+"\n");
            else
            	host.write("fish\n");
            host.flush();
            while(input.hasNext()){
            	msg = input.next();
            }
            datacom.close();
        }catch(UnknownHostException ex){
            msg = ex.toString();
        }catch(IOException ex){
            msg = ex.toString();
            ex.printStackTrace();
        }//end try catch
        this.result=msg;
        return msg;
    }//end run

    /**runs in the main thread after doInBackground compleates */
    protected void onPostExecute(String msg){
        this.screen.setOutput(this.result);
    }//end onPostExecute
}//end CodeExchange

