package etc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    // 인풋 스트림에서 문자열을 끝까지 읽어 하나의 string으로 반환합니다.
    public static ArrayList<String> inputStreamToLines(InputStream in){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ArrayList<String> linesArray = new ArrayList<String>();
        String line;

        try {
            line = reader.readLine();
            while(!line.isEmpty()){
                linesArray.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return linesArray;
    }

    // url param 파싱할 때 사
    // a=1&b=2&c=3이라는 문자열을 넣고 outerDelimiter에 &, innerDelimiter에 "="을 넣으면 {a:1,b:2,c:3}의 map이 나온다.
    public static Map<String,String> parseStringToMap(String string, String outerDelimiter, String innerDelimiter){
        Map<String,String> map = new HashMap<>();
        for(String param : string.split(outerDelimiter)){
            String[] keyval = param.split(innerDelimiter);
            map.put(keyval[0],keyval[1]);
        }
        return map;
    }
}
