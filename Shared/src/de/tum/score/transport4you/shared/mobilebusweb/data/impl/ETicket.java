package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Represents an ETicket
 * 
 * @author hoerning
 */
@Entity
@Cacheable(false)
public abstract class ETicket extends AbstractPersistenceObject implements
		BinaryRepresentation, Cloneable {
	private static final long serialVersionUID = 4865268647836014207L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	protected long id;

	protected long customerId;

	protected int validMinutes;

	@Temporal(value = TemporalType.TIMESTAMP)
	protected Date invalidatedAt;

	@Temporal(value = TemporalType.TIMESTAMP)
	protected Date sellingDate;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	protected ETicketType ticketType;

	@Transient
	protected byte[] encryptedTicket;

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

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Date getInvalidatedAt() {
		return invalidatedAt;
	}

	protected void setInvalidatedAt(Date invalidatedAt) {
		this.invalidatedAt = invalidatedAt;
	}

	/**
	 * Invalidates this ETicket by setting the <code>invalidatedAt</code> date
	 * to the current date and time.
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

	@Override
	public boolean equals(Object o) {

		if (!(o instanceof ETicket)) {
			return false;
		}

		ETicket toCheck = (ETicket) o;
		if (toCheck.id == this.id) {
			return true;
		}

		return false;
	}

	public void encryptTicket() {
		EncryptionManager em;
		try {
			em = new EncryptionManager();
			this.encryptedTicket = em.encryptSequence(this.toBytes());
		} catch (Exception e) {
			System.err.println("Error encrypting ticket!");
			e.printStackTrace();
		}
	}

	public byte[] getEncryptedTicket() {
		return encryptedTicket;
	}

	@Override
	public long getPersistenceId() {
		return this.id;
	}

	@Override
	public ETicket clone() {
		try {
			return (ETicket) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(
					"Should not happen according to the java api, as long as ETicket implements Cloneable.",
					e);
		}
	}
}
