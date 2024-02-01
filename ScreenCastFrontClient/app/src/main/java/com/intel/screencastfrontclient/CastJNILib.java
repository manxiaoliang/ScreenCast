package com.intel.screencastfrontclient;

public class CastJNILib {
    static {
       System.loadLibrary("CastFrontClientJni");
        // System.loadLibrary("screencastfrontclient");
    }

    public static native String stringFromJNI();

    public static native void init(String ip,int port);
    public static native void startCast();
    public static native void stopCast();
}

