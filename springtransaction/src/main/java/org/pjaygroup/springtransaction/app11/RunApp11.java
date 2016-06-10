/**
 * 
 */
package org.pjaygroup.springtransaction.app11;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp11 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app11.xml");

		StoreTransaction storeTransaction = ctx.getBean("store_bean", StoreTransaction.class);
		@SuppressWarnings("unchecked")
		Map<String, Integer> products = ctx.getBean("products", Map.class);
		System.out.println("\n");
		storeTransaction.tryPrint();
		try {
			storeTransaction.addCustomerData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			storeTransaction.purchaseProduct(products);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Example for my local while running through eclipse use VM args as
	 * "-javaagent:C:/Users/vijayk/.m2/repository/org/springframework/spring-instrument/4.2.6.RELEASE/spring-instrument-4.2.6.RELEASE.jar"
	 * 
	 * http://www.springbyexample.org/examples/aspectj-ltw.html
	 */
	
	/**
	 * Important Links:
	 * 
	 * https://www.atomikos.com/Documentation/SpringIntegration
	 * http://stackoverflow.com/questions/10212274/two-phase-commit-2pc-configuration-with-atomikos
	 * http://stackoverflow.com/questions/28765339/configure-an-atomikos-usertransactionmanager-for-hibernate-in-spring-batch
	 * https://www.atomikos.com/Documentation/HibernateIntegration
	 * https://www.javacodegeeks.com/2013/07/spring-jta-multiple-resource-transactions-in-tomcat-with-atomikos-example.html
	 * 
	 * http://stackoverflow.com/questions/18832889/spring-transactions-and-hibernate-current-session-context-class
	 * http://stackoverflow.com/questions/13087928/spring-hibernate-transaction-management
	 * http://docs.spring.io/autorepo/docs/spring/4.2.x/spring-framework-reference/html/transaction.html
	 * http://howtodoinjava.com/spring/spring-orm/spring-3-and-hibernate-integration-tutorial-with-example/
	 * http://blogs.sourceallies.com/2011/08/spring-injection-with-resource-and-autowired/
	 */

}
