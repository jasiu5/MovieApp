package com.pis_projekt.MovieService.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinIOService {
    final static String endPoint = "http://127.0.0.1:9000";
    final static String accessKey = "TTeKtaePWGwGM1IB";
    final static String secretKey="IGeMJlTC9tua7BYsa8OvWqbOEXkx7jmT";
    final static String bucketName = "pisbucket1";
    final static String home = System.getProperty("user.home");
    final static String localFileFolder = home + "/Downloads/";
    final static MinioClient minioClient = MinioClient.builder().endpoint(endPoint)
            .credentials(accessKey, secretKey).build();

    public void WriteToMinIO(MultipartFile multipartFile)
            throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException, MinioException {

            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            InputStream is = multipartFile.getInputStream();
            String fileToUpload = multipartFile.getInputStream().toString();
            PutObjectArgs args = PutObjectArgs.builder().bucket(bucketName).object(multipartFile.getOriginalFilename())
                    .stream(is, -1, 10485760).build();
            minioClient.putObject(args);

            System.out.println(fileToUpload + " successfully uploaded to:");
            System.out.println("   container: " + bucketName);
            System.out.println("   blob: " + fileToUpload);
            System.out.println();
    }

    public File ReadFromMinIO(String fileName)
            throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException, MinioException {
            String downloadedFile = localFileFolder + "D_" + fileName;
            File file = new File(downloadedFile);
            if (file.exists()){
                file.delete();
            }
            DownloadObjectArgs args = DownloadObjectArgs.builder().bucket(bucketName).object(fileName)
                    .filename(downloadedFile).build();
            minioClient.downloadObject(args);

            System.out.println("Downloaded file to ");
            System.out.println(" " + downloadedFile);
            System.out.println();
            return file;
        }

    public String getFileUrl(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeyException, MinioException {
        String url =
                minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(fileName)
                                .expiry(2, TimeUnit.HOURS)
                                .build());
        System.out.println(url);
        return url;
    }
    public List<String> getListOfMovies() throws
            MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException{

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());
        List<String> movieNames = new ArrayList<>();
        for (Result<Item> r : results) {
            movieNames.add(r.get().objectName());
        }
        return movieNames;
    }
}
