package com.example.bd2021bookdex.database;

import com.example.bd2021bookdex.Department;
import com.example.bd2021bookdex.Employee;
import com.example.bd2021bookdex.database.entities.*;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@org.springframework.context.annotation.Configuration
@Component
public class FactoryCreator {
    
    @Bean
    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Department.class);
        configuration.addAnnotatedClass(BookEntity.class);
        configuration.addAnnotatedClass(AuthorEntity.class);
        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(BookStatusEntity.class);
        configuration.addAnnotatedClass(ChangesEntity.class);
        configuration.addAnnotatedClass(TagEntity.class);
        configuration.addAnnotatedClass(BookCollectionEntity.class);
        return configuration.buildSessionFactory();
    }
}
