package linfo2252.model;

import java.util.ArrayList;

public class UserProfile {
    public String name = "";
    public String email = "";
    public String phoneNumber = "";
    public InsuranceLevel insurance = InsuranceLevel.PREMIUM;
    public ArrayList<MedicalHistoryEvent> medicalhistory = new ArrayList<>();
}
