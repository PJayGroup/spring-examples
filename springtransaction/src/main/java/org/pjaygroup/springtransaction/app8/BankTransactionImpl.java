/**
 * 
 */
package org.pjaygroup.springtransaction.app8;

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
@Component("bank_bean")
public class BankTransactionImpl implements BankTransaction{

	private DataSource bank_abc_ds;
	private DataSource bank_xyz_ds;
	private final String SELECT_SQL = "SELECT * FROM account";
	private final String SELECT_ACC_BALANCE_SQL = "SELECT acc_balance FROM account WHERE acc_number = ";
	private final String ACC_BALANCE_UPDATE_SQL = "UPDATE account SET acc_balance = ";
	private final String ACC_BALANCE_HERE_CONDITION = " WHERE acc_number = ";
//	private final String ACC_NUMBER = "acc_number";
//	private final String CUSTOMER_NAME = "customer_name";
	private final String ACC_BALANCE = "acc_balance";
	

	public DataSource getBank_abc_ds() {
		return bank_abc_ds;
	}

    @Autowired
	public void setBank_abc_ds(DataSource bank_abc_ds) {
		this.bank_abc_ds = bank_abc_ds;
	}

	public DataSource getBank_xyz_ds() {
		return bank_xyz_ds;
	}

    @Autowired
	public void setBank_xyz_ds(DataSource bank_xyz_ds) {
		this.bank_xyz_ds = bank_xyz_ds;
	}
    
    @Override
    public void tryPrint(){
    	System.out.println("******************************************");
    	System.out.println("bank_abc_ds :: " + getBank_abc_ds().toString());
    	System.out.println("bank_xyz_ds :: " + getBank_xyz_ds().toString());
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getBank_abc_ds());
		try {
			List<Map<String, Object>> rs = jdbcTemplate.queryForList(SELECT_SQL);
			for (Map<String, Object> map : rs) {
				for (String key : map.keySet()) {
					System.out.println(" :: key :: " + key + " :: value :: " + map.get(key));
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		}
	}

	@Transactional(value="bank_xyz_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	public void selectFromBank_abc() throws Exception{
		System.out.println(" :: From bank bank_xyz :: ");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getBank_xyz_ds());
		try {
			//Thread.sleep(6000);// Doesn't work as this code is not managed by spring container, to check time out have a debug point and delay the flow more than required time
			List<Map<String, Object>> rs = jdbcTemplate.queryForList(SELECT_SQL);
			for (Map<String, Object> map : rs) {
				for (String key : map.keySet()) {
					System.out.println(" :: key :: " + key + " :: value :: " + map.get(key));
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		}
	}

	@Override
	//@Transactional //Cannot use it here as there two transaction managers for different databases.
	//This should be ideally used for Local Transactions and not for Global Transactions
	@Transactional(value="bank_abc_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	public void transferFunds(int accNumber1, int accNumber2, int amount) throws Exception{
		try {
			this.withdrawAmount(accNumber1,amount);
			this.depositAmount(accNumber2,amount);
		} catch (Exception e) {
			throw e;
		}
	}
	
	//@Transactional(value="bank_abc_txm_qual",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=5)
	//@Transactional(value="bank_abc_txm",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=2)
	//Even bean name or id also works without qualifier
	private void withdrawAmount(int accNumber1, int amount) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		JdbcTemplate jdbcTemplate_abc = new JdbcTemplate(getBank_abc_ds());
		try {
			List<Map<String, Object>> rs = jdbcTemplate_abc.queryForList(SELECT_ACC_BALANCE_SQL + accNumber1);
			for(Map<String, Object> map : rs){
				if((Integer)map.get(ACC_BALANCE) < amount){
					throw new LowBalanceException("Balance is not sufficient for completing this transaction");
				}else{
					jdbcTemplate_abc.update(ACC_BALANCE_UPDATE_SQL + ((Integer)map.get(ACC_BALANCE) - amount) + ACC_BALANCE_HERE_CONDITION + accNumber1);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(value="bank_xyz_txm_qual",propagation=Propagation.REQUIRES_NEW,isolation=Isolation.READ_COMMITTED,timeout=5)
	//@Transactional(value="bank_xyz_txm",propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,timeout=2)
	//Even bean name or id also works without qualifier
	private void depositAmount(int accNumber2, int amount) throws Exception{
		System.out.println(" :: TransactionSynchronizationManager.isActualTransactionActive() :: " + TransactionSynchronizationManager.isActualTransactionActive());
		JdbcTemplate jdbcTemplate_xyz = new JdbcTemplate(getBank_xyz_ds());
		try {
			List<Map<String, Object>> rs = jdbcTemplate_xyz.queryForList(SELECT_ACC_BALANCE_SQL + accNumber2);
			for(Map<String, Object> map : rs){
				jdbcTemplate_xyz.update(ACC_BALANCE_UPDATE_SQL + ((Integer)map.get(ACC_BALANCE) + amount) + ACC_BALANCE_HERE_CONDITION + accNumber2);
			}
			// Just Throwing exception to rollback the transaction
			throw new LowBalanceException("Creating Low Balance Exception for simulaton of transaction failure");
		} catch (Exception e) {
			throw e;
		}
	}

}
