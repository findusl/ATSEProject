package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ETicketSeason extends ETicket {
	private static final long serialVersionUID = 3943512294028102303L;

	protected ETicketSeason() {}
	
	public ETicketSeason(ETicketSeasonType ticketType, long customerId) {
		this.ticketType = ticketType;
		this.customerId = customerId;
		this.validMinutes = ticketType.getValidMinutes();
	}

	@Override
	public Date getValidUntil() {
		final long MINUTE_IN_MILLIS = 60 * 1000;
		long validMillis = this.getValidMinutes() * MINUTE_IN_MILLIS;
		
		return new Date(this.getSellingDate().getTime() + validMillis);
	}

}
