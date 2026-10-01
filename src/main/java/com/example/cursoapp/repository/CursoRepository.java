package com.example.cursoapp.repository;

import com.example.cursoapp.entity.Curso;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	@Override
	@EntityGraph(attributePaths = {"profesor", "estudiantes"})
	List<Curso> findAll();
}