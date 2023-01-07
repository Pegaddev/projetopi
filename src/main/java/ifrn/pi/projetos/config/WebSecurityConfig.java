package ifrn.pi.projetos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
	        authorizeRequests()
			.antMatchers("/cadastro").permitAll()
			.antMatchers("/user/").hasRole("USUARIO")
			.antMatchers("/prof/").hasRole("PROFESSOR")
			.antMatchers("/admin/").hasRole("ADMIN")
			.anyRequest().authenticated()
		.and()
		.formLogin()
				.loginPage("/login")
				.permitAll()
			.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				.permitAll();
        
        return http.build();
    }
}