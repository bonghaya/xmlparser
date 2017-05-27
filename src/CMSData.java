import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;

public class CMSData {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub		
		
		insertCmsData();
	}

	public static void insertCmsData() throws SQLException{
		
		System.out.println("Cms Data Insert Start !!");
		
		String oracle_host = "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 100.100.10.73)(PORT = 1521)) (CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = CMSDB)))";
		//String oracle_host 	= "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 211.233.93.10)(PORT = 7521)) (CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = CMSDB)))";
		String oracle_id 	= "ksearch";
		String oracle_pw 	= "ksearch#12";
		
		//String mysql_host 	= "jdbc:mysql://192.168.0.31:3306/search?characterEncoding=UTF-8";
		String mysql_host 	= "jdbc:mysql://211.233.93.242:3306/search?characterEncoding=UTF-8";
		String mysql_id 	= "search";
		String mysql_pw 	= "search";
		
		//http://smart.kbs.co.kr
		
		Connection oracle_conn = null;
		Connection mysql_conn = null;
		
		oracle_conn = DBConn(oracle_conn, "oracle", oracle_host, oracle_id, oracle_pw);
		mysql_conn = DBConn(oracle_conn, "mysql", mysql_host, mysql_id, mysql_pw);
		
		//CMS Data 조회
		selectCmsData(oracle_conn, mysql_conn);
		
		if(oracle_conn != null)
			oracle_conn.close();
		
		if(mysql_conn != null)
			mysql_conn.close();
		
		System.out.println("Cms Data Insert End !!");
	}
	
	public static void selectCmsData(Connection oracle_conn, Connection mysql_conn){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		PreparedStatement pstmtMobile = null;
		ResultSet rsMobile = null;
		
		String sql = "";
		String sqlMobile = "";
		String table = "ics5.total_search";
		String tableMobile = "ICS5.total_search_mobile_view";
		String mysqlTable = "CmsData";
		
		sql = makeQuery(table, "web");
		sqlMobile = makeQuery(tableMobile, "mobiles");
			
		try {
			
			pstmt = oracle_conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			pstmtMobile = oracle_conn.prepareStatement(sqlMobile);
			rsMobile = pstmtMobile.executeQuery();
			
			
			
			//테이블을 비운다.
			cmsTableTruncate(mysql_conn, mysqlTable);
			
			//오라클 cms data vod 검색 결과를 MSSQL 테이블에 Insert
			insertCMSData(mysql_conn, rs, mysqlTable, "web");
			
			insertCMSData(mysql_conn, rsMobile, mysqlTable, "mobile");
			
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
			if(rsMobile != null)
				rsMobile.close();
			if(pstmtMobile != null)
				pstmtMobile.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public static void insertCMSData(Connection conn, ResultSet rs, String table, String gubun) throws SQLException{
		
		PreparedStatement pstmt = null;
		
		String sql = "INSERT INTO "+table+ " VALUES (?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		
		int i=0;
		
		System.out.println("Insert Query :: " + sql);
		
		while(rs.next()){
			pstmt.setString(1, rs.getString(1));
			pstmt.setString(2, rs.getString(2));
			pstmt.setString(3, rs.getString(3));
			pstmt.setString(4, rs.getString(4));
			pstmt.setString(5, rs.getString(5));
			pstmt.setString(6, rs.getString(6));
			pstmt.setString(7, rs.getString(7));
			pstmt.setString(8, gubun);
			pstmt.setString(9, getToday(0));
			
			pstmt.executeUpdate();
			
			//System.out.println("Insert Data Count :: " + i++);
			i++;
			
			if(i%1000 == 0){
				System.out.print(".");
			}
			if(i%10000 == 0){
				System.out.print("["+i+"]");
			}
			if(i%100000 == 0){
				System.out.println("");
			}
			if(i%Integer.parseInt("1000") == 0){
				try {
					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(pstmt != null)
			pstmt.close();
	}
	
	//테이블 비우기
	public static void cmsTableTruncate(Connection conn , String table) throws SQLException{
		
		Statement stmt = null;
		String sql = "truncate table " + table;
		
		if("" != table){
			
			System.out.println("Truncate Table Query :: " + sql);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			System.out.println("Success Truncate Table :: " + table);
		}
		
		if(stmt != null)
			stmt.close();
	}
	
	public static Connection DBConn(Connection conn, String className, String host, String id, String pw){		
		try{
			Class.forName(getClassName(className));
			conn = DriverManager.getConnection(host, id, pw);
			conn.setAutoCommit(false);
			
			if(conn != null)
				System.out.println("Connection Success :: " + className);
			else
				System.out.println("Connection Fail");

		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
				
		return conn;
	}
	
	public static String getClassName(String dbName){
		
		String className = "";
		
		if("oracle".equalsIgnoreCase(dbName))
			className = "oracle.jdbc.driver.OracleDriver";
		else if("mssql".equalsIgnoreCase(dbName))
			className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		else if("mysql".equalsIgnoreCase(dbName))
			className = "com.mysql.jdbc.Driver";
		else if("cubrid".equalsIgnoreCase(dbName))
			className = "cubrid.jdbc.driver.CUBRIDDriver";
		
		return className;
	}
	
	public static String makeQuery(String table, String gubun){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select");
		sql.append("	 catid");
		sql.append("	,catnm");
		
		if("web".equals(gubun))
			sql.append("	,substr(catdirpath,4) as catdirpath");
		else
			sql.append("	,substr(catdirpath,11) as catdirpath");
		
		sql.append("	,catdirpathnm");
		sql.append("	,uppercatid");
		sql.append("	,program_name");
		sql.append("	,group_code");
		sql.append(" from " + table);
				
		return sql.toString();
	}
	
	//오늘 날짜를 가져온다(yyyymmdd)
    public static String getToday(int num){   	
    	String curYear = ""; 
    	DecimalFormat df = new DecimalFormat("00");
    	Calendar temp = Calendar.getInstance();
    	temp.add(temp.DATE, num);
    	
    	String year = Integer.toString(temp.get(Calendar.YEAR));
    	String month = df.format(temp.get(Calendar.MONTH)+1);
    	String day = df.format(temp.get(Calendar.DAY_OF_MONTH));  
    	
    	curYear = year+month+day;
    	
    	return curYear;
    }
}