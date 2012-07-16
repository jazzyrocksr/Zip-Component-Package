Features : 
Command Line using ANT,Java, Selenium automated fetching of Package components by given package link

Pre requisite : Firefox Browser

Configuration : 
1 Unzip the ZipPackageComponent.zip file
2 Edit config.properties for input data. Refer the config.properties file to fill each and every input data

Run : 
Go to command prompt and run following command
	ant RunZipPackage			//This would run the main class file and perform automation as well as fetching of package components
	
To clean the classes run  > ant clean
To build the classes run > ant build-project
to run the main program run-> ant RunZipPackage


Classes Specs : 
1.MetadataLoginUtil.java : This java file is use to login into the destination sf org.
2. PackageInstall.java : This is the Junit test case developed by Selenium framework to automate the package installation process
3. RetreivePackagComponent.java : This java class would fetch the package components and zip it on the root level with the package name.
i.e <Package_Name>.zip
4. RunZipPackage : This is the main class which internally fires the Junit test class

Version 2 : As part of this challenge, there was a requirement to host it on some PAAS solution like Heroku. I am still not clear how would there be a browser support on platform like Heroku as Selenium webdriver needs a browser to run the testcases. But I believe there could be a feasible solution using testingbot.com.

Approach : 
1. Signup the trial version of testingbot.com which would give 100mins of test cases to run remotely on their server.
2. Setup windows environment with Java on testingbot.com
3. Download testingbot.jar from github and put in our local lib directory
4. Download testingbot API key and put it into classpath
5. Now make the changes in the Junit testcase file called PackageInstall.java by replacing the Firefox driver to RemoteWebdriver and the URL given by testingbot along with the API keys received.
6. Push the whole java project on Heroku
7. Run the main java class... Bingo!! we are good to go here.Hopefully, this can be a next challenge :)