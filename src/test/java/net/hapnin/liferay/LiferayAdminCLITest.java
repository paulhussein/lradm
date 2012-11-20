package net.hapnin.liferay;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LiferayAdminCLITest {
	
	LiferayAdminCLI lradmin;
	String args[];
	String result;
	
	private static final String DefaultCompanyID = "10136";
	
	  @BeforeClass
	  public void setup() {
		  lradmin = new LiferayAdminCLI();
	  }
	
//	  @Test
//	  public void companyget1() {  
//		  args[0] = "companyget";
//		  Assert.assertTrue(lradmin.go(args).equalsIgnoreCase(DefaultCompanyID));		  
//	  }
//	  @Test
//	  public void companyget2() {  
//		  args[0] = "companyget -vh localhost";
//		  Assert.assertTrue(lradmin.go(args).equalsIgnoreCase(DefaultCompanyID));		  
//	  }
//	  @Test
//	  public void companyget3() {  
//		  args[0] = "companyget -vh localhost -server localhost";
//		  Assert.assertTrue(lradmin.go(args).equalsIgnoreCase(DefaultCompanyID));		  
//	  }
	  @Test
	  public void companyget4() {  
		  args = new String[5];
		  args[0] = "companyget";
		  args[1] = "-vh";
		  args[2] = "localhost";
		  args[4] = "-server";
		  args[4] = "llbdev22";
		  result = lradmin.go(args);
		  System.out.println ( "Result : " + result );
		  Assert.assertTrue(result.equalsIgnoreCase(DefaultCompanyID));		  
	  }
	  @Test
	  public void AddUser() {
	  }
}
