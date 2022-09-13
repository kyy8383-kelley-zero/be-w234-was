package etc;

import java.util.Map;

public interface HttpRequest {
    public static enum METHOD {
        GET("GET"), POST("POST"), DELETE("DELETE"), PUT("PUT");

        private final String value;

        METHOD(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }

        public static METHOD findMethodByValue(String methodValue){
            for(METHOD method : METHOD.values()){
                if(method.getValue().equals(methodValue)){
                    return method;
                }
            }
            return null;
        }
    };

    public Map<String,String> getHeaderMap();
    public METHOD getMethod();
    public String getPath();
    public Map<String,String> getParams();
    public String getBody();
}
