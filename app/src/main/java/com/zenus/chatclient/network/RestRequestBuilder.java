package com.zenus.chatclient.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RestRequestBuilder {
    protected String url;
    protected List<NameValuePair> headers;
    protected List<NameValuePair> postParams;
    protected List<NameValuePair> urlParams;
    protected RestCallback callback;
    protected byte[] postBody;
    private RequestType type;
    private ResponseType responseType;
    private String tag;

    public RestRequestBuilder(RequestType type, String url) {
        this.type = type;
        this.url = url;
        this.responseType = ResponseType.JsonObject;
        this.headers = new ArrayList<>();
        this.postParams = new ArrayList<>();
        this.urlParams = new ArrayList<>();
    }

    public RestRequestBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getRequestUrl() {
        if (urlParams == null) return url;
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        Iterator<NameValuePair> it = urlParams.iterator();
        while (it.hasNext()) {
            NameValuePair pair = it.next();
            sb.append(pair.getName() + "=" + pair.getValue());
            if (it.hasNext())
                sb.append("&");
        }
        return sb.toString();
    }

    public RestRequestBuilder setHeaders(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.headers.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public RestRequestBuilder setPostBody(byte[] postBody) {
        this.postBody = postBody;
        return this;
    }

    public RestRequestBuilder setPostParams(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public RestRequestBuilder setURLParams(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.urlParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public RestRequestBuilder setCallback(RestCallback callback) {
        this.callback = callback;
        return this;
    }

    public enum RequestType {
        GET, POST, POST_BODY, DELETE
    }

    public enum ResponseType {
        JsonObject, JsonArray
    }

    public RestRequestBuilder setResponseType(ResponseType responseType) {
        this.responseType = responseType;
        return this;
    }

    public Request build() {
        switch (type) {
            case GET:
                String actualUrl = getRequestUrl();
                if (responseType.equals(ResponseType.JsonObject)) {
                    return new JsonObjectRequest(Request.Method.GET, actualUrl, new JSONObject(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    callback.onResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    callback.onError(error);
                                }
                            }){
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> _headers = new HashMap<>();
                                    for (NameValuePair pair : headers)
                                        _headers.put(pair.getName(), pair.getValue());
                                    return _headers;
                                }
                            };
                } else if (responseType.equals(ResponseType.JsonArray)){
                    return new JsonArrayRequest(Request.Method.GET, actualUrl, new JSONArray(),
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    callback.onResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    callback.onError(error);
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> _headers = new HashMap<>();
                                    for (NameValuePair pair : headers)
                                        _headers.put(pair.getName(), pair.getValue());
                                    return _headers;
                                }
                            };
                }
            case POST:
                return new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                callback.onResponse(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                NetworkResponse response = error.networkResponse;
                                if (error instanceof ServerError && response != null) {
                                    try {
                                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        // Now you can use any deserializer to make sense of data
                                        JSONObject obj = new JSONObject(res);
                                    } catch (UnsupportedEncodingException e1) {
                                        // Couldn't properly decode data to string
                                        e1.printStackTrace();
                                    } catch (JSONException e2) {
                                        // returned data is not JSONObject?
                                        e2.printStackTrace();
                                    }
                                }

                                callback.onError(error);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        for (NameValuePair pair : postParams)
                            params.put(pair.getName(), pair.getValue());
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> _headers = new HashMap<>();
                        for (NameValuePair pair : headers)
                            _headers.put(pair.getName(), pair.getValue());
                        return _headers;
                    }
                };
            case POST_BODY:
                if (responseType.equals(ResponseType.JsonObject)) {
                    return new JsonObjectRequest(Request.Method.POST, url, new JSONObject(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    callback.onResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    callback.onError(error);
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            for (NameValuePair pair : postParams)
                                params.put(pair.getName(), pair.getValue());
                            return params;
                        }

                        @Override
                        public byte[] getBody() {
                            return postBody;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> _headers = new HashMap<>();
                            for (NameValuePair pair : headers)
                                _headers.put(pair.getName(), pair.getValue());
                            return _headers;
                        }
                    };
                } else if (responseType.equals(ResponseType.JsonArray)){
                    return new JsonArrayRequest(Request.Method.POST, url, new JSONArray(),
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    callback.onResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    callback.onError(error);
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            for (NameValuePair pair : postParams)
                                params.put(pair.getName(), pair.getValue());
                            return params;
                        }

                        @Override
                        public byte[] getBody() {
                            return postBody;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> _headers = new HashMap<>();
                            for (NameValuePair pair : headers)
                                _headers.put(pair.getName(), pair.getValue());
                            return _headers;
                        }
                    };
                }
            case DELETE:
                if (responseType.equals(ResponseType.JsonObject)) {
                    return new JsonObjectRequest(Request.Method.DELETE, url, new JSONObject(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    callback.onResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    callback.onError(error);
                                }
                            });
                } else if (responseType.equals(ResponseType.JsonArray)){
                    return new JsonArrayRequest(Request.Method.DELETE, url, new JSONArray(),
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    callback.onResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    callback.onError(error);
                                }
                            });
                }
            default:
                return null;
        }
    }
}
