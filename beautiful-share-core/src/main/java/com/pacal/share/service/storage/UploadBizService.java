package com.pacal.share.service.storage;

import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.config.LocalUploadProperties;
import com.pacal.share.entity.dto.StorageDTO;
import com.pacal.share.entity.vo.UploadVO;
import com.pacal.share.utils.DateUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class UploadBizService {
    @Resource
    LocalUploadProperties localUploadProperties;

    public UploadVO upload(MultipartFile file) {
        if ( file.isEmpty() ) {
            throw new PacalException( ErrorCode.FILE_UPLOAD_EMPTY );
        }
        String originName = file.getOriginalFilename();
        if ( originName == null ) {
            throw new PacalException( ErrorCode.FILE_UPLOAD_ERROR );
        }

        int lastIndex = originName.lastIndexOf( "." );
        String extension = lastIndex >= 0 ? originName.substring( lastIndex ) : "";
        String uid = UUID.randomUUID().toString();
        String objectName = DateUtil.getCurrentDate() + "/" + uid + extension;

        UploadVO uploadVO = new UploadVO();

        StorageDTO storageDTO = new StorageDTO();
        try {
            if ( !"video/mp4".equals( file.getContentType() ) ) {
                BufferedImage image = ImageIO.read( file.getInputStream() );
                if ( image != null ) {
                    uploadVO.setWidth( image.getWidth() );
                    uploadVO.setHeight( image.getHeight() );
                }
            }

            storageDTO.setContentLength( file.getSize() );
            storageDTO.setContentType( file.getContentType() );
            storageDTO.setObjectName( objectName );
            storageDTO.setInputStream( file.getInputStream() );
        } catch ( Exception ex ) {
            log.error( "upload exception ", ex );
            throw new PacalException( ErrorCode.FILE_UPLOAD_ERROR );
        }
        String storageUrl = uploadToLocal( storageDTO );
        uploadVO.setUrl( storageUrl );

        return uploadVO;
    }

    public String uploadToCloud(StorageDTO storageDTO) {
        // 当前项目默认走本地上传，云存储按需扩展
        return uploadToLocal( storageDTO );
    }

    public String uploadToLocal(StorageDTO storageDTO) {
        String baseDir = localUploadProperties.getDirectory();
        String publicBase = localUploadProperties.getPublicBaseUrl();
        if ( StringUtils.isEmpty( baseDir ) || StringUtils.isEmpty( publicBase ) ) {
            throw new PacalException( ErrorCode.FILE_UPLOAD_ERROR );
        }
        Path root = Paths.get( baseDir ).normalize();
        Path target = root.resolve( storageDTO.getObjectName() ).normalize();
        if ( !target.startsWith( root ) ) {
            throw new PacalException( ErrorCode.FILE_UPLOAD_ERROR );
        }
        try ( InputStream in = storageDTO.getInputStream() ) {
            if ( in == null ) {
                throw new PacalException( ErrorCode.FILE_UPLOAD_ERROR );
            }
            Files.createDirectories( target.getParent() );
            Files.copy( in, target, StandardCopyOption.REPLACE_EXISTING );
        } catch ( IOException ex ) {
            log.error( "uploadToLocal failed", ex );
            throw new PacalException( ErrorCode.FILE_UPLOAD_ERROR );
        }
        return publicBase + "/" + storageDTO.getObjectName();
    }
}