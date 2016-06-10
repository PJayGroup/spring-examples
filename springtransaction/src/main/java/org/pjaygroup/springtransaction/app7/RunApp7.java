/**
 * 
 */
package org.pjaygroup.springtransaction.app7;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp7 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app7.xml");

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

}
