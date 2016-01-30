package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

	@Override
	public byte[] toBytes() {		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			// header for single ticket
			dos.writeBytes("T4YB");
			// customer ID
			dos.writeLong(this.getCustomerId());
			// selling date
			dos.writeLong(this.getSellingDate().getTime());
			// valid minutes
			dos.writeLong(this.getValidMinutes());
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("SeasonTicket in bytes: " + baos.toByteArray() + " (" + baos.size() + ")");
		return baos.toByteArray();
	}
	
	public static void main(String[] args) {
		DataManager dm = DataManager.getInstance();
		
		System.out.println("Proudly presenting: ETickets!");
		for (ETicket ticket : dm.getETicketsForUser(5629499534213120L)) {
			System.out.println(ticket);
			ticket.toBytes();
		}
	}
}
