package local.epul4a.fotosharing.config;

import local.epul4a.fotosharing.service.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        // Permet l'accès aux images sans token CSRF
                        .ignoringRequestMatchers("/img/**", "/thumb/**")
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // Accès public aux pages de base
                                .requestMatchers("/", "/index", "/login", "/register/**").permitAll()

                                // Accès public aux ressources statiques
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                                // Accès public aux images et détails de photos
                                .requestMatchers("/img/**", "/thumb/**").permitAll()
                                .requestMatchers("/photo/{id:[0-9]+}").permitAll()  // Détails photos publiques

                                // Routes nécessitant authentification
                                .requestMatchers("/photo/upload", "/photo/*/edit", "/photo/*/delete").authenticated()
                                .requestMatchers("/photo/*/share", "/photo/*/comment").authenticated()

                                // Administration
                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                // Par défaut : authentification requise
                                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/perform_login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/index", true)
                                .failureUrl("/login?error")
                                .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
}

