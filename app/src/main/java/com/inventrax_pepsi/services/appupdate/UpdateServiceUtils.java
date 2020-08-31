package com.inventrax_pepsi.services.appupdate;

import android.app.Notification;
import android.content.Context;
import android.os.Build;

import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.util.NotificationUtils;

/**
 * Created by nareshp on 28/01/2016.
 */
public class UpdateServiceUtils {

    private Context context;
    private UpdateRequest.Builder builder;

    public UpdateServiceUtils(){
        this.context= AbstractApplication.get();
        builder = new UpdateRequest.Builder(context);
    }

    public void checkUpdate(){
        try
        {
            builder.setVersionCheckStrategy(buildVersionCheckStrategy())
                    .setPreDownloadConfirmationStrategy(buildPreDownloadConfirmationStrategy())
                    .setDownloadStrategy(buildDownloadStrategy())
                    .setPreInstallConfirmationStrategy(buildPreInstallConfirmationStrategy())
                    .execute();

        }catch (Exception ex){

        }
    }

    private VersionCheckStrategy buildVersionCheckStrategy() {
        return (new SimpleHttpVersionCheckStrategy(ServiceURLConstants.UPDATE_APK_URL));
    }

    private ConfirmationStrategy buildPreDownloadConfirmationStrategy() {
        Notification n = NotificationUtils.getUpdateNotification(context, "Update Available", "Click to download the update!");
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return (new NotificationConfirmationStrategy(n));
    }

    private DownloadStrategy buildDownloadStrategy() {
        if (Build.VERSION.SDK_INT >= 11) {
            return (new InternalHttpDownloadStrategy());
        }

        return (new SimpleHttpDownloadStrategy());
    }

    private ConfirmationStrategy buildPreInstallConfirmationStrategy() {
        Notification n =NotificationUtils.getUpdateNotification(context, "Update Ready to Install", "Click to install the update!");
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        return (new NotificationConfirmationStrategy(n));
    }
}
