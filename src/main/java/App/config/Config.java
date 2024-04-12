package App.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "addresses")
public class Config {
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<WebSite> getServers() { return servers; }

    public void setServers(List<WebSite> servers) { this.servers = servers; }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    private boolean enabled;
    private List<WebSite> servers = new ArrayList<>();
}
