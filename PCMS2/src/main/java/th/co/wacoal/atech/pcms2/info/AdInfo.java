package th.co.wacoal.atech.pcms2.info;

import org.springframework.stereotype.Repository;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import th.in.totemplate.core.authen.AuthenInfo;

/**
 *
 * @author nuttapong.sri
 */
@Repository
public class AdInfo implements AuthenInfo {
	 private static final String _domain   = "atech.co.th";
    @SuppressWarnings("unused")
	private static final String _ipserver = "10.11.44.210";
    private static final String _adname   = "dc=atech,dc=co,dc=th";
    private static final String _admin    = "interface";
    private static final String _password = "1nt3rf@c3";

    private static AdInfo _info;
    private AdInfo() { }
    public static AdInfo getInstance() {
        if(AdInfo._info == null) {
			AdInfo._info = new AdInfo();
		}

        return AdInfo._info;
    }

    @Override
    public String getDomain() {
        return AdInfo._domain;
    }

    public String getAdminUser()     { return AdInfo._admin; }
    public String getAdminPassword() { return AdInfo._password; }

    public String getSearchName()                  { return AdInfo._adname; }
    public String getSearchFilter(String username) { return "(&(cn=" + username + ")(objectClass=*))"; }
}
