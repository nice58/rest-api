package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ApiConfig extends Config {

    @Key("baseURI")
    String getBaseUrl();

    @Key("basePath")
    String getBasePath();
}
