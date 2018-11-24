package com.demo.abel.framework.mvc;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 视图解析
 * @author: liuzijian
 * @date: 2018-11-22 11:18
 */
@Setter
@Getter
public class ViewResolver {
    private String viewName;
    private String fullPath;
    private File template;

    public String resolve(ModelAndView mv){
        try {
            Map<String,Object> model = mv.getModel();
            RandomAccessFile file = new RandomAccessFile(template,"r");
            StringBuilder content = new StringBuilder();
            String line;
            while (null!=(line=file.readLine())){
                Pattern pattern = Pattern.compile(".*@\\{\\s*(\\w+)\\s*}.*");
                Matcher matcher = pattern.matcher(line);
                if(matcher.matches()){
                    Object value = model.get(matcher.group(1));
                    line = line.replaceAll("@\\{\\s*(\\w+)\\s*}",value.toString()) +"<br/>";
                }
                content.append(line);
            }

            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
