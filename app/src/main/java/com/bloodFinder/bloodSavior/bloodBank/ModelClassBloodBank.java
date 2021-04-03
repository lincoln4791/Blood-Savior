package com.bloodFinder.bloodSavior.bloodBank;

public class ModelClassBloodBank {
    private String bloodBankName;
    private String bloodBankAddress;
    private String bloodBankPhoneNumber;

    public ModelClassBloodBank(String bloodBankName, String bloodBankAddress, String bloodBankPhoneNumber) {
        this.bloodBankName = bloodBankName;
        this.bloodBankAddress = bloodBankAddress;
        this.bloodBankPhoneNumber = bloodBankPhoneNumber;
    }

    public String getBloodBankName() {
        return bloodBankName;
    }

    public void setBloodBankName(String bloodBankName) {
        this.bloodBankName = bloodBankName;
    }

    public String getBloodBankAddress() {
        return bloodBankAddress;
    }

    public void setBloodBankAddress(String bloodBankAddress) {
        this.bloodBankAddress = bloodBankAddress;
    }

    public String getBloodBankPhoneNumber() {
        return bloodBankPhoneNumber;
    }

    public void setBloodBankPhoneNumber(String bloodBankPhoneNumber) {
        this.bloodBankPhoneNumber = bloodBankPhoneNumber;
    }
}
