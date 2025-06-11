package com.gosquad.usecase.files;

import java.io.IOException;

public interface FileService {
    byte[] processFile(byte[] inputData) throws IOException;
    String uploadFileImage(byte[] base64EncryptedImage);
    byte[] downloadFileImage(String url) throws IOException;
    void deleteFileImage(String url) throws IOException;
}
