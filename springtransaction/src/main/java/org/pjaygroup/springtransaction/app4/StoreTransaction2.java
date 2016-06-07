/**
 * 
 */
package org.pjaygroup.springtransaction.app4;

import java.util.Map;

/**
 * @author Vijay Konduru
 *
 */
public interface StoreTransaction2 {
	void buyRemainingFromAnotherStore(Map<String, Integer> products) throws Exception;
}
