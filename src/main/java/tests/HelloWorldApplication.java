package tests;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class HelloWorldApplication extends WebApplication {
    public Class<? extends Page> getHomePage() {
        return WeatherPage.class;
    }
}
