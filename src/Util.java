import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@SuppressWarnings("unused")
public class Util {
	public static String separator = System.getProperty("line.separator");
	public static String fileEncoding = "UTF-8";

	public static Document stringToDocument(String xmlString) {
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(xmlString
					.getBytes(fileEncoding)));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			doc = null;
		}
		return doc;
	}

	public static String getFileData(java.io.File finput) {
		String line = "";
		String retStr = "";
		try {
			FileInputStream fis = new FileInputStream(finput);
			ReadableByteChannel source = Channels.newChannel(fis);
			Reader channelReader = Channels.newReader(source, fileEncoding);
			BufferedReader reader = new BufferedReader(channelReader);
			while ((line = reader.readLine()) != null) {
				retStr += line + "\n";
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			retStr = "";
		}
		return retStr;
	}

	public static void createFile(String filePath, String contentData) {
		java.io.BufferedWriter output = null;
		String a = "";
		try {
			if(filePath != null){
				java.io.File targetFile = new java.io.File(filePath);
				targetFile.getParentFile().mkdirs();
				if (targetFile.isFile()) {
					targetFile.delete();
				} else {
					targetFile.createNewFile();
				}
				output = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
						new java.io.FileOutputStream(targetFile.getPath()),
						fileEncoding));
				output.write(contentData);
			}
		} catch (java.io.FileNotFoundException e1) {
			e1.printStackTrace();
			System.out.println(e1.toString());
		} catch (java.lang.Exception e2) {
			e2.printStackTrace();
			System.out.println(e2.toString());
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					//e.printStackTrace();
					a = "";
				}
		}
	}

	public static String doccumentToString(Document doc) {
		try {
			java.io.StringWriter sw = new java.io.StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, fileEncoding);

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (java.lang.Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}

	public static Document changeNodeValue(Document doc, String xPathCondition,
			String changeValue) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr = null;
		String a = "";
		try {
			expr = xPath.compile(xPathCondition);
			java.lang.Object result = expr
					.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				nodes.item(i).setNodeValue(changeValue);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			a = "";
		}
		return doc;
	}

	public static Document deleteNodeValue(Document doc, String xPathCondition) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr = null;
		String a = "";
		try {
			expr = xPath.compile(xPathCondition);
			java.lang.Object result = expr
					.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				nodes.item(i).getParentNode().removeChild(nodes.item(i));
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			a = "";
		}
		// doc.normalize();
		return doc;
	}

	public static String xmlNodeFirst(Document doc, String xPathCondition) {
		String retStr = "";
		try{
			Vector<String> retV = xmlNode(doc, xPathCondition);
			retStr = retV.firstElement().toString();
			//retStr = retV.toString();
			retStr = retStr.trim();
		}catch(java.lang.Exception e){
			e.printStackTrace();
			println("[xPathCondition]-"+xPathCondition);
			retStr = "";
		}
		return retStr;
	}
	
	public static String xmlNodeData(Document doc, String xPathCondition) {
		String retStr = "";
		try{
			Vector<String> retV = xmlNode(doc, xPathCondition);
			//retStr = retV.firstElement().toString();
			retStr = retV.toString();
			retStr = retStr.trim();
		}catch(java.lang.Exception e){
			e.printStackTrace();
			println("[xPathCondition]-"+xPathCondition);
			retStr = "";
		}
		return retStr;
	}

	/*
	public static Vector<String> xmlNode(Document doc, String xPathCondition) {
		Vector<String> retV = new Vector<String>();
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr = null;
		try {
			expr = xPath.compile(xPathCondition);
			java.lang.Object result = expr
					.evaluate(doc, XPathConstants.NODESET);
			NodeList node = (NodeList) result;
			for (int i = 0; i < node.getLength(); i++) {
				retV.add(node.item(i).getNodeValue().trim());
			}
			if (retV.size() == 0) {
				retV.add("");
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return retV;
	}
*/
	public static Vector<String> xmlNode(Document doc, String xPathCondition) {
		return xmlNode(doc, xPathCondition, "utf-8");
	}
	
	public static Vector<String> xmlNode(Document doc, String xPathCondition, String charSet){
		Vector<String> retV = new Vector<String>();
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr = null;
		try {
			if(xPathCondition.startsWith("string(") || xPathCondition.startsWith("count(")){
				String str = xPath.evaluate(xPathCondition, doc, XPathConstants.STRING).toString();
				str = new String(str.getBytes(charSet));
				retV.add(str);
			}else{
				expr = xPath.compile(xPathCondition);
				java.lang.Object result = expr.evaluate(doc, XPathConstants.NODESET);
				NodeList node = (NodeList)result;
				for(int i=0;i<node.getLength();i++){
					String b = node.item(i).getNodeValue();
					try {
						b = new String(b.getBytes(charSet));
					} catch (java.io.UnsupportedEncodingException e) {
						e.printStackTrace();
						b = "";
					}
					retV.add(b);
				}
				if(retV.size() == 0){
					retV.add("");
				}
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			retV = new Vector<String>();
		}
		return retV;
	}
	
	public static String replace(String mainString, String oldString,
			String newString) {
		if (mainString == null) {
			return null;
		}
		if (oldString == null || oldString.length() == 0) {
			return mainString;
		}
		if (newString == null) {
			newString = "";
		}
		int i = mainString.lastIndexOf(oldString);
		if (i < 0)
			return mainString;
		StringBuffer mainSb = new StringBuffer(mainString);
		while (i >= 0) {
			mainSb.replace(i, (i + oldString.length()), newString);
			i = mainString.lastIndexOf(oldString, i - 1);
		}
		return mainSb.toString();
	}

	public static String execute(String command) {
		StringBuffer output = new StringBuffer();
		Process process = null;
		BufferedReader bufferReader = null;
		Runtime runtime = Runtime.getRuntime();
		String osName = System.getProperty("os.name");

		if (osName.indexOf("Windows") > -1) {
			command = "cmd /c " + command;
		}

		try {
			process = runtime.exec(command);
			if (osName.indexOf("Windows") > -1) {
				bufferReader = new BufferedReader(
						new java.io.InputStreamReader(process.getInputStream()));
			} else {
				bufferReader = new BufferedReader(
						new java.io.InputStreamReader(process.getInputStream(),
								"KSC5601"));
			}
			String msg = null;
			while ((msg = bufferReader.readLine()) != null) {
				output.append(msg + separator);
			}
			bufferReader.close();

			if (osName.indexOf("Windows") > -1) {
				bufferReader = new BufferedReader(
						new java.io.InputStreamReader(process.getErrorStream()));
			} else {
				bufferReader = new BufferedReader(
						new java.io.InputStreamReader(process.getErrorStream(),
								"KSC5601"));
			}
			while ((msg = bufferReader.readLine()) != null) {
				output.append(msg + separator);
			}
		} catch (IOException e) {
			output.append("IOException : " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(process != null){
					process.destroy();
				}
				if (bufferReader != null){
					bufferReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return output.toString();
	}

	public static String getTime() {
		long time = System.currentTimeMillis();
		java.text.SimpleDateFormat dayTime = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String str = dayTime.format(new java.util.Date(time));
		dayTime = null;
		return "[" + str + "] ";
	}

	public static String gatHtmlv2(String receiverURL, String enc, int timeout){
		StringBuffer receiveMsg = new StringBuffer();
		
		java.io.BufferedReader in = null;
        try {// file:///e:/bongtest/test.xml
            URL url=new URL(receiverURL);
//          in=url.openStream();
            String currLine = new String();
            URLConnection conn=url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),enc));
            
            while ((currLine = in.readLine()) != null) {
				receiveMsg.append(currLine).append("\r\n");
			}
        }catch(java.net.SocketTimeoutException e1){
			System.out.println("");
			println("SocketTimeoutException : "+receiverURL+" / timeout : "+timeout);
			receiveMsg = new StringBuffer();
			receiveMsg.append("TIMEOUT");
        }catch (Exception e) {
        	e.printStackTrace();
        }
        return receiveMsg.toString().trim();
	}
	
	public static String getHtmls(String receiverURL, String enc, int timeout) {
		String retStr = "";
		StringBuffer receiveMsg = new StringBuffer();
		
		System.out.println("############################"+receiverURL);
		
		if(receiverURL.startsWith("file:")){
			retStr = gatHtmlv2(receiverURL,enc,timeout);
		}else{
			try {
				int errorCode = 0;
				// -- receive servlet connect
				java.net.URL servletUrl = new java.net.URL(receiverURL);
				java.net.HttpURLConnection uc = (java.net.HttpURLConnection) servletUrl
						.openConnection();
				uc.setReadTimeout(timeout);
				uc.setRequestMethod("GET");
				//uc.setFixedLengthStreamingMode(1024000);
				uc.setDoOutput(true);
				uc.setDoInput(true);
				uc.setUseCaches(false);
				uc.connect();
				// init
				errorCode = 0;
				// -- Network error check
				if (uc.getResponseCode() == java.net.HttpURLConnection.HTTP_OK) {
					String currLine = new String();
					// UTF-8
					java.io.BufferedReader in = null;
					if (!"".equals(enc)) {
						in = new java.io.BufferedReader(
								new java.io.InputStreamReader(uc.getInputStream(),
										enc));
					} else {
						in = new java.io.BufferedReader(
								new java.io.InputStreamReader(uc.getInputStream()));
					}
					while ((currLine = in.readLine()) != null) {
						receiveMsg.append(currLine).append("\r\n");
					}
				} else {
					errorCode = uc.getResponseCode();
					return receiveMsg.toString();
				}
				uc.disconnect();
			} catch(java.net.SocketTimeoutException e1){
				System.out.println("");
				println("SocketTimeoutException : "+receiverURL+" / timeout : "+timeout);
				receiveMsg = new StringBuffer();
				receiveMsg.append("TIMEOUT");
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
				receiveMsg = new StringBuffer();
				receiveMsg.append("TIMEOUT");
			}	
			
			retStr = receiveMsg.toString().trim();
		}
		
		if(retStr.indexOf("encoding=\"EUC-KR\"") != -1 || retStr.indexOf("encoding=\"euc-kr\"") != -1){
			retStr = Util.replace(retStr, "<?xml version=\"1.0\" encoding=\"EUC-KR\"?>", "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			retStr = Util.replace(retStr, "<?xml version=\"1.0\" encoding=\"euc-kr\"?>", "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		}
		
		return retStr;
	}

	public static java.sql.Connection getConnection(Document configDocument) {
		java.sql.Connection conn = null;
		String driver = Util.xmlNodeFirst(configDocument,
				"/Config/Database/ClassName/text()");
		String url = Util.xmlNodeFirst(configDocument,
				"/Config/Database/Url/text()");
		try {
			Class.forName(driver);
			String id = Util.xmlNodeFirst(configDocument,
					"/Config/Database/User/text()");
			String pw = Util.xmlNodeFirst(configDocument,
					"/Config/Database/Password/text()");
			conn = java.sql.DriverManager.getConnection(url, id, pw);
			conn.setAutoCommit(false);
		} catch (java.lang.ClassNotFoundException e) {
			e.printStackTrace();
			conn = (Connection) new Object();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			conn = (Connection) new Object();
		}
		return conn;
	}

	public static void closeConnection(java.sql.Connection conn) {
		if (conn != null) {
			try {
				conn.commit();
				conn.close();
			} catch (java.lang.Exception e) {
				e.printStackTrace();
				conn = (Connection) new Object();
			}
		};
	}

	public static String selectParamData(java.sql.Connection conn, String sql){

        java.sql.ResultSet rs = null;
        java.sql.Statement stmt = null;
        String query = sql;

		//System.out.println(query);
		String selectData = "";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
            	selectData = selectData + rs.getString(1) + ";";
            }
        } catch (java.sql.SQLException se) {
            se.printStackTrace();
            selectData = "";
        } finally {
            if(rs != null) {try{ rs.close(); }catch(java.lang.Exception e){selectData = "";}};
            if(stmt != null) {try{ stmt.close(); }catch(java.lang.Exception e){selectData = "";}};
        }
        /*
        if(selectData.indexOf(";") != -1){
        	selectData = selectData.substring(0,selectData.length()-1);
        }
        */
		return selectData;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int insertDataTable(java.sql.Connection conn, String tableName, java.util.HashMap<String,String> rowData){
		int retInt = 0;
		String sql = "insert into <TABLENAME>(<FIELDNAMES>) values (<DATAS>)";
		sql = replace(sql, "<TABLENAME>", tableName);
		java.util.Set keyset = rowData.keySet();
		java.util.Iterator it = keyset.iterator();
		String key = "";
		String value = "";
		String fieldNames = "";
		String datas = "";
		java.util.Vector valuesVec = new java.util.Vector();
		while(it.hasNext()){
			key = (String)it.next();
			fieldNames = fieldNames + key + ",";
			datas = datas + "?" + ",";
			valuesVec.add(rowData.get(key));
		}
/*
		fieldNames = fieldNames + "WISE_ROW_STATUS" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("I");

		fieldNames = fieldNames + "WISE_ROW_CHANGED" + ",";
		datas = datas + "?" + ",";
		valuesVec.add(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		
		fieldNames = fieldNames + "WISE_ROW_CHANGER" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("MHDBMatcher");

		fieldNames = fieldNames + "WISE_ROW_CRAWYN" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("N");
		
		fieldNames = fieldNames + "WISE_ROW_CHANGE_HOLD_YN" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("N");

		fieldNames = fieldNames + "WISE_ROW_MATCH_TARGET_YN" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("N");
*/		
		if(fieldNames.indexOf(",") != -1){
			fieldNames = fieldNames.trim();
			fieldNames = fieldNames.substring(0,fieldNames.length()-1);
		}
		if(datas.indexOf(",") != -1){
			datas = datas.trim();
			datas = datas.substring(0,datas.length()-1);
		}
		sql = replace(sql, "<FIELDNAMES>", fieldNames);
		sql = replace(sql, "<DATAS>", datas);
		System.out.println("insert SQL !!!! = "+sql);
		
		java.sql.ResultSet rs = null;
        java.sql.PreparedStatement ptmt = null;
        
        try {
			ptmt = conn.prepareStatement(sql);
			for(int i=0;i<valuesVec.size();i++){
				ptmt.setString((i+1),valuesVec.get(i)+"");
			}
			retInt = ptmt.executeUpdate();
        } catch (java.sql.SQLTimeoutException se1) {
            se1.printStackTrace();
            retInt = 0;
        } catch (java.sql.SQLException se) {
        	if(se.toString().indexOf("Duplicate entry") != -1 && se.toString().indexOf("PRIMARY") != -1){
                //retInt = deleteInsertDataTable(conn, tableName, rowData);
                retInt = 1;
        	}
        } catch (java.lang.Exception se2) {
            se2.printStackTrace();
            retInt = 0;
        } finally {
            if(rs != null) {try{ rs.close(); }catch(java.lang.Exception e){retInt = 0;}};
            if(ptmt != null) {try{ ptmt.close(); }catch(java.lang.Exception e){retInt = 0;}};
            if(valuesVec != null) valuesVec.clear();
            if(rowData != null) rowData.clear();
        }
        
        return retInt;
	}
	
	public static void deleteDataTable(java.sql.Connection conn, String tableName){
		String sql = "delete from <TABLENAME>";
		//String sql = " DELETE FROM <TABLENAME> WHERE 1=1 AND WISE_ROW_CHANGE_HOLD_YN = 'N'; ";
		sql = replace(sql, "<TABLENAME>", tableName);
		java.sql.ResultSet rs = null;
        java.sql.PreparedStatement ptmt = null;
        String a = "";
        try {
			ptmt = conn.prepareStatement(sql);
			System.out.println("************ sql:"+sql);
            ptmt.execute();
            conn.commit();
        } catch (java.lang.Exception se) {
            se.printStackTrace();
            a = "";
        } finally {
            if(rs != null) {try{ rs.close(); }catch(java.lang.Exception e){a = "";}};
            if(ptmt != null) {try{ ptmt.close(); }catch(java.lang.Exception e){a = "";}};
        }
	}	

	public static int selectHoldCheckDataRow(java.sql.Connection conn, String tableName, String nid){
		String sql = " SELECT count(*) as cnt FROM <TABLENAME> WHERE 1=1 AND WISE_ROW_CHANGE_HOLD_YN = 'Y' AND NID = ? ; ";
		sql = replace(sql, "<TABLENAME>", tableName);
		java.sql.ResultSet rs = null;
        java.sql.PreparedStatement ptmt = null;
        String selectData = "0";
        try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1,nid);
            rs = ptmt.executeQuery();
            while(rs.next()){
            	selectData = rs.getString(1);
            }
            conn.commit();
        } catch (java.lang.Exception se) {
            se.printStackTrace();
            selectData = "0";
        } finally {
            if(rs != null) {try{ rs.close(); }catch(java.lang.Exception e){selectData = "0";}};
            if(ptmt != null) {try{ ptmt.close(); }catch(java.lang.Exception e){selectData = "0";}};
        }
        return Integer.parseInt(selectData);
	}	

	public static void deleteHoldCheckDataRow(java.sql.Connection conn, String tableName, String nid){
		String sql = " DELETE FROM <TABLENAME> WHERE 1=1 AND WISE_ROW_CHANGE_HOLD_YN = 'N' AND NID = ? ; ";
		sql = replace(sql, "<TABLENAME>", tableName);
		java.sql.ResultSet rs = null;
        java.sql.PreparedStatement ptmt = null;
        String a = "";
        try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1,nid);
            ptmt.execute();
            conn.commit();
        } catch (java.lang.Exception se) {
            se.printStackTrace();
            a = "";
        } finally {
            if(rs != null) {try{ rs.close(); }catch(java.lang.Exception e){a = "";}};
            if(ptmt != null) {try{ ptmt.close(); }catch(java.lang.Exception e){a = "";}};
        }
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int deleteInsertDataTable(java.sql.Connection conn, String tableName, java.util.HashMap<String,String> rowData){
		int retInt = 0;
		String sql = "insert into <TABLENAME>(<FIELDNAMES>) values (<DATAS>);";
		sql = replace(sql, "<TABLENAME>", tableName);
		java.util.Set keyset = rowData.keySet();
		java.util.Iterator it = keyset.iterator();
		String key = "";
		String value = "";
		String fieldNames = "";
		String datas = "";
		java.util.Vector valuesVec = new java.util.Vector();
		
		deleteHoldCheckDataRow(conn, tableName, rowData.get("NID"));
		
		while(it.hasNext()){
			key = (String)it.next();
			fieldNames = fieldNames + key + ",";
			datas = datas + "?" + ",";
			valuesVec.add(rowData.get(key));
		}
		
		fieldNames = fieldNames + "WISE_ROW_STATUS" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("U");

		fieldNames = fieldNames + "WISE_ROW_CHANGED" + ",";
		datas = datas + "?" + ",";
		valuesVec.add(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		
		fieldNames = fieldNames + "WISE_ROW_CHANGER" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("MHDBMatcher");
		
		fieldNames = fieldNames + "WISE_ROW_CRAWYN" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("N");

		fieldNames = fieldNames + "WISE_ROW_CHANGE_HOLD_YN" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("N");
		
		fieldNames = fieldNames + "WISE_ROW_MATCH_TARGET_YN" + ",";
		datas = datas + "?" + ",";
		valuesVec.add("N");

		if(fieldNames.indexOf(",") != -1){
			fieldNames = fieldNames.trim();
			fieldNames = fieldNames.substring(0,fieldNames.length()-1);
		}
		if(datas.indexOf(",") != -1){
			datas = datas.trim();
			datas = datas.substring(0,datas.length()-1);
		}
		sql = replace(sql, "<FIELDNAMES>", fieldNames);
		sql = replace(sql, "<DATAS>", datas);
		//System.out.println(sql);
		

		if(selectHoldCheckDataRow(conn, tableName, rowData.get("NID")) == 0){
			java.sql.ResultSet rs = null;
	        java.sql.PreparedStatement ptmt = null;
	        
	        try {
				ptmt = conn.prepareStatement(sql);
				for(int i=0;i<valuesVec.size();i++){
					ptmt.setString((i+1),valuesVec.get(i)+"");
				}
				retInt = ptmt.executeUpdate();
	        } catch (java.lang.Exception se) {
	            se.printStackTrace();
	            retInt = 0;
	        } finally {
	            if(rs != null) {try{ rs.close(); }catch(java.lang.Exception e){retInt = 0;}};
	            if(ptmt != null) {try{ ptmt.close(); }catch(java.lang.Exception e){retInt = 0;}};
	            if(valuesVec != null) valuesVec.clear();
	            if(rowData != null) rowData.clear();
	        }
		}

		return retInt;
	}	
	
	public static void print(String out){
		out = getTime()+out;
		try{
			if ((System.getProperty("os.name")).indexOf("Windows") > -1) {
				System.out.print(new String(out.getBytes("utf-8"),"8859_1"));
			}else{
				System.out.print(out);
			}
		}catch(java.lang.Exception e){
			e.printStackTrace();
			out = "";
		}
	}

	public static void println(String out){
		out = getTime()+out;
		try{
			if ((System.getProperty("os.name")).indexOf("Windows") > -1) {
				System.out.println(new String(out.getBytes("utf-8"),"8859_1"));
			}else{
				System.out.println(out);
			}
		}catch(java.lang.Exception e){
			e.printStackTrace();
			out = "";
		}
	}
}
