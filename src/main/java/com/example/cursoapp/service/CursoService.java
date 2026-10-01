package com.example.cursoapp.service;

import com.example.cursoapp.entity.Curso;
import com.example.cursoapp.entity.Estudiante;
import com.example.cursoapp.entity.Profesor;
import com.example.cursoapp.repository.CursoRepository;
import com.example.cursoapp.repository.EstudianteRepository;
import com.example.cursoapp.repository.ProfesorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final ProfesorRepository profesorRepository;
    private final EstudianteRepository estudianteRepository;

    public CursoService(
            CursoRepository cursoRepository,
            ProfesorRepository profesorRepository,
            EstudianteRepository estudianteRepository
    ) {
        this.cursoRepository = cursoRepository;
        this.profesorRepository = profesorRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @Transactional
    public Curso crear(Curso curso) {
        if (curso.getProfesor() == null || curso.getProfesor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El curso debe incluir un profesor existente");
        }

        Profesor profesor = profesorRepository.findById(curso.getProfesor().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado"));

        List<Estudiante> estudiantes = new ArrayList<>();
        if (curso.getEstudiantes() != null) {
            for (Estudiante referencia : curso.getEstudiantes()) {
                if (referencia.getId() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada estudiante debe incluir un id existente");
                }
                estudiantes.add(estudianteRepository.findById(referencia.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Estudiante no encontrado: " + referencia.getId())));
            }
        }

        curso.setId(null);
        curso.setProfesor(profesor);
        curso.setEstudiantes(estudiantes);
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso agregarEstudiante(Long cursoId, Long estudianteId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));

        boolean yaAsignado = curso.getEstudiantes().stream()
                .anyMatch(actual -> actual.getId().equals(estudianteId));
        if (!yaAsignado) {
            curso.getEstudiantes().add(estudiante);
        }
        return cursoRepository.save(curso);
    }
}