package com.k365.video_common.handler.fastdfs;

import org.apache.commons.io.FileUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author Gavin
 * @date 2019/7/26 19:37
 * @description：
 */
public class FastDFSHelper {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSHelper.class);
    private static TrackerClient trackerClient;

    static {
        try {

//            ClientGlobal.init(new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath());

            ClassPathResource classPathResource = new ClassPathResource("fdfs_client.conf");
            //创建临时文件，将fdfs_client.conf的值赋值到临时文件中，创建这个临时文件的原因是springboot打成jar后无法获取classpath下文件
            String tempPath = System.getProperty("java.io.tmpdir") + System.currentTimeMillis() + ".conf";
            File file = new File(tempPath);
            FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), file);
            ClientGlobal.init(file.getAbsolutePath());
            trackerClient = new TrackerClient();

        } catch (IOException | MyException e) {
            logger.error("error", e);
        }
    }

    /**
     * 向FastDFS上传文件
     */
    public static String[] uploadFile(FastDFSFile file) {
        logger.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);

        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", file.getAuthor());

        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            logger.error("trackerClient getConnection fail", e);
            return null;
        }
        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        StorageClient storageClient = new StorageClient(trackerServer, null);

        try {
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        } finally {
            closeTrackerServer(trackerServer);
        }

        logger.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

        if (uploadResults == null || uploadResults.length < 2) {
            logger.error("upload file fail, error code:" + storageClient.getErrorCode());
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];
        logger.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
        return uploadResults;

    }

    public static StorageServer[] getStoreStorages(String groupName)
            throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorages(trackerServer, groupName);
    }

    public static ServerInfo[] getFetchStorages(String groupName,
                                                String remoteFileName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    public static String getTrackerUrl() throws IOException {
        getFetchStorages("group1", "M00/00/00/Z2SO0F09LGWAFpTfABAAABr-uyU216.jpg")[0].getPort();
        int port = getTrackerServer().getInetSocketAddress().getPort();
        return "http://" + getTrackerServer().getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port() + "/";
    }

    private static StorageClient getTrackerClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    /**
     * 从FastDFS下载文件
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) {
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            logger.error("error", e);
            return null;
        }

        try {
            StorageClient storageClient = new StorageClient(trackerServer, null);
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        } finally {
            closeTrackerServer(trackerServer);
        }
        return null;

    }

    /**
     * 从FastDFS删除文件
     */
    public static void deleteFile(String groupName, String remoteFileName) {
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            logger.error("error", e);
            return;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);

        int i = 0;
        try {
            i = storageClient.delete_file(groupName, remoteFileName);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        } finally {
            closeTrackerServer(trackerServer);
        }
        logger.info("delete file successfully!!!" + i);

    }


    /**
     * 获取文件信息
     */
    public static FileInfo getFileInfo(String groupName, String remoteFileName) {
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            logger.error("error", e);
            return null;
        }
        StorageClient storageClient;
        try {
            storageClient = new StorageClient(trackerServer, null);
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        } finally {
            closeTrackerServer(trackerServer);
        }
        return null;
    }

    /**
     * 关闭trackerServer连接
     */
    private static void closeTrackerServer(TrackerServer trackerServer) {
        try {
            if (trackerServer != null) {
                logger.info("关闭trackerServer连接");
                trackerServer.close();
            }
        } catch (IOException e) {
            logger.error("error", e);
        }
    }


    public static byte[] getFileBuff(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            return null;


        InputStream inputStream = null;
        ByteArrayOutputStream out = null;
        try {
            inputStream = multipartFile.getInputStream();
            out = new ByteArrayOutputStream();
            int length = 0;
            byte[] file_buff = new byte[1024];
            if (inputStream != null) {
                while ((length = inputStream.read(file_buff)) != -1) {
                    out.write(file_buff,0,length);
                    out.flush();
                }
                return out.toByteArray();
            }
        } catch (IOException e) {
            logger.error("multipartFile InputStream read fail", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    logger.error("multipartFile inputStream close fail", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.error("multipartFile inputStream close fail", e);
                }
            }
        }
        return null;

    }

}


