package com.gosquad.infrastructure.persistence.B2.impl;
import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import com.gosquad.infrastructure.persistence.B2.B2Repository;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class B2RepositoryImpl implements B2Repository {

    private final S3Client s3Client;
    private final String bucketName;
    private final String endpoint;

    public B2RepositoryImpl() {
        Dotenv dotenv = Dotenv.load();

        String accessKeyId = dotenv.get("B2_APPLICATION_KEY_ID");
        String secretAccessKey = dotenv.get("B2_APPLICATION_KEY");
        this.endpoint = dotenv.get("B2_ENDPOINT");
        this.bucketName = dotenv.get("B2_BUCKETNAME");

        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create("https://" + endpoint)) // Ex: https://s3.eu-central-003.backblazeb2.com
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .region(Region.US_EAST_1) // Obligatoire pour AWS SDK (même si B2 l'ignore)
                .build();
    }

    @Override
    public String uploadImage(byte[] base64EncryptedImage) {
        String key = "image_" + UUID.randomUUID() + ".bin";

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/octet-stream")
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(base64EncryptedImage));

        return endpoint + "/" + bucketName + "/" + key;
    }

    @Override
    public byte[] downloadImage(String url) throws IOException {
        String key = extractKeyFromUrl(url);
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObject(getRequest).readAllBytes();
    }

    @Override
    public void deleteImage(String url) {
        String key = extractKeyFromUrl(url);

        try {
            // D'abord lister toutes les versions de ce fichier
            ListObjectVersionsRequest listRequest = ListObjectVersionsRequest.builder()
                    .bucket(bucketName)
                    .prefix(key)
                    .build();

            ListObjectVersionsResponse response = s3Client.listObjectVersions(listRequest);

            // Supprimer chaque version individuellement
            for (ObjectVersion version : response.versions()) {
                if (version.key().equals(key)) {
                    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .versionId(version.versionId())
                            .build();

                    s3Client.deleteObject(deleteRequest);
                }
            }

            // Supprimer aussi les "delete markers" (versions hide)
            for (DeleteMarkerEntry marker : response.deleteMarkers()) {
                if (marker.key().equals(key)) {
                    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .versionId(marker.versionId())
                            .build();

                    s3Client.deleteObject(deleteRequest);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression définitive: " + e.getMessage());
        }
    }

    private String extractKeyFromUrl(String url) {
        String prefix = "/" + bucketName + "/";
        int index = url.indexOf(prefix);
        if (index == -1) {
            throw new IllegalArgumentException("URL does not contain bucket name");
        }
        return url.substring(index + prefix.length());
    }


}
