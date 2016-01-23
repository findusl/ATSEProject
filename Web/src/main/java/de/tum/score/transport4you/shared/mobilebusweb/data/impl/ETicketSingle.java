package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.Date;

public class ETicketSingle extends ETicket {
	private static final long serialVersionUID = 7076088907132401192L;
	
	public ETicketSingle(ETicketSingleType ticketType, String customerId) {
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
}
