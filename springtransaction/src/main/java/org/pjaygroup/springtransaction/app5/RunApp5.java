/**
 * 
 */
package org.pjaygroup.springtransaction.app5;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vijay Konduru
 *
 */
public class RunApp5 {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-app5.xml");

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
	 * WARNING: Exception encountered during context initialization - cancelling
	 * refresh attempt: org.springframework.beans.factory.BeanCreationException:
	 * Error creating bean with name
	 * 'org.springframework.context.weaving.AspectJWeavingEnabler#0':
	 * Initialization of bean failed; nested exception is
	 * org.springframework.beans.factory.BeanCreationException: Error creating
	 * bean with name 'loadTimeWeaver': Initialization of bean failed; nested
	 * exception is java.lang.IllegalStateException: ClassLoader
	 * [sun.misc.Launcher$AppClassLoader] does NOT provide an
	 * 'addTransformer(ClassFileTransformer)' method. Specify a custom
	 * LoadTimeWeaver or start your Java virtual machine with Spring's agent:
	 * -javaagent:org.springframework.instrument.jar Exception in thread "main"
	 * org.springframework.beans.factory.BeanCreationException: Error creating
	 * bean with name
	 * 'org.springframework.context.weaving.AspectJWeavingEnabler#0':
	 * Initialization of bean failed; nested exception is
	 * org.springframework.beans.factory.BeanCreationException: Error creating
	 * bean with name 'loadTimeWeaver': Initialization of bean failed; nested
	 * exception is java.lang.IllegalStateException: ClassLoader
	 * [sun.misc.Launcher$AppClassLoader] does NOT provide an
	 * 'addTransformer(ClassFileTransformer)' method. Specify a custom
	 * LoadTimeWeaver or start your Java virtual machine with Spring's agent:
	 * -javaagent:org.springframework.instrument.jar
	 * 
	 * Example for my local while running through eclipse use VM args as
	 * "-javaagent:C:/Users/krishna/.m2/repository/org/springframework/spring-instrument/4.2.6.RELEASE/spring-instrument-4.2.6.RELEASE.jar"
	 * 
	 * http://www.springbyexample.org/examples/aspectj-ltw.html
	 */

}
