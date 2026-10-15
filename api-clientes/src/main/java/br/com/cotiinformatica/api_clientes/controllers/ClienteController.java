package br.com.cotiinformatica.api_clientes.controllers;

import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    private Map<String, Object> post(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String cpf,
            @RequestParam Integer planoId
    ) throws Exception {

        var cliente = new Cliente();

        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setCpf(cpf);
        cliente.setPlanoId(planoId);

        var id = clienteRepository.createCliente(cliente);

        return Map.of(
                "mensagem", "Cliente cadastrado com sucesso.",
                "id", id

        );

    }
}
