package etc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpRequest implements HttpRequest {
    public static final String KEY_METHOD = "_METHOD";
    public static final String KEY_PROTOCOL = "_PROTOCOL";
    public static final String KEY_PATH = "_PATH";
    public static final String KEY_BODY = "_BODY";

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpRequest.class);
    private Map<String,String> headerMap;
    private Map<String,String> paramsMap;
    private String body;

    private SimpleHttpRequest(){}

    private Map<String,String> parseParamString(String paramString){
        return StringUtil.parseStringToMap(paramString, "&", "=");
    }

    //첫 줄 파싱. 예시 : "GET /user?q=john&religion=atheism HTTP/1.1"
    public Map<String,String> parseFirstLineOfHTTPRequest(String firstLine){
        Map<String,String> result = new HashMap<>();
        String[] methodParamsProtocol = firstLine.split(" "); // split to method, path+params, protocol
        String[] pathAndParams = methodParamsProtocol[1].split("\\?"); // split to path, params
        paramsMap = parseParamString(pathAndParams[1]);

        for(String key : paramsMap.keySet()) result.put(key, paramsMap.get(key));
        result.put(KEY_METHOD, methodParamsProtocol[0]);
        result.put(KEY_PROTOCOL, methodParamsProtocol[2]);
        result.put(KEY_PATH, pathAndParams[0]);
        return result;
    }

    public Map<String, String> parseHeaderExceptFirstLine(List<String> headerPartList){
        Map<String,String> result = new HashMap<>();
        for(String line : headerPartList){
            String[] keyval = line.split(": ");
            result.put(keyval[0], keyval[1]);
        }
        return result;
    }

    public Map<String, String> parseHTTPHeader(List<String> headerLines){
        Map<String,String> result = parseFirstLineOfHTTPRequest(headerLines.get(0));
        Map<String,String> parsedExceptFirstLine = parseHeaderExceptFirstLine(headerLines.subList(1, headerLines.size()));
        result.putAll(parsedExceptFirstLine);
        headerMap = result;
        return result;
    }

    public List<List<String>> splitHeaderAndBody(List<String> linesOfHTTPRequest){
        List<List<String>> result = new ArrayList<>();
        for(int i = 0; i < linesOfHTTPRequest.size(); i++){
            if (linesOfHTTPRequest.get(i).equals("")){
                result.add(linesOfHTTPRequest.subList(0,i));
                result.add(linesOfHTTPRequest.subList(i+1, linesOfHTTPRequest.size()));
                return result;
            }
        }
        result.add(linesOfHTTPRequest.subList(0, linesOfHTTPRequest.size()));
        return result;
    }

    public Map<String,String> parseHTTPRequest(List<String> linesOfHTTPReqeust) throws Exception{
        List<List<String>> headerAndBody = splitHeaderAndBody(linesOfHTTPReqeust);
        Map<String,String> result = parseHTTPHeader(headerAndBody.get(0));
        if(headerAndBody.size() == 2) {
            String body = String.join(",", headerAndBody.get(1));
            result.put(KEY_BODY, body);
            this.body = body;
        }
        return result;
    }

    public SimpleHttpRequest(InputStream inputStream){
        try {
            ArrayList<String> requestHeaderList = StringUtil.inputStreamToLines(inputStream);
            this.headerMap = parseHTTPRequest(requestHeaderList);
        } catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    @Override
    public METHOD getMethod() {
        return METHOD.findMethodByValue(headerMap.get(KEY_METHOD));
    }

    @Override
    public String getPath() {
        return headerMap.get(KEY_PATH);
    }

    @Override
    public Map<String, String> getParams() {
        return paramsMap;
    }

    @Override
    public String getBody() {
        return body;
    }
}
