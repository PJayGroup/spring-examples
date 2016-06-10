/**
 * 
 */
package org.pjaygroup.springtransaction.app10;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp10 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app10.xml");
        BankTransaction bankTransaction = ctx.getBean("bank_bean", BankTransaction.class);
        bankTransaction.tryPrint();
        System.out.println("\n");
        bankTransaction.selectData();
        try {
			bankTransaction.transferFunds(100, 101, 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Important Links:
	 * 
	 * https://www.atomikos.com/Documentation/SpringIntegration
	 * http://stackoverflow.com/questions/10212274/two-phase-commit-2pc-configuration-with-atomikos
	 * http://stackoverflow.com/questions/28765339/configure-an-atomikos-usertransactionmanager-for-hibernate-in-spring-batch
	 * https://www.atomikos.com/Documentation/HibernateIntegration
	 * https://www.javacodegeeks.com/2013/07/spring-jta-multiple-resource-transactions-in-tomcat-with-atomikos-example.html
	 * 
	 */
	
	/**
	 * Error:
	 * 
	 * org.springframework.transaction.InvalidIsolationLevelException:
	 * JtaTransactionManager does not support custom isolation levels by default
	 * - switch 'allowCustomIsolationLevels' to 'true'
	 * 
	 * Solution:
	 * http://stackoverflow.com/questions/3917496/how-to-enable-custom-isolation-levels-for-a-jta-transaction-manager-in-spring
	 * Added: <property name="allowCustomIsolationLevels" value="true"/>
	 * Please use this flag carefully as some DB's with Application servers do not support changing isolation levels
	 */

	/**
	 * Example for my local while running through eclipse use VM args as
	 * "-javaagent:C:/Users/vijayk/.m2/repository/org/springframework/spring-instrument/4.2.6.RELEASE/spring-instrument-4.2.6.RELEASE.jar"
	 * 
	 * http://www.springbyexample.org/examples/aspectj-ltw.html
	 */
	
	/**
	 * Solving Below Issues:
	 * 
	 * [AppClassLoader@35d22ddb] warning javax.* types are not being woven
	 * because the weaver option '-Xset:weaveJavaxPackages=true' has not been
	 * specified No org.slf4j.impl.StaticLoggerBinder found in ClassPath, trying
	 * with log4j... No org.apache.logging.log4j.Logger found found in
	 * ClassPath, trying with log4j... No org.apache.log4j.Logger found found in
	 * ClassPath, falling back default...
	 * 
	 * Added required dependency jars of logging and ignoring the warning about '-Xset:weaveJavaxPackages=true'
	 * Added "com.atomikos.icatch.registered=true" property in jta.properties -- to remove atomikos registration waning
	 * 
	 * http://stackoverflow.com/questions/25487116/log4j2-configuration-no-log4j2-configuration-file-found
	 * http://stackoverflow.com/questions/27994264/log4j2-disable-no-log4j2-configuration-file-found-print-when-configuring-pr
	 */

}
