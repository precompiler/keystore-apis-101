package com.github.precompiler.keystore;


import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * @author Richard Li
 */
public class Demo {
    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException, InvalidKeyException, NoSuchProviderException, SignatureException {
        KeyStore keyStore = KeyStore.getInstance("JCEKS"); // JKS cannot store symmetric keys
        keyStore.load(null, "admin".toCharArray());

        // store symmetric key
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        System.out.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        KeyStore.SecretKeyEntry symmetricKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter password
                = new KeyStore.PasswordProtection("admin123".toCharArray());
        keyStore.setEntry("des-entry", symmetricKeyEntry, password);
        KeyStore.Entry e = keyStore.getEntry("des-entry", password);
        System.out.println(Base64.getEncoder().encodeToString(((KeyStore.SecretKeyEntry) e).getSecretKey().getEncoded()));

        // store private key
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        System.out.println(keyPair.getPublic());
        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        X509Certificate[] certificateChain = new X509Certificate[2];
        keyStore.setKeyEntry("rsa-entry", keyPair.getPrivate(), "admin".toCharArray(), certificateChain);
        System.out.println(Base64.getEncoder().encodeToString(keyStore.getKey("rsa-entry", "admin".toCharArray()).getEncoded()));

        // store cert
        Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(Demo.class.getClassLoader().getResourceAsStream("google.cer"));
        keyStore.setCertificateEntry("google.com", cert);
        Certificate gcert = keyStore.getCertificate("google.com");
        System.out.println(gcert);
    }
}
