package com.texto.primeiroprojetobackend.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texto.primeiroprojetobackend.services.ProdutoService;
import com.texto.primeiroprojetobackend.shared.ProdutoDTO;
import com.texto.primeiroprojetobackend.view.model.ProdutoRequest;
import com.texto.primeiroprojetobackend.view.model.ProdutoResponse;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> obterTodos() {

        List<ProdutoDTO> produtos = produtoService.obterTodos();
        ModelMapper mapper = new ModelMapper();

        List<ProdutoResponse> resposta = produtos.stream()
                .map(produtoDto -> mapper.map(produtoDto, ProdutoResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(resposta, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProdutoResponse>> obterPorId(@PathVariable Integer id) {
        //try{
        Optional<ProdutoDTO> dto = produtoService.obterPorId(id);
        ModelMapper mapper = new ModelMapper();
        ProdutoResponse produto = mapper.map(dto.get(), ProdutoResponse.class);
        return new ResponseEntity<>(Optional.of(produto), HttpStatus.OK);
       // }catch(Exception e){
       //    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       // }

    }

    @PostMapping
    public  ResponseEntity<ProdutoResponse> adicionar(@RequestBody ProdutoRequest produtoRequest) {
        ModelMapper mapper = new ModelMapper();
        ProdutoDTO produtoDto = mapper.map(produtoRequest, ProdutoDTO.class);
        produtoDto = produtoService.adicionar(produtoDto);
        ProdutoResponse produtoResponse = mapper.map(produtoDto, ProdutoResponse.class);
        return new ResponseEntity<>(produtoResponse,HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@RequestBody ProdutoRequest produtoRequest, @PathVariable Integer id) {
        ModelMapper mapper = new ModelMapper();
        ProdutoDTO produtoDto = mapper.map(produtoRequest, ProdutoDTO.class);
        produtoDto =  produtoService.atualizar(id, produtoDto);
        ProdutoResponse produtoResponse = mapper.map(produtoDto, ProdutoResponse.class);
        return new ResponseEntity<>(produtoResponse,HttpStatus.CREATED);

    }
}
