package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DataManager {
	private static DataManager instance;
	
	private EntityManagerFactory factory;
	private EntityManager entityManager;
	
	private DataManager() {
		this.factory = Persistence.createEntityManagerFactory("T4Y_BUS");
		this.entityManager = factory.createEntityManager();
	}
	
	public static synchronized DataManager getInstance() {
		if (DataManager.instance == null) {
			DataManager.instance = new DataManager();
		}
		return DataManager.instance;
	}
	
	/*
	 * Public DataManager API
	 */
	@SuppressWarnings("unchecked")
	public List<ETicketType> getETicketTypes() {
		try {
			this.entityManager.getTransaction().begin();
		
			Query query = this.entityManager.createQuery("SELECT t FROM ETicketType t");
			
			this.entityManager.flush();
			this.entityManager.getTransaction().commit();
			
			return query.getResultList();
		} catch (Exception e) {
			System.err.println("Error fetching all eticket types: " + e.getMessage());
			e.printStackTrace();
		}
		return new ArrayList<ETicketType>();
	}
	
	@SuppressWarnings("unchecked")
	public List<ETicket> getETicketsForUser(long userId) {
		System.out.println("Get tickets for user " + userId);
		
		return this.entityManager.createQuery("SELECT t FROM ETicket t WHERE t.customerId = :customerId")
			.setParameter("customerId", userId)
			.getResultList();
	}

	public void purchaseETicket(long ticketTypeId, long userId) {
		System.out.println("Purchase ticket type " + ticketTypeId + " for user " + userId);
		
		this.entityManager.getTransaction().begin();
		
		// get ticket from database
		ETicketType ticketType = this.entityManager.find(ETicketType.class, ticketTypeId);
		
		List<ETicket> tickets = ticketType.createTickets(userId);
		
		System.out.println("Generated " + tickets.size() + " tickets.");
		
		for (ETicket ticket : tickets) {
			this.entityManager.persist(ticket);
		}
		
		this.entityManager.flush();
		this.entityManager.getTransaction().commit();
	}
}
