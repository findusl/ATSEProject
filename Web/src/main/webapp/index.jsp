<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType"%>
<%@page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketSeasonType"%>
<%@page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketSingleType"%>
<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.DataManager"%>
<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket"%>
<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketSingle"%>
<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketSeason"%>
	<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.DurationFormatter"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="javax.xml.bind.DatatypeConverter"%>
<%@ page import="java.io.ByteArrayInputStream"%>
<%@ page import="java.io.ObjectInputStream"%>
<%@ page import="java.io.ObjectInput"%>
<%@ page import="com.googlecode.objectify.Key"%>
<%@ page import="com.googlecode.objectify.ObjectifyService"%>
<%@ page import="de.tum.score.transport4you.web.User"%>
<%@ page import="de.tum.score.transport4you.web.Group"%>
<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope"%>
<%@ page
	import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
</head>

<body>
	<div class="container">
		<%
			String title;
			if (session.getAttribute("userId") == null
					&& (request.getParameter("user") == null || request.getParameter("pass") == null)) {
		%>
		<h3>Login</h3>
		<form method="post" action="index.jsp" class="form-horizontal">
			<div class="form-group">
				<label for="inputName" class="col-sm-2 control-label">Username</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="user" id="user" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputPassword" class="col-sm-2 control-label">Password</label>
				<div class="col-sm-10">
					<input type="password" class="form-control" name="pass" id="user" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-default">Sign in</button>
				</div>
			</div>
		</form>
		<%
			//check credentials & log in user if successful
			} else if (session.getAttribute("userId") == null) {
				Key<Group> group = Key.create(Group.class, "default");
				List<User> users = ObjectifyService.ofy().load().type(User.class).ancestor(group).order("-date").list();
				for (User u : users) {
					if (u.name.equals(request.getParameter("user"))
							&& u.password.equals(User.hash(request.getParameter("pass")))) {
						session.setAttribute("userId", u.id);
						session.setAttribute("userName", u.name);
						out.println("<p>Login for " + u.name + " (" + u.password + ") succeeded.</p>");
					}

				}
				// we have autheticated the user 
			}
		%>

		<%
			if (session.getAttribute("userId") != null) {
				out.println("<h2>Hello, " + session.getAttribute("userName") + "!</h2>");
		%>
		<h3>Your Tickets</h3>
		<div class="row">
			<div class="col-sm-6">
				<h4>Single Tickets</h4>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Purchased</th>
							<th>Validity</th>
							<th>Invalidated</th>
						</tr>
					</thead>
					<tbody>
						<%
							DataManager dm = DataManager.getInstance();

							for (ETicket ticket : dm.getETicketsForUser(Long.parseLong(String.valueOf(session.getAttribute("userId"))))) {
								if (ticket instanceof ETicketSingle) {
						%>
						<tr>
							<td><%=ticket.getSellingDate()%></td>
							<td><%=new DurationFormatter(ticket.getValidMinutes())%></td>
							<%
								if (ticket.getInvalidatedAt() == null) {
							%>
								<td><span class="text-success">Still Valid</span></td>
							<%
								} else {
							%>
								<td><span class="text-danger"><%=ticket.getInvalidatedAt()%></span>
							<%
								}
							%>
						</tr>
						<%
								}
							}
						%>
					</tbody>
				</table>
			</div>
			<div class="col-sm-6">
				<h4>Single Tickets</h4>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Purchased</th>
							<th>Validity</th>
							<th>Valid Until</th>
						</tr>
					</thead>
					<tbody>
						<%
							for (ETicket ticket : dm.getETicketsForUser(Long.parseLong(String.valueOf(session.getAttribute("userId"))))) {
								if (ticket instanceof ETicketSeason) {
						%>
						<tr>
							<td><%=ticket.getSellingDate()%></td>
							<td><%=new DurationFormatter(ticket.getValidMinutes())%></td>
							<%
								if (ticket.getValidUntil().before(new java.util.Date())) {
							%>
								<td><span class="text-danger"><%=ticket.getValidUntil()%></span></td>
							<%
								} else {
							%>
								<td><span class="text-success"><%=ticket.getValidUntil()%></span>
							<%
								}
							%>
						</tr>
						<%
								}
							}
						%>
					</tbody>
				</table>
			</div>
		</div>
		
		<hr/>
		
		<h3>Buy Tickets</h3>

		<div class="row">
			<div class="col-sm-6">
				<h4>Single Tickets</h4>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Name</th>
							<th>Amount</th>
							<th>Validity</th>
							<th colspan="2">Price</th>
						</tr>
					</thead>
					<tbody>
						<%
							for (ETicketType type : dm.getETicketTypes()) {
								if (type instanceof ETicketSingleType) {
						%>
						<tr>
							<td><%=type.getName()%></td>
							<td><%=((ETicketSingleType) type).getAmountTickets()%></td>
							<td><%=new DurationFormatter(type.getValidMinutes())%></td>
							<td><%=type.getTotalPrice()%> €</td>
							<td style="width: 1px">
								<form style="margin: 0" method="POST" action="/purchaseTicket">
									<input type="hidden" name="ticketType" value="<%=type.getId()%>" />
									<button type="submit" class="btn btn-primary btn-xs">Buy</button>
								</form>
							</td>
						</tr>
						<%
								}
							}
						%>
					</tbody>
				</table>
			</div>
			<div class="col-sm-6">
				<h4>Season Tickets</h4>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Name</th>
							<th>Validity</th>
							<th colspan="2">Price</th>
						</tr>
					</thead>
					<tbody>
						<%
							for (ETicketType type : dm.getETicketTypes()) {
								if (type instanceof ETicketSeasonType) {
						%>
						<tr>
							<td><%=type.getName()%></td>
							<td><%=new DurationFormatter(type.getValidMinutes())%></td>
							<td><%=type.getTotalPrice()%> €</td>
							<td style="width: 1px">
								<form style="margin: 0" method="POST" action="/purchaseTicket">
									<input type="hidden" name="ticketType" value="<%=type.getId()%>" />
									<button type="submit" class="btn btn-primary btn-xs">Buy</button>
								</form>
							</td>
						</tr>
						<%
								}
							}
						%>
					</tbody>
				</table>
			</div>
		</div>
		<%
			} // end-if user is logged in
		%>
	</div>

</body>
</html>
