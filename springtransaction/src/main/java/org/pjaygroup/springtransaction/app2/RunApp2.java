/**
 * 
 */
package org.pjaygroup.springtransaction.app2;

import java.util.Map;

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
        
//        Map<String, Integer> products = new HashMap<>();
//        products.put("iPhone 7s", 11);
//        products.put("Samsung Galaxy S7 Edge", 5);
//        storeTransaction.purchaseProduct(products);
	}
	
	/**
	 * IMP: Some Url's used for finding solutions in creating this project
	 * 
		http://crunchify.com/how-to-import-all-spring-mvc-dependencies-to-your-maven-project/
		
		https://github.com/spring-guides
		https://maven.apache.org/pom.html
		https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html
		http://docs.spring.io/autorepo/docs/spring/4.2.x/spring-framework-reference/html/transaction.html
		http://www.springframework.org/schema/
		https://commons.apache.org/proper/commons-dbcp/configuration.html
		https://commons.apache.org/proper/commons-dbcp/apidocs/org/apache/commons/dbcp2/BasicDataSource.html
		
		http://stackoverflow.com/questions/25412778/setup-spring-bean-configuration-for-dbcp2-gives-poolableconnectionfactory-not-l
		http://stackoverflow.com/questions/23038261/multiple-datasources-with-multiple-transaction-managers-in-spring
		http://stackoverflow.com/questions/4423125/spring-is-it-possible-to-use-multiple-transaction-managers-in-the-same-applica
		http://www.tutorialspoint.com/spring/spring_qualifier_annotation.htm
		http://www.mkyong.com/spring/spring-autowiring-qualifier-example/
		http://stackoverflow.com/questions/1961566/how-to-configure-transaction-management-for-working-with-2-different-db-in-sprin
		
		http://blogs.sourceallies.com/2011/08/spring-injection-with-resource-and-autowired/
		http://stackoverflow.com/questions/16603750/very-simple-spring-transactions-of-jdbc-not-roll-back-even-log-said-yes
		
		http://stackoverflow.com/questions/13948921/can-create-hashmap-with-spring-but-cant-create-map
		http://stackoverflow.com/questions/19461452/spring-injecting-a-hashmapclass-listx-from-xml
		http://stackoverflow.com/questions/19070138/creating-hashmap-in-spring
		
		http://forum.spring.io/forum/spring-projects/data/48667-how-does-one-start-tomcat-with-spring-agent
		http://stackoverflow.com/questions/6225682/passing-jvm-arguments-to-tomcat-when-running-as-a-service
		http://stackoverflow.com/questions/5045608/proper-usage-of-java-d-command-line-parameters
		https://docs.newrelic.com/docs/agents/java-agent/frameworks/tomcat-installation-java
		http://stackoverflow.com/questions/6697063/adding-javaagent-to-tomcat-6-server-where-do-i-put-itand-in-what-format
		http://www.coderanch.com/t/84391/Tomcat/Launching-tomcat-additional-VM-parameters
		http://stackoverflow.com/questions/5891123/specifying-jvm-arguments-when-calling-a-jar-file
		
		Important: (Read them properly to understand better)
		
		http://www.springbyexample.org/examples/aspectj-ltw.html
		http://docs.spring.io/autorepo/docs/spring/4.2.x/spring-framework-reference/html/transaction.html
		http://stackoverflow.com/questions/3037006/starting-new-transaction-in-spring-bean
		http://howtodoinjava.com/spring/spring-transaction/spring-transactions-on-non-public-methods-with-load-time-weaving/
		http://blog.timmlinder.com/2011/01/spring-transactional-checking-for-transaction-support-and-local-method-calls/
		https://www.credera.com/blog/technology-insights/java/common-oversights-utilizing-nested-transactions-spring/
		http://stackoverflow.com/questions/5232624/nested-transaction-on-spring
		http://docs.spring.io/spring/docs/2.5.6/reference/transaction.html#transaction-declarative-annotations
		
	 * 
	 */
	
	
	/**
		WARNING: Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'org.springframework.context.weaving.AspectJWeavingEnabler#0': Initialization of bean failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'loadTimeWeaver': Initialization of bean failed; nested exception is java.lang.IllegalStateException: ClassLoader [sun.misc.Launcher$AppClassLoader] does NOT provide an 'addTransformer(ClassFileTransformer)' method. Specify a custom LoadTimeWeaver or start your Java virtual machine with Spring's agent: -javaagent:org.springframework.instrument.jar
		Exception in thread "main" org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'org.springframework.context.weaving.AspectJWeavingEnabler#0': Initialization of bean failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'loadTimeWeaver': Initialization of bean failed; nested exception is java.lang.IllegalStateException: ClassLoader [sun.misc.Launcher$AppClassLoader] does NOT provide an 'addTransformer(ClassFileTransformer)' method. Specify a custom LoadTimeWeaver or start your Java virtual machine with Spring's agent: -javaagent:org.springframework.instrument.jar
	 */

}
