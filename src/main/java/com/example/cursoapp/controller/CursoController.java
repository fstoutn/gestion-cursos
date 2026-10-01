package com.example.cursoapp.controller;

import com.example.cursoapp.entity.Curso;
import com.example.cursoapp.repository.CursoRepository;
import com.example.cursoapp.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoRepository cursoRepository;
    private final CursoService cursoService;

    public CursoController(CursoRepository cursoRepository, CursoService cursoService) {
        this.cursoRepository = cursoRepository;
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Curso crear(@Valid @RequestBody Curso curso) {
        return cursoService.crear(curso);
    }

    @PostMapping("/{cursoId}/agregar-estudiante/{estudianteId}")
    public Curso agregarEstudiante(@PathVariable Long cursoId, @PathVariable Long estudianteId) {
        return cursoService.agregarEstudiante(cursoId, estudianteId);
    }
}