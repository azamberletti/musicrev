package com.example.andrea.musicreview.utility;

import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import org.json.JSONObject;

public class FacebookInformationHelper {

    private String facebookAPIResponse;

    public String getRecommendedAlbums() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        if(object != null){
                            Log.v("FACEBOOK_JSON", object.toString());
                            facebookAPIResponse = object.toString();
                        }

                        //new ListDownloader().execute("");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "music");
        request.setParameters(parameters);
        request.executeAndWait();
        return facebookAPIResponse;
    }

}
