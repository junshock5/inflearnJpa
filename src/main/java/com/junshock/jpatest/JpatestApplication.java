package com.junshock.jpatest;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpatestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpatestApplication.class, args);
	}

	@Bean // 엔터티를 외부에 노출하면 좋지 않다.. , 나중 업데이트시 외부 연동 다 직접적으로 영향을 준다. 성능 저하.
	Hibernate5Module hibernate5Module(){
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		//hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5Module;
	}
}
