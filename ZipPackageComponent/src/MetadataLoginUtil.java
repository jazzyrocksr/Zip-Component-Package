import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
/**
* Login utility.
*/
public class MetadataLoginUtil {
	
	public static MetadataConnection login() throws ConnectionException, IOException {

		final LoginResult loginResult = loginToSalesforce(PackageInstall.userName, PackageInstall.password, PackageInstall.apiURL);
		return createMetadataConnection(loginResult);
	}
	
	private static MetadataConnection createMetadataConnection(final LoginResult loginResult) throws ConnectionException {
		final ConnectorConfig config = new ConnectorConfig();
		config.setServiceEndpoint(loginResult.getMetadataServerUrl());
		config.setSessionId(loginResult.getSessionId());
		return new MetadataConnection(config);
	}
	private static LoginResult loginToSalesforce(final String username, final String password,final String loginUrl) throws ConnectionException {
		final ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(loginUrl);
		config.setServiceEndpoint(loginUrl);
		config.setManualLogin(true);
		return (new EnterpriseConnection(config)).login(username, password);
	}
}