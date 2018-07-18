package com.example.thesis.booktrading.object;

public class PersonalInfo {
    private String PersonalInfoFullName;
    private String PersonalInfoPhone;


    public PersonalInfo(String personalInfoFullName, String personalInfoPhone) {
        PersonalInfoFullName = personalInfoFullName;
        PersonalInfoPhone = personalInfoPhone;
    }

    public PersonalInfo(){
        PersonalInfoFullName = "Anonymous";
        PersonalInfoPhone = "000000";
    }

    public String getPersonalInfoFullName() {
        return PersonalInfoFullName;
    }

    public void setPersonalInfoFullName(String personalInfoFullName) {
        PersonalInfoFullName = personalInfoFullName;
    }

    public String getPersonalInfoPhone() {
        return PersonalInfoPhone;
    }

    public void setPersonalInfoPhone(String personalInfoPhone) {
        PersonalInfoPhone = personalInfoPhone;
    }
}
