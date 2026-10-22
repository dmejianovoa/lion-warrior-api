package co.edu.sena.lionwarrior.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Configuración general: encriptador de contraseñas y CORS para los frontends. */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    // Spring entrega esta misma instancia a quien pida un PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Permite que Angular (4200) y React con Vite (5173) llamen a la API
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
