/**
 * 
 */
package org.pjaygroup.springtransaction.app9;

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
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
    //@Transactional(value="jtaTransactionManager",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
    //As "jtaTransactionManager" is the default transaction manager in "tx:annotation-driven" tag, no need to mention in above annotation, no harm mentioning also
    public void addCustomerData() throws Exception{
		try{
			System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
			JdbcTemplate jdbcTemplate = new JdbcTemplate(getStore_akart_ds());
			jdbcTemplate.update("INSERT INTO customer (id, name, number) VALUES (141,'Vijay',999999)");
			//jdbcTemplate.update("INSERT INTO customer (id, name, number) VALUES (142,'Vijay',99999999999)");//This fails to insert due size of data more than column size
			jdbcTemplate.update("INSERT INTO address (id, customer_id, address) VALUES (143,141,'FOSTER CITY, SFO, 94404')");
			jdbcTemplate.update("INSERT INTO customer (id, name, number) VALUES (142,'Vijay',99999999999)");
		} catch (Exception e) {
			throw e;
		}
	}
    
	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
    //@Transactional(value="jtaTransactionManager",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
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
	
	private void buyRemainingFromStore_xkart(Map<String, Integer> products) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		JdbcTemplate jdbcTemplate_xkart = new JdbcTemplate(getStore_xkart_ds());
		List<Map<String, Object>> rs = jdbcTemplate_xkart.queryForList(SELECT_PRODUCTS_SQL);
		for (Map<String, Object> map : rs) {
			productName = (null!=map.get(PRODUCT_NAME)?((String)map.get(PRODUCT_NAME)).trim():"");
			if(products.get(productName) > (Integer)map.get(QUANTITY)){
				System.out.println("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//assert false:("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//Putting some wrong statement for throwing "AssertionError" which is type Error
				//assert 5>=6:("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//Example VM args in my local for eclipse: "-javaagent:C:/Users/vijayk/.m2/repository/org/springframework/spring-instrument/4.2.6.RELEASE/spring-instrument-4.2.6.RELEASE.jar -ea"
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
