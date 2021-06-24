import java.net.*;
import java.io.*;

public class PacketSender {
    public static void main(String[] args) throws Exception{

       
        //try to connect to server - localhost @ port 8888
        Socket client = new Socket("localhost",8888);
        //if server is not listening - You will get Exception
        // java.net.ConnectException: Connection refused: connect

   
        //get user input message
        StringBuffer user_input = new StringBuffer();
        for(int n = 0; n < args.length; n++) { 
            if(n==(args.length-1)){
                user_input.append(args[n]) ;
            }else{
                user_input.append(args[n]+" ") ;
            }

        }
        String raw_input = user_input.toString();
        

        StringBuffer sb = new StringBuffer();
        //Converting received string to Hex
        char ch[] = raw_input.toCharArray();
        for(int i = 0; i < ch.length; i++) {
            String hex_input = Integer.toHexString(ch[i]);
            if(i%2==0){
                sb.append(hex_input);
            }else{
                sb.append(hex_input+" ");
            }

        }
        //Hex value of received message
        String result = sb.toString();
      
        //remove spaces
        String data = result.replaceAll("\\s", "");

        //Intalize the static positions
        String headerLength = "45";
        String tOS = "00";

        //Convert the IP header length to hex, add any padding to the value if it does not have 4 bits
        String ipHeaderLength = Integer.toHexString(20 + ((data.length())/4)*2);
        if (ipHeaderLength.length()%4 != 0){
            int padding = ipHeaderLength.length()%4;
            for (int i = 0; i < padding; i++){
                
                ipHeaderLength = "0"+ipHeaderLength;
            }
        }

        //Intalize the IPs given
        String ipSrs ="192.168.0.3";
        String ipDest ="192.168.0.1";
        String idField ="1c46"; 
        String flags ="4000";
        String ttl ="4006";
        String checkSum = "0000";

        //Split the IPs into an array by spliting it on the "."
        String[] ipSource = ipSrs.split("\\.");
        String[] ipDst = ipDest.split("\\.");

        String ipSourceHEX = "";
        String ipDstHEX = "";
       
        //Take each element and of the array and convert it to a hex, if the hex hve the proper length then pad with 0s
        for (int i = 0; i < ipSource.length; i++){
      
            String temp = Integer.toHexString(Integer.parseInt(ipSource[i])) ;
           
            
            if (temp.length()%2 != 0){
                int padding = temp.length()%4;
                for (int x = 0; x < padding; x++){
                    
                    temp = "0"+temp;
                }
            }

            if (i == 2){
                ipSourceHEX = ipSourceHEX + " ";
            }
            ipSourceHEX = ipSourceHEX + temp;
        }

        //Take each element and of the array and convert it to a hex, if the hex hve the proper length then pad with 0s
        for (int i = 0; i < ipDst.length; i++){
      
            String temp = Integer.toHexString(Integer.parseInt(ipDst[i])) ;
           
            
            if (temp.length()%2 != 0){
                int padding = temp.length()%4;
                for (int x = 0; x < padding; x++){
                    
                    temp = "0"+temp;
                }
            }
            if (i == 2){
                ipDstHEX = ipDstHEX + " ";
            }
            ipDstHEX = ipDstHEX + temp;
        }

      //ipheader without checksum
      String ipHeader = headerLength + tOS + " " + ipHeaderLength + " " + idField + " " + flags + " " + ttl + " " + checkSum + " " + ipSourceHEX + " " + ipDstHEX;
      //ipheader without spaces
      String header = ipHeader.replaceAll("\\s+", "");
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
      for(int i = 0; i < header.length(); i++){
          counter++;
          if(counter%4==0){
              end=counter;
               //next ip field to be processed as Hex
              String nextField = header.substring(start,end);
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
          }
      }

      checkint = Integer.parseInt(checksum, 16);
        //one's complement
      checkint = 65535 - checkint;
      checksum = Integer.toHexString(checkint);
      //ip header with checksum
      String finalIPHeader = headerLength + tOS + " " + ipHeaderLength + " " + idField + " " + flags + " " + ttl + " " + checksum + " " + ipSourceHEX + " " + ipDstHEX;
      //ip header with message 
      String finalmessage = finalIPHeader + " " + result;
      
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeUTF(finalmessage);

        //read from the server
        DataInputStream in = new DataInputStream(client.getInputStream());
        System.out.println("Data received from the server is -> " + in.readUTF());

        //close the connection
        client.close();
  }
}