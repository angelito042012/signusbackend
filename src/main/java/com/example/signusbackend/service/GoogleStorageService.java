package com.example.signusbackend.service;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GoogleStorageService {
    @Value("${gcp.storage.bucket}")
    private String bucketName;

    @Value("${gcp.storage.folder}")
    private String folder;

    

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    private final Storage storage;

    public GoogleStorageService(@Value("${gcp.credentials.path}") String credentialsPath) throws IOException {
        this.storage = StorageOptions.newBuilder()
                .setCredentials(
                    GoogleCredentials.fromStream(
                        new ClassPathResource(credentialsPath.replace("classpath:", "")).getInputStream()
                    )
                )
                .build()
                .getService();
    }

    public URL generarSignedUrl(String filename, String contentType) {

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, folder + "/" + filename)
                .setContentType(contentType)
                .build();

        return storage.signUrl(
                blobInfo,
                15, TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withContentType()
        );
    }
}
