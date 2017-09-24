package com.ajibigad.erazer.utils;

import com.ajibigad.erazer.controller.exception.ErazerException;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by ajibigad on 11/08/2017.
 */
public class ImageHelper {

    public static final String IMAGE_DIRECTORY = "images";
    public static final String IMAGE_EXT = "jpeg";
    public static final String PROOF_IMG_DIR = "proofImage";
    private static final Logger LOG = Logger.getLogger(ImageHelper.class);
    private static final String IMG_PREFIX = "IMG_";

    public static String saveImage(String base64Image, String username) throws ErazerException {
        BufferedImage bufferedImage = null;
        ByteArrayInputStream inputStream = null;
        byte[] imageBytes;
        String imageName;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            imageBytes = decoder.decodeBuffer(base64Image);
            inputStream = new ByteArrayInputStream(imageBytes);
            bufferedImage = ImageIO.read(inputStream);
            String savePath = createUserImageDirectory(username);
            imageName = appendExtension(IMG_PREFIX + Calendar.getInstance().getTimeInMillis());
            LOG.info(Arrays.toString(ImageIO.getWriterFormatNames()));
            if(ImageIO.write(bufferedImage, IMAGE_EXT, new File(savePath, imageName))){
                LOG.info("Image saving successful");
                LOG.info("Image name " + imageName);
                LOG.info("Image save path " + savePath);
            } else throw new ErazerException("Image upload failed");
        } catch (IOException ex) {
            LOG.error("Image Upload failed", ex);
            ex.printStackTrace();
            throw new ErazerException("Image Upload Failed. Pls try again");
        }
        finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error("ByteArrayInputStream failed to close", e);
                    e.printStackTrace();
                }
            }
        }
        LOG.info("Image Upload successful");
        return imageName;
    }

    private static String createUserImageDirectory(String username) throws IOException {
        StringBuilder sb = new StringBuilder();
        String path = getUsernameProofImageDirectory(username);
        LOG.info(Files.createDirectories(Paths.get(path)).toAbsolutePath());
        return path;
    }

    public static byte[] getImage(String username, String imageName) throws IOException {
        return Files.readAllBytes(Paths.get(getUsernameProofImageDirectory(username)
                + File.separator + appendExtension(imageName)));
    }

    private static String appendExtension(String imageName){
        return new StringBuilder().append(imageName).append(".").append(IMAGE_EXT).toString();
    }

    private static String getUsernameProofImageDirectory(String username){
        StringBuilder sb = new StringBuilder();
        sb.append(IMAGE_DIRECTORY)
                .append(File.separator)
                .append(PROOF_IMG_DIR)
                .append(File.separator).append(username); // eg images/proofImage/ajibigad
        return sb.toString();
    }
}
