package de.tum.score.transport4you.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

import com.googlecode.objectify.ObjectifyService;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.DataManager;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

import com.googlecode.objectify.Key;



public class UsersResource extends ServerResource {

    @Get
    public String represent() throws Exception {
		Key<Group> group = Key.create(Group.class, "default");
		List<User> users = ObjectifyService.ofy()
		.load()
		.type(User.class)
		.ancestor(group)
		.order("-date") 
		.list();

		String result = "<users>\n";
		for (User u : users) {
			// find tickets for the user
			List<ETicket> tickets = DataManager.getInstance().getETicketsForUser(u.id);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			
			// create the encrypted version for each ticket and save it to output
			for (ETicket ticket : tickets) {
				ticket.encryptTicket();
				oos.writeObject(ticket);
			}
			
			String ticketsBase64 = DatatypeConverter.printBase64Binary(baos.toByteArray());
			
			result += "<user>\n";
			result += "\t<id>"		+ u.id 		+ "</id>\n";
			result += "\t<name>"	+ u.name 	+ "</name>\n";
			result += "\t<password>"	+ u.password 	+ "</password>\n";
			result += "\t<email>"	+ u.email 	+ "</email>\n";
			result += "\t<ticket>"	+ ticketsBase64	+ "</ticket>\n";
			result += "</user>\n";
		}
		result += "</users>";
        return result;
    }

}
