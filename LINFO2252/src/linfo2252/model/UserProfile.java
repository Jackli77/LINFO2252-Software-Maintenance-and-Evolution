package linfo2252.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the profile of the active user.
 * <p>
 * This class stores personal identification details, insurance classification, 
 * and the user's personal medical history record. It acts as the primary data 
 * source for the "User Profile" view.
 */
public class UserProfile {
    
    // Identity Information
    private String name = "";
    private String email = "";
    private String phoneNumber = "";
    
    // Status & Classification
    private InsuranceLevel insurance = InsuranceLevel.PREMIUM;
    private AccountType accountType = AccountType.PRIMARY_USER;
    
    // Medical Records
    private ArrayList<MedicalHistoryEvent> medicalhistory = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Accessors (Getters)
    // -------------------------------------------------------------------------

    public String getName() { 
        return name; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public InsuranceLevel getInsurance() { 
        return insurance; 
    }
    
    public AccountType getAccountType() { 
        return accountType; 
    }
    
    /**
     * Retrieves the list of past medical events.
     * @return A list of history objects.
     */
    public List<MedicalHistoryEvent> getMedicalHistory() { 
        return medicalhistory; 
    }

    // -------------------------------------------------------------------------
    // Mutators (Setters)
    // -------------------------------------------------------------------------

    public void setName(String name) { 
        this.name = name; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    
    public void setInsurance(InsuranceLevel insurance) { 
        this.insurance = insurance; 
    }
    
    public void setAccountType(AccountType accountType) { 
        this.accountType = accountType; 
    }
}