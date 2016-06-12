/**
 * 
 */
package org.pjaygroup.springtransaction.app1;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.pjaygroup.springtransaction.model.Address;
import org.pjaygroup.springtransaction.model.Customer;
import org.pjaygroup.springtransaction.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	private SessionFactory sessionFactory1;
	private SessionFactory sessionFactory2;
	private final String SELECT_PRODUCTS_HQL = "FROM Products";
	private String productName = "";
	
    public SessionFactory getSessionFactory1() {
		return sessionFactory1;
	}

    @Autowired
    @Qualifier("store_akart_sessionfactory")
	public void setSessionFactory1(SessionFactory sessionFactory1) {
		this.sessionFactory1 = sessionFactory1;
	}

	public SessionFactory getSessionFactory2() {
		return sessionFactory2;
	}

	@Resource
	@Qualifier("store_xkart_sessionfactory")
	public void setSessionFactory2(SessionFactory sessionFactory2) {
		this.sessionFactory2 = sessionFactory2;
	}

	@Override
    public void tryPrint(){
    	System.out.println("******************************************");
    	System.out.println("store_akart_ds :: " + getSessionFactory1().toString());
    	System.out.println("store_xkart_ds :: " + getSessionFactory2().toString());
    	System.out.println("******************************************");
    }
    
    @Override
    @Transactional(value="jtaTransactionManager",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
    public void addCustomerData() throws Exception{
		try{
			System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
			Customer customer1 = new Customer();/*customer1.setId(1);*/customer1.setName("Vijay");customer1.setNumber(999999999);
			Customer customer2 = new Customer();/*customer2.setId(2);*/customer2.setName("Kumar");customer2.setNumber(888888888);//customer2.setNumber(99999999999) - Compiler error The literal 99999999999 of type int is out of range
			Address address = new Address();/*address.setId(1);address.setCustomer_id(1);*/address.setAddress("FOSTER CITY, SFO, 94404");
			getSessionFactory1().getCurrentSession().save(customer1);
			address.setCustomer_id(customer1.getId());
			getSessionFactory1().getCurrentSession().save(address);
			getSessionFactory1().getCurrentSession().save(customer2);
			//assert false:"Creating Error for rollback of transaction.";
			throw new SimpleRuntimeException("Throwing runtime error for checking rollback of transaction");
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(value="jtaTransactionManager",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
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
		@SuppressWarnings("rawtypes")
		List productsFromDB = getSessionFactory1().getCurrentSession().createQuery(SELECT_PRODUCTS_HQL).list();
		for (Object object : productsFromDB) {
			Products productsTemp = (Products)object;
			productName = (null!=productsTemp.getProduct_name()?productsTemp.getProduct_name().trim():"");
			if(products.get(productName) > productsTemp.getQuantity()){
				System.out.println("Buying from akart Store :: " + productsTemp.getQuantity() + " :: " + productName);
				products.put(productName, products.get(productName) - productsTemp.getQuantity());
				productsTemp.setQuantity(0);
				getSessionFactory1().getCurrentSession().save(productsTemp);
			}else{
				System.out.println("Buying from akart Store :: " + products.get(productName) + " :: " + productName);
				products.put(productName, 0);
				productsTemp.setQuantity(productsTemp.getQuantity() - products.get(productName));
				getSessionFactory1().getCurrentSession().save(productsTemp);
			}
		}
		return products;
	}
	
	private void buyRemainingFromStore_xkart(Map<String, Integer> products) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		@SuppressWarnings("rawtypes")
		List productsFromDB = getSessionFactory2().getCurrentSession().createQuery(SELECT_PRODUCTS_HQL).list();
		String errorMsg = null;
		for (Object object : productsFromDB) {
			Products productsTemp = (Products)object;
			productName = (null!=productsTemp.getProduct_name()?productsTemp.getProduct_name().trim():"");
			if(products.get(productName) > productsTemp.getQuantity()){
				errorMsg = "Cannot Buy from xkart Store as we don't have the quantity you are looking for :: " + products.get(productName) + " :: " + productName;
				System.out.println(errorMsg);
			}else{
				System.out.println("Buying from xkart Store :: " + products.get(productName) + " :: " + productName);
				products.put(productName, 0);
				productsTemp.setQuantity(productsTemp.getQuantity() - products.get(productName));
				getSessionFactory2().getCurrentSession().save(productsTemp);
			}
		}
		if(null != errorMsg){
			throw new LowProductQuantityException(errorMsg);
		}else{
			throw new SimpleRuntimeException("Throwing runtime error for checking rollback of transaction");
		}
	}

}
