import java.util.Scanner;
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

public class EvilCorp 
{

	public static void main(String[] args) 
	{
		DecimalFormat format = new DecimalFormat("$#,##0.00;$-#,##0.00");
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Scanner keyboard= new Scanner(System.in);
		HashMap<Long,Double> myMap = new HashMap<Long,Double>();
		HashMap <Long, String> type = new HashMap<Long, String>();
		
		int accNum, d, m, y, i;
		double bal, transAmount, finAmount=0;
		long time, sum=0;
		String accName, trans, date, month, day, year, choice="y", start="y";
		ArrayList<Long> tMS = new ArrayList<Long>();
		ArrayList<String> tTypes = new ArrayList<String>();
		ArrayList<Double> amount = new ArrayList<Double>();
		ArrayList<Integer> charges = new ArrayList<Integer>();
	
		Map<Long,String> typeMap = new TreeMap<Long,String>(type);
		
		System.out.println("Welcome to Evil Corp Savings and Loan. Please create the user account(s).");
	
	while(start.equalsIgnoreCase("Y"))
	{
		System.out.println("Please enter your account #: "); 
		accNum= keyboard.nextInt();
		System.out.println("Please enter the name of your account: ");
		accName=keyboard.next();
		System.out.println("Please enter your account balance: "); 
		bal=keyboard.nextDouble();

	
		while(choice.equalsIgnoreCase("Y"))
		{
			
			System.out.println("Enter a transaction type related to this account( C= Check, DC= Debit card, DP= Deposit or W= Withdrawal) or -1 to finish");
			trans=keyboard.next();
			System.out.println("Enter the transaction amount:");
			transAmount=keyboard.nextDouble();
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
			
			//breaks up the string into day month and year
			String str=new String(date);
			month=str.substring(0,2);
			day= str.substring(3,5);
			year= str.substring(6,10);
			
			d=Integer.parseInt(day);
			m=Integer.parseInt(month);
			y=Integer.parseInt(year);
			
			Calendar cal = new GregorianCalendar(y,m-1,d);
			time= cal.getTime().getTime(); //gets time in ms of the date
			
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
		}
		else
		{
			System.out.println("Thank you for working with evil corp");
		}
		
		}
	}
}
