package org.openstack.model.identity.keystone;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.openstack.model.identity.User;
import org.openstack.model.identity.UserList;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.NONE)
public class KeystoneUserList implements Serializable, UserList {

	@XmlElement(name = "user", type = KeystoneUser.class)
	@JsonDeserialize(as=List.class, contentAs=KeystoneUser.class)
	private List<User> users;

	/* (non-Javadoc)
	 * @see org.openstack.model.identity.keystone.UserList#getList()
	 */
	@Override
	public List<User> getList() {
		return (List<User>) (List<?>) users;
	}

	public void setList(List<User> list) {
		this.users = list;
	}

	@Override
	public String toString() {
		return "KeyStoneUserList [users=" + users + "]";
	}

}
