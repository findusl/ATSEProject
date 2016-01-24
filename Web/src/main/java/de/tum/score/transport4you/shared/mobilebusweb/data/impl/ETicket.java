package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents an ETicket
 * @author hoerning
 */
@Entity
public abstract class ETicket extends AbstractPersistenceObject {
	private static final long serialVersionUID = 4865268647836014207L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	protected long id;
	
	protected String customerId;
	
	protected int validMinutes;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	protected Date invalidatedAt;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	protected Date sellingDate;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	protected ETicketType ticketType;
	
	/**
	 * The constructor ensures that the selling date is always set on creation.
	 */
	protected ETicket() {
		this.sellingDate = new Date();
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isInvalidated() {
		return this.getInvalidatedAt() != null;
	}

	public long getValidMinutes() {
		return validMinutes;
	}

	public abstract Date getValidUntil();

	public Date getSellingDate() {
		return sellingDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getInvalidatedAt() {
		return invalidatedAt;
	}

	protected void setInvalidatedAt(Date invalidatedAt) {
		this.invalidatedAt = invalidatedAt;
	}
	
	/**
	 * Invalidates this ETicket by setting the <code>invalidatedAt</code> date to the current date and time.
	 * 
	 * @return true if successfully invalidated or false if already invalidated
	 */
	public boolean invalidate() {
		if (!this.isInvalidated()) {
			// set invalidation date to now
			this.setInvalidatedAt(new Date());
			
			// indicate successful invalidation
			return true;
		}
		return false;
	}
	
	public boolean equals(Object o)  {
		
		if(!(o instanceof ETicket)){
			return false;
		}
		
		ETicket toCheck = (ETicket) o;
		if(toCheck.id == this.id) {
			return true;
		}
		
		return false;
	}

	@Override
	public long getPersistenceId() {
		return this.id;
	}

}
