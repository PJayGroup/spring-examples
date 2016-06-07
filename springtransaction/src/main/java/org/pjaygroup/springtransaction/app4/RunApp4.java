/**
 * 
 */
package org.pjaygroup.springtransaction.app4;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp4 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app4.xml");

		StoreTransaction1 storeTransaction1 = ctx.getBean("store_bean_1", StoreTransaction1.class);
		@SuppressWarnings("unchecked")
		Map<String, Integer> products = ctx.getBean("products", Map.class);
		System.out.println("\n");
		storeTransaction1.tryPrint();
		try {
			storeTransaction1.addCustomerData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			storeTransaction1.purchaseProduct(products);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Important:
		// Looks like a bad design for separating nested transactions into two separate interfaces to make it work. Also need to use public methods for transactional methods 
		// Later examples we will consider using AspectJ recommended by spring for more flexibility when using transactions with spring
		// Till we only did some kind of rough work, for reaching the right strategy.

	}
}
