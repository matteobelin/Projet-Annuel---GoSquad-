package com.gosquad.usecase.files.impl;

import com.gosquad.infrastructure.persistence.B2.B2Repository;
import com.gosquad.usecase.files.FileService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {

    private final B2Repository b2Repository;

    public FileServiceImpl(B2Repository b2Repository) {
        this.b2Repository = b2Repository;
    }

    public byte[] processFile(byte[] inputData) throws IOException {
        if (isPdf(inputData)) {
            return inputData;
        } else if (isImage(inputData)) {
            BufferedImage image = bytesToBufferedImage(inputData);
            return convertImageWithFallback(image);
        } else {
            throw new IllegalArgumentException("Format de fichier non supporté");
        }
    }
    private byte[] convertImageWithFallback(BufferedImage image) throws IOException {
        try {
            return convertToWebP(image);
        } catch (IOException e) {
            return convertToJPEG(image);
        }
    }

    private byte[] convertToJPEG(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (image.getColorModel().hasAlpha()) {
            BufferedImage rgbImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            rgbImage.getGraphics().drawImage(image, 0, 0, null);
            image = rgbImage;
        }

        boolean success = ImageIO.write(image, "jpeg", baos);
        if (!success) {
            throw new IOException("Conversion en JPEG échouée");
        }

        return baos.toByteArray();
    }

    private boolean isPdf(byte[] data) {
        return data.length > 4 &&
                data[0] == '%' && data[1] == 'P' && data[2] == 'D' && data[3] == 'F';
    }

    private boolean isImage(byte[] data) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            return img != null;
        } catch (IOException e) {
            return false;
        }
    }

    private BufferedImage bytesToBufferedImage(byte[] data) throws IOException {
        try (InputStream in = new ByteArrayInputStream(data)) {
            BufferedImage img = ImageIO.read(in);
            if (img == null) {
                throw new IOException("Impossible de décoder l'image");
            }
            return img;
        }
    }

    private byte[] convertToWebP(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean success = ImageIO.write(image, "webp", baos);
        if (!success) {
            throw new IOException("Conversion en WebP non supportée (plugin manquant ?)");
        }
        return baos.toByteArray();
    }

    public String uploadFileImage(byte[] base64EncryptedImage){
        return b2Repository.uploadImage(base64EncryptedImage);
    };

    public byte[] downloadFileImage(String url) throws IOException{
        return b2Repository.downloadImage(url);
    };

    public void deleteFileImage(String url) throws IOException{
        b2Repository.deleteImage(url);
    };

}
