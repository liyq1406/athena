/**
 * 
 */
package com.athena.authority;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class AthenaRealmAuthenticator extends ModularRealmAuthenticator{

	private Realm realm;
	
	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
		Collection<Realm> realms = new ArrayList<Realm>();
		realms.add(realm);
		this.setRealms(realms);
	}

}
