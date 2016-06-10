/**
 * 
 */
package org.pjaygroup.springtransaction.app12;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.pjaygroup.springtransaction.model.Account;
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
@Component("bank_bean")
public class BankTransactionImpl implements BankTransaction{
	
	private SessionFactory sessionFactory1;
	private SessionFactory sessionFactory2;
	private final String SELECT_HQL = "FROM Account";
	
    public SessionFactory getSessionFactory1() {
		return sessionFactory1;
	}
	
    @Resource
    @Qualifier("bank_abc_sessionfactory")
	public void setSessionFactory1(SessionFactory sessionFactory1) {
		this.sessionFactory1 = sessionFactory1;
	}

	public SessionFactory getSessionFactory2() {
		return sessionFactory2;
	}

	@Inject
	@Qualifier("bank_xyz_sessionfactory")
	public void setSessionFactory2(SessionFactory sessionFactory2) {
		this.sessionFactory2 = sessionFactory2;
	}

	@Override
    public void tryPrint(){
    	System.out.println("******************************************");
    	System.out.println("bank_abc_ds :: " + getSessionFactory1().toString());
    	System.out.println("bank_xyz_ds :: " + getSessionFactory2().toString());
    	System.out.println("******************************************");
    }
    
    @Override
	public void selectData() {
		try {
			selectFromBank_abc();
			selectFromBank_xyz();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional(value="bank_abc_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	public void selectFromBank_xyz() throws Exception{
		System.out.println(" :: From bank bank_abc :: ");
		try {
			@SuppressWarnings("rawtypes")
			List accounts = getSessionFactory1().getCurrentSession().createQuery(SELECT_HQL).list();
			for (Object object : accounts) {
				System.out.println(" :: Account details :: " + object);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		}
	}

	@Transactional(value="bank_xyz_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	public void selectFromBank_abc() throws Exception{
		System.out.println(" :: From bank bank_xyz :: ");
		try {
			@SuppressWarnings("rawtypes")
			List accounts = getSessionFactory2().getCurrentSession().createQuery(SELECT_HQL).list();
			for (Object object : accounts) {
				System.out.println(" :: Account details :: " + object);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		}
	}

	@Override
	@Transactional(value="bank_abc_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	public void transferFunds(int accNumber1, int accNumber2, int amount) throws Exception{
		try {
			this.withdrawAmount(accNumber1,amount);
			this.depositAmount(accNumber2,amount);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void withdrawAmount(int accNumber1, int amount) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		try {
			Account account = getSessionFactory1().getCurrentSession().get(Account.class, accNumber1);
			if(account.getAcc_balance() < amount){
				throw new LowBalanceException("Balance is not sufficient for completing this transaction");
			}else{
				account.setAcc_balance((account.getAcc_balance() - amount));
				getSessionFactory1().getCurrentSession().update(account);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(value="bank_xyz_txm_qual",propagation=Propagation.REQUIRES_NEW,isolation=Isolation.READ_COMMITTED,timeout=5)
	private void depositAmount(int accNumber2, int amount) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		try {
			Account account = getSessionFactory2().getCurrentSession().get(Account.class, accNumber2);
			account.setAcc_balance(account.getAcc_balance() + amount);
			getSessionFactory1().getCurrentSession().update(account);
			// Just Throwing exception to rollback the transaction
			throw new LowBalanceException("Creating Low Balance Exception for simulaton of transaction failure");
		} catch (Exception e) {
			throw e;
		}
	}

}
