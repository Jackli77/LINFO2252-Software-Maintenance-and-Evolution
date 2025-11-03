package linfo2252.model;

import java.util.ArrayList;

public class UserProfile {
    private String name = "";
    private String email = "";
    private String phoneNumber = "";
    private InsuranceLevel insurance = InsuranceLevel.PREMIUM;
    private AccountType accountType = AccountType.PRIMARY_USER;
    private ArrayList<MedicalHistoryEvent> medicalhistory = new ArrayList<>();

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public InsuranceLevel getInsurance() { return insurance; }
    public AccountType getAccountType() { return accountType; }
    public ArrayList<MedicalHistoryEvent> getMedicalHistory() { return medicalhistory; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setInsurance(InsuranceLevel insurance) { this.insurance = insurance; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}
