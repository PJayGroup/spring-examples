/**
 * 
 */
package org.pjaygroup.springtransaction.app2;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp2 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app2.xml");
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
	 * Example for my local while running through eclipse use VM args as
	 * "-javaagent:C:/Users/vijayk/.m2/repository/org/springframework/spring-instrument/4.0.2.RELEASE/spring-instrument-4.0.2.RELEASE.jar"
	 * 
	 * http://www.springbyexample.org/examples/aspectj-ltw.html
	 */
	
	/**
	 * Important Links:
	 * 
	 * https://www.atomikos.com/Documentation/SpringIntegration
	 * https://www.atomikos.com/Main/InstallingTransactionsEssentials -- download code to understand integration for diff usecases
	 * http://stackoverflow.com/questions/10212274/two-phase-commit-2pc-configuration-with-atomikos
	 * http://stackoverflow.com/questions/28765339/configure-an-atomikos-usertransactionmanager-for-hibernate-in-spring-batch
	 * https://www.atomikos.com/Documentation/HibernateIntegration
	 * https://www.javacodegeeks.com/2013/07/spring-jta-multiple-resource-transactions-in-tomcat-with-atomikos-example.html
	 * http://vkuzel.blogspot.in/2016/03/spring-boot-jpa-hibernate-atomikos.html
	 * 
	 * http://stackoverflow.com/questions/18832889/spring-transactions-and-hibernate-current-session-context-class
	 * http://stackoverflow.com/questions/13087928/spring-hibernate-transaction-management
	 * http://docs.spring.io/autorepo/docs/spring/4.2.x/spring-framework-reference/html/transaction.html
	 * http://blogs.sourceallies.com/2011/08/spring-injection-with-resource-and-autowired/
	 * 
	 * WARN: HHH000008: JTASessionContext being used with JDBCTransactionFactory; auto-flush will not operate correctly with getCurrentSession()
	 * 
	 * http://stackoverflow.com/questions/14456622/jtasessioncontext-being-used-with-jdbctransactionfactory-auto-flush-will-not-op
	 * 
	 * Works, but not confident if things are done correctly
	 * 
	 * Known Problems:
	 * http://stackoverflow.com/questions/36912251/atomikos-exception-when-transaction-contains-more-than-one-persist
	 * https://www.atomikos.com/Documentation/KnownProblems
	 * https://www.atomikos.com/Documentation/KnownProblems#MySQL_XA_bug
	 */
}
