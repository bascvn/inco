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
    private String BuildNumber;
    private String Status;
    private String url_ios;
    private String url_android;
    private String msg;
    private String android_ver;

    public String getAndroid_ver() {
        return android_ver;
    }

    public String getMsg() {
        return msg;
    }

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

    public String getDescription() {
        return Description;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public String getDateExpired() {
        return DateExpired;
    }

    public String getBuildNumber() {
        return BuildNumber;
    }

    public String getStatus() {
        return Status;
    }

    public String getUrl_ios() {
        return url_ios;
    }

    public String getUrl_android() {
        return url_android;
    }

    @Override
    public String toString() {
        return ClientCode;
    }
}
