package com.piola.PiolaSchool.controller;

import com.piola.PiolaSchool.DAO.IAdministrador;
import com.piola.PiolaSchool.DAO.IProfessor;
import com.piola.PiolaSchool.model.Aluno;
import com.piola.PiolaSchool.model.Professor;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/Professor")
public class ProfessorController {
    private PasswordEncoder passWordEncoder;
    public ProfessorController(IProfessor dao){
        this.passWordEncoder = new BCryptPasswordEncoder();
    }
    @Autowired
    private IProfessor dao;
    @GetMapping
    public List<Professor> ProfessorList (){
        return (List<Professor>)dao.findAll();
    }
    @PostMapping
    public Professor criarProfessor(@Valid @RequestBody Professor professor){
        String encoder =this.passWordEncoder.encode(professor.getSenha());
        professor.setSenha(encoder);
        Professor ProfessorCreate =dao.save(professor);
        return ProfessorCreate;}
    @DeleteMapping("/{matricula}")
    public Optional<Professor> deletarProfessor(@PathVariable Integer matricula){
        Optional<Professor> Professor =dao.findById(matricula);
        dao.deleteById(matricula);
        return Professor;}
    public Map<String,String> handleValidatioExpetion(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName =((FieldError)error).getField();
            String errorMessage =error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        } ) ;
        return errors;}

}