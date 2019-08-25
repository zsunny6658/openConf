package com.sunny.source.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.commom.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * create by zsunny
 * data: 2018/10/20
 **/
public class LoadJson extends AbstractLoadProperties {

    private LoadJson() {
    }

    private static class LoadXmlHolder {
        private static LoadJson loadJson = new LoadJson();
    }

    public static LoadJson getInstance() {
        return LoadXmlHolder.loadJson;
    }

    @Override
    public Object loadSources(String path) throws Exception {
        return jsonToMap(path);
    }

    private Object jsonToMap(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream in = FileUtils.getFileInputStream(path);
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };

        return mapper.readValue(in, typeRef);
    }
}
