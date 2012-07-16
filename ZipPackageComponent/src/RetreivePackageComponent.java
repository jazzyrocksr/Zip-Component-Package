
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.sforce.soap.metadata.AsyncRequestState;
import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.RetrieveMessage;
import com.sforce.soap.metadata.RetrieveRequest;
import com.sforce.soap.metadata.RetrieveResult;


/**
 * Sample that logs in and shows a menu of retrieve and deploy metadata options.
 */ 
    
public class RetreivePackageComponent {

    private MetadataConnection metadataConnection;

    // one second in milliseconds 
    
    private static final long ONE_SECOND = 1000;

    // maximum number of attempts to deploy the zip file 
    
    private static final int MAX_NUM_POLL_REQUESTS = 50;

    public static void main(String[] args) throws Exception {
    	
        RetreivePackageComponent rpc = new RetreivePackageComponent();
        rpc.run();
        
    }

    public RetreivePackageComponent() {
    }

    public void run() throws Exception {
        this.metadataConnection = MetadataLoginUtil.login();
        retrieveZip();
    }

    /*
    * Print out any errors, if any, related to the deploy.
    * @param result - DeployResult
    */ 
    
    private void retrieveZip() throws Exception {
    	
        RetrieveRequest retrieveRequest = new RetrieveRequest();
        retrieveRequest.setApiVersion(PackageInstall.apiVersion);
        retrieveRequest.setSinglePackage(true);
        String packageName=PackageInstall.pkgName;
        String packageNames[]={packageName};
        retrieveRequest.setPackageNames(packageNames);
        AsyncResult asyncResult = metadataConnection.retrieve(retrieveRequest);
        asyncResult = waitForCompletion(asyncResult);
        RetrieveResult result = metadataConnection.checkRetrieveStatus(asyncResult.getId());

        // Print out any warning messages 
    
        StringBuilder stringBuilder = new StringBuilder();
        if (result.getMessages() != null) {
            for (RetrieveMessage rm : result.getMessages()) {
                stringBuilder.append(rm.getFileName() + " - " + rm.getProblem() + "\n");
            }
        }
        if (stringBuilder.length() > 0) {
            System.out.println("Retrieve warnings:\n" + stringBuilder);
        }

        System.out.println("Writing results to zip file");
        File resultsFile = new File(packageName+".zip");
        FileOutputStream os = new FileOutputStream(resultsFile);

        try {
            os.write(result.getZipFile());
        } finally {
            os.close();
            System.out.println("Retrieving the code done successfuly");
        }
        
    }

    private AsyncResult waitForCompletion(AsyncResult asyncResult) throws Exception {
        int poll = 0;
        long waitTimeMilliSecs = ONE_SECOND;
        while (!asyncResult.isDone()) {
            Thread.sleep(waitTimeMilliSecs);
            // double the wait time for the next iteration 
    

            waitTimeMilliSecs *= 2;
            if (poll++ > MAX_NUM_POLL_REQUESTS) {
                throw new Exception("Request timed out. If this is a large set of metadata components, " +
                        "ensure that MAX_NUM_POLL_REQUESTS is sufficient.");
            }

            asyncResult = metadataConnection.checkStatus(new String[]{asyncResult.getId()})[0];
            System.out.println("Status is: " + asyncResult.getState());
        }

        if (asyncResult.getState() != AsyncRequestState.Completed) {
            throw new Exception(asyncResult.getStatusCode() + " msg: " + asyncResult.getMessage());
        }
        return asyncResult;
    }

    
}