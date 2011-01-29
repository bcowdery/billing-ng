package com.billing.ng.crypto.context;

import com.billing.ng.crypto.CipherAlgorithm;

import java.io.Serializable;

/**
 * Stores the {@link CipherAlgorithm} being used by the system.
 *
 * @author Brian Cowdery
 * @since 16/01/11
 */
public class CipherAlgorithmHolder implements Serializable {

    // default to AES unless otherwise set
    private static CipherAlgorithm holder = CipherAlgorithm.AES;

    public static void setAlgorithm(CipherAlgorithm algorithm) {
        holder = algorithm;
    }

    public static CipherAlgorithm getAlgorithm() {
        return holder;
    }
}
