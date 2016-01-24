package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.List;
import java.util.LinkedList;
import javax.persistence.Entity;

@Entity
public class ETicketSingleType extends ETicketType {
	private static final long serialVersionUID = -3660552449625301723L;

	/**
	 * Single Tickets may come in batches of multiple tickets, this is the number of tickets in one batch.
	 */
	private int amountTickets;
	
	/**
	 * Since buying multiple tickets is cheaper, this defines a factor to calculate a discount on the total price.
	 * 
	 * Should be 0 <= <code>discountFactor</code> <= 1. Lower value means lower end price.
	 */
	private Double discountFactor;
	
	protected ETicketSingleType() {}
	
	public ETicketSingleType(String name, int validMinutes) {
		super(name, validMinutes);
		
		this.amountTickets = 1;
		this.discountFactor = 1.0;
	}
	
	public ETicketSingleType(String name, int validMinutes, int amountTickets) {
		this(name, validMinutes);
		assert amountTickets > 0;
		
		this.amountTickets = amountTickets;
	}
	
	public ETicketSingleType(String name, int validMinutes, int amountTickets, Double discountFactor) {
		this(name, validMinutes, amountTickets);
		assert discountFactor <= 1.0;
		assert discountFactor >= 0.0;
		
		this.discountFactor = discountFactor;
	}
	
	/**
	 * Calculates the total price of a ticket packet using the number of tickets contained and a discount factor;
	 * 
	 * @return total price of this ticket packet
	 */
	@Override
	public double getTotalPrice() {
		return this.getPrice() * this.getAmountTickets() * this.getDiscountFactor();
	}
	
	public int getAmountTickets() {
		return amountTickets;
	}

	public void setAmountTickets(int amountTickets) {
		this.amountTickets = amountTickets;
	}

	public Double getDiscountFactor() {
		return discountFactor;
	}

	public void setDiscountFactor(Double discountFactor) {
		this.discountFactor = discountFactor;
	}

	@Override
	public List<ETicket> createTickets(String customerId) {
		LinkedList<ETicket> tickets = new LinkedList<>();
		
		// generate amountTickets many single tickets
		for (int i = 0; i < this.getAmountTickets(); i++) {
			ETicketSingle ticket = new ETicketSingle(this, customerId);
			
			tickets.add(ticket);
		}
		
		return tickets;
	}
	
	@Override
	public String toString() {
		return "ETicketSingleType["+ 
				"validMinutes=" + this.getValidMinutes() + "," +
				"amount=" + this.getAmountTickets() + "," +
				"discountFactor=" + this.getDiscountFactor() +
				"]";
	}
}
