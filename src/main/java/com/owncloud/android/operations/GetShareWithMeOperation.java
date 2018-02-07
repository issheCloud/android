package com.owncloud.android.operations;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.common.utils.Log_OC;
import com.owncloud.android.operations.common.SyncOperation;

/**
 * Created by win7 on 2018/2/5.
 */

public class GetShareWithMeOperation extends SyncOperation {
    private static final String TAG = GetShareWithMeOperation.class.getSimpleName();

    private String mPath;
    private boolean mSharewithme;

    /**
     * Constructor
     *
     * @param path          Path to file or folder
     * @param sharewithme   Whether to share with me
     */
    public GetShareWithMeOperation(String path, boolean sharewithme) {
        mPath = path;
        mSharewithme = sharewithme;
    }

    @Override
    protected RemoteOperationResult run(OwnCloudClient client) {
        GetRemoteShareWithMeOperation operation = new GetRemoteShareWithMeOperation(mPath, mSharewithme);
        RemoteOperationResult result = operation.execute(client);

        if (result.isSuccess()) {
            Log_OC.d(TAG, "File = " + mPath + " Share list size  " + result.getData().size());

        } else if (result.getCode() == RemoteOperationResult.ResultCode.SHARE_NOT_FOUND) {
            Log_OC.d(TAG, "File = " + mPath + " not found  ");

        }

        return result;
    }
}
