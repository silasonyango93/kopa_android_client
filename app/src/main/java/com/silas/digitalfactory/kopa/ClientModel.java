package com.silas.digitalfactory.kopa;

public class ClientModel {

    String ClientId,ClientFirstName,ClientMiddleName,ClientSurname,ClientNationalId,ClientProfilePicName,GenderId,ClientDOB,ClientPhoneNumber,ClientPhysicalAddress,ClientEmail,ClientRegistrationDate,EmploymentStatus,EmploymentCategoryId,Occupation,EmploymentStation;

    public ClientModel(String ClientId,String ClientFirstName,String ClientMiddleName,String ClientSurname,String ClientNationalId,String ClientProfilePicName,String GenderId,String ClientDOB,String ClientPhoneNumber,String ClientPhysicalAddress,String ClientEmail,String ClientRegistrationDate, String EmploymentStatus, String EmploymentCategoryId, String Occupation, String EmploymentStation){
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
        this.EmploymentStatus=EmploymentStatus;
        this.EmploymentCategoryId=EmploymentCategoryId;
        this.Occupation=Occupation;
        this.EmploymentStation=EmploymentStation;
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

    public String getGender() {
        String strGender = "";
        if(GenderId.equals("1")) {
            strGender = "Male";
        } else {
            strGender = "Female";
        }
        return strGender;
    }

    public String getClientFirstName() {
        return ClientFirstName;
    }

    public String getClientMiddleName() {
        return ClientMiddleName;
    }

    public String getClientSurname() {
        return ClientSurname;
    }

    public String getClientNationalId() {
        return ClientNationalId;
    }

    public String getClientDOB() {
        return ClientDOB;
    }

    public String getClientPhoneNumber() {
        return ClientPhoneNumber;
    }

    public String getClientPhysicalAddress() {
        return ClientPhysicalAddress;
    }

    public String getEmploymentStatus() {
        return EmploymentStatus;
    }

    public String getEmploymentCategoryId() {
        return EmploymentCategoryId;
    }

    public String getOccupation() {
        return Occupation;
    }

    public String getEmploymentStation() {
        return EmploymentStation;
    }
}
