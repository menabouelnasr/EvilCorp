import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
public class Database 
{
	private int add_Acc, update_Acc, remove_Acc, check_Acc, add_Trans;
	static Connection conn;
	
	public static void openConnection()
	{
		//URL of Oracle database server
        String url = "jdbc:oracle:thin:testuser/password@localhost"; 
      
        //properties for creating connection to Oracle database
        Properties props = new Properties();
        props.setProperty("user", "testdb");
        props.setProperty("password", "password");
      
        //creating connection to Oracle database using JDBC
        try {
			conn = DriverManager.getConnection(url,props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet getFromDB(String sql)throws SQLException 
	{
		PreparedStatement preStatement = conn.prepareStatement(sql);
	    
        ResultSet result = preStatement.executeQuery();
      
        return result;
	}
	
	public int getCheck_Acc() throws SQLException 
	{
		
        String sql ="select count(*) as NumberOfAccounts from acc_Info where acc_Num = "+ check_Acc;

        ResultSet result= getFromDB(sql);
      
        result.next();
		return result.getInt("NumberofAccounts");
	}

	public void setCheck_Acc(int check_Acc) 
	{
		this.check_Acc = check_Acc;
	}

	public void UpdateDB(String sql) throws SQLException 
	{

        //creating PreparedStatement object to execute query
        PreparedStatement preStatement = conn.prepareStatement(sql);
    
        preStatement.executeUpdate();

	}

	public void setAdd_Acc(int acc, String name, Double balance, String birthday) 
	{
		int account= acc;
		String names= name;
		Double bal = balance;
		String bday= birthday;
		
		String sql ="INSERT INTO Acc_Info (ACC_NUM, ACC_NAME, BAL, BIRTHDAY) "
        		+ "VALUES ("+ account + ",'"+ names + "', "+ bal + ",To_DATE('" + bday + "','mm/dd/yyyy'))";
		try {
			UpdateDB(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	public void setAdd_Trans(int acc, double trans_Amount, int trans_Type, String date, int status) 
	{
		int account= acc;
		double amount= trans_Amount;
		int type = trans_Type;
		String day = date;
		int stat= status;
		
		String sql2 = "INSERT INTO TRANSACTION (ACC_NUM, TRANS_AMOUNT, TRANS_TYPE, TRANS_DATE, Status, Trans_ID) " + 
		"VALUES (" + account + "," + amount + "," + type + "," + "To_DATE('" + day + "','mm/dd/yyyy')," + stat + ",seq_transaction.nextVal )";
		try {
			UpdateDB(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void setUpdate_Acc(int acc, double trans_bal) 
	{
		ResultSet r = null;
		int account = acc;
		double bal= trans_bal;
		
		String sql = "Select bal from acc_Info where acc_Num = " + acc;
		
		try {
			r=getFromDB(sql);
			r.next();
			bal=trans_bal + r.getDouble("bal");
			if( bal<0)
			{
				bal-= 35;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "Update acc_Info set bal = " + bal + "where acc_Num = " + acc;
		try {
			UpdateDB(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setRemove_Acc(int acc_Num) 
	{
		String sql = "Delete from transaction where acc_Num = "  + acc_Num ;
		try {
			UpdateDB(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "Delete from acc_Info where acc_Num = "  + acc_Num ;
		try {
			UpdateDB(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
