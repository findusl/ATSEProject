<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="de.tum.score.transport4you.web.Group"%>
<%@ page import="de.tum.score.transport4you.web.User"%>
<%@ page import="com.googlecode.objectify.Key"%>
<%@ page import="com.googlecode.objectify.ObjectifyService"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
</head>

<body>

	<%
		Key<Group> group = Key.create(Group.class, "default");
		List<User> users = ObjectifyService.ofy().load().type(User.class) // We want only Greetings
				.ancestor(group) // Anyone in this book
				.order("-date") // Most recent first - date is indexed.
				//	.limit(5)             // Only show 5 of them.
				.list();

		if (users.isEmpty()) {
	%>
	<p>No Users.</p>
	<%
		} else {
	%>
	<h2>Users:</h2>
	<ul>
		<%
			}
			// Look at all of our greetings
			for (User u : users) {
				pageContext.setAttribute("name", u.name);
		%>
		<li>${fn:escapeXml(name)}</li>
		<%
			}
		%>
	</ul>
	<h2>Add User</h2>
	<form action="/add" method="post">
		<div>
			Name:<input type="text" name="user_name" />
		</div>
		<div>
			E-Mail:<input type="text" name="user_email" />
		</div>
		<div>
			Password:<input type="text" name="user_password" />
		</div>
		<div>
			<input type="submit" value="Add User" />
		</div>
	</form>
</body>
</html>
<%-- //[END all]--%>
