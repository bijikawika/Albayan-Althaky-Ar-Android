package com.kawika.smart_survey.pushNotification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Siva on 13-03-18.
 */

public class FcmTokenInitialise extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        System.out.println("refreshedToken dcdcd= " + refreshedToken);


    }
}
