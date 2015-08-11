import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator 
{
	public static boolean getAccName(String prompt)
	{
		String name = "[a-zA-Z]+";
		Pattern pattern = Pattern.compile(name);
		Matcher match = pattern.matcher(prompt);
	    return match.matches();
	    
	}

	public static boolean getBal(double prompt)
	{
	    int i = 0;
	    boolean isValid = false;
	    while (isValid == false)
	    {
	        if (prompt<0.0)
	        {
	            System.out.println( "Error! Invalid balance value. Try again.");
	        	break;
	        }
	        else
	        	isValid= true;
	    }
		return isValid;
	
	}

	public static boolean getAccNum(int prompt)
	{
	    int i = 0;
	    boolean isValid = false;
	    while (isValid == false)
	    {
	    	if (prompt<=0)
	    	{
	            System.out.println( "Error! Invalid Account Number. Try again.");
	    		break;
	    	}
	    	else
	    		isValid = true;
	    }
	    return isValid;
	}

	public static boolean getTrans(String prompt)
	{
	    boolean isValid = false;
	    while (isValid == false)
	    {
		    if (prompt.equalsIgnoreCase("c") || prompt.equalsIgnoreCase("dp") || prompt.equalsIgnoreCase("w") || prompt.equalsIgnoreCase("d") || prompt.equalsIgnoreCase("dc"))
		    {
		    	isValid = true;
		    }
	        else
	        {
	        	System.out.println("Error! Invalid Transaction Type. Try again.");
	        	break;
	        }
	       
	    }
	    return isValid;
	}

	public static boolean getAmount(double prompt)
	{
	    double d = 0;
	    boolean isValid = false;
	    while (isValid == false)
	    {
	    	if (prompt<=0.0)
	    	{
	            System.out.println("Please enter a transaction amount that is greater than $0.00.");
	            break;
	    	}
	        else
	        	isValid = true;
	        
	    }
	    return isValid;
	}
}
