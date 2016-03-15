package com.sk.library.http;

import java.util.Map;

/**
 * Created by sk on 16/2/23.
 * http listener
 */
public interface IHttpHelper {


    /**
     * http get request
     *
     * @param baseUrl
     * @param params
     */
    void doGet(String baseUrl, Map<String, String> params);


    /**
     * http post request
     *
     * @param baseUrl
     * @param params
     */
    void doPost(String baseUrl, Map<String, String> params);

    /**
     * post file
     * @param baseUrl
     * @param filePath   file path
     */
    void doPostFile(String baseUrl, String filePath);
}
