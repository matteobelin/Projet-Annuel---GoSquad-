package com.gosquad.infrastructure.persistence.B2;

import java.io.IOException;

public interface B2Repository {
    String uploadImage(byte[] base64EncryptedImage);
    byte[] downloadImage(String url) throws IOException;
    void deleteImage(String url) throws IOException;

}
