package com.owncloud.android.operations;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.common.utils.Log_OC;
import com.owncloud.android.lib.resources.shares.GetRemoteSharesForFileOperation;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by win7 on 2018/2/5.
 */

public class GetRemoteShareWithMeOperation extends RemoteOperation {
    private static final String TAG = GetRemoteSharesForFileOperation.class.getSimpleName();
    private static final String PARAM_PATH = "path";
    private static final String PARAM_RESHARES = "sharewith";
    private String mRemoteFilePath;
    private boolean mSharewithme;

    public GetRemoteShareWithMeOperation(String remoteFilePath, boolean sharewithme) {
        this.mRemoteFilePath = remoteFilePath;
        this.mSharewithme = sharewithme;
    }

    protected RemoteOperationResult run(OwnCloudClient client) {
        RemoteOperationResult result;
        GetMethod get = null;

        try {
            get = new GetMethod(client.getBaseUri() + "/ocs/v2.php/apps/files_sharing/api/v1/shares");
            get.setQueryString(new NameValuePair[]{new NameValuePair("format", "json"), new NameValuePair("path", this.mRemoteFilePath), new NameValuePair("shared_with_me", String.valueOf(this.mSharewithme))});
            get.addRequestHeader("OCS-APIREQUEST", "true");
            int status = client.executeMethod(get);
            String response;

            if(this.isSuccess(status)) {
                response = get.getResponseBodyAsString();
                JSONObject respJSON = new JSONObject(response);
                JSONObject respOCS = respJSON.getJSONObject("ocs");
                JSONArray respData = respOCS.getJSONArray("data");
                result = new RemoteOperationResult(true, get);
                ArrayList<Object> data = new ArrayList();
                data.add(respData);
                result.setData(data);
            } else {
                result = new RemoteOperationResult(false, get);
                response = get.getResponseBodyAsString();
                Log_OC.e(TAG, "Failed response while getting user information ");
                if(response != null) {
                    Log_OC.e(TAG, "*** status code: " + status + " ; response message: " + response);
                } else {
                    Log_OC.e(TAG, "*** status code: " + status);
                }
            }
        } catch (Exception var10) {
            result = new RemoteOperationResult(var10);
            Log_OC.e(TAG, "Exception while getting shares", var10);
        } finally {
            if(get != null) {
                get.releaseConnection();
            }

        }

        return result;
    }

    private boolean isSuccess(int status) {
        return status == 200;
    }
}
