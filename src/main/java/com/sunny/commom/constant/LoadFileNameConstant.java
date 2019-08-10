package com.sunny.commom.constant;

import com.sunny.source.bean.LoadFileName;
import com.sunny.source.file.LoadJson;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;

public class LoadFileNameConstant {

    public static final LoadFileName APPLICATION_YAML =
            new LoadFileName("application.yaml", 10, LoadYaml.getInstance());
    public static final LoadFileName APPLICATION_YML =
            new LoadFileName("application.yml", 100, LoadYaml.getInstance());
    public static final LoadFileName APPLICATION_PROPERTIES =
            new LoadFileName("application.properties", 1, LoadProperties.getInstance());
    public static final LoadFileName APPLICATION_XML =
            new LoadFileName("application.xml", 1000, LoadXml.getInstance());
    public static final LoadFileName APPLICATION_JSON =
            new LoadFileName("application.json", 1000, LoadJson.getInstance());


}
