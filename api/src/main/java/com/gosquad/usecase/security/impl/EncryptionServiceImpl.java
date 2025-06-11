package com.gosquad.usecase.security.impl;

import com.gosquad.usecase.security.EncryptionService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    private final Key key;
    private final SecureRandom secureRandom;

    public EncryptionServiceImpl() {
        String base64Key = Dotenv.load().get("ENCRYPTION_KEY");
        this.key = createValidAESKey(base64Key);
        this.secureRandom = new SecureRandom();
    }

    public EncryptionServiceImpl(String secret) {
        this.key = createValidAESKey(secret);
        this.secureRandom = new SecureRandom();
    }

    /**
     * Crée une clé AES valide à partir d'une chaîne
     * Ajuste automatiquement la taille à 32 bytes (AES-256)
     */
    private Key createValidAESKey(String base64Key) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(base64Key);

            // Ajuster la clé à 32 bytes pour AES-256
            byte[] validKey = new byte[32];

            if (decodedKey.length >= 32) {
                // Tronquer si trop long
                System.arraycopy(decodedKey, 0, validKey, 0, 32);
            } else {
                // Étendre si trop court en répétant la clé
                int pos = 0;
                while (pos < 32) {
                    int copyLength = Math.min(decodedKey.length, 32 - pos);
                    System.arraycopy(decodedKey, 0, validKey, pos, copyLength);
                    pos += copyLength;
                }
            }

            return new SecretKeySpec(validKey, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la clé AES", e);
        }
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Générer un IV aléatoire
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Combiner IV + données chiffrées
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de chiffrement", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);

            // Extraire IV et données chiffrées
            byte[] iv = Arrays.copyOfRange(combined, 0, 16);
            byte[] encrypted = Arrays.copyOfRange(combined, 16, combined.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de déchiffrement", e);
        }
    }

    public byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Générer un IV aléatoire
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encrypted = cipher.doFinal(data);

            // Combiner IV + données chiffrées
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return combined;
        } catch (Exception e) {
            throw new RuntimeException("Erreur de chiffrement", e);
        }
    }

    public byte[] decrypt(byte[] encryptedData) {
        try {
            // Extraire IV et données chiffrées
            byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);
            byte[] encrypted = Arrays.copyOfRange(encryptedData, 16, encryptedData.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de déchiffrement", e);
        }
    }

}