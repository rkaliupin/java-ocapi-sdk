package com.rk.ocapi.sdk.utils;

public class OCAPIUrlConfig {
    private String host;
    private String ocapiVersion;
    private String siteId;
    private String clientId;
    private String clientPass;

    public OCAPIUrlConfig(String host, String ocapiVersion, String siteId) {
        this.host = host;
        this.ocapiVersion = ocapiVersion;
        this.siteId = siteId;
    }

    public OCAPIUrlConfig(String host, String ocapiVersion, String siteId, String clientId, String clientPass) {
        this.host = host;
        this.ocapiVersion = ocapiVersion;
        this.siteId = siteId;
        this.clientId = clientId;
        this.clientPass = clientPass;
    }

    public String getHost() {
        return this.host;
    }

    public String getOcapiVersion() {
        return this.ocapiVersion;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientPass() {
        return this.clientPass;
    }

    public String getShopBaseUrl() {
        return "https://" + this.getHost() + "/s/" + this.getSiteId() + "/dw/shop/" + this.getOcapiVersion();
    }
}
