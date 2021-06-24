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
     
        
        //message received from client
        String receivedMsg = in.readUTF();
        
        //remove spaces
        String msg = receivedMsg.replaceAll("\\s+", "");
        String ipTypeString = String.valueOf(msg.charAt(0)); //Get the IP Type
        String headerLengthString = String.valueOf(msg.charAt(1)); //Get the header length 
        int ipType = Integer.parseInt(ipTypeString);
        int headerLength = Integer.parseInt(headerLengthString);
  

        int length = ipType * headerLength;//Find the amount of bytes
        length = length*2;//Find the amount of characters for the payload

        msg = msg.substring(0,length);
        

        //initialize checksum to 0000
        String checksum = "0000";
        //int value of checksum
        int checkint = 0;
        //start position of substring
        int start =0;
        //end position of substring
        int end =0;
        int counter = 0;
        //next ip field to be processed as int
        int next;
        for(int i = 0; i < msg.length(); i++){
            counter++;
            if(counter%4==0){
                end=counter;
                //next ip field to be processed as Hex
                String nextField = msg.substring(start,end);
                next = Integer.parseInt(nextField, 16);
                checkint = Integer.parseInt(checksum, 16);
                checkint= checkint + next;
                checksum = Integer.toHexString(checkint);

                //if overflow, remove the most significant bit and add a 1 to get the wrapped sum
                if(checksum.length() == 5){
                    
                    checksum = checksum.substring(1);
                    checkint = Integer.parseInt(checksum, 16);
                    checkint++;
                    checksum = Integer.toHexString(checkint);
                   
                }
                start = end;
                //System.out.println(checksum);
            }
        }
        checkint = Integer.parseInt(checksum, 16);
        //one's complement
        checkint = 65535 - checkint;

        
        if(checkint == 0){
            
        String message = receivedMsg.replaceAll("\\s", ""); //Remove Spaces
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

      
            //Print out the messages to the user if the checksum is 0
            System.out.println("The data recived from "+ipSource+" is "+ payloadMessage);
            System.out.println("The data has "+payload.length()+" bits or "+payload.length()/2+" bytes. The total length of the packet is "+((payload.length()/2)+(length/2))+" bytes");
            System.out.println("The verification of the checksum demonstrates that the packet received is correct.");
        
    
        }else{

            //If there was an error then print that to the user
            System.out.println("The verification of the checksum demonstrates that the packet received is corrupted. Packet discarded!" );
        }
        
    
        //Write to client using output stream
        out = new DataOutputStream(server.getOutputStream());
        out.writeUTF("Welcome and Bye Client!");

        //close the connection
        server.close();
            
    }
}
        
    
            



        
    

		