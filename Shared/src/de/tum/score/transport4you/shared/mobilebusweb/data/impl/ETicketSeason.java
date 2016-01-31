package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

	@Override
	public byte[] toBytes() {		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			// header for season ticket
			dos.writeBytes("T4YA");
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
}