/**
 * 
 */
package org.pjaygroup.springtransaction.app3;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp3 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app3.xml");

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
}
