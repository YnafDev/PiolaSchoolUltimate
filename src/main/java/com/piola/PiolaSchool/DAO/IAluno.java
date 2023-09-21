package com.piola.PiolaSchool.DAO;

import com.piola.PiolaSchool.model.Aluno;
import org.springframework.data.repository.CrudRepository;

public interface IAluno extends CrudRepository <Aluno, Integer> {


}
