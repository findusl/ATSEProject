package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Represents an ETicket Type
 * @author hoerning
 *
 */
@Entity
public abstract class ETicketType extends AbstractPersistenceObject{
	private static final long serialVersionUID = 8280256021201295627L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	protected String name;
	
	/**
	 * Indicates the number of minutes this ticket is valid, beginning at validated
	 */
	protected int validMinutes;
	
	protected Double price;
	
	protected ETicketType(String name, int validMinutes) {
		assert validMinutes > 0;
		
		this.name = name;
		this.validMinutes = validMinutes;
	}
	
	abstract public List<ETicket> createTickets(String customerId);
	
	abstract public double getTotalPrice();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValidMinutes() {
		return validMinutes;
	}

	public void setValidMinutes(int validMinutes) {
		this.validMinutes = validMinutes;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getPersistenceId() {
		return this.id;
	}
	
}
