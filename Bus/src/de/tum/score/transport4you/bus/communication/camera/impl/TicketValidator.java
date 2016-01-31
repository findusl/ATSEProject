package de.tum.score.transport4you.bus.communication.camera.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.EncryptionManager;

public class TicketValidator {

	private static final String USED_TICKETS_FILE = "used_tickets.txt";

	private boolean singleTicket, isValid;
	private int ticketId, startDate, endDate;

	public TicketValidator(byte[] encrypted) {
		byte[] cleartext = decrypt(encrypted);

		ByteBuffer bb = ByteBuffer.wrap(cleartext);
		switch (bb.getInt()) {
		case 0x54345941:
			singleTicket = false;
			break;
		case 0x54345942:
			singleTicket = true;
			break;
		default:
			isValid = false;
			System.out.println("Ticket header invalid");
			return;
		}
		isValid = true;
		ticketId = bb.getInt();
		startDate = bb.getInt();
		endDate = bb.getInt();
		System.out
				.println("Single ticket: " + singleTicket + " ticket id "
						+ ticketId + " start date " + startDate + " enddate "
						+ endDate);
		System.out.println();
	}

	private byte[] decrypt(byte[] encrypted) {
		try {
			EncryptionManager m = new EncryptionManager();
			return m.decryptSequence(encrypted);
		} catch (Exception e) {
			System.err
					.println("Could not decrypt the message from the qr code.");
			e.printStackTrace();
			return encrypted;
		}
	}

	public boolean isValid() {
		if (!isValid)
			return false;
		long time = System.currentTimeMillis();
		time = time / 1000;
		return startDate < time && endDate > time;
	}

	public boolean invalidateTicket() {
		if (!singleTicket)
			return true;
		File f = new File(USED_TICKETS_FILE);
		try (FileWriter fw = new FileWriter(f, true)) {
			fw.write(Integer.toString(ticketId) + " used at "
					+ System.currentTimeMillis() + ",\n");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
