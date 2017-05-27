import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Enumeration;

import org.w3c.dom.Document;


public class MHDBMatcher {
	private static String fileEncoding = "UTF-8";
	private static String targetRunId = "ALL";
	private static String targetRunMode = "";
	private static String configFilePath = "";
	private static String configXmlStr = "";
	private static String separator = System.getProperty("line.separator");
	
	/**
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public static void main(String[] args) {
		String msg = separator;
		msg += "*** MetaHub DB Matcher v0.1 ***"+separator;
		System.out.println(msg);

		if(args.length < 2){
			msg = "";
			msg += "-----------------------------------------------------------------------"+separator;
			msg += "Please Set args !!!"+separator;
			msg += "args[0] : Target ID (Important Value: ALL, Target id , delimiter: [,] )"+separator;
			msg += "args[1] : Target Run Mode (Important Value: static, dynamic, match)"+separator;
			msg += "args[2] : File Full Path Of config XML (Option Value, Default: config.xml)"+separator;
			msg += "args[3] : File Encoding (Option Value, Default: UTF-8)"+separator;
			msg += "-----------------------------------------------------------------------"+separator;
			System.out.println(msg);
			//System.exit(1);
		}

		try{
			targetRunId = args[0];
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			//targetRunId = "ALL";
			//targetRunId = "mh_group,mh_person,mh_people_mapping,mh_pgm_trans_image";
			//targetRunId = "mh_pgm_master_image,mh_weekly_schedule,mh_weekly_schedule_rate,mh_star_prizewinner,mh_star_corp";
			targetRunId = "mh_pgm_trans_image";
		}
		
		try{
			targetRunMode = args[1];
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			//targetRunMode = "static";
			//targetRunMode = "dynamic";
			targetRunMode = "match";
		}

		try{
			configFilePath = args[2];
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			configFilePath = MHDBMatcher.class.getResource("").getPath()+"config.xml";
		}
		
		try{
			fileEncoding = args[3];
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			fileEncoding = "UTF-8";
		}

		msg = "";
		msg += "-----------------------------------------------------------------------"+separator;
		msg += "Setting args !!!"+separator;
		msg += "args[0] : Target ID ("+targetRunId+")"+separator;
		msg += "args[1] : Target Run Mode ("+targetRunMode+")"+separator;
		msg += "args[2] : File Full Path Of config XML ("+configFilePath+")"+separator;
		msg += "args[3] : File Encoding ("+fileEncoding+")"+separator;
		msg += "-----------------------------------------------------------------------"+separator;
		System.out.println(msg);
		
		configXmlStr = Util.getFileData(new File(configFilePath));
		
		String targetRunModeStr = "";
		if("static".equals(targetRunMode)){
			targetRunModeStr = "Static";
		}else if("dynamic".equals(targetRunMode)){
			targetRunModeStr = "Dynamic";
		}else if("match".equals(targetRunMode)){
			targetRunModeStr = "Match";
		}
		
		//System.out.println(configXmlStr);
		Document configDocument = Util.stringToDocument(configXmlStr);
		java.util.Vector configVectorId = Util.xmlNode(configDocument,"/Config/Target/@id");
    	java.util.Vector configVectorKor = Util.xmlNode(configDocument,"/Config/Target/@kor");
    	java.util.Vector configVectorURL = Util.xmlNode(configDocument,"/Config/Target/"+targetRunModeStr+"/URL/text()");
    	
    	String urlConnectionRetryWait = Util.xmlNodeFirst(configDocument,"/Config/URLConnectionRetry/@wait");
    	String urlConnectionRetryCount = Util.xmlNodeFirst(configDocument,"/Config/URLConnectionRetry/@retryCount");
    	
    	//System.out.println(configVectorId);
    	//System.out.println(configVectorKor);
    	//System.out.println(configVectorURL);
    	
    	//if(configVectorId.size() == configVectorKor.size() && configVectorKor.size() == configVectorURL.size()){
       	if(configVectorId.size() == configVectorKor.size()){
    		String apiResultXMLStr = "";
    		String baseUrl = "";
    		String urlText = "";
    		String url = "";
    		String urlEncoding = "";
    		String urlTimeOut = "";
    		Document apiResultDocument = null;
    		String nodeMore = "";
    		String nodeCount = "";
    		java.util.Vector nidVec = null;
    		Enumeration er = null;
    		Enumeration erField = null;
    		String nid = "";
    		String lastNid = "";
    		long cnt = 0;
    		String targetId = "";
    		String targetIdKor = "";
    		java.util.Vector mappingFieldVec = null;
    		java.util.Vector mappingFieldXPathVec = null;
    		java.util.Vector urlParamNameVec = null;
    		java.util.Vector urlParamValueModeVec = null;
    		java.util.Vector urlParamAppendModeVec = null;
    		java.util.Vector urlParamTextVec = null;
    		String urlParamName = "";
    		String urlParamValueMode = "";
    		String urlParamText = "";
    		String dbFieldName = "";
    		String dbFieldXPath = "";
    		String dbFieldValue = "";
    		String insertTableName = "";
    		java.util.HashMap<String,String> insertRowMap = null;
    		String insertCommitCount = "";
    		String targetType = "";
    		String urlStrTemp = "";
    		String urlPramTemp = "";
    		String urlTempStrFrom = "";
    		String urlTempStrTo = "";
    		java.util.HashMap<String, java.util.Vector<String>> dynamicURLParam = null;
    		for(int i=0;i<configVectorId.size();i++){
    			
        		apiResultXMLStr = "";
        		baseUrl = "";
        		urlText = "";
        		url = "";
        		urlEncoding = "";
        		urlTimeOut = "";
        		apiResultDocument = null;
        		nodeMore = "";
        		nodeCount = "";
        		nidVec = null;
        		er = null;
        		erField = null;
        		nid = "";
        		lastNid = "";
        		cnt = 0;
        		targetId = "";
        		targetIdKor = "";
        		mappingFieldVec = null;
        		mappingFieldXPathVec = null;
        		dbFieldName = "";
        		dbFieldXPath = "";
        		dbFieldValue = "";
        		insertTableName = "";
        		insertRowMap = null;
        		insertCommitCount = "";
        		targetType = "";
        		urlStrTemp = "";
        		urlPramTemp = "";
        		urlTempStrFrom = "";
        		urlTempStrTo = "";
        		dynamicURLParam = new java.util.HashMap<String, java.util.Vector<String>>();
    			
    			targetId = (String)configVectorId.get(i);
    			targetIdKor = (String)configVectorKor.get(i);
    			
    			String[] targetRunIds = targetRunId.split("[,]");
    			for(int a=0;a<targetRunIds.length;a++){
    				dynamicURLParam = new java.util.HashMap<String, java.util.Vector<String>>();
    				if(targetRunIds[a] != null) targetRunIds[a] = targetRunIds[a].trim();
    				if(targetRunIds[a].equals("") || targetRunIds[a] == null) continue;
    				if(targetRunId.equals("ALL") || targetRunIds[a].equals(targetId)){

    	    			insertTableName = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/@insertTableName");
    	    			insertCommitCount = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/@insertCommitCount");
    	    			targetType = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/@type");
    	    			if("".equals(targetType)){
    	    				targetType = "A";
    	    			}
    	    			urlText = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URL/text()");
    	    			/* URL param */
    	        		urlParamNameVec = Util.xmlNode(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URLParam/param/@name");
    	        		urlParamValueModeVec = Util.xmlNode(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URLParam/param/@valueMode");
    	        		urlParamTextVec = Util.xmlNode(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URLParam/param/text()");
    	    			if(urlParamNameVec.size() == urlParamValueModeVec.size()){
    	    				if(urlText.indexOf("?") != -1){
    	    					urlStrTemp = urlText.substring(0,urlText.indexOf("?"));
    	    					urlPramTemp = urlText.substring(urlText.indexOf("?")+1,urlText.length());
    	    				}else{
    	    					urlStrTemp = urlText;
    	    					urlPramTemp = "";
    	    				}
    	    				for(int paramCount=0;paramCount<urlParamNameVec.size();paramCount++){
    	    					urlParamName = (String)urlParamNameVec.get(paramCount);
    	    					urlParamValueMode = (String)urlParamValueModeVec.get(paramCount);
    	    					urlParamText = (String)urlParamTextVec.get(paramCount);
	    						if(dynamicURLParam.get(urlParamName) == null){
	    							dynamicURLParam.put(urlParamName,new java.util.Vector<String>());
	    						}
    	    					if("text".equals(urlParamValueMode.trim().toLowerCase())){
    	    						urlPramTemp = urlPramTemp + "&" + urlParamName + "=" + urlParamText.trim();
    	    					}else if("sql".equals(urlParamValueMode.trim().toLowerCase())){
    	    						java.sql.Connection connParam = Util.getConnection(configDocument);
    	    						urlParamText = Util.selectParamData(connParam, urlParamText);
    	    						Util.closeConnection(connParam);
    	    						if(urlParamText.indexOf(";") != -1){
        	    						String urlParamTextArr[] = urlParamText.split("[;]");
        	    						for(int urlParamTextIdx=0;urlParamTextIdx<urlParamTextArr.length;urlParamTextIdx++){
        	    							urlParamTextArr[urlParamTextIdx] = urlParamTextArr[urlParamTextIdx].trim();
        	    							if("".equals(urlParamTextArr[urlParamTextIdx])) continue;
        	    							dynamicURLParam.get(urlParamName).add(urlParamTextArr[urlParamTextIdx]);
        	    						}
    	    						}else{
        	    						urlPramTemp = urlPramTemp + "&" + urlParamName + "=" + urlParamText;
        	    						//System.out.println("urlPramTemp:"+urlPramTemp);
    	    						}
    	    						
    	    					}else if("sql-from-to".equals(urlParamValueMode.trim().toLowerCase())){
    	    						urlTempStrFrom = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URLParam/param/from/text()");
    	    						urlTempStrTo = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URLParam/param/to/text()");
    	    						java.sql.Connection connParam = Util.getConnection(configDocument);
    	    						urlTempStrFrom = Util.selectParamData(connParam, urlTempStrFrom);
    	    						urlTempStrTo = Util.selectParamData(connParam, urlTempStrTo);
    	    						Util.closeConnection(connParam);

    	    						java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
    	    						java.util.Date d1 = null;
    	    						java.util.Date d2 = null;
									try {
										d1 = df.parse( urlTempStrFrom );
	    	    						d2 = df.parse( urlTempStrTo );
									} catch (ParseException e) {
										//e.printStackTrace();
										Util.println("[CHECK] ParseException !!");
									}
    	    						java.util.Calendar c1 = java.util.Calendar.getInstance();
    	    						java.util.Calendar c2 = java.util.Calendar.getInstance();
    	    						c1.setTime( d1 );
    	    						c2.setTime( d2 );
    	    						while( c1.compareTo( c2 ) !=1 ){
    	    							dynamicURLParam.get(urlParamName).add(df.format(c1.getTime()));
    	    							c1.add(java.util.Calendar.DATE, 1);
    	    						}
    	    					}
    	    					
    	    				}//end for
    	    			}
    	    			
						if(!"".equals(urlStrTemp) && !"".equals(urlPramTemp)){
	    	    			url = urlStrTemp + "?" + urlPramTemp;
						}else{
	    	    			url = urlText + "?";
						}
						url = Util.replace(url, "?&", "?");
						baseUrl = url;
						
    	    			
						java.util.HashMap<String, java.util.Vector<String>> dynamicURLParamTemp = new java.util.HashMap<String, java.util.Vector<String>>();
    	    			java.util.Set keyset1 = dynamicURLParam.keySet();
    	    			java.util.Iterator it1 = keyset1.iterator();
    	    			String keyset1Key = "";
    	    			String keyset1Value = "";
    	    			while(it1.hasNext()){
    	    				keyset1Key = (String)it1.next();
    	    				//System.out.println(keyset1Key);
    	    				java.util.Vector valueVec = dynamicURLParam.get(keyset1Key);
    	    				if(valueVec.size() == 1){
    	    					baseUrl = baseUrl + "&" + keyset1Key + "=" + valueVec.get(0);
    	    				}else{
    	    					dynamicURLParamTemp.put(keyset1Key, valueVec); 
    	    				}
    	    			}
    	    			dynamicURLParam = dynamicURLParamTemp;
						//System.out.println("baseUrl:"+baseUrl);
    	    			//System.out.println(dynamicURLParam);

						urlEncoding = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URL/@encoding");
    	    			urlTimeOut = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/"+targetRunModeStr+"/URL/@timeout");
    	    			mappingFieldVec = Util.xmlNode(configDocument,"/Config/Target[@id='"+targetId+"']/MappingField/@dbFieldName");
    	    			mappingFieldXPathVec = Util.xmlNode(configDocument,"/Config/Target[@id='"+targetId+"']/MappingField/text()");
    	    			
    	    			if(mappingFieldVec.size() != mappingFieldXPathVec.size()){
    	    				Util.println("[CHECK] targetId : "+targetId+" MappingField dbFieldName count:"+mappingFieldVec.size()+" / MappingField Xpath count:"+mappingFieldXPathVec.size());
    	    				System.exit(1);
    	    			}
    	    			
    	    			
    	    			msg = ""+separator+separator;
    	    			msg += "-----------------------------------------------------------------------"+separator;
    	    			msg += "targetId : "+targetId+separator;
    	    			msg += "targetIdKor : "+targetIdKor+separator;
    	    			msg += "targetRunMode : "+targetRunMode+separator;
    	    			msg += "insertTableName : "+insertTableName+separator;
    	    			msg += "targetType : "+targetType+separator;
    	    			msg += "-----------------------------------------------------------------------"+separator;
    	    			System.out.println(msg);

    	    			msg = "";
    	    			msg += "[targetId:"+targetId+"] [targetRunMode:"+targetRunMode+"]";
    	    			Util.println(msg);
    	    			msg = "";
    	    			msg += "[insertTableName:"+insertTableName+"] [targetType:"+targetType+"]";
    	    			Util.println(msg);

    	    			if("A".equals(targetType)){
    	    				
        	    			if("static".equals(targetRunMode)){
        	    				cnt = 0;
        	    				long startTime = System.currentTimeMillis();
        	    				//java.sql.Connection conn = null;
        	    				
        	    				java.sql.Connection conn = Util.getConnection(configDocument);

        	    				if(conn != null){
        	    					Util.println("DB Connection - Success!! : "+conn);
        	    					Util.println("Delete data : "+insertTableName);
        	    					Util.deleteDataTable(conn, insertTableName);
        	    				}else{
        	    					Util.println("\nDB Connection - Fail!!");
        	    					System.exit(1);
        	    				}

        	    				
        	    				Util.println("Insert data : "+insertTableName);
        	    				nodeMore = "";
        	        			while(!"false".equals(nodeMore)){
        	        				url = Util.replace(url, "?&", "?");
        	        				if(url.endsWith("?")){
            	        				url = Util.replace(url, "?", "");        	
        	        				}
        	        				//System.out.println(url);
        	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
        	            			if("TIMEOUT".equals(apiResultXMLStr)){
        	            				for(int z=0;z<Integer.parseInt(urlConnectionRetryCount);z++){
            	            				try {
            	            					Util.println(""+(Integer.parseInt(urlConnectionRetryWait)/1000)+"[sec] wait !!");
    											Thread.sleep(Integer.parseInt(urlConnectionRetryWait));
    										} catch (InterruptedException e) {
    											//e.printStackTrace();
    											Util.println("Please Check InterruptedException !!");
    										}
            	            				Util.println("Retry getHtml !! - ("+(z+1)+")");
                	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
                	            			if(!"TIMEOUT".equals(apiResultXMLStr)){
                	            				break;
                	            			}
        	            				}
        	            				if("TIMEOUT".equals(apiResultXMLStr)){
        	            					Util.println("Please Check Target System !!");
        	            					System.exit(1);
            	            			}
        	            			}
        	            			//System.out.println(apiResultXMLStr);
        	            			apiResultDocument = Util.stringToDocument(apiResultXMLStr);
        	            			nodeCount = Util.xmlNodeFirst(apiResultDocument,"/nodes/@count");
        	            			if("0".equals(nodeCount)) break;
        	            			nodeMore = Util.xmlNodeFirst(apiResultDocument,"/nodes/@more");
        	            			nidVec = Util.xmlNode(apiResultDocument,"/nodes/node/@nid");
        	            			er = nidVec.elements();
        	            			while(er.hasMoreElements()){
        	            				nid = er.nextElement()+"";
        	            				//cnt = cnt + 1;
        	            				//System.out.println(cnt+":"+nid);
        	            				erField = mappingFieldVec.elements();
        	            				int fieldCnt = -1;
        	            				
        	            				insertRowMap = new java.util.HashMap<String,String>();
        	            				while(erField.hasMoreElements()){
        	            					fieldCnt = fieldCnt + 1;
        	            					dbFieldName = erField.nextElement()+"";
        	            					dbFieldXPath = mappingFieldXPathVec.get(fieldCnt)+"";
        	            					if(dbFieldXPath.indexOf("<NID>") != -1){
            	            					dbFieldXPath = Util.replace(dbFieldXPath, "<NID>", nid);
        	            					}else if(dbFieldXPath.indexOf("<nid>") != -1){
            	            					dbFieldXPath = Util.replace(dbFieldXPath, "<nid>", nid);
        	            					}
        	            					dbFieldValue = Util.xmlNodeFirst(apiResultDocument,dbFieldXPath);
        	            					//System.out.println("dbFieldName:"+dbFieldName+" / dbFieldXPath:"+dbFieldXPath+" / dbFieldValue:"+dbFieldValue);
        	            					insertRowMap.put(dbFieldName, dbFieldValue);
        	            				}
        	            				
        	            				
        	            				if(Util.insertDataTable(conn, insertTableName, insertRowMap) == 0) continue;
        	            				else cnt = cnt + 1;
        	            				
        	            				lastNid = nid;
        	            				//er.nextElement();
        	            				if(cnt%100 == 0){
        	                				System.out.print(".");
        	            				}
        	            				if(cnt%1000 == 0){
        	            					System.out.print("["+cnt+"]");
        	            				}
        	            				if(cnt%5000 == 0){
        	            					System.out.println("");
        	            				}
        	            				if(cnt%Integer.parseInt(insertCommitCount) == 0){
        	            					try {
        										conn.commit();
        									} catch (SQLException e) {
        										e.printStackTrace();
        										Util.println("DB commit - Fail!!");
        									}
        	            				}
        	            			}
        	            			//System.out.println(nodeMore + " / " + lastNid);
        	            			if("true".equals(nodeMore)){
        	            				url = baseUrl + "&position="+lastNid;
        	            			}
        	        			}
        	        			Util.closeConnection(conn);
        	        			Util.println("DB Connection - Close!!");
        	        			
        	        			long endTime = System.currentTimeMillis();
        	                    long runtimeL = endTime - startTime;
        	                    double runtime = runtimeL;
        	                    runtime = runtime / (double)1000;
        	        			
        	        			msg = "";
        	        			msg += "-----------------------------------------------------------------------"+separator;
        	        			msg += "targetId : "+targetId+separator;
        	        			msg += ""+insertTableName+" Table insert Count : "+cnt+separator;
        	        			msg += "run time : "+runtime+"[sec]"+separator;
        	        			msg += "-----------------------------------------------------------------------"+separator;
        	        			System.out.print(msg);

        	    			}else if("dynamic".equals(targetRunMode)){
        	    				cnt = 0;
        	    				long startTime = System.currentTimeMillis();
        	    				java.sql.Connection conn = Util.getConnection(configDocument);
        	    				
        	    				Util.println("Insert data : "+insertTableName);
        	    				nodeMore = "";
        						
        						java.util.Set keyset = dynamicURLParam.keySet();
        						java.util.Iterator it = keyset.iterator();
        						java.util.Enumeration erParamVal = null;
        						String key = "";
        						String value = "";
        						java.util.Vector<String> dynamicURLVec = new java.util.Vector<String>();
        						while(it.hasNext()){
        							key = (String)it.next();
        							erParamVal = dynamicURLParam.get(key).elements();
        							while(erParamVal.hasMoreElements()){
        								value = (String)erParamVal.nextElement();
        								if(url.indexOf("?") == -1){
        									url = baseUrl + "?";
        								}
        								url = baseUrl + "&" + key + "=" + value;
            	        				url = Util.replace(url, "?&", "?");
            	        				if(url.endsWith("?")){
                	        				url = Util.replace(url, "?", "");        	
            	        				}
                						dynamicURLVec.add(url);
        							}
        						}
        						//System.exit(1);
        	    				
        						for(int v=0;v<dynamicURLVec.size();v++){
        							baseUrl = dynamicURLVec.get(v);
        							//System.out.println("baseUrl:"+baseUrl);
        							url = baseUrl;
        							nodeMore = "";
            	        			while(!"false".equals(nodeMore)){
            	        				//System.out.println("url:"+url);
            	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
            	            			if("TIMEOUT".equals(apiResultXMLStr)){
            	            				for(int z=0;z<Integer.parseInt(urlConnectionRetryCount);z++){
                	            				try {
                	            					Util.println(""+(Integer.parseInt(urlConnectionRetryWait)/1000)+"[sec] wait !!");
        											Thread.sleep(Integer.parseInt(urlConnectionRetryWait));
        										} catch (InterruptedException e) {
        											//e.printStackTrace();
        											Util.println("InterruptedException !!");
        										}
                	            				Util.println("Retry getHtml !! - ("+(z+1)+")");
                    	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
                    	            			if(!"TIMEOUT".equals(apiResultXMLStr)){
                    	            				break;
                    	            			}
            	            				}
            	            				if("TIMEOUT".equals(apiResultXMLStr)){
            	            					Util.println("Please Check Target System !!");
            	            					System.exit(1);
                	            			}
            	            			}
            	            			//System.out.println(apiResultXMLStr);
            	            			apiResultDocument = Util.stringToDocument(apiResultXMLStr);
            	            			nodeMore = Util.xmlNodeFirst(apiResultDocument,"/nodes/@more");
										nodeCount = Util.xmlNodeFirst(apiResultDocument,"/nodes/@count");
            	            			if("0".equals(nodeCount)) break;
            	            			nidVec = Util.xmlNode(apiResultDocument,"/nodes/node/@nid");
            	            			er = nidVec.elements();
            	            			while(er.hasMoreElements()){
            	            				nid = er.nextElement()+"";
            	            				
            	            				//System.out.println(cnt+":"+nid);
            	            				erField = mappingFieldVec.elements();
            	            				int fieldCnt = -1;
            	            				
            	            				insertRowMap = new java.util.HashMap<String,String>();
            	            				while(erField.hasMoreElements()){
            	            					fieldCnt = fieldCnt + 1;
            	            					dbFieldName = erField.nextElement()+"";
            	            					dbFieldXPath = mappingFieldXPathVec.get(fieldCnt)+"";
            	            					if(dbFieldXPath.indexOf("<NID>") != -1){
                	            					dbFieldXPath = Util.replace(dbFieldXPath, "<NID>", nid);
            	            					}else if(dbFieldXPath.indexOf("<nid>") != -1){
                	            					dbFieldXPath = Util.replace(dbFieldXPath, "<nid>", nid);
            	            					}
            	            					dbFieldValue = Util.xmlNodeFirst(apiResultDocument,dbFieldXPath);
            	            					//System.out.println("dbFieldName:"+dbFieldName+" / dbFieldXPath:"+dbFieldXPath+" / dbFieldValue:"+dbFieldValue);
            	            					insertRowMap.put(dbFieldName, dbFieldValue);
            	            				}
            	            				
            	            				
            	            				if(Util.deleteInsertDataTable(conn, insertTableName, insertRowMap) == 0) continue;
            	            				else cnt = cnt + 1;
            	            				
            	            				lastNid = nid;
            	            				//er.nextElement();
            	            				if(cnt%100 == 0){
            	                				System.out.print(".");
            	            				}
            	            				if(cnt%1000 == 0){
            	            					System.out.print("["+cnt+"]");
            	            				}
            	            				if(cnt%5000 == 0){
            	            					System.out.println("");
            	            				}
            	            				if(cnt%Integer.parseInt(insertCommitCount) == 0){
            	            					try {
            										conn.commit();
            									} catch (SQLException e) {
            										e.printStackTrace();
            										Util.println("DB commit - Fail!!");
            									}
            	            				}
            	            			}
            	            			//System.out.println(nodeMore);
            	            			if("true".equals(nodeMore)){
            	            				url = baseUrl + "&position="+lastNid;
            	            			}
            	        			}//end while
        							
        						}//end for
        	        			Util.closeConnection(conn);
        	        			Util.println("DB Connection - Close!!");
        	        			
        	        			long endTime = System.currentTimeMillis();
        	                    long runtimeL = endTime - startTime;
        	                    double runtime = runtimeL;
        	                    runtime = runtime / (double)1000;
        	        			
        	        			msg = "";
        	        			msg += "-----------------------------------------------------------------------"+separator;
        	        			msg += "targetId : "+targetId+separator;
        	        			msg += ""+insertTableName+" Table delete & insert Count : "+cnt+separator;
        	        			msg += "run time : "+runtime+"[sec]"+separator;
        	        			msg += "-----------------------------------------------------------------------"+separator;
        	        			System.out.print(msg);
        	    				
        	        			
        	    			}else if("match".equals(targetRunMode)){
        	    				cnt = 0;
        	    				long startTime = System.currentTimeMillis();
        	    				java.sql.Connection conn = Util.getConnection(configDocument);
        	    				
        	    				Util.println("Insert data : "+insertTableName);
        	    				nodeMore = "";
        						
        						java.util.Set keyset = dynamicURLParam.keySet();
        						java.util.Iterator it = keyset.iterator();
        						java.util.Enumeration erParamVal = null;
        						String key = "";
        						String value = "";
        						java.util.Vector<String> dynamicURLVec = new java.util.Vector<String>();
        						while(it.hasNext()){
        							key = (String)it.next();
        							erParamVal = dynamicURLParam.get(key).elements();
        							while(erParamVal.hasMoreElements()){
        								value = (String)erParamVal.nextElement();
        								if(url.indexOf("?") == -1){
        									url = baseUrl + "?";
        								}
        								url = baseUrl + "&" + key + "=" + value;
        								url = Util.replace(url, "?&", "?");
            	        				if(url.endsWith("?")){
                	        				url = Util.replace(url, "?", "");        	
            	        				}
        								//System.out.println(url);
                						dynamicURLVec.add(url);
        							}
        						}
        						
        						if(dynamicURLVec.size() == 0){
        							if(!baseUrl.endsWith("=")){
            							dynamicURLVec.add(baseUrl);
        							}
        						}
        						//System.exit(1);

        						for(int v=0;v<dynamicURLVec.size();v++){
        							baseUrl = dynamicURLVec.get(v);
        							System.out.println("baseUrl2:"+baseUrl);
        							url = baseUrl;
        							nodeMore = "";
            	        			//while(!"false".equals(nodeMore)){
            	        				//System.out.println("url:"+url);
            	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
            	            			if("TIMEOUT".equals(apiResultXMLStr)){
            	            				for(int z=0;z<Integer.parseInt(urlConnectionRetryCount);z++){
                	            				try {
                	            					Util.println(""+(Integer.parseInt(urlConnectionRetryWait)/1000)+"[sec] wait !!");
        											Thread.sleep(Integer.parseInt(urlConnectionRetryWait));
        										} catch (InterruptedException e) {
        											e.printStackTrace();
        											Util.println("Please Check InterruptedException !!");
        										}
                	            				Util.println("Retry getHtml !! - ("+(z+1)+")");
                    	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
                    	            			if(!"TIMEOUT".equals(apiResultXMLStr)){
                    	            				break;
                    	            			}
            	            				}
            	            				if("TIMEOUT".equals(apiResultXMLStr)){
            	            					Util.println("Please Check Target System !!");
            	            					System.exit(1);
                	            			}
            	            			}
            	            			//System.out.println(apiResultXMLStr);
            	            			apiResultDocument = Util.stringToDocument(apiResultXMLStr);
            	            			nodeMore = Util.xmlNodeFirst(apiResultDocument,"/nodes/@more");
            	            			nodeCount = Util.xmlNodeFirst(apiResultDocument,"/nodes/@count");
            	            			if("0".equals(nodeCount)) break;
            	            			//nidVec = Util.xmlNode(apiResultDocument,"/nodes/node/@nid");
            	            			nidVec = Util.xmlNode(apiResultDocument,"/node/@nid");
            	            			er = nidVec.elements();
            	            			while(er.hasMoreElements()){
            	            				nid = er.nextElement()+"";
            	            				//cnt = cnt + 1;
            	            				//System.out.println(cnt+":"+nid);
            	            				erField = mappingFieldVec.elements();
            	            				int fieldCnt = -1;
            	            				
            	            				insertRowMap = new java.util.HashMap<String,String>();
            	            				while(erField.hasMoreElements()){
            	            					fieldCnt = fieldCnt + 1;
            	            					dbFieldName = erField.nextElement()+"";
            	            					dbFieldXPath = mappingFieldXPathVec.get(fieldCnt)+"";
            	            					if(dbFieldXPath.indexOf("<NID>") != -1){
                	            					dbFieldXPath = Util.replace(dbFieldXPath, "<NID>", nid);
            	            					}else if(dbFieldXPath.indexOf("<nid>") != -1){
                	            					dbFieldXPath = Util.replace(dbFieldXPath, "<nid>", nid);
            	            					}
            	            					dbFieldXPath = Util.replace(dbFieldXPath, "/nodes", "");
            	            					dbFieldValue = Util.xmlNodeFirst(apiResultDocument,dbFieldXPath);
            	            					//System.out.println("dbFieldName:"+dbFieldName+" / dbFieldXPath:"+dbFieldXPath+" / dbFieldValue:"+dbFieldValue);
            	            					insertRowMap.put(dbFieldName, dbFieldValue);
            	            				}
            	            				
            	            				
            	            				if(Util.deleteInsertDataTable(conn, insertTableName, insertRowMap) == 0) continue;
            	            				else cnt = cnt + 1;
            	            				
            	            				lastNid = nid;
            	            				//er.nextElement();
            	            				if(cnt%100 == 0){
            	                				System.out.print(".");
            	            				}
            	            				if(cnt%1000 == 0){
            	            					System.out.print("["+cnt+"]");
            	            				}
            	            				if(cnt%5000 == 0){
            	            					System.out.println("");
            	            				}
            	            				if(cnt%Integer.parseInt(insertCommitCount) == 0){
            	            					try {
            										conn.commit();
            									} catch (SQLException e) {
            										e.printStackTrace();
            										Util.println("DB commit - Fail!!");
            									}
            	            				}
            	            			}
            	            			//System.out.println(nodeMore);
            	            			//if("true".equals(nodeMore)){
            	            			//	url = baseUrl + "&position="+lastNid;
            	            			//}
            	        			//}//end while
        							
        						}//end for
        	        			Util.closeConnection(conn);
        	        			Util.println("DB Connection - Close!!");
        	        			
        	        			long endTime = System.currentTimeMillis();
        	                    long runtimeL = endTime - startTime;
        	                    double runtime = runtimeL;
        	                    runtime = runtime / (double)1000;
        	        			
        	        			msg = "";
        	        			msg += "-----------------------------------------------------------------------"+separator;
        	        			msg += "targetId : "+targetId+separator;
        	        			msg += ""+insertTableName+" Table delete & insert Count : "+cnt+separator;
        	        			msg += "run time : "+runtime+"[sec]"+separator;
        	        			msg += "-----------------------------------------------------------------------"+separator;
        	        			System.out.print(msg);
        	    				
        	    			}
        	    			
    	    			}else if("B".equals(targetType)){
    	    				cnt = 0;
    	    				long startTime = System.currentTimeMillis();
    	    				
    	    				java.sql.Connection conn = null;

    	    				conn = Util.getConnection(configDocument);
    	    				if(conn != null){
    	    					Util.println("DB Connection - Success!! : "+conn);
    	    					Util.println("Delete data : "+insertTableName);
    	    					Util.deleteDataTable(conn, insertTableName);
    	    				}else{
    	    					Util.println("DB Connection - Fail!!");
    	    					System.exit(1);
    	    				}

    	    				
    	    				Util.println("Insert data : "+insertTableName);
    	    				
    	    				java.util.Set keyset = dynamicURLParam.keySet();
    						java.util.Iterator it = keyset.iterator();
    						java.util.Enumeration erParamVal = null;
    						String key = "";
    						String value = "";
    						java.util.Vector<String> dynamicURLVec = new java.util.Vector<String>();
    						while(it.hasNext()){
    							key = (String)it.next();
    							erParamVal = dynamicURLParam.get(key).elements();
    							while(erParamVal.hasMoreElements()){
    								value = (String)erParamVal.nextElement();
    								if(url.indexOf("?") == -1){
    									url = baseUrl + "?";
    								}
    								url = baseUrl + "&" + key + "=" + value;
        	        				url = Util.replace(url, "?&", "?");
        	        				if(url.endsWith("?")){
            	        				url = Util.replace(url, "?", "");        	
        	        				}
            						dynamicURLVec.add(url);
    							}
    						}
    						
    						//System.out.println("AAA:"+dynamicURLVec);
    						if(dynamicURLVec.size() == 0){
    							dynamicURLVec.add(baseUrl);
    						}
    						
    						for(int v=0;v<dynamicURLVec.size();v++){
    							baseUrl = dynamicURLVec.get(v);
    							//System.out.println("baseUrl22:"+baseUrl);
    							url = baseUrl;
    	        				if(url.endsWith("?")){
        	        				url = Util.replace(url, "?", "");        	
    	        				}
    							//System.out.println("url:"+url);
    	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
    	            			if("TIMEOUT".equals(apiResultXMLStr)){
    	            				for(int z=0;z<Integer.parseInt(urlConnectionRetryCount);z++){
        	            				try {
        	            					Util.println(""+(Integer.parseInt(urlConnectionRetryWait)/1000)+"[sec] wait !!");
    										Thread.sleep(Integer.parseInt(urlConnectionRetryWait));
    									} catch (InterruptedException e) {
    										e.printStackTrace();
    										Util.println("Please Check InterruptedException !!");
    									}
        	            				Util.println("Retry getHtml !! - ("+(z+1)+")");
            	            			apiResultXMLStr = Util.getHtmls(url, urlEncoding, Integer.parseInt(urlTimeOut));
            	            			if(!"TIMEOUT".equals(apiResultXMLStr)){
            	            				break;
            	            			}
    	            				}
    	            				if("TIMEOUT".equals(apiResultXMLStr)){
    	            					Util.println("Please Check Target System !!");
    	            					System.exit(1);
        	            			}
    	            			}
    	            			
    	            			//System.out.println(apiResultXMLStr);
    	            			apiResultDocument = Util.stringToDocument(apiResultXMLStr);
    	            			// System.out.println("BONG::"+apiResultDocument.getTextContent());

    	            			String rowCheckXpath = Util.xmlNodeFirst(configDocument,"/Config/Target[@id='"+targetId+"']/ROW/text()");
    	            			String rowCountStr = "1";
    	            			if(!"".equals(rowCheckXpath)){
        	            			rowCheckXpath = "count("+rowCheckXpath+")";
        	            			rowCountStr = Util.xmlNodeFirst(apiResultDocument,rowCheckXpath);
    	            			}
    	            			
    	            			for(int row=0;row<Integer.parseInt(rowCountStr);row++){
    		            			//cnt = cnt + 1;
    	            				erField = mappingFieldVec.elements();
    	            				int fieldCnt = -1;
    	            				insertRowMap = new java.util.HashMap<String,String>();
    	            				while(erField.hasMoreElements()){
    	            					fieldCnt = fieldCnt + 1;
    	            					dbFieldName = erField.nextElement()+"";
    	            					dbFieldXPath = mappingFieldXPathVec.get(fieldCnt)+"";
    	            					if(dbFieldXPath.indexOf("<ROW>") != -1){
    		            					dbFieldXPath = Util.replace(dbFieldXPath, "<ROW>", (row+1)+"");
    	            					}
    	            					
    	            					
    	            					//System.out.println(dbFieldXPath);
    	            					dbFieldValue = Util.xmlNodeData(apiResultDocument,dbFieldXPath);
    	            					System.out.println(dbFieldValue);
    	            					
    	            					
    	            					insertRowMap.put(dbFieldName, dbFieldValue);
    	            				}//end while
    	            				
    	            				if(Util.insertDataTable(conn, insertTableName, insertRowMap) == 0) continue;
    	            				else cnt = cnt + 1;
    	            				
    	            				if(cnt%10 == 0){
    	                				System.out.print(".");
    	            				}
    	            				if(cnt%100 == 0){
    	            					System.out.print("["+cnt+"]");
    	            				}
    	            				if(cnt%1000 == 0){
    	            					System.out.println("");
    	            				}
    	            				if(cnt%Integer.parseInt(insertCommitCount) == 0){
    	            					try {
    										conn.commit();
    									} catch (SQLException e) {
    										e.printStackTrace();
    										Util.println("DB commit - Fail!!");
    									}
    	            				}
    	            			}
    	            			
    						}//end for
    						
    	        			Util.closeConnection(conn);
    	        			Util.println("DB Connection - Close!!");
    	        			
    	        			long endTime = System.currentTimeMillis();
    	                    long runtimeL = endTime - startTime;
    	                    double runtime = runtimeL;
    	                    runtime = runtime / (double)1000;
    	        			
    	        			msg = "";
    	        			msg += "-----------------------------------------------------------------------"+separator;
    	        			msg += "targetId : "+targetId+separator;
    	        			msg += ""+insertTableName+" Table insert Count : "+cnt+separator;
    	        			msg += "run time : "+runtime+"[sec]"+separator;
    	        			msg += "-----------------------------------------------------------------------"+separator;
    	        			System.out.println(msg);

    	        			
    	    			}//end if

    					
    					
    				}//end if
    			}//end for
    			
    			
    		}
    	}
		
	}//end main

}
