package Siryakov.com.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Calendar;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {



    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;
}
