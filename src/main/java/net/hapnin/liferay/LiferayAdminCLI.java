package net.hapnin.liferay;

import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.*;
import com.liferay.client.soap.portal.model.CompanySoap;
import com.liferay.client.soap.portal.model.UserSoap;
import com.liferay.client.soap.portal.service.ServiceContext;
import com.liferay.client.soap.portal.service.http.CompanyServiceSoap;
import com.liferay.client.soap.portal.service.http.CompanyServiceSoapServiceLocator;
import com.liferay.client.soap.portal.service.http.UserServiceSoap;
import com.liferay.client.soap.portal.service.http.UserServiceSoapServiceLocator;

public class LiferayAdminCLI {

	static Logger log = LoggerFactory.getLogger(LiferayAdminCLI.class);

	private static URL _getURL(String server, Integer port, String remoteUser,
			String password, String serviceName, boolean authenicate)
			throws Exception {
		String url = "";
		// Unauthenticated url
		url = "http://" + server +":" + port +"/tunnel-web/axis/" + serviceName;

		// Authenticated url
		if (authenicate) {
			url = "http://" + remoteUser + ":" + password + "@" + server +":" + port +"/tunnel-web/secure/axis/" + serviceName;
		}
		log.debug("URL : " + url);		
		return new URL(url);
	}

	private String companyGet(String server, Integer port, String remoteUser,
			String password, String virtualhost) {
		String company = "";
		try {
			// Locate the Company
			CompanyServiceSoapServiceLocator locatorCompany = new CompanyServiceSoapServiceLocator();

			CompanyServiceSoap soapCompany = locatorCompany
					.getPortal_CompanyService(_getURL(server, port, remoteUser,
							password, "Portal_CompanyService", true));

			// Grab default company
			CompanySoap companySoap = soapCompany
					.getCompanyByVirtualHost(virtualhost);
			company = "" + companySoap.getCompanyId();
		} catch (Exception e) {
			log.error(e.toString());
		}
		log.error("Company" + company);
		return company;
	}

	private class ServerDelegate {
		@Parameter(names = "-vh", description = "The virtual host e.g. localhost")
		private String virtualhost = "localhost";
		@Parameter(names = "-server")
		private String server = "localhost";
		@Parameter(names = "-port")
		private int port = 8080;
		@Parameter(names = "-user")
		private String user = "test";
		@Parameter(names = "-password")
		private String password = "test";
		@Parameter(names = "-https")
		private Boolean https = false;
	}

	@Parameters(separators = "=", commandDescription = "Get company from virtualhost")
	private class CommandCompanyGet {

		@ParametersDelegate
		private ServerDelegate serverDelegate = new ServerDelegate();

		public String commandCompanyGet() {
			return companyGet(serverDelegate.server,
					serverDelegate.port, serverDelegate.user,
					serverDelegate.password, "" + serverDelegate.virtualhost);

		}
	}
	@Parameters(separators = "=", commandDescription = "Add User")
	private class CommandUserAdd {

		@Parameter(names = "-screenname")
		private String screenname;
		@Parameter(names = "-email")
		private String email;
		
		@ParametersDelegate
		private ServerDelegate serverDelegate = new ServerDelegate();

		public String commandUserAdd() {
			String result = null;
			long companyid;
			try {
				UserServiceSoapServiceLocator locator = new UserServiceSoapServiceLocator();
				UserServiceSoap userService = locator.getPortal_UserService(_getURL(serverDelegate.server,
						serverDelegate.port, serverDelegate.user,
						serverDelegate.password, "Portal_CompanyService", true));
				ServiceContext serviceContext = new ServiceContext();
				companyid = Long.parseLong(companyGet(serverDelegate.server,
						serverDelegate.port, serverDelegate.user,
						serverDelegate.password, "" + serverDelegate.virtualhost));
				result = userService.getUserByEmailAddress(companyid, "test@liferay.com").getFirstName();
						//UserSoap usoap = userService.addUser(companyid, false, "password", "password", false, "screenName", "email@liferay.com", 0, "", "en_US", "firstname", "middlename", "lastname", 0, 0, true, 1, 1, 2012, "", null, null, null, null, false, serviceContext);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				log.error(e.toString());
				result = e.getMessage();
			}
			return result;

		}
	}

	public String go(String[] args) {
		String result = null;
		try {
			JCommander jc = new JCommander();
			CommandCompanyGet companyGet = new CommandCompanyGet();
			jc.addCommand("companyget", companyGet);
			CommandUserAdd userAdd = new CommandUserAdd ();
			jc.addCommand("useradd", userAdd);
			jc.parse(args);
			
			if (args.length > 0 )
			{
			if (args[0].equalsIgnoreCase("companyGet"))
				result =  companyGet.commandCompanyGet();
			if (args[0].equalsIgnoreCase("useradd"))
				result = userAdd.commandUserAdd();
			}
			else
			{
				jc.usage();
			}
					
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			result = e.getMessage();
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println ( new LiferayAdminCLI().go(args) );
	}

}
