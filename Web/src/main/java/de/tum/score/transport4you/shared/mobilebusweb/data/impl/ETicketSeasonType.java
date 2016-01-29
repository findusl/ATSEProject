package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;

/**
 * The <code>ETicketSeasonType</code> contains no specific fields and just exists so that season tickets can be created.
 * 
 * There will always be only one ticket generated which is why the total price simply equals the price of one ticket.
 * 
 * @author Stefan Fochler
 *
 */
@Entity
public class ETicketSeasonType extends ETicketType {
	private static final long serialVersionUID = -8892059607321704059L;
	
	protected ETicketSeasonType() {}

	public ETicketSeasonType(String name, int validMinutes) {
		super(name, validMinutes);
	}
	
	@Override
	public List<ETicket> createTickets(long customerId) {
		LinkedList<ETicket> tickets = new LinkedList<>();
		
		tickets.add(new ETicketSeason(this, customerId));
		
		return tickets;
	}

	@Override
	public double getTotalPrice() {
		return this.getPrice();
	}

}
