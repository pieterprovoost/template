package be.pieterprovoost.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
public class DemoApplication {

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Autowired
        private BCryptPasswordEncoder encoder;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .formLogin().and()
                .logout().and()
                .authorizeRequests()
                .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll().anyRequest()
                .authenticated().and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                .csrf().csrfTokenRepository(csrfTokenRepository());
        }

        @Autowired
        public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(encoder)
                .usersByUsernameQuery("select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery("select users.username, role.role from users left join role on role.user_id = users.id where username = ?");
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/bower_components/**");
        }

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource datasource = new DriverManagerDataSource();
            datasource.setDriverClassName("org.postgresql.Driver");
            datasource.setUrl("jdbc:postgresql://localhost/demo");
            datasource.setUsername("demo");
            datasource.setPassword("demo");
            return datasource;
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource());
            factoryBean.setPackagesToScan("be.pieterprovoost.demo.model");
            JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            factoryBean.setJpaVendorAdapter(vendorAdapter);
            factoryBean.setJpaProperties(this.additionalProperties());
            return factoryBean;
        }

        Properties additionalProperties() {
            Properties properties = new Properties();
            properties.setProperty("hibernate.show_sql", "false");
            properties.setProperty("hibernate.format_sql", "false");
            properties.setProperty("hibernate.hbm2ddl.auto", "update");
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            return properties;
        }

    }

    private static CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}