package com.gosquad.usecase.files;

import com.gosquad.usecase.files.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FileServiceImplTest {

    private FileService fileService;

    @BeforeEach
    void setup() {
        // On mock juste le repo (pas utilisé dans processFile)
        fileService = new FileServiceImpl(mock(com.gosquad.infrastructure.persistence.B2.B2Repository.class));
    }
    @Test
    void testProcessFile_withPdf() throws IOException {
        byte[] pdfData = new byte[] { '%', 'P', 'D', 'F', 0x00, 0x01 };
        byte[] result = fileService.processFile(pdfData);
        assertArrayEquals(pdfData, result, "Les données PDF doivent être retournées telles quelles");
    }

    @Test
    void testProcessFile_withValidImage() throws IOException {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] pngData = baos.toByteArray();

        byte[] result = fileService.processFile(pngData);
        assertNotNull(result, "La conversion doit retourner un tableau de bytes non nul");
        assertTrue(result.length > 0, "Le résultat ne doit pas être vide");
        assertFalse(java.util.Arrays.equals(pngData, result), "Les données d'entrée et de sortie ne doivent pas être identiques");
    }

    @Test
    void testProcessFile_withInvalidData_throws() {
        byte[] invalidData = new byte[] {0, 1, 2, 3, 4};
        assertThrows(IllegalArgumentException.class, () -> fileService.processFile(invalidData));
    }


   @Test
    void testConvertImageWithFallback_fallsBackToJPEG() throws IOException {
        FileService failingWebPService = new FileServiceImpl(mock(com.gosquad.infrastructure.persistence.B2.B2Repository.class)) {
            protected byte[] convertToWebP(BufferedImage image) throws IOException {
                throw new IOException("Simulated WebP failure");
            }
        };

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] pngData = baos.toByteArray();

        byte[] result = failingWebPService.processFile(pngData);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
