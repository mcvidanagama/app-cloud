package org.wso2.appcloud.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.appcloud.common.AppCloudException;
import org.wso2.appcloud.core.dao.CustomDockerImageDAO;
import org.wso2.appcloud.core.dto.CustomImage;
import org.wso2.carbon.context.CarbonContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CustomDockerImageManager {
    private static Log log = LogFactory.getLog(CustomDockerImageManager.class);

    /**
     * Add custom image details to database
     * @param imageId tenant domain-alphaneumeric umage url
     * @param remoteUrl image url
     * @throws AppCloudException
     */
    public static void addCustomDockerImage(String imageId, String remoteUrl) throws AppCloudException{
        Connection dbConnection = DBUtil.getDBConnection();
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            Date date = new Date();
            String timeStamp = String.valueOf(new java.sql.Timestamp(date.getTime()));
            CustomDockerImageDAO.getInstance().
                    addCustomDockerImage(dbConnection, imageId, tenantId, remoteUrl, timeStamp);
            dbConnection.commit();
        } catch (AppCloudException e) {
            String msg = "Error while adding custom image of : " + remoteUrl + " for tenant id : " + tenantId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } catch (SQLException e) {
            String msg = "Error while committing transaction for adding image from : " +
                         remoteUrl + " for tenant id : " + tenantId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    public static void updateCustomDockerTestResults(String imageId, String resultsJsonString, String status)
            throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        try {
            Date date = new Date();
            String timeStamp = String.valueOf(new java.sql.Timestamp(date.getTime()));
            CustomDockerImageDAO.getInstance().
                    updateCustomDockerImageRecord(dbConnection, imageId, resultsJsonString, status, timeStamp);
            dbConnection.commit();
        } catch (AppCloudException e) {
            String msg = "Error while updating custom image  : " + imageId + " with results : " + resultsJsonString +
                         " with status : " + status;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } catch (SQLException e) {
            String msg = "Error while committing transaction for updating image : " + imageId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    public static boolean isImageAvailable(String remoteUrl) throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            return CustomDockerImageDAO.getInstance().isImageAvailable(dbConnection, remoteUrl, tenantId);
        } catch (AppCloudException e) {
            String msg = "Error while chcking image availability in db for image : " + remoteUrl + " of tenant id : "
                         + tenantId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    public static CustomImage[] getAllCustomImages(String status) throws AppCloudException { // status null means all
        Connection dbConnection = DBUtil.getDBConnection();
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            List<CustomImage> imagesList = CustomDockerImageDAO.getInstance().getCustomImages(dbConnection, tenantId, status);
            if(imagesList.size() > 0) { // to avoid indexOutOfBound exception
                return imagesList.toArray(new CustomImage[imagesList.size()]);
            } else {
                return new CustomImage[0];
            }
        } catch (AppCloudException e) {
            String msg = "Error while retrieving custom images";
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    public static boolean deleteImage(String imageId) throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        try {
            return CustomDockerImageDAO.getInstance().deleteImage(dbConnection, imageId);
        } catch (AppCloudException e) {
            String msg = "Error while deleting custom images";
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    public static CustomImage getImageById(String imageId) throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        try {
            return CustomDockerImageDAO.getInstance().getImageById(dbConnection, imageId);

        } catch (AppCloudException e) {
            String msg = "Error while retrieving custom images";
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }

    }


}
