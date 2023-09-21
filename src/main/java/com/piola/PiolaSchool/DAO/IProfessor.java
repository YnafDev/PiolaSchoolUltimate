package com.piola.PiolaSchool.DAO;

import com.piola.PiolaSchool.model.Professor;
import org.springframework.data.repository.CrudRepository;

public interface IProfessor extends CrudRepository <Professor, Integer> {
}
