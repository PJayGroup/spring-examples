/**
 * 
 */
package org.pjaygroup.springtransaction.app4;

import java.util.Map;

/**
 * @author Vijay Konduru
 *
 */
public interface StoreTransaction1 {
	void purchaseProduct(Map<String, Integer> products) throws Exception;
	void addCustomerData() throws Exception;
	void tryPrint();
}
