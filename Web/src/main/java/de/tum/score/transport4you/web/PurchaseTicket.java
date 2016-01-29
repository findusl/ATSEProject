package de.tum.score.transport4you.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.DataManager;

public class PurchaseTicket extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		long ticketTypeId = Long.parseLong(req.getParameter("ticketType"));
		
		long userId = (long)req.getSession().getAttribute("userId");
		
		DataManager dm = DataManager.getInstance();
		dm.purchaseETicket(ticketTypeId, userId);
		
		resp.sendRedirect("/index.jsp");
	}
}
