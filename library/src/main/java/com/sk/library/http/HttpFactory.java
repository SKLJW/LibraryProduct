package com.sk.library.http;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sk on 16/2/23.
 * http request factory
 */
public class HttpFactory {


    private static final String PREFIX = HttpContant.HTPPMODE.toLowerCase() + ".";
    private static final String SUFFIX = "HttpManager";

    public static IHttpHelper getHttpHelper(Context context, HttpListener httpListener) {
        IHttpHelper iHttpHelper = null;

        String pkgName = HttpFactory.class.getPackage().getName() + ".";
        try {
            String clsName = pkgName + PREFIX + HttpContant.HTPPMODE + SUFFIX;
            Constructor mConstructor = Class.forName(clsName).getConstructor(Context.class, HttpListener.class);
            iHttpHelper = (IHttpHelper) mConstructor.newInstance(context, httpListener);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return iHttpHelper;
    }
}
