import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class EvilCorpApp
{
	
	public static void main(String[] args) throws IOException 
	{
		DecimalFormat format = new DecimalFormat("$#,##0.00;$-#,##0.00");
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Scanner keyboard= new Scanner(System.in);
		HashMap<Long,Double> myMap = new HashMap<Long,Double>();
		HashMap <Long, String> type = new HashMap<Long, String>();
		HashMap<Integer,Double> myFile = new HashMap<Integer,Double>();
		HashMap<Integer,String> myAccNames = new HashMap<Integer,String>();
		
		int accNum = 0, d, m, y, i;
		double bal = 0, transAmount, finAmount=0;
		long time, sum=0;
		boolean promptNum, promptName, promptBal, promptTrans, promptAmount;
		String accName, trans, date, month, day, year, choice="y", start="y", close="n";
		ArrayList<Long> tMS = new ArrayList<Long>();
		ArrayList<String> tTypes = new ArrayList<String>();
		ArrayList<Double> amount = new ArrayList<Double>();
		ArrayList<Integer> charges = new ArrayList<Integer>();
		
		EvilCorp account = new EvilCorp();
		
		System.out.println((System.getProperty("user.dir") + File.separatorChar +"myEC.txt"));
		String filename = (System.getProperty("user.dir") + File.separatorChar +"myEC.txt");
		
		System.out.println(Paths.get("c:\\myfolder\\myEC.txt"));
		PrintWriter writer = new PrintWriter(new File(filename));
		FileWriter fstream  = new FileWriter("myEC.txt"); ; 
		BufferedWriter out = new BufferedWriter(fstream); 	
		
		System.out.println("\nWelcome to Evil Corp Savings and Loan. Please create the user account(s).");
	
	while(start.equalsIgnoreCase("Y"))
	{
		
		do{
			System.out.println("Please enter your account #: "); 
			accNum= keyboard.nextInt();
			promptNum= Validator.getAccNum(accNum); //validates account number input
		}while(promptNum==false);
		
		do{
		System.out.println("Please enter the name of your account: ");
		accName=keyboard.next();
		promptName= Validator.getAccName(accName); //validates name input
		}while(promptNum==false);
		
		do{
		System.out.println("Please enter your account balance: "); 
		bal=keyboard.nextDouble();
		promptBal= Validator.getBal(bal); //validates balance input
		}while(promptBal==false);
		
		while(choice.equalsIgnoreCase("Y"))
		{
			do{
			System.out.println("Enter a transaction type related to this account( C= Check, DC= Debit card, DP= Deposit or W= Withdrawal) or -1 to finish");
			trans=keyboard.next();
			promptTrans= Validator.getTrans(trans); //validates transaction type input
			}while(promptTrans==false);
			
			do{
			System.out.println("Enter the transaction amount:");
			transAmount=keyboard.nextDouble();
			promptAmount= Validator.getAmount(transAmount); //validates transaction amount input
			account.setTrans(transAmount);
			}while(promptAmount==false);
			
			if(trans.equalsIgnoreCase("DP"))
			{
				finAmount=transAmount;
			}
			else
			{
				finAmount=-transAmount;
			}
			System.out.println("Enter the transaction date: ");
			date=keyboard.next();
			account.setDate(date);
						
			time= Long.parseLong(account.getDate());
			
			while(myMap.containsKey(time))
			{
				time++;
			}
			myMap.put(time, finAmount); //adds time and transaction amount to the hashmap
			
			type.put(time, trans); //associates time with transaction type
			tMS.add(time); //adds time to array
			Collections.sort(tMS); //sorts the array by date
			
			System.out.println("Would you like to enter another transaction? (Y/N) ");
			choice= keyboard.next();
		}
		
		for(i=0; i< myMap.size() ; i++)
		{
			sum+=myMap.get(tMS.get(i));
			tTypes.add(type.get(tMS.get(i)));
			
			if ((bal+sum <0))
			{
				sum-=35;//adds a 35 dollar charge each time a withdrawal occurs when the balance is below 0
				charges.add(-35);
			}
			else
			{
				charges.add(0);
			}
			
		}
		
		if((bal+sum)==0) //asks user if they want to close their account when the balance is 0
		{
			System.out.println("Your current account balance is $0.00. Would you like to close this account? (Y/N)");
			close= keyboard.next();
			if(close.equalsIgnoreCase("y"))
			{
				myMap.remove(accNum);
				System.out.println("Account number "+ accNum + " has been closed. Thank you for your business!");
			}
			else
			{
				writer.println(myFile.put(accNum, (bal+sum))); //writes to the file everytime, overriding an account if it is present
				myAccNames.put(accNum, accName);
			}
		}
		writer.println(myFile.put(accNum, (bal+sum))); //writes to the file everytime, overriding an account if it is present
		myAccNames.put(accNum, accName);
		
		System.out.println("\nYour starting balance is: "+ format.format(bal) + "\n");
		System.out.println("Transaction Type   Date of Transaction    Transaction Amount  Additional Charges");
		System.out.println("--------------------------------------------------------------------------------");
		for (int k = 0; k< myMap.size();k++)
		{
			System.out.println(String.format("%8s",type.get(tMS.get(k)).toUpperCase())+ String.format("%25s",formatter.format(tMS.get(k)))+ String.format("%22s",format.format(myMap.get(tMS.get(k)))) +  String.format("%18s",format.format(charges.get(k))));
		}
		System.out.println("");
		System.out.println("The balance for account number " + accNum + " is: "+ format.format(bal+sum));
		
		
		
		System.out.println("\nWould you like to enter transactions for a different account? (Y/N)");
		start=keyboard.next();
		
		if(start.equalsIgnoreCase("y"))
		{
			choice="y";
			System.out.println("");
			
			//resetting all variables for a new account
			accNum=0;
			sum=0;
			bal=0;
			myMap.clear();
			type.clear();
			tMS.clear();
			charges.clear();
		}
		else
		{
			System.out.println("Thank you for working with Evil Corp, see you soon!");
		}
		
		}
	writer.close(); //closes the PrintWriter
	
	Iterator<Entry<Integer, Double>> it = myFile.entrySet().iterator(); //iterates through the hashmap to print
	Iterator<Entry<Integer, String>> it2= myAccNames.entrySet().iterator();
	while (it.hasNext())
	{	
		Map.Entry<Integer, String> names = it2.next();
		Map.Entry<Integer, Double> values = it.next(); //gets the keys and values at each point in the hashmap
		out.write("Account Name: " + names.getValue() + "     Account Number: "+values.getKey() + "      Balance: $" +values.getValue() + "\n"); //writes each line of the hashmap to the file
	}
	out.close(); // closes the BufferedWriter
	
	}
}
