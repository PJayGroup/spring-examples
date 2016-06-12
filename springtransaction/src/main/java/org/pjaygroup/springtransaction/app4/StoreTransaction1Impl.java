/**
 * 
 */
package org.pjaygroup.springtransaction.app4;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Component("store_bean_1")
public class StoreTransaction1Impl implements StoreTransaction1{
	
	private DataSource store_akart_ds;
//	private DataSource store_xkart_ds;
	private final String SELECT_PRODUCTS_SQL = "SELECT * FROM products";
	private final String PRODUCT_NAME = "product_name";
	private final String QUANTITY = "quantity";
	private String productName = "";
	private StoreTransaction2 storeTransaction2;

	public StoreTransaction2 getStoreTransaction2() {
		return storeTransaction2;
	}

	@Autowired
	@Qualifier("store_bean_2")
	public void setStoreTransaction2(StoreTransaction2 storeTransaction2) {
		this.storeTransaction2 = storeTransaction2;
	}

	public DataSource getStore_akart_ds() {
		return store_akart_ds;
	}

    @Autowired
	public void setStore_akart_ds(DataSource store_akart_ds) {
		this.store_akart_ds = store_akart_ds;
	}

    @Override
    public void tryPrint(){
    	System.out.println("******************************************");
    	System.out.println("store_akart_ds :: " + getStore_akart_ds().toString());
//    	System.out.println("store_xkart_ds :: " + getStore_xkart_ds().toString());
    	System.out.println("******************************************");
    }
    
    @Transactional(value="store_akart_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
    @Override
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

    /**
	 * According to
	 * 
	 * http://docs.spring.io/autorepo/docs/spring/4.2.x/spring-framework-
	 * reference/html/transaction.html
	 * http://stackoverflow.com/questions/3037006/starting-new-transaction-in-
	 * spring-bean
	 * 
	 * Method visibility and @Transactional
	 * 
	 * In proxy mode (which is the default), only external method calls coming
	 * in through the proxy are intercepted. This means that self-invocation, in
	 * effect, a method within the target object calling another method of the
	 * target object, will not lead to an actual transaction at runtime even if
	 * the invoked method is marked with @Transactional. Also, the proxy must be
	 * fully initialized to provide the expected behaviour so you should not
	 * rely on this feature in your initialization code, i.e. @PostConstruct.
	 * 
	 * "TransactionInterceptor" is now involved in the code flow You can verify
	 * the print from
	 * "TransactionSynchronizationManager.isActualTransactionActive()". Rollback
	 * of transactions is as expected now. Looks like a bad design. Let move to AspectJ recommended by spring
	 */
	@Override
	@Transactional(value="store_akart_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	public void purchaseProduct(Map<String, Integer> products) throws Exception{
		if(null != products){
			try {
				Map<String, Integer> remainingQuantity = buyFromStore_akart(products);
				//buyRemainingFromStore_xkart(remainingQuantity);
				storeTransaction2.buyRemainingFromAnotherStore(remainingQuantity);
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public Map<String, Integer> buyFromStore_akart(Map<String, Integer> products) throws Exception{
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
		// Let's run a wrong query for creating an exception
		//jdbcTemplate_akart.update("INSERT INTO products (id, product_name, quantity) VALUES (3, 'Xperia Z5', 99999999999)");
		return products;
	}

}
