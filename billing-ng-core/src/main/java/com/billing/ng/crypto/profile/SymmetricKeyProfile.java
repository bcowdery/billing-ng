package com.billing.ng.crypto.profile;

import com.billing.ng.crypto.key.KeyPair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * A simple {@link CipherProfile} for symmetric-key algorithms. Symmetric key algorithms use
 * the same key for encrypting and decrypting data.
 *
 * @author Brian Cowdery
 * @since 16/01/11
 */
public class SymmetricKeyProfile implements CipherProfile {

    private final String identifier;
    private final Integer keysize;

    public SymmetricKeyProfile() {
        this.identifier = null;
        this.keysize = null;
    }

    public SymmetricKeyProfile(String identifier) {
        this.identifier = identifier;
        this.keysize = null;
    }

    public SymmetricKeyProfile(String identifier, Integer keysize) {
        this.identifier = identifier;
        this.keysize = keysize;
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return null;
    }

    public KeyPair generateKey() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(identifier, BouncyCastleProvider.PROVIDER_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Key generator algorithm not supported by the JCE provider.");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("BouncyCastle JCE provider not configured or not loaded.");
        }

        if (keysize != null) keyGen.init(keysize);

        return new KeyPair(null, keyGen.generateKey());
    }
}
