package uk.ac.healthcare.controller;

import uk.ac.healthcare.model.Referral;
import uk.ac.healthcare.repository.DataStore;
import uk.ac.healthcare.repository.ReferralRepository;
import uk.ac.healthcare.service.ReferralManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ReferralController {
    private final DataStore store;
    private final ReferralRepository repo;
    private final Path dataDir;

    public ReferralController(DataStore store, ReferralRepository repo, Path dataDir) {
        this.store = store;
        this.repo = repo;
        this.dataDir = dataDir;
    }

    public List<Referral> getAll() {
        return new ArrayList<>(store.referrals.values());
    }

    public void add(Referral referral) {
        store.referrals.put(referral.getReferralId(), referral);
        ReferralManager.getInstance().enqueue(referral); // optional but nice
    }

    public void update(Referral r) {
        store.referrals.put(r.getReferralId(), r);
    }

    public void delete(String referralId) {
        store.referrals.remove(referralId);
    }

    public void saveNow() {
        try {
            repo.save(dataDir.resolve("referrals.csv"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save referrals.csv", e);
        }
    }

    public Path exportEmailText(String referralId) {
        Referral r = store.referrals.get(referralId);
        if (r == null) throw new IllegalArgumentException("Referral not found: " + referralId);

        try {
            return ReferralManager.getInstance()
                    .exportReferralEmail(r, dataDir.resolve("output"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to export referral email text", e);
        }
    }
}

