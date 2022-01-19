/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import th.in.totemplate.core.net.FtpInfo;

/**
 *
 * @author nuttapong.sri
 */
public class FtpSapInfo implements FtpInfo {
    ////////////////////////////////////////////////////////////////////////////
    private static final String _server   = "10.11.9.32";
    private static final String _username = "ftpatech";
    private static final String _password = "ftp@atech";
    private static final String _basepath = "\\LBMS\\";
    ////////////////////////////////////////////////////////////////////////////
//    private static final String _server   = "127.0.0.1";
//    private static final String _username = "boothsystem";
//    private static final String _password = "1234";
//    private static final String _basepath = "\\Consign\\";
    ////////////////////////////////////////////////////////////////////////////

    private static FtpSapInfo _info;
    private FtpSapInfo() { }
    public static FtpSapInfo getInstance() {
        if(FtpSapInfo._info == null)
            FtpSapInfo._info = new FtpSapInfo();

        return FtpSapInfo._info;
    }

    @Override public  String getServer()   { return FtpSapInfo._server; }
    @Override public String getUsername() { return FtpSapInfo._username; }
    @Override public String getPassword() { return FtpSapInfo._password; }
              public String getBasePath() { return FtpSapInfo._basepath; }

}
