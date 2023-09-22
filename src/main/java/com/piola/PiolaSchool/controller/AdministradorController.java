package com.piola.PiolaSchool.controller;
import com.piola.PiolaSchool.DAO.IAdministrador;
import com.piola.PiolaSchool.model.Administrador;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Administrador")
public class AdministradorController  {


    @Autowired
    private IAdministrador dao;
    @Autowired
    private PasswordEncoder encoder;


    public AdministradorController(IAdministrador dao){
        this.dao=dao;
        this.encoder=encoder;

    }
    @GetMapping
    public List<Administrador> administradorList (){
return (List<Administrador>)dao.findAll();
    }
    @PostMapping
    public Administrador criarAdministrador(@Valid @RequestBody Administrador admiministrador){
        admiministrador.setSenha(encoder.encode(admiministrador.getSenha()));
        Administrador administradorCreate =dao.save(admiministrador);

        return administradorCreate;}
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map <String,String> handleValidatioExpetion(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName =((FieldError)error).getField();
            String errorMessage =error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        } ) ;
        return errors;}
    @DeleteMapping("/{matricula}")
    public Optional<Administrador> deletarAdministrador(@PathVariable Integer matricula){
            Optional<Administrador> administrador =dao.findById(matricula);
            dao.deleteById(matricula);
            return administrador;

    }



    @PostMapping("/login")
    public ResponseEntity<Boolean> validarSenha (@RequestParam String nome,
                                                 @RequestParam String senha){

        Optional<Administrador> optionalAdministrador = dao.findByNome(nome);
        if (optionalAdministrador.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        Administrador administrador = optionalAdministrador.get();

        boolean valid = encoder.matches(senha, administrador.getSenha());

        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valid);

    }

}







