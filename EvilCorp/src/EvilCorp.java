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
	int accNum, d, m, y, i, num;
	double bal, transAmount, finAmount=0;
	String time;
	long sum=0;
	String  trans, date, month, day, year, choice="y", start="y";
	
	public EvilCorp()
	{
		
	}

	public int getNum() 
	{
		return accNum;
	}

	public void setNum(int num) 
	{
		this.num = num;
	}

	public double getTrans() 
	{
		return bal;
	}

	public void setTrans(double bal) 
	{
		this.bal = bal;
	}


	public String getDate() 
	{
		String str=new String(date);
		month=str.substring(0,2);
		day= str.substring(3,5);
		year= str.substring(6,10);
		
		d=Integer.parseInt(day);
		m=Integer.parseInt(month);
		y=Integer.parseInt(year);
		
		Calendar cal = new GregorianCalendar(y,m-1,d);
		time= Long.toString(cal.getTime().getTime()); //gets time in ms of the date
		return time;
	}

	public void setDate(String date) 
	{
		this.date = date;
	}
	
	
		
}
	
