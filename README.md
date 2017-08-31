<h1>Spring Boot Server Prototype</h1>

<h3>Add dependency</h3>

```xml
    <properties>
        <org.springframework.boot.version>1.5.6.RELEASE</org.springframework.boot.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
            <version>${org.springframework.boot.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${org.springframework.boot.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
            <version>${org.springframework.boot.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>${org.springframework.boot.version}</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.43</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Optional, for bootstrap -->
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>3.3.7</version>
        </dependency>

        <dependency>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-core</artifactId>
            <version>3.5.3</version>
        </dependency>
       
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
    </dependencies>
```

<h3>Set the application properties</h3>

```properties
#Log

logging.level.org.springframework.web=trace
logging.level.org.hibernate=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder= TRACE

#Database

spring.datasource.url=jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=qwertyui
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect

spring.jpa.hibernate.ddl-auto=none

spring.jpa.show-sql=true

liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml
```

<h2>Add liquibase script to create and modify database structure</h2>

<h3>Create role table</h3>

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
		http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd">


    <changeSet author="pethoalpar" id="tbl_role" runOnChange="true">
        <preConditions onFail="MARK_RAN">
            <not> <tableExists tableName="role" /> </not>
        </preConditions>
        <createTable tableName="role">
            <column name="id" type="int(11)" autoincrement="true">
                <constraints nullable="false"/>
            </column>
            <column name="role_name" type="varchar(100)">
                <constraints nullable="false"/>
            </column>
            <column name="active" type="int(1)" defaultValue="NULL"/>
        </createTable>
    </changeSet>

    <changeSet author="pethoalpar" id="pk_role" runOnChange="true">
        <preConditions onFail="MARK_RAN">
            <not> <primaryKeyExists tableName="role"/> </not>
        </preConditions>
        <addPrimaryKey
                constraintName="pk_role"
                tableName="role"
                columnNames="id"/>
        <rollback />
    </changeSet>

    <changeSet author="atraxo" id="uk_role_1" runOnChange="true">
        <preConditions onFail="MARK_RAN">
            <not> <indexExists indexName="uk_role_1" /> </not>
        </preConditions>
        <addUniqueConstraint
                constraintName="uk_role_1"
                tableName="role"
                columnNames="role_name"/>
    </changeSet>

</databaseChangeLog>
```

<h3>Create user table and foeign key</h3>

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
		http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd">

    <changeSet author="atraxo" id="tbl_user" runOnChange="true">
        <preConditions onFail="MARK_RAN">
            <not> <tableExists tableName="user" /> </not>
        </preConditions>
        <createTable tableName="user">
            <column name="role_id" type="int(11)" autoincrement="true">
                <constraints nullable="false"/>
            </column>
            <column name="id" type="int(11)">
                <constraints nullable="false"/>
            </column>
            <column name="user_name" type="varchar(255)">
                <constraints nullable="false"/>
            </column>
            <column name="password" type="varchar(255)">
                <constraints nullable="false"/>
            </column>
            <column name="email" type="varchar(255)">
                <constraints nullable="true"/>
            </column>
            <column name="phone_number" type="varchar(255)">
                <constraints nullable="true"/>
            </column>
        </createTable>
    </changeSet>

    <changeSet author="atraxo" id="pk-user" runOnChange="true">
        <preConditions onFail="MARK_RAN">
            <not> <primaryKeyExists tableName="user"/> </not>
        </preConditions>
        <addPrimaryKey
                constraintName="pk_user"
                tableName="user"
                columnNames="id"/>
        <rollback />
    </changeSet>

    <changeSet author="atraxo" id="uk_user_1" runOnChange="true">
        <preConditions onFail="MARK_RAN">
            <not> <indexExists indexName="uk_user_1" /> </not>
        </preConditions>
        <addUniqueConstraint
                constraintName="uk_user_1"
                tableName="user"
                columnNames="user_name"/>
    </changeSet>

    <changeSet author="pethoalpar" id="fk_user_1" runOnChange="true">
        <preConditions onFail="MARK_RAN">
            <not> <foreignKeyConstraintExists foreignKeyName="fk_user_1" /> </not>
        </preConditions>
        <addForeignKeyConstraint constraintName="fk_user_1"
                                 baseTableName="user"
                                 baseColumnNames="role_id"
                                 referencedTableName="role"
                                 referencedColumnNames="id"/>
    </changeSet>

</databaseChangeLog>
```

<h3>DB changelog. Liquibase config</h3>

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
		http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd">



    <include file="/db/changelog/create_role.xml"/>
    <include file="/db/changelog/create_user.xml"/>

</databaseChangeLog>
```

<h3>Security configuration</h3>

```java
@Controller
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/home","/about").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAnyAuthority("USER","ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{

        auth.userDetailsService(myUserDetailService).passwordEncoder(new BCryptPasswordEncoder());

    }
}
```

<h3>Set up the default controller</h3>

```java
@Controller
public class DefaultController {

    @GetMapping("/")
    public String home(){
        return "/home";
    }

    @GetMapping("/home")
    public String home1(){
        return "/home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "/admin";
    }

    @GetMapping("/user")
    public String user (){
        return "/user";
    }

    @GetMapping("/about")
    public String about(){
        return "/about";
    }

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @GetMapping("/error")
    public String error(){
        return "/invalidUser";
    }

    @GetMapping("/403")
    public String invalidUser(){
        return "/invalidUser";
    }
}
```

<h3>Role entity</h3>

```java
@Entity
public class Role{

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, precision = 11)
    private Integer id;

    @NotBlank
    @Column(name = "role_name", nullable = false, length = 255)
    private String roleName;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = User.class, mappedBy = "role", cascade = {CascadeType.DETACH})
    private Collection<User> users;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Collection<User> getUsers() {
        return this.users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    /**
     * @param e
     */
    public void addToUsers(User e) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        e.setRole(this);
        this.users.add(e);
    }
}
```

<h3>User entity</h3>

```java
@Entity
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "id", nullable = false, precision = 11)
    private Integer id;

    @NotBlank
    @Column(name = "user_name", nullable = false, length = 255)
    private String userName;

    @NotBlank
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
```

<h3>Role repository</h3>

```java
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
```

<h3>User repository</h3>

<h5>Spring boot automatically generates queries if the method name is correct.</h5>

```java
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserName(String userName);
}
```

<h3>User default service</h3>

```java
@Service
public class MyUserDetailService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(userName);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(), Arrays.asList(authority));
        logger.info(user.getRole().getRoleName());
        return userDetails;
    }
}
```

<h3>Main</h3>

```java
@SpringBootApplication
public class Main implements CommandLineRunner {

    public static void main(String [] args){
        SpringApplication.run(Main.class, args);
    }

    public void run(String... strings) throws Exception {
    }
}
```
