package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Patient;
import uk.ac.healthcare.util.CsvUtils;
import java.util.List;

public class PatientRepository {
    private final DataStore dataStore;
    public PatientRepository(DataStore dataStore) {
        this.dataStore = dataStore;
}
    public void loadPatients(String filePath) {
        List<String> lines = CsvUtils.readLines(filePath);
        for (int i = 1; i < lines.size(); i++) {
            String[] cols = lines.get(i).split(",");

            String patientId = cols[0];
            String firstName = cols[1];
            String lastName = cols[2];
            String email = cols[3];
            String nhsNumber = cols[4];
            String surgeryId = cols[5];

            Patient patient = new Patient(
                    patientId,
                    firstName,
                    lastName,
                    email,
                    nhsNumber,
                    surgeryId
            );

            dataStore.patients.put(patientId, patient);
        }
    }
}
