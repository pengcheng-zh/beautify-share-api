package com.pacal.share.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class HttpService {
    private final OkHttpClient oKHttpClient;

    public HttpService(@Autowired OkHttpClient okHttpClient) {
        this.oKHttpClient = okHttpClient;
    }

    public String callPost(Request request) {
        try {
            Response response = oKHttpClient.newCall( request ).execute();
            if ( response.isSuccessful() ) {
                if ( Objects.isNull( response.body() ) ) {
                    return "";
                }
                return response.body().string();
            } else {
                String errorMsg = response.body().string();
                log.error( "http request failed: {}", errorMsg );
                return errorMsg;
            }
        }catch ( Exception ex ) {
            log.error( "sendRequest failed: ", ex );
        }
        return "";
    }

    public String postRequest(String requestUrl, String jsonBody) {
        RequestBody requestBody = RequestBody.create( jsonBody, MediaType.parse( "application/json;charset=utf-8" ) );

        Request request = new Request.Builder()
                .url( requestUrl )
                .addHeader( "Accept", "application/json" )
                .addHeader( "Content-Type", "application/json" )
                .method( "POST", requestBody )
                .build();


        return callPost( request );
    }

    public String postRequest(String requestUrl, Map<String, String> headers, Map<String, String> params) {
        RequestBody requestBody = RequestBody.create( JSON.toJSONString( params ), MediaType.parse( ContentType.APPLICATION_FORM_URLENCODED.getMimeType() ) );
        try {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull( HttpUrl.parse( requestUrl ) ).newBuilder();
            if ( params!= null && !params.isEmpty() ) {
                for ( String key : params.keySet() ) {
                    urlBuilder.addQueryParameter( key, params.get( key ) );
                }
            }
            log.info( "headers: {}, params: {}, fullUrl: {}", headers, params, urlBuilder.build() );
            String fullUrl = urlBuilder.build().toString();
            Request.Builder requestBuilder = new Request.Builder()
                    .url( fullUrl )
                    .method( "POST", requestBody );
            if ( headers!= null && !headers.isEmpty() ) {
                for ( String key : headers.keySet() ) {
                    requestBuilder.addHeader( key, headers.get( key ) );
                }
            }
            Request request = requestBuilder.build();
            return callPost( request );
        } catch ( Exception ex ) {
            log.error( "getRequest failed: ", ex );
        }
        return "";
    }

    public String getRequest(String requestUrl, Map<String, String> headers, Map<String, String> params) {
        try {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull( HttpUrl.parse( requestUrl ) ).newBuilder();
            if ( params!= null && !params.isEmpty() ) {
                for ( String key : params.keySet() ) {
                    urlBuilder.addQueryParameter( key, params.get( key ) );
                }
            }
            log.info( "headers: {}, params: {}, fullUrl: {}", headers, params, urlBuilder.build() );
            String fullUrl = urlBuilder.build().toString();
            Request.Builder requestBuilder = new Request.Builder()
                    .url( fullUrl )
                    .method( "GET", null );
            if ( headers!= null && !headers.isEmpty() ) {
                for ( String key : headers.keySet() ) {
                    requestBuilder.addHeader( key, headers.get( key ) );
                }
            }
            Request request = requestBuilder.build();
            return callPost( request );
        } catch ( Exception ex ) {
            log.error( "getRequest failed: ", ex );
        }
        return "";
    }
}
