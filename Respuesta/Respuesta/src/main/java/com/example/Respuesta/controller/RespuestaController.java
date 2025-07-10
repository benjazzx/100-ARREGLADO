package com.example.Respuesta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Respuesta.model.Respuesta;
import com.example.Respuesta.service.RespuestaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;
    
    // GET /api/v1/respuestas
    @Operation(
        summary = "Obtener todas las respuestas",
        description = "Retorna una lista con todas las respuestas registradas en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Respuestas encontradas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Respuesta.class)))
        ),
        @ApiResponse(responseCode = "204", description = "No se encontraron respuestas")
    })
    @GetMapping
    public ResponseEntity<List<Respuesta>> getAllRespuestas(){
        List<Respuesta> respuestas = respuestaService.findAll();
        return ResponseEntity.ok(respuestas);
    }

    
    @Operation(
        summary = "Obtener una respuesta por ID",
        description = "Retorna la respuesta correspondiente al ID proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Respuesta encontrada",
            content = @Content(schema = @Schema(implementation = Respuesta.class))
        ),
        @ApiResponse(responseCode = "404", description = "Respuesta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Respuesta> getRespuestaById(@PathVariable Long id) {
        Optional<Respuesta> respuesta = respuestaService.findById(id);
        return respuesta.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }    
    
    
    @Operation(
        summary = "Crear una nueva respuesta",
        description = "Crea una nueva respuesta utilizando la información proporcionada en el cuerpo de la petición."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Respuesta creada exitosamente",
            content = @Content(schema = @Schema(implementation = Respuesta.class))
        ),
        @ApiResponse(responseCode = "400", description = "Error al crear la respuesta")
    })
      @PostMapping
public ResponseEntity<String> createRespuesta(@RequestBody Respuesta nuevoRespuesta) {
    if (nuevoRespuesta == null) {
        return ResponseEntity.badRequest().body("No se recibió un objeto válido");
    }
    return ResponseEntity.ok("Contenido: " + nuevoRespuesta.getContenido() + ", soporteId: " + nuevoRespuesta.getSoporteId());
}
    
    
    @Operation(
        summary = "Actualizar una respuesta existente",
        description = "Actualiza la respuesta correspondiente al ID proporcionado con la información del cuerpo de la petición."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Respuesta actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = Respuesta.class))
        ),
        @ApiResponse(responseCode = "404", description = "Respuesta no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar la respuesta")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Respuesta> updateRespuesta(@PathVariable Long id, @RequestBody Respuesta respuestaActualizado) {
        Optional<Respuesta> respuestaExistente = respuestaService.findById(id);

        if (respuestaExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        respuestaActualizado.setId(id);

        try {
            Respuesta respuestaGuardado = respuestaService.saveSoporte(respuestaActualizado);
            return ResponseEntity.ok(respuestaGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

