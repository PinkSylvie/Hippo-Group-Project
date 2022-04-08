package com.example.hippo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseUser.registerSubclass(HippoUser.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Vv8wrtFmdvXXtvvPPnOOy4ifzd88n0oNBnCGYYmQ")
                .clientKey("KuzApe2Ol2q90tNYpAYSRE177n88R0HPteoiD4w1")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
