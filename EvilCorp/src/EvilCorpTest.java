import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class EvilCorpTest 
{

	   @Test
	   public void test_returnDollar() 
	   {
	      System.out.println("Test if pricePerMonth returns Dollars...") ;
	      EvilCorp S = new EvilCorp() ;
	      
	      assertTrue(S.getDate()==null) ; 
	   }

}
