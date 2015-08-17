import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;


public class EvilCorpApp
{
	
	public static void main(String[] args) throws IOException, SQLException 
	{
		Scanner keyboard= new Scanner(System.in);
		int accNum = 0, i, transfer_F=0, transfer_T=0;
		double bal = 0, transAmount, finAmount=0, trans_bal=0, finalBal=0, Sbal=0;
		boolean promptNum, promptName, promptBal, promptAmount;
		String bday, date;
		String accName, choice="y", start="y", close="n", remove, accType = null;
		int trans, savingsNum=0;

		ArrayList<Double> amount = new ArrayList<Double>();

		Database evil = new Database();
		Database.openConnection();
		System.out.println("\nWelcome to Evil Corp Savings and Loan. Please create the user account(s).");
	
	while(start.equalsIgnoreCase("Y"))
	{
		//if statement to check for acc # in DB
		System.out.println("What account would you like to perform a transaction for? (C/S)");
		accType=keyboard.next();
		
		do{
			System.out.println("Please enter your account #: "); 
			accNum= keyboard.nextInt();
			promptNum= Validator.getAccNum(accNum); //validates account number input
			if(accType.equalsIgnoreCase("c"))
			{
			evil.setCheck_Acc(accNum);
			}
			else
			{
			evil.setSav_Check_Acc(accNum);
			}
			
		}while(promptNum==false);
		
		if(evil.getCheck_Acc()>0 || evil.getCheck_Sav_Acc()>0)
		{
			System.out.println("Account is available");
		}
		else
		{
			System.out.println("Account number unavailable, insert new account.");
			
			Random r = new Random();
			int x = 100 + r.nextInt(999);
			
			accNum= x;
			savingsNum= accNum+1;
			System.out.println("Your new checking account number is: " + accNum);
			System.out.println("Your new savings account number is : "+ savingsNum);
			
			do{
			System.out.println("Please enter the name on your account: ");
			accName=keyboard.next();
			promptName= Validator.getAccName(accName); //validates name input
			}while(promptNum==false);
			
			//check acc # with DB table, if it exists, complete transactions and update table
			
			do{
			System.out.println("Please enter your checking account balance: "); 
			bal=keyboard.nextDouble();
			promptBal= Validator.getBal(bal); //validates balance input
			}while(promptBal==false);
			
			System.out.println("Please enter your savings account balance: "); 
			Sbal=keyboard.nextDouble();
			
			System.out.println("Please enter your birthdate (MM/DD/YYYY): "); 
			bday=keyboard.next();
			
			evil.setAdd_Acc(accNum, accName, bal, bday, savingsNum, Sbal);
		}
		
		
		outerloop:
		while(choice.equalsIgnoreCase("Y"))
		{
			System.out.println("Enter a transaction type related to this account(1 - Deposit, 2 - Check, 3 - Withdrawal, 4 - Transfer) or -1 to finish");
			trans=keyboard.nextInt();
			
			if(trans==-1)
			{
				break outerloop;
			}
			
			if(trans==4)
			{
				System.out.print("Transfer from: ");
				transfer_F=keyboard.nextInt();
				
				System.out.print("Transfer to: ");
				transfer_T=keyboard.nextInt();		
			}
			
			do{
			System.out.println("Enter the transaction amount:");
			transAmount=keyboard.nextDouble();
			promptAmount= Validator.getAmount(transAmount); //validates transaction amount input
			}while(promptAmount==false);
			
			if(trans==1)
				finAmount=transAmount;
			else
				finAmount=-transAmount;
			
			System.out.println("Enter the transaction date: (MM/DD/YYYY)");
			date=keyboard.next();
			
			if(accType.equalsIgnoreCase("c"))
			{
				evil.setAdd_Trans(accNum, finAmount, trans, date, 1, accType);
			}
			else
			{
				evil.setAdd_Trans(accNum, finAmount, trans, date, 1, accType);
			}
			
			if(trans==4)
			{
				evil.transferAccounts(transfer_F, transfer_T, transAmount);
			}
			
			System.out.println("Would you like to enter another transaction? (Y/N) ");
			choice= keyboard.next();
			amount.add(finAmount);
		}
			for(i=0; i<amount.size(); i++)
			{
				 trans_bal +=amount.get(i);
			}
			
			if(accType.equalsIgnoreCase("c"))
			{
				evil.setUpdate_Acc(accNum,trans_bal);
			}
			else
			{
				evil.setSav_Update_Acc((accNum-1),trans_bal);
			}
			
			//gets the updated checking account balance
			
			if(accType.equalsIgnoreCase("c"))
			{
				String sql = "Select bal from acc_Info where acc_Num = " + accNum;
				ResultSet rs= null;
				try {
					rs= evil.getFromDB(sql);
					rs.next();
					finalBal= rs.getDouble("bal");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				String sql = "Select savings_bal from acc_Info where Savings_Num = " + accNum;
				ResultSet rs= null;
				try {
					rs= evil.getFromDB(sql);
					rs.next();
					finalBal= rs.getDouble("savings_bal");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			if(finalBal==0.0)
			{
				System.out.println("The account balance is $0.00, would you like to remove your account? (Y/N)");
				remove= keyboard.next();
				if(remove.equalsIgnoreCase("y"))
				{
					evil.setRemove_Acc(accNum);
				}
				
			}
			System.out.println("Would you like to enter information for another account? (Y/N)");
			start=keyboard.next();
			choice="y";
	}
		System.out.println("Thank you for banking with Evil Corp :)");
		Database.conn.close();
	}
	
}