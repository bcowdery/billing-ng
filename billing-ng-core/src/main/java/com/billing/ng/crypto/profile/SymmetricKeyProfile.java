package com.billing.ng.crypto.profile;

import com.billing.ng.crypto.DigestAlgorithm;
import com.billing.ng.crypto.key.KeyPair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

/**
 * A simple {@link CipherProfile} for symmetric-key algorithms. Symmetric key algorithms use
 * the same key for encrypting and decrypting data.
 *
 * @author Brian Cowdery
 * @since 16/01/11
 */
public class SymmetricKeyProfile implements CipherProfile {

    private final String identifier;
    private final Integer keysize; // in bits

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

    public KeyPair generateKey(String password) {
        // use a hashing algorithm to provide a large number of bytes based off of the password
        // this should ensure that we meet the key size without needing to pad with predictable data
        byte[] bytes = DigestAlgorithm.SHA256.digestBytes(password);

        // truncate, or pad with zeros to reach the target keysize
        if (keysize != null) {
            bytes = Arrays.copyOf(bytes, keysize / 8);
        }

        // create a new secret key
        Key key = new SecretKeySpec(bytes, identifier);
        return new KeyPair(null, key);
    }
}
