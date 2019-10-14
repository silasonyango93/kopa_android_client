package com.softmed.tanzania.referral;

public class MyBasket {
    String server_id;
    String name;
    String refno;

    public MyBasket(String server_id, String name, String refno) {
        this.server_id = server_id;
        this.name = name;
        this.refno = refno;

    }

    public String getByServerId() {
        return server_id;
    }

    public String getByName() {
        return name;
    }

    public String getByRefNo() {
        return refno;
    }


}