package com.example.Categorias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Categorias.model.Categoria;
import com.example.Categorias.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Operaciones relacionadas con las categorías")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(
        summary = "Obtener todas las categorías",
        description = "Retorna una lista de todas las categorías registradas. Devuelve 204 si no hay resultados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorías encontradas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Categoria.class)))),
        @ApiResponse(responseCode = "204", description = "No se encontraron categorías"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategoria() {
        List<Categoria> lista = categoriaService.getCategorias();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(
        summary = "Obtener una categoría por ID",
        description = "Busca y devuelve la categoría correspondiente al ID proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada",
            content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido proporcionado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaporId(
        @Parameter(description = "ID de la categoría a buscar", example = "1")
        @PathVariable Long id) {

        try {
            Categoria categoria = categoriaService.getCategoriaPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Crear una nueva categoría",
        description = "Crea una categoría nueva en base al cuerpo enviado en la solicitud."
    )
  
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
            content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno al crear la categoría")
    })
    @PostMapping
    public ResponseEntity<Categoria> guardarCliente(@RequestBody Categoria nuevo) {
        return ResponseEntity.status(201).body(categoriaService.saveCategoria(nuevo));
    }

    @Operation(
        summary = "Eliminar una categoría",
        description = "Elimina la categoría correspondiente al ID proporcionado si existe."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido proporcionado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoporte(
        @Parameter(description = "ID de la categoría a eliminar", example = "1")
        @PathVariable Long id) {

        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
