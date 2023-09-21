package com.piola.PiolaSchool.controller;

import com.piola.PiolaSchool.DAO.IAdministrador;
import com.piola.PiolaSchool.DAO.IAluno;
import com.piola.PiolaSchool.model.Aluno;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/Aluno")
public class AlunoController {
    private PasswordEncoder passWordEncoder;
    public AlunoController(IAdministrador dao){
        this.passWordEncoder = new BCryptPasswordEncoder();
    }
    @Autowired
    private IAluno dao;
    @GetMapping
    public List<Aluno> AlunoList (){
        return (List<Aluno>)dao.findAll();
    }
    @PostMapping
    public Aluno criarAluno(@Valid @RequestBody Aluno aluno){
        String encoder =this.passWordEncoder.encode(aluno.getSenha());
        aluno.setSenha(encoder);
        Aluno alunoCreate =dao.save(aluno);
        return alunoCreate;}
    @DeleteMapping("/{matricula}")
    public Optional<Aluno> deletarAluno(@PathVariable Integer matricula){
        Optional<Aluno> Aluno =dao.findById(matricula);
        dao.deleteById(matricula);
        return Aluno;}
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidatioExpetion(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName =((FieldError)error).getField();
            String errorMessage =error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        } ) ;
        return errors;}
}