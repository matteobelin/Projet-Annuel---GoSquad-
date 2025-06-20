package com.gosquad.usecase.security;

public interface EncryptionService {
    String encrypt(String plainText);
    String decrypt(String encryptedText);
    byte[] encrypt(byte[] data);
    byte[] decrypt(byte[] encryptedData);
}
