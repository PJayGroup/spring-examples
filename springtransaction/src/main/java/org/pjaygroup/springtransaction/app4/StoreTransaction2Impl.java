/**
 * 
 */
package org.pjaygroup.springtransaction.app4;

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
@Component("store_bean_2")
public class StoreTransaction2Impl implements StoreTransaction2{
	
//	private DataSource store_akart_ds;
	private DataSource store_xkart_ds;
	private final String SELECT_PRODUCTS_SQL = "SELECT * FROM products";
	private final String PRODUCT_NAME = "product_name";
	private final String QUANTITY = "quantity";
	private String productName = "";

	public DataSource getStore_xkart_ds() {
		return store_xkart_ds;
	}

    @Autowired
	public void setStore_xkart_ds(DataSource store_xkart_ds) {
		this.store_xkart_ds = store_xkart_ds;
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
	@Transactional(value="store_xkart_txm_qual",propagation=Propagation.REQUIRES_NEW,isolation=Isolation.READ_COMMITTED,timeout=5)
	public void buyRemainingFromAnotherStore(Map<String, Integer> products) throws Exception {
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		JdbcTemplate jdbcTemplate_xkart = new JdbcTemplate(getStore_xkart_ds());
		List<Map<String, Object>> rs = jdbcTemplate_xkart.queryForList(SELECT_PRODUCTS_SQL);
		for (Map<String, Object> map : rs) {
			productName = (null!=map.get(PRODUCT_NAME)?((String)map.get(PRODUCT_NAME)).trim():"");
			if(products.get(productName) > (Integer)map.get(QUANTITY)){
				System.out.println("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
				//throw new Exception("Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName);
			} else {
				System.out.println("Buying from xkart Store :: " + products.get(productName) + " :: " + productName);
				jdbcTemplate_xkart.update("UPDATE products SET quantity = " + ((Integer)map.get(QUANTITY) - products.get(productName)) + " WHERE " + PRODUCT_NAME + " = '" + productName +"'");
				products.put(productName, 0);
			}
		}
		// Let's run a wrong query for creating an exception
		jdbcTemplate_xkart.update("UPDATE products SET quantity = 99999999999" + " WHERE " + PRODUCT_NAME + " = 'Xperia Z5'");
		//jdbcTemplate_xkart.update("INSERT INTO products (id, product_name, quantity) VALUES (3, 'Xperia Z5', 99999999999)");
	}

}
