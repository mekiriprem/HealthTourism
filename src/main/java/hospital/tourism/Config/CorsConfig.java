package hospital.tourism.Config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class CorsConfig implements WebMvcConfigurer{
	/*"https://hospital-tourism-fe.vercel.app"*/
	// "http://localhost:8081/"
//	@Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(@NonNull CorsRegistry registry) {
//                registry.addMapping("/**")  // allow all paths
//                        .allowedOriginPatterns(
//                            "https://hospital-tourism-fe.vercel.app",
//                            "http://localhost:*",
//                            "http://127.0.0.1:*",
//                            "https://medi-tailor.com"
//                        )
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
//                        .allowedHeaders("*")
//                        .exposedHeaders("*")
//                        .allowCredentials(true)
//                        .maxAge(3600);
//            }
//        };
//    }
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .cors(cors -> cors.configurationSource(request -> {
//                var corsConfiguration = new CorsConfiguration();
//                corsConfiguration.setAllowedOriginPatterns(java.util.List.of(
//                    "https://hospital-tourism-fe.vercel.app",
//                    "http://localhost:*",
//                    "http://127.0.0.1:*",
//                    "https://medi-tailor.com"
//                ));
//                corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
//                corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
//                corsConfiguration.setExposedHeaders(java.util.List.of("*"));
//                corsConfiguration.setAllowCredentials(true);
//                corsConfiguration.setMaxAge(3600L);
//                return corsConfiguration;
//            }))
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll()
//            )
//            .formLogin(form -> form.disable())
//            .httpBasic(httpBasic -> httpBasic.disable());
//
//        return http.build();
//    }

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of(
                    "http://localhost:8081",       // your frontend port
                    "http://localhost:3000",       // common React dev port
                    "http://127.0.0.1:8081",
                    "https://medi-tailor.com",     // production
                    "https://hospital-tourism-fe.vercel.app"
                ));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                config.setAllowedHeaders(List.of("*"));
                config.setExposedHeaders(List.of("Authorization", "Content-Type"));
                config.setAllowCredentials(true); // for cookies/sessions
                config.setMaxAge(3600L);
                return config;
            }))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

