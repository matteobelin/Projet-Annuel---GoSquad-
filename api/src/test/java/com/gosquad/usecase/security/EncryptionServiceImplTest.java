package com.gosquad.usecase.security;

import com.gosquad.usecase.security.impl.EncryptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceImplTest {

    private EncryptionService encryptionService;

    // Une clé AES 256 en base64 (32 bytes = 256 bits)
    // Pour les tests, on génère une clé fixe simple (juste des 'A' encodés)
    private static final String TEST_KEY_BASE64 = Base64.getEncoder().encodeToString("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes());

    @BeforeEach
    void setUp() {
        // Instancie l'implémentation avec la clé fixe
        encryptionService = new EncryptionServiceImpl(TEST_KEY_BASE64);
    }

    @Test
    void testEncryptDecryptString() {
        String original = "Message secret à chiffrer";

        String encrypted = encryptionService.encrypt(original);
        assertNotNull(encrypted);
        assertNotEquals(original, encrypted);

        String decrypted = encryptionService.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    void testEncryptDecryptBytes() {
        byte[] original = "Données binaires à chiffrer".getBytes();

        byte[] encrypted = encryptionService.encrypt(original);
        assertNotNull(encrypted);
        assertNotEquals(0, encrypted.length);

        byte[] decrypted = encryptionService.decrypt(encrypted);
        assertArrayEquals(original, decrypted);
    }

    @Test
    void testEncryptProducesDifferentCiphertextEachTime() {
        String original = "Même texte";

        String encrypted1 = encryptionService.encrypt(original);
        String encrypted2 = encryptionService.encrypt(original);

        // Avec IV aléatoire, le résultat doit être différent même si le texte est identique
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void testDecryptInvalidDataThrows() {
        byte[] invalidData = new byte[]{1, 2, 3, 4, 5};

        assertThrows(RuntimeException.class, () -> {
            encryptionService.decrypt(invalidData);
        });

        assertThrows(RuntimeException.class, () -> {
            encryptionService.decrypt("donnéeNonValideBase64");
        });
    }
}
