import java.net.*;
import java.io.*;
import java.util.Arrays.*;
import java.util.Arrays;

public class PacketReceiver extends Thread {

    public static void main(String[] args) throws Exception {

        
        System.out.println("Server Listening on 8888");
        ServerSocket serverSocket = new ServerSocket(8888);
        //server timeout 60 minutes
        serverSocket.setSoTimeout(1000 * 60 * 60);

        //Below method waits until client socket tries to connect
        Socket server = serverSocket.accept();

        //Read from client using input stream
        DataInputStream in = new DataInputStream(server.getInputStream());
        //System.out.println(in.readUTF());

        //Write to client using output stream
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        out.writeUTF("Welcome and Bye Client!");

        //close the connection
     
        

        String bababooey = in.readUTF();
        
        String baba = bababooey.replaceAll("\\s+", "");
        String ipTypeString = String.valueOf(baba.charAt(0)); //Get the IP Type
        String headerLengthString = String.valueOf(baba.charAt(1)); //Get the header length 
        int ipType = Integer.parseInt(ipTypeString);
        int headerLength = Integer.parseInt(headerLengthString);
  

        int length = ipType * headerLength;//Find the amount of bytes
        length = length*2;//Find the amount of characters for the payload

        baba = baba.substring(0,length);
        

        //System.out.println(baba);
        String checksum = "0000";
        int checkint = 0;
        int start =0;
        int end =0;
        int counter = 0;
        int joe;
        for(int i = 0; i < baba.length(); i++){
            counter++;
            if(counter%4==0){
                end=counter;
                String joemama = baba.substring(start,end);
                //System.out.println(joemama);
                joe = Integer.parseInt(joemama, 16);
                //System.out.println(joe);
                checkint = Integer.parseInt(checksum, 16);
                checkint= checkint + joe;
                checksum = Integer.toHexString(checkint);
                if(checksum.length() == 5){
                    //System.out.println(checksum);
                    checksum = checksum.substring(1);
                    checkint = Integer.parseInt(checksum, 16);
                    checkint++;
                    checksum = Integer.toHexString(checkint);
                    //System.out.println(checksum);
                }
                start = end;
                //System.out.println(checksum);
            }
        }
        checkint = Integer.parseInt(checksum, 16);
        checkint = 65535 - checkint;
        if(checkint == 0){
            
        String message = bababooey.replaceAll("\\s", ""); //Remove Spaces
        ipTypeString = String.valueOf(message.charAt(0)); //Get the IP Type
        headerLengthString = String.valueOf(message.charAt(1)); //Get the header length 
        ipType = Integer.parseInt(ipTypeString);
        headerLength = Integer.parseInt(headerLengthString);
  

        length = ipType * headerLength;//Find the amount of bytes
        length = length*2;//Find the amount of characters for the payload
        int ipPosition = length - 16; //Get the source IP position 
        String ipSourceHex = message.substring(ipPosition, ipPosition+8); //Get the hex string of the source IP 
    
        //Get the payload message.
        String payload = message.substring(length, message.length());

        //Convert the Source IP to intigeter then string 
        String ipSource = "";
        char[] charArray = ipSourceHex.toCharArray();

        for (int i = 0; i<charArray.length; i=i+2){
            String temp = ""+charArray[i] + "" + charArray[i+1];
            int tempch = Integer.parseInt(temp,16);
            String tempString = Integer.toString(tempch);

            if (i!= charArray.length-2){
                ipSource = ipSource + tempString + ".";
            }
            else {
                ipSource = ipSource + tempString;
            }
        }

        //Convert the hex payload back to string.
        String payloadMessage = "";
        charArray = payload.toCharArray();

        for (int i = 0; i<charArray.length; i=i+2){
            String temp = ""+charArray[i] + "" + charArray[i+1];
            char tempch = (char)Integer.parseInt(temp,16);
            payloadMessage = payloadMessage + tempch;
        }

      
     
            System.out.println("The data recived from "+ipSource+" is "+ payloadMessage);
            System.out.println("The data has "+payload.length()+" bits or "+payload.length()/2+" bytes. The total length of the packet is "+((payload.length()/2)+(length/2))+" bytes");
            System.out.println("The verification of the checksum demonstrates that the packet received is correct.");
        
    
        }else{
            System.out.println("The verification of the checksum demonstrates that the packet received is corrupted. Packet discarded!" );
        }
        
    
//Write to client using output stream
out = new DataOutputStream(server.getOutputStream());
out.writeUTF("Welcome and Bye Client!");

//close the connection
server.close();
    
}
}
        
    
            



        /*

        //Write to client using output stream
        out = new DataOutputStream(server.getOutputStream());
        out.writeUTF("Welcome and Bye Client!");

        //close the connection
        server.close();


        String messageRecived = "4500 0028 1c46 4000 4006 9D35 C0A8 0003 C0A8 0001 434f 4c4f 4d42 4941 2032 202d 204d 4553 5349 2030";
        String message = messageRecived.replaceAll("\\s", ""); //Remove Spaces
        String ipTypeString = String.valueOf(message.charAt(0)); //Get the IP Type
        String headerLengthString = String.valueOf(message.charAt(1)); //Get the header length 
        int ipType = Integer.parseInt(ipTypeString);
        int headerLength = Integer.parseInt(headerLengthString);
  

        int length = ipType * headerLength;//Find the amount of bytes
        length = length*2;//Find the amount of characters for the payload
        int ipPosition = length - 16; //Get the source IP position 
        String ipSourceHex = message.substring(ipPosition, ipPosition+8); //Get the hex string of the source IP 
    
        //Get the payload message.
        String payload = message.substring(length, message.length());

        //Convert the Source IP to intigeter then string 
        String ipSource = "";
        char[] charArray = ipSourceHex.toCharArray();

        for (int i = 0; i<charArray.length; i=i+2){
            String temp = ""+charArray[i] + "" + charArray[i+1];
            int tempch = Integer.parseInt(temp,16);
            String tempString = Integer.toString(tempch);

            if (i!= charArray.length-2){
                ipSource = ipSource + tempString + ".";
            }
            else {
                ipSource = ipSource + tempString;
            }
        }

        //Convert the hex payload back to string.
        String payloadMessage = "";
        charArray = payload.toCharArray();

        for (int i = 0; i<charArray.length; i=i+2){
            String temp = ""+charArray[i] + "" + charArray[i+1];
            char tempch = (char)Integer.parseInt(temp,16);
            payloadMessage = payloadMessage + tempch;
        }

        int checksum = 1;
        if (checksum == 0){
            System.out.println("The data recived from "+ipSource+" is "+ payloadMessage);
            System.out.println("The data has "+payload.length()+" bits or "+payload.length()/2+" bytes. The total length of the packet is "+((payload.length()/2)+(length/2))+" bytes");
            System.out.println("The verification of the checksum demonstrates that the packet received is correct.");
        }
        else {
            System.out.println("The verification of the checksum demonstrates that the packet received is corrupted. Packet discarded!" );
        }
        */

    

		