package com.pacal.share.service.storage;

import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.config.TencentCOSProperties;
import com.pacal.share.entity.dto.StorageDTO;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class TencentStorageService implements UploadInterface{
    @Resource
    TencentCOSProperties tencentCOSProperties;

    private static COSClient cosClient;

    private COSClient getCosClient() {
        if ( Objects.isNull(cosClient)) {
            COSCredentials credentials = new BasicCOSCredentials( tencentCOSProperties.getSecretId(), tencentCOSProperties.getSecretKey() );
            ClientConfig clientConfig = new ClientConfig( new Region( tencentCOSProperties.getRegion() ));
            cosClient = new COSClient( credentials, clientConfig );
        }
        return cosClient;
    }

    @Override
    public String store(StorageDTO storageDTO) {
        System.out.println("aaa" + storageDTO);
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(storageDTO.getContentLength());
//			objectMetadata.setContentType("image/jpg");
            objectMetadata.setContentType(storageDTO.getContentType());
            objectMetadata.setContentDisposition( "inline" );

            PutObjectRequest putObjectRequest = new PutObjectRequest( tencentCOSProperties.getBucketName(), storageDTO.getObjectName(), storageDTO.getInputStream(), objectMetadata);

            PutObjectResult putObjectResult = getCosClient().putObject(putObjectRequest);
            //image.xs-architecture.com/2025-06-19/e246aeed-ac5f-4b3c-9c6e-233b2f23052d.png
            if ( Objects.nonNull( putObjectResult.getETag() ) ) {
                return tencentCOSProperties.getImageEndpoint() + "/" + storageDTO.getObjectName();
            }
            log.error("upload failed 1: {}", putObjectResult);
        } catch (Exception ex) {
            log.error("upload failed 2: ", ex);
        }
        throw new PacalException( ErrorCode.FILE_UPLOAD_ERROR);
    }
}
