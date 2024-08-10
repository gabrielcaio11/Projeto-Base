package com.texto.primeiroprojetobackend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.texto.primeiroprojetobackend.model.Produto;
import com.texto.primeiroprojetobackend.model.exception.ResourceNotFoundException;
import com.texto.primeiroprojetobackend.repository.ProdutoRepository;
import com.texto.primeiroprojetobackend.shared.ProdutoDTO;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Metodo que retorna uma lista de ProdutoDTO.
     * 
     * @return Lista de Produtos
     */
    public List<ProdutoDTO> obterTodos() {

        //obtendo uma lista de Produto
        List<Produto> produtos = produtoRepository.findAll();

        //Convertendo um Produto em um ProdutoDTO 
        //retornando um ProdutoDTO
        return produtos.stream()
                .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna um ProdutoDTO por id.
     * 
     * @param id do produto que sera localizado.
     * @return Retorna um ProdutoDTO caso seja encontrado.
     */
    public Optional<ProdutoDTO> obterPorId(Integer id) {
        // obtendo um Optional de Produto por id.
        Optional<Produto> produto = produtoRepository.findById(id);

        // se for nulo lança uma exception.
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id:" + id + " não encontrado");
        }

        // convertendo o meu Optional de Produto em um ProdutoDTO.
        ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);

        // criando e retornando um optional de DTO.
        return Optional.of(dto);
    }

    /**
     * Metodo para adicionar o ProdutoDTO na lista.
     * 
     * @param ProdutoDTO que vai ser adicionado.
     * @return ProdutoDTO que foi adicionado na lista.
     */
    public ProdutoDTO adicionar(ProdutoDTO produtoDTO) {

        // removendo o id para conseguir fazer o cadastro
        // se tiver um id vai e chamar o metodo save vai atualizar
        // o que nao é o desejado
        produtoDTO.setId(null);

        // criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // converter o nosso ProdutoDTO em um Produto
        Produto produto = mapper.map(produtoDTO, Produto.class);

        // salvar o produto no banco
        produtoRepository.save(produto);

        // retorna o produto mas antes muda o id
        produtoDTO.setId(produto.getId());
        return produtoDTO;

    }

    /**
     * Metodo para deletar o Produto por id.
     * 
     * @param id do Produto que vai ser deletado.
     */
    public void deletar(Integer id) {
        // obtendo um Optional de produto por id.
        Optional<Produto> produto = produtoRepository.findById(id);

        // se for nulo lança uma exception.
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id:" + id + " não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    /**
     * Metodo para atualizar o produto na lista.
     * 
     * @param ProdutoDTO que sera atualizado.
     * @param id do ProdutoDTO que sera atualizado.
     * @return ProdutoDTO que foi atualizado.
     */
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtodDto) {
        // passar o id para o produtoDTO
        produtodDto.setId(id);

        // criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // converte o DTO em um Produto
        Produto produto = mapper.map(produtodDto, Produto.class);

        // atualizar o produto no banco de dados
        produtoRepository.save(produto);

        //retorna o ProdutoDTO
        return produtodDto;

    }
}
