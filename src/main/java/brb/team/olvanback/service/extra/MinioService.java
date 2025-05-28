package brb.team.olvanback.service.extra;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MinioService {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.username}")
    private String username;
    @Value("${minio.password}")
    private String password;

    public void sendingTheFileToMinio(MultipartFile file) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException {
        // Create MinIO client
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url) // MinIO server URL
                .credentials(username, password)
                .build();
        // Upload the file directly using InputStream from MultipartFile
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)  // The bucket name
                        .object(file.getOriginalFilename())  // The object name in the bucket
                        .stream(file.getInputStream(), file.getSize(), -1) // InputStream and file size
                        .build());
        log.warn("File uploaded successfully!");
    }
}
