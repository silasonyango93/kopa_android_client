package com.silas.digitalfactory.kopa;

public class ClientModel {

    String ClientId,ClientFirstName,ClientMiddleName,ClientSurname,ClientNationalId,ClientProfilePicName,GenderId,ClientDOB,ClientPhoneNumber,ClientPhysicalAddress,ClientEmail,ClientRegistrationDate;

    public ClientModel(String ClientId,String ClientFirstName,String ClientMiddleName,String ClientSurname,String ClientNationalId,String ClientProfilePicName,String GenderId,String ClientDOB,String ClientPhoneNumber,String ClientPhysicalAddress,String ClientEmail,String ClientRegistrationDate){
        this.ClientId=ClientId;
        this.ClientFirstName=ClientFirstName;
        this.ClientMiddleName=ClientMiddleName;
        this.ClientSurname=ClientSurname;
        this.ClientNationalId=ClientNationalId;
        this.ClientProfilePicName=ClientProfilePicName;
        this.GenderId=GenderId;
        this.ClientDOB=ClientDOB;
        this.ClientPhoneNumber=ClientPhoneNumber;
        this.ClientPhysicalAddress=ClientPhysicalAddress;
        this.ClientEmail=ClientEmail;
        this.ClientRegistrationDate=ClientRegistrationDate;




    }

    public String getByClientId() {
        return ClientId;
    }
    public String getImageUrl() {
        return Config.ip+ClientProfilePicName;
    }
    public String getByRegistrationDate() {
        return ClientRegistrationDate;
    }
    public String getByClientEmail() {
        return ClientEmail;
    }
    public String getByClientName() {
        return ClientFirstName+" "+ClientMiddleName+" "+ClientSurname;
    }

}
