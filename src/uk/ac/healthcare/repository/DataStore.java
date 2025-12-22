package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataStore {
    public final Map<String, Patient> patients = new LinkedHashMap<>();
    public final Map<String, Clinician> clinicians = new LinkedHashMap<>();
    public final Map<String, Appointment> appointments = new LinkedHashMap<>();
    public final Map<String, Prescription> prescriptions = new LinkedHashMap<>();
    public final Map<String, Referral> referrals = new LinkedHashMap<>();
    public final Map<String, AdminStaff> staff = new LinkedHashMap<>();
    public final Map<String, Facility> facilities = new LinkedHashMap<>();
}

