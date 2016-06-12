/**
 * 
 */
package org.pjaygroup.springtransaction.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Vijay Konduru
 *
 */
@Entity
@Table(name="account")
public class Account {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int acc_number;
	private String customer_name;
	private int acc_balance;

	public int getAcc_number() {
		return acc_number;
	}

	public void setAcc_number(int acc_number) {
		this.acc_number = acc_number;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public int getAcc_balance() {
		return acc_balance;
	}

	public void setAcc_balance(int acc_balance) {
		this.acc_balance = acc_balance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acc_balance;
		result = prime * result + acc_number;
		result = prime * result + ((customer_name == null) ? 0 : customer_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (acc_balance != other.acc_balance)
			return false;
		if (acc_number != other.acc_number)
			return false;
		if (customer_name == null) {
			if (other.customer_name != null)
				return false;
		} else if (!customer_name.equals(other.customer_name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [acc_number=" + acc_number + ", customer_name=" + customer_name + ", acc_balance=" + acc_balance
				+ "]";
	}

}
