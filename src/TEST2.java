import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;


public class TEST2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//InputStream in=null;
		
		StringBuffer receiveMsg = new StringBuffer();
		
		java.io.BufferedReader in = null;
        try {// file:///e:/bongtest/test.xml
            URL url=new URL("file:///E:/bongtest/test.xml");
//          in=url.openStream();
            String currLine = new String();
            URLConnection conn=url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.connect();
            in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"euc-kr"));
            
            while ((currLine = in.readLine()) != null) {
				receiveMsg.append(currLine).append("\r\n");
			}
        }
        catch (SocketTimeoutException e){
        	e.printStackTrace();
            System.out.println("timed out");
        }
        catch (MalformedURLException e) {
        	e.printStackTrace();
            System.out.println("URL not valid");
        }

        catch (IOException e) {
        	e.printStackTrace();
            System.out.println("unable to get data");
        }
        
        System.out.println(receiveMsg.toString().trim());
	}

}
