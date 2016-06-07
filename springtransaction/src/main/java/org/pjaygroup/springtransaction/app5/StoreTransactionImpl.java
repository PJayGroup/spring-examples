/**
 * 
 */
package org.pjaygroup.springtransaction.app5;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author Vijay Konduru
 *
 */
@Component("store_bean")
public class StoreTransactionImpl implements StoreTransaction{
	
	private DataSource store_akart_ds;
	private DataSource store_xkart_ds;
	private final String SELECT_PRODUCTS_SQL = "SELECT * FROM products";
	private final String PRODUCT_NAME = "product_name";
	private final String QUANTITY = "quantity";
	private String productName = "";

	public DataSource getStore_akart_ds() {
		return store_akart_ds;
	}

    @Autowired
	public void setStore_akart_ds(DataSource store_akart_ds) {
		this.store_akart_ds = store_akart_ds;
	}

	public DataSource getStore_xkart_ds() {
		return store_xkart_ds;
	}

    @Autowired
	public void setStore_xkart_ds(DataSource store_xkart_ds) {
		this.store_xkart_ds = store_xkart_ds;
	}
    
    @Override
    public void tryPrint(){
    	System.out.println("******************************************");
    	System.out.println("store_akart_ds :: " + getStore_akart_ds().toString());
    	System.out.println("store_xkart_ds :: " + getStore_xkart_ds().toString());
    	System.out.println("******************************************");
    }
    
    /**
     * 
     * http://docs.spring.io/autorepo/docs/spring/4.2.x/spring-framework-reference/html/transaction.html
     * http://stackoverflow.com/questions/3037006/starting-new-transaction-in-spring-bean
     * 
	 * Spring recommends that you only annotate concrete classes (and methods of
	 * concrete classes) with the @Transactional annotation, as opposed to
	 * annotating interfaces. You certainly can place the @Transactional
	 * annotation on an interface (or an interface method), but this works only
	 * as you would expect it to if you are using interface-based proxies. The
	 * fact that Java annotations are not inherited from interfaces means that
	 * if you are using class-based proxies ( proxy-target-class="true") or the
	 * weaving-based aspect ( mode="aspectj"), then the transaction settings are
	 * not recognized by the proxying and weaving infrastructure, and the object
	 * will not be wrapped in a transactional proxy, which would be decidedly
	 * bad.
	 */
    
    @Override
    @Transactional(value="store_akart_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
    public void addCustomerData() throws Exception{
		try{
			System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
			JdbcTemplate jdbcTemplate = new JdbcTemplate(getStore_akart_ds());
			jdbcTemplate.update("INSERT INTO customer (id, name, number) VALUES (358,'Vijay',999999)");
			//jdbcTemplate.update("INSERT INTO customer (id, name, number) VALUES (525,'Vijay',99999999999)");//This fails to insert due size of data more than column size
			jdbcTemplate.update("INSERT INTO address (id, customer_id, address) VALUES (143,358,'FOSTER CITY, SFO, 94404')");
			jdbcTemplate.update("INSERT INTO customer (id, name, number) VALUES (525,'Vijay',99999999999)");
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(value="store_akart_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	//Above annotation works fine but trying to directly on main method will detach them as nested transactions and will commit them individually
	//Example "buyFromStore_akart()" method goes with one transaction where as other with new transaction, when other fails, there is no chance to 
	//revert "buyFromStore_akart()" method transaction. As it is spinning its own transaction boundary and commits on its method success 
	public void purchaseProduct(Map<String, Integer> products) throws Exception{
		if(null != products){
			try {
				Map<String, Integer> remainingQuantity = buyFromStore_akart(products);
				buyRemainingFromStore_xkart(remainingQuantity);
			} catch (Exception e) {
				throw e;
			}
		}
	}

	//@Transactional(value="store_akart_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	//This annotation detaches it from nested transaction boundary and will commit within its method boundary. So above is wrong to use in nested trasaction
	private Map<String, Integer> buyFromStore_akart(Map<String, Integer> products) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		JdbcTemplate jdbcTemplate_akart = new JdbcTemplate(getStore_akart_ds());
		List<Map<String, Object>> rs = jdbcTemplate_akart.queryForList(SELECT_PRODUCTS_SQL);
		for (Map<String, Object> map : rs) {
			productName = (null!=map.get(PRODUCT_NAME)?((String)map.get(PRODUCT_NAME)).trim():"");
			if(products.get(productName) > (Integer)map.get(QUANTITY)){
				System.out.println("Buying from akart Store :: " + map.get(QUANTITY) + " :: " + productName);
				jdbcTemplate_akart.update("UPDATE products SET quantity = 0 WHERE " + PRODUCT_NAME + " = '" + productName +"'");
				products.put( productName, products.get(productName) - (Integer)map.get(QUANTITY));
			} else {
				System.out.println("Buying from akart Store :: " + products.get(productName) + " :: " + productName);
				jdbcTemplate_akart.update("UPDATE products SET quantity = " + ((Integer)map.get(QUANTITY) - products.get(productName)) + " WHERE " + PRODUCT_NAME + " = '" + productName +"'");
				products.put(productName, 0);
			}
		}
		//jdbcTemplate_akart.update("INSERT INTO products (id, product_name, quantity) VALUES (3, 'Xperia Z5', 99999999999)");
		return products;
	}

	/**
	 * 
	 * http://docs.spring.io/autorepo/docs/spring/4.2.x/spring-framework-
	 * reference/html/transaction.html
	 * http://stackoverflow.com/questions/13883966/is-exception-handling-
	 * required-in-spring-transaction
	 * 
	 * Transaction timeout defaults to the default timeout of the underlying
	 * transaction system, or none if timeouts are not supported. Any
	 * RuntimeException triggers rollback, and any checked Exception does not
	 * 
	 * In its default configuration, the Spring Framework's transaction
	 * infrastructure code only marks a transaction for rollback in the case of
	 * runtime, unchecked exceptions; that is, when the thrown exception is an
	 * instance or subclass of RuntimeException. (Errors will also - by default
	 * - result in a rollback). Checked exceptions that are thrown from a
	 * transactional method do not result in rollback in the default
	 * configuration
	 * 
	 * You set rollbackFor = {Throwable.class} or some kind of exceptions you
	 * like, now Spring will rollback for any Exception / Error. By default,
	 * whether we like it or not, Spring will rollback only for
	 * RuintimeException, and commit otherwise
	 * 
	 * https://docs.oracle.com/cd/E19683-01/806-7930/6jgp65ikq/index.html
	 * http://stackoverflow.com/questions/5509082/eclipse-enable-assertions
	 * https://www.catalysts.cc/en/wissenswertes/spring-transactional-rollback-on-checked-exceptions/
	 */
	
	@Transactional(value="store_xkart_txm_qual",propagation=Propagation.REQUIRES_NEW,isolation=Isolation.READ_COMMITTED,timeout=5)
	private void buyRemainingFromStore_xkart(Map<String, Integer> products) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		JdbcTemplate jdbcTemplate_xkart = new JdbcTemplate(getStore_xkart_ds());
		List<Map<String, Object>> rs = jdbcTemplate_xkart.queryForList(SELECT_PRODUCTS_SQL);
		for (Map<String, Object> map : rs) {
			productName = (null!=map.get(PRODUCT_NAME)?((String)map.get(PRODUCT_NAME)).trim():"");
			if(products.get(productName) > (Integer)map.get(QUANTITY)){
				System.out.println("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//throw new Exception("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//throw new SQLException("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//assert false:("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				// Putting some wrong statement for throwing "AssertionError" which is type Error
				//assert 5>=6:("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//Example VM args in my local for eclipse: "-javaagent:C:/Users/krishna/.m2/repository/org/springframework/spring-instrument/4.2.6.RELEASE/spring-instrument-4.2.6.RELEASE.jar -ea"
				//Rollback is happening for Error thrown - java.lang.AssertionError
			} else {
				System.out.println("Buying from xkart Store :: " + products.get(productName) + " :: " + productName);
				jdbcTemplate_xkart.update("UPDATE products SET quantity = " + ((Integer)map.get(QUANTITY) - products.get(productName)) + " WHERE " + PRODUCT_NAME + " = '" + productName +"'");
				products.put(productName, 0);
			}
		}
		// Let's run a wrong query for creating an exception
		//jdbcTemplate_xkart.update("UPDATE products SET quantity = 99999999999" + " WHERE " + PRODUCT_NAME + " = 'Xperia Z5'");
		jdbcTemplate_xkart.update("INSERT INTO products (id, product_name, quantity) VALUES (3, 'Xperia Z5', 99999999999)");
	}

}
