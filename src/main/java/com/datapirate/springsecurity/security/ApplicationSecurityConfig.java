package com.datapirate.springsecurity.security;

import com.datapirate.springsecurity.auth.ApplicationUserService;
import com.datapirate.springsecurity.jwt.JwtTokenVerifier;
import com.datapirate.springsecurity.jwt.JwtConfig;
import com.datapirate.springsecurity.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

import static com.datapirate.springsecurity.security.ApplicationUserRole.*;

/**
 * @author Akash on 16-Jun-20
 */
@Configuration
@EnableWebSecurity
// TODO: Ye @EnableGlobalMethodSecurity(prePostEnabled = true) hum us time use krty hn jis time hmen method level security chahiye hoti hy
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    // 1: https://youtu.be/her_7pa0vrg?t=4324 start video from here. 😊
    // 2: https://youtu.be/her_7pa0vrg?t=8062 start video from here. 😊
    // 3: https://youtu.be/her_7pa0vrg?t=10830 start video from here. 😊

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService,
                                     JwtConfig jwtConfig, SecretKey secretKey) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("Inside configure(HttpSecurity http) method");

        // Is tareeke mn hum jis jis url ko permit kren gy wo wo urls without authentication chal jaen gi.
        http
                .csrf() // TODO: CROSS-SITE REQUEST FORGERY
                    .disable()

                // TODO: Or we can crsf as follow. Ye sirf smjhne k liye likha hy baaki inta zaroori nhn h
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()

                // TODO: agr formLogin() use kr rahy hn to neche waly 5 methods ko comment kren yw sirf hum jwt k liye use kr rahy hn
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)

                .authorizeRequests()

                // TODO: Ye method hum patterns define krne k liye use krty hn k kis kism ki url ko hmen authenticate krna h or kis ko nhn.
                .antMatchers("/", "index", "/css/*", "/js/*")

                // TODO: Ye method antMatchers k sath use hota hy k hmen is paticular pattern permission dene hy is url ki ya nhn.
                .permitAll()

                .antMatchers("/api/**")

                // TODO: Is method mn hum role based api's ki permission dete hn.
                .hasRole(STUDENT.name())

                // TODO: Yehan pr hum permission based authentication kr rahy hn k jo user is particular url ko hit kr rahy h us ko is permission hy ya nhn
                // TODO: Order does matter in antMatchers() method be careful
//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())

                //  TODO: Ye oper antMatchers() method is liye comment kiye hn k hum ye hi kaam annotation se bhi kr skhty hn @PreAuthorize() k saath

                .anyRequest()
                .authenticated();

                // TODO: Neche wala code is liye comment kiya hy k hum ab JWT use kren gy.
                /*.and()

                // TODO: Agr hum ye method use kren gy to mtlb hum form based authentication use kr rahy hn (By default spring ye hi use kr rah hota hy)
                .formLogin()

                    // TODO: Agr hum apna custom login page use krna chahity hn to is method k through default login page ko override kren gy
                    .loginPage("/login").permitAll()

                    // TODO: Ye method humain redirect kr de ga next page pe agr agr humari authentication success ho jati hy to;
                    .defaultSuccessUrl("/courses", true)
                    .passwordParameter("password") // TODO: agr hum apne custom passwordParameter bna rahy hn to is method ka use kren lazmi otherwise ye defualt consider krta hy
                    .usernameParameter("username") // TODO: agr hum apne custom usernameParameter bna rahy hn to is method ka use kren lazmi otherwise ye defualt consider krta hy
                .and()

                // TODO: Agr hum is method ko use kr rahy hn to hume by default server ak cookie deta hy jis ki max-age 2 weeks hoti hy. Agr hum chahen to isy customized bhi kr skhty hn.
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("somethingverysecured")
                    .rememberMeParameter("remember-me") // TODO: agr hum apne custom rememberMeParameter bna rahy hn to is method ka use kren lazmi otherwise ye defualt consider krta hy
                .and()
                .logout()
                    .logoutUrl("/logout")

                    // TODO: agr neche waly method bary mn sahe smjhna hy to is url pe jao: https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html#logoutUrl-java.lang.String-
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))

                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");*/

                // TODO: Ye maine is liye comment kiya hy k mujhe form based authentication use krni thi.
//                .httpBasic();




        // TODO: Is tareeke mn hmari har ak request authenticate hone h k baad agy process hogi bus hmare pass username & pass hona chahiye..
        // TODO: ku k hum yehan role based ya permission based authentication use nhn kr rahy.
        /*http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()

                // Basic auth mn hum hr bar username & password bhejty hn server ko.
                .httpBasic();*/
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    /*@Override
    @Bean
    protected UserDetailsService userDetailsService() {
        System.out.println("Inside userDetailsService() method");

        UserDetails akashUser = User.builder()
                .username("akash")
                .password(passwordEncoder.encode("password"))
//                .roles(STUDENT.name()) //TODO: ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMIN.name()) //TODO: ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMINTRAINEE.name()) //TODO: ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                akashUser,
                adminUser,
                tomUser
        );
    }*/
}
