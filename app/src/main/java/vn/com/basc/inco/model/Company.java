package vn.com.basc.inco.model;

/**
 * Created by SONY on 7/3/2016.
 */
public class Company {
    private String ClientID;
    private String ClientName;
    private String ClientCode;
    private String Logo;
    private String Description;
    private String DateCreated;
    private String DateExpired;
    public String getClientID() {
        return ClientID;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getClientCode() {
        return ClientCode;
    }

    public String getLogo() {
        return Logo;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    @Override
    public String toString() {
        return ClientCode;
    }
}
