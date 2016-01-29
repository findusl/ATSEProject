package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Entity
public class ETicketSingle extends ETicket {
	private static final long serialVersionUID = 7076088907132401192L;
	
	protected ETicketSingle() {}
	
	public ETicketSingle(ETicketSingleType ticketType, long customerId) {
		this.ticketType = ticketType;
		this.customerId = customerId;
		this.validMinutes = ticketType.getValidMinutes();
	}

	@Override
	public Date getValidUntil() {
		final long MINUTE_IN_MILLIS = 60 * 1000;
		
		if (this.isInvalidated()) {
			// invalidated tickets are valid for `validMinutes` after invalidation date
			long validMillis = this.getValidMinutes() * MINUTE_IN_MILLIS;
			return new Date(this.getInvalidatedAt().getTime() + validMillis);
		} else {
			// not-invalidated tickets have no expiration date
			return new Date(Long.MAX_VALUE);
		}
	}
	
	public static void main(String[] args) {
		DataManager dm = DataManager.getInstance();
		
		System.out.println("Proudly presenting: ETicketTypes!");
		for (ETicketType type : dm.getETicketTypes()) {
			System.out.println(type);
		}
	}
}
