package com.ssonzm.productservice.service.aws_s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.util.IOUtils;
import com.ssonzm.coremodule.domain.ImageFile;
import com.ssonzm.coremodule.dto.property.CloudFrontProperties;
import com.ssonzm.coremodule.dto.property.S3Properties;
import com.ssonzm.productservice.common.util.CloudFrontUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
public class S3Service {

    private final AmazonS3 amazonS3;
    private final S3Properties s3Properties;
    private final AmazonS3Client amazonS3Client;
    private final CloudFrontProperties cloudFrontProperties;

    public S3Service(AmazonS3 amazonS3, S3Properties s3Properties, AmazonS3Client amazonS3Client,
                     CloudFrontProperties cloudFrontProperties) {
        this.amazonS3 = amazonS3;
        this.s3Properties = s3Properties;
        this.amazonS3Client = amazonS3Client;
        this.cloudFrontProperties = cloudFrontProperties;
    }

    /**
     * S3 bucket 파일 다운로드
     */
    public ResponseEntity<byte[]> getObject(String storedFileName) throws IOException {
        S3Object o = amazonS3.getObject(new GetObjectRequest(s3Properties.getS3().getBucket(), storedFileName));
        S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    /**
     * 파일 업로드
     */
    public ImageFile upload(MultipartFile multipartFile, String fileRootPath) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename(); // 파일 이름
        long size = multipartFile.getSize(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(size);

        // S3에 업로드
        String storeFileName = fileRootPath + "/" + UUID.randomUUID() + originalFilename;
        amazonS3Client.putObject(
                new PutObjectRequest(s3Properties.getS3().getBucket(), storeFileName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imageUrl = getS3ImageUrl(storeFileName);
//        String imageUrl = CloudFrontUtil.getCloudFrontImageUrl(cloudFrontProperties.getDomain(), storeFileName);
        return new ImageFile(storeFileName, imageUrl);
    }

    public String getS3ImageUrl(String storeFileName) {
        return amazonS3Client.getUrl(s3Properties.getS3().getBucket(), storeFileName).toString();
    }

    /**
     * 여러 파일 업로드
     * @param multipartFiles
     * @param fileRootPath
     * @return
     * @throws IOException
     */
    public List<ImageFile> upload(List<MultipartFile> multipartFiles, String fileRootPath) throws IOException {
        List<ImageFile> imageFiles = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename(); // 파일 이름
            long size = multipartFile.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
            String storeFileName = fileRootPath + "/" + UUID.randomUUID() + originalFilename;
            amazonS3Client.putObject(
                    new PutObjectRequest(s3Properties.getS3().getBucket(), storeFileName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imageUrl = getS3ImageUrl(storeFileName);

            ImageFile imageFile = new ImageFile(storeFileName, imageUrl);
            imageFiles.add(imageFile);
        }

        return imageFiles;
    }

    /**
     * 파일 삭제
     */
    public void delete(String storeFileName) {
        boolean isExistObject = amazonS3Client.doesObjectExist(s3Properties.getS3().getBucket(), storeFileName);
        if (isExistObject)
            amazonS3Client.deleteObject(new DeleteObjectRequest(s3Properties.getS3().getBucket(), storeFileName));
    }

    /**
     * 파일 여러개 삭제
     * @param storeFileNameList
     */
    public void deleteFiles(List<String> storeFileNameList) {
        List<KeyVersion> keyList = new ArrayList<>();

        for (String fileName : storeFileNameList) {
            keyList.add(new KeyVersion(fileName));
        }

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(s3Properties.getS3().getBucket());
        deleteObjectsRequest.setKeys(keyList);

        amazonS3Client.deleteObjects(deleteObjectsRequest);
    }
}