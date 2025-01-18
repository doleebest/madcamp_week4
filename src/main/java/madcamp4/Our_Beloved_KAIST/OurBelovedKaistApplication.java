package madcamp4.Our_Beloved_KAIST;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
public class OurBelovedKaistApplication {

	public static void main(String[] args) {
		SpringApplication.run(OurBelovedKaistApplication.class, args);
	}

}
