package com.softmed.tanzania.referral;

public class ClientModel {

    String ClientId,UserId,FirstName,MiddleName,SurName,PhoneNumber,Email,PhysicalAddress,DOB,Gender,VillageId,VillageName,WardId,WardName,VillageRefNo,WardRefNo,IsAChildOf,RegistrationDate;

    public ClientModel(String ClientId,String UserId,String FirstName,String MiddleName,String SurName,String PhoneNumber,String Email,String PhysicalAddress,String DOB,String Gender,String VillageId,String VillageName,String WardId,String WardName,String VillageRefNo,String WardRefNo,String IsAChildOf,String RegistrationDate){
        this.ClientId=ClientId;
        this.UserId=UserId;
        this.FirstName=FirstName;
        this.MiddleName=MiddleName;
        this.SurName=SurName;
        this.PhoneNumber=PhoneNumber;
        this.Email=Email;
        this.PhysicalAddress=PhysicalAddress;
        this.DOB=DOB;
        this.Gender=Gender;
        this.VillageId=VillageId;
        this.VillageName=VillageName;
        this.WardId=WardId;
        this.WardName=WardName;
        this.VillageRefNo=VillageRefNo;
        this.WardRefNo=WardRefNo;
        this.IsAChildOf=IsAChildOf;
        this.RegistrationDate=RegistrationDate;



    }

    public String getByClientId() {
        return ClientId;
    }
    public String getByClientName() {
        return FirstName+" "+MiddleName+" "+SurName;
    }
    public String getByClientVillageName() {
        return VillageName;
    }
    public String getByClientRegistrationDate() {
        return RegistrationDate;
    }

}
