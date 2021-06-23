import java.net.*;
import java.io.*;

public class PacketSender {
    public static void main(String[] args) throws Exception{

       
        //try to connect to server - localhost @ port 8888
        Socket client = new Socket("localhost",8888);
        //if server is not listening - You will get Exception
        // java.net.ConnectException: Connection refused: connect

   
        //get user input 
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
        //Converting string to character array
        char ch[] = raw_input.toCharArray();
        for(int i = 0; i < ch.length; i++) {
            String hex_input = Integer.toHexString(ch[i]);
            if(i%2==0){
                sb.append(hex_input);
            }else{
                sb.append(hex_input+" ");
            }

        }
        String result = sb.toString();
      

        String data = result.replaceAll("\\s", "");
        String headerLength = "45";
        String tOS = "00";
        String ipHeaderLength = Integer.toHexString(20 + ((data.length())/4)*2);//fix
        if (ipHeaderLength.length()%4 != 0){
            int padding = ipHeaderLength.length()%4;
            for (int i = 0; i < padding; i++){
                
                ipHeaderLength = "0"+ipHeaderLength;
            }
        }
        String ipSrs ="192.168.0.3";
        String ipDest ="192.168.0.1";
        String idField ="1c46"; //Whats this
        String flags ="4000";
        String ttl ="4006";
        String checkSum = "0000";
        String[] ipSource = ipSrs.split("\\.");
        String[] ipDst = ipDest.split("\\.");

        String ipSourceHEX = "";
        String ipDstHEX = "";
       
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

      //sample input
      String bababooey = headerLength + tOS + " " + ipHeaderLength + " " + idField + " " + flags + " " + ttl + " " + checkSum + " " + ipSourceHEX + " " + ipDstHEX;
      String baba = bababooey.replaceAll("\\s+", "");
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
      checksum = Integer.toHexString(checkint);
      System.out.println(checksum);
      String finalIPHeader = headerLength + tOS + " " + ipHeaderLength + " " + idField + " " + flags + " " + ttl + " " + checksum + " " + ipSourceHEX + " " + ipDstHEX;
      System.out.println(finalIPHeader);
      String finalmessage = finalIPHeader + " " + result;
      //try to connect to server - localhost @ port 8888
      //Socket client = new Socket("localhost",8888);
      //if server is not listening - You will get Exception
      // java.net.ConnectException: Connection refused: connect

      //write to server using output stream
      //DataOutputStream out = new DataOutputStream(client.getOutputStream());
      //out.writeUTF("Hello server - How are you sent by Client");

      //read from the server
      //DataInputStream in = new DataInputStream(client.getInputStream());
      //System.out.println("Data received from the server is -> " + in.readUTF());

      //close the connection
      //client.close();
      //write to server using output stream
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeUTF(finalmessage);

        //read from the server
        DataInputStream in = new DataInputStream(client.getInputStream());
        System.out.println("Data received from the server is -> " + in.readUTF());

        //close the connection
        client.close();
  }
}