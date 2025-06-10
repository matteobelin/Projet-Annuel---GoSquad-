package com.gosquad.usecase.secutity;

public interface EncryptionService {
    String encrypt(String plainText);
    String decrypt(String encryptedText);
}
