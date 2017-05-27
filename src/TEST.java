
public class TEST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String url = "file:/E:/bongtest/test.xml";
		String urlEncoding = "utf-8";
		String urlTimeOut = "5000";
		
		java.io.File myFile=new java.io.File("E:\\bongtest\\test.xml");
		try{
			java.net.URL myUrl = myFile.toURI().toURL();
			System.out.println(myUrl);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));	
		System.out.println(apiResultXMLStr);
	}

}
