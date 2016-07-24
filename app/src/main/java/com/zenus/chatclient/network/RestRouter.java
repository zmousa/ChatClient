package com.zenus.chatclient.network;


public class RestRouter {
    private static final String BASE_URL = "http://$ip$/$folder$index.php/restful/api/";
    private static final String SURVEY_MODEL = "survey";
    private static final boolean IS_LOCAL_TESTING = false;

    private StringBuilder urlBuilder;

    public enum Route {
        CHAT_MESSAGE_POST("create/chatmessage/");

        private String route;

        public String getRoute() {
            return route;
        }

        private Route(String route) {
            this.route = route;
        }
    }

    private RestRouter() {
        String IP, folder;

        IP = "";
        folder = "";

        String resultURL = BASE_URL.replace("$ip$", IP);
        resultURL = resultURL.replace("$folder$", folder);
        urlBuilder = new StringBuilder(resultURL);
    }

    public static RestRouter getDefault() {
        return new RestRouter();
    }

    public final String to(Route to) {
        urlBuilder.append(to.route);
        return urlBuilder.toString();
    }

    public final String toId(Route to, String id) {
        urlBuilder.append(to.route).append(id);
        return urlBuilder.toString();
    }

    public final String direct(String url) {
        return urlBuilder.append(url).toString();
    }


}
