/**
 * 
 */
package org.pjaygroup.springtransaction.app6;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp6 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app6.xml");
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
	 * "-javaagent:C:/Users/krishna/.m2/repository/org/springframework/spring-instrument/4.2.6.RELEASE/spring-instrument-4.2.6.RELEASE.jar"
	 * 
	 * http://www.springbyexample.org/examples/aspectj-ltw.html
	 */
}
