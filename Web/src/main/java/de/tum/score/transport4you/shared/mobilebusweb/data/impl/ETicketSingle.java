package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
	
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("T4Y_BUS");
		EntityManager entityManager = factory.createEntityManager();
		
		try {
			entityManager.getTransaction().begin();
			
			// generate a sample type
			//ETicketSingleType type = new ETicketSingleType("60mins 2-for-1", 60, 2, 0.5);
			//entityManager.persist(type);
			
			// query types
			javax.persistence.Query query = entityManager.createQuery("SELECT t FROM ETicketType t");
			@SuppressWarnings("unchecked")
			java.util.List<ETicketType> results = query.getResultList();
			for (ETicketType ticket : results) {
				System.out.println(ticket);
			}
			
			entityManager.flush();
			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			System.err.println("Error while accessing the db: "+e.getMessage());
		}
	}
}
