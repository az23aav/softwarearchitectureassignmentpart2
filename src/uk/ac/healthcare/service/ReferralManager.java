package uk.ac.healthcare.service;

import uk.ac.healthcare.model.Referral;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ReferralManager {
    private static final ReferralManager INSTANCE = new ReferralManager();

    private final List<Referral> queue = new ArrayList<>();

    private ReferralManager() {}

    public static ReferralManager getInstance() {
        return INSTANCE;
    }

    public void enqueue(Referral referral) {
        if (referral != null) queue.add(referral);
    }

    public List<Referral> getQueueSnapshot() {
        return Collections.unmodifiableList(new ArrayList<>(queue));
    }

    public Path exportReferralEmail(Referral referral, Path outputDir) throws IOException {
        if (referral == null) throw new IllegalArgumentException("referral is null");

        Files.createDirectories(outputDir);

        String fileName = "referral_" + referral.getReferralId() + ".txt";
        Path out = outputDir.resolve(fileName);

        Files.writeString(out, referral.toEmailText(), StandardCharsets.UTF_8);

        return out;
    }
}

