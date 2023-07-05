package com.luxoft.homework.repostory;

import com.luxoft.homework.domain.Person;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

    List<Person> findAll();
}
