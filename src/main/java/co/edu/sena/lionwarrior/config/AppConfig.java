package co.edu.sena.lionwarrior.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

/** Configuración general: encriptador de contraseñas y CORS para los frontends. */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    // Spring entrega esta misma instancia a quien pida un PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Convertir texto separado por comas en un arreglo
    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    // Origenes vienen de configuración
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
