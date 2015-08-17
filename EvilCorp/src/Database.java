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
		return result.getInt("NUMBEROFACCOUNTS");
		
	}
	public void setCheck_Acc(int check_Acc) 
	{
		this.check_Acc = check_Acc;
	}

	public int getCheck_Sav_Acc() throws SQLException 
	{
		
        String sql ="select count(*) as NumberOfAccounts from acc_Info where Savings_Num = "+ check_Acc;
        ResultSet result= getFromDB(sql);
      
        result.next();
		return result.getInt("NUMBEROFACCOUNTS");
	}
	public void setSav_Check_Acc(int check_Acc) 
	{
		this.check_Acc = check_Acc;
	}
	
	public void UpdateDB(String sql) throws SQLException 
	{

        //creating PreparedStatement object to execute query
        PreparedStatement preStatement = conn.prepareStatement(sql);
    
        preStatement.executeUpdate();

	}

	public void setAdd_Acc(int acc, String name, Double balance, String birthday, int Sacc, double Sbal) 
	{
		int account= acc;
		String names= name;
		Double bal = balance;
		String bday= birthday;
		int savings= Sacc;
		double savings_Bal=Sbal;
		
		String sql ="INSERT INTO Acc_Info (ACC_NUM, ACC_NAME, BAL, BIRTHDAY, Savings_Num, Savings_Bal) "
        		+ "VALUES ("+ account + ",'"+ names + "', "+ bal + ",To_DATE('" + bday + "','mm/dd/yyyy')," + savings + "," + savings_Bal + ")";
		try {
			UpdateDB(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	public void setAdd_Trans(int acc, double trans_Amount, int trans_Type, String date, int status, String types) 
	{
		int account= acc;
		double amount= trans_Amount;
		int type = trans_Type;
		String day = date;
		int stat= status;
		String sometype=types;
	
		
		String sql2 = "INSERT INTO TRANSACTION (ACC_NUM, TRANS_AMOUNT, TRANS_TYPE, TRANS_DATE, Status, sometype, Trans_ID ) VALUES (" 
		+ account + "," + amount + "," + type + "," + "To_DATE('" + day + "','mm/dd/yyyy')," + stat + ",'" + sometype +"',seq_transaction.nextVal)";
		
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
		double savings_trans;
		
		String sql = "Select bal from acc_Info where acc_Num = " + acc;
		
		try {
			r=getFromDB(sql);
			r.next();
			bal=trans_bal + r.getDouble("bal");
		
			if( bal<0)
			{
				bal-= 45;
				savings_trans= -bal;
				sql = "Update acc_Info set bal = " + 0 + "where acc_Num = " + acc;
				try 
				{
					UpdateDB(sql);
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
				sql="Update acc_Info set savings_bal = savings_bal - "+ savings_trans + "where acc_Num = " + acc;
				try 
				{
					UpdateDB(sql);
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				sql = "Update acc_Info set bal = " + bal + "where acc_Num = " + acc;
				try 
				{
					UpdateDB(sql);
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void setSav_Update_Acc(int acc, double trans_bal)
	{
		ResultSet r = null;
		int account = acc;
		double bal= trans_bal;
		
		String sql = "Select savings_bal from acc_Info where acc_Num = " + acc;
		
		try {
			r=getFromDB(sql);
			r.next();
			bal=trans_bal + r.getDouble("savings_bal");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "Update acc_Info set savings_bal = " + bal + "where acc_Num = " + acc;
		
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
	
	public void transferAccounts(int transfer_F, int transfer_T,  double T_amount)
	{
		int t_T=transfer_T;
		int t_F= transfer_F;
		double transA = T_amount;
		double Sbal=0, Cbal=0;
		if(t_F>t_T)
		{
			//transfer to checking = t_T
			//saving = t_F
			ResultSet r = null;
			ResultSet r2= null;
			
			String sql = "Select savings_bal from acc_Info where acc_Num = " + t_T;
			String sql2 = "Select bal from acc_Info where acc_Num = " + t_T;
			
			try 
			{
				r=getFromDB(sql);
				r2=getFromDB(sql2);
				r.next();
				r2.next();
				Sbal= r.getDouble("savings_bal") - transA;
				Cbal = r2.getDouble("bal") + transA;
				
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sql2 = "Update acc_Info set bal = " + Cbal + "where acc_Num = " + t_T;
			try 
			{
				UpdateDB(sql2);
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sql = "Update acc_Info set savings_bal = " + Sbal + "where acc_Num = " + t_T;
			try {
				UpdateDB(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{
			//transfer to savings = t_T
			//checking = t_F
			ResultSet r = null;
			ResultSet r2= null;
			
			String sql = "Select savings_bal from acc_Info where acc_Num = " + t_F;
			String sql2 = "Select bal from acc_Info where acc_Num = " + t_F;
			
			try {
				r=getFromDB(sql);
				r2=getFromDB(sql2);
				r.next();
				r2.next();
				Sbal= r.getDouble("savings_bal") + transA;
				Cbal = r2.getDouble("bal") - transA;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sql = "Update acc_Info set savings_bal = " + Sbal + "where acc_Num = " + t_F;
			try {
				UpdateDB(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sql2 = "Update acc_Info set bal = " + Cbal + "where acc_Num = " + t_F;
			try {
				UpdateDB(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


}