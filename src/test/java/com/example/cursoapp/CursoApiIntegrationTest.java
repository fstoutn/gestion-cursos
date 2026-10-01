package com.example.cursoapp;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CursoApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void creaCursoAsignaEstudianteYListaLaRelacion() throws Exception {
        MvcResult profesorResult = mockMvc.perform(post("/profesores")
                        .contentType("application/json")
                        .content("{\"nombre\":\"Juan Perez\",\"email\":\"juan@mail.com\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        int profesorId = JsonPath.read(profesorResult.getResponse().getContentAsString(), "$.id");

        MvcResult estudianteResult = mockMvc.perform(post("/estudiantes")
                        .contentType("application/json")
                        .content("{\"nombre\":\"Ana\",\"matricula\":\"A123\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        int estudianteId = JsonPath.read(estudianteResult.getResponse().getContentAsString(), "$.id");

        MvcResult cursoResult = mockMvc.perform(post("/cursos")
                        .contentType("application/json")
                        .content("{\"nombre\":\"Programacion Java\",\"profesor\":{\"id\":"
                                + profesorId + "},\"estudiantes\":[]}"))
                .andExpect(status().isCreated())
                .andReturn();
        int cursoId = JsonPath.read(cursoResult.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(post("/cursos/{cursoId}/agregar-estudiante/{estudianteId}", cursoId, estudianteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiantes[0].matricula").value("A123"));

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].profesor.email").value("juan@mail.com"))
                .andExpect(jsonPath("$[0].estudiantes[0].matricula").value("A123"));
    }
}