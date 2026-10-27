package br.com.cotiinformatica.api_clientes.controllers;

import br.com.cotiinformatica.api_clientes.dtos.ClienteRequest;
import br.com.cotiinformatica.api_clientes.dtos.ClienteResponse;
import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    private ResponseEntity<?> post(@RequestBody ClienteRequest request) {

        var cliente = new Cliente();

        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setCpf(request.cpf());
        cliente.setPlanoId(request.planoId());

        try {
            var id = clienteRepository.createCliente(cliente);

            var response = new ClienteResponse(id, cliente.getNome(), cliente.getEmail(), cliente.getCpf(), cliente.getPlanoId());


            var result = Map.of(
                    "mensagem", "Cliente cadastrado com sucesso.",
                    "cliente", response,
                    "dataHoraCadastro", LocalDateTime.now()

            );

            return ResponseEntity.status(201).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Falha: " + e.getMessage()  );
        }

    }

    @PutMapping("{id}")
    private ResponseEntity<?> put(@PathVariable Integer id, @RequestBody ClienteRequest request) {

        var cliente = new Cliente();

        cliente.setId(id);
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setCpf(request.cpf());
        cliente.setPlanoId(request.planoId());

        try {
            if(clienteRepository.alterar(cliente)) {
                var response = new ClienteResponse(id, cliente.getNome(), cliente.getEmail(), cliente.getCpf(), cliente.getPlanoId());


                var result = Map.of(
                        "mensagem", "Cliente atualizado com sucesso.",
                        "cliente", response,
                        "dataHoraAlteracao", LocalDateTime.now()

                );

                return ResponseEntity.status(201).body(result);
            }
            else {
                return ResponseEntity.status(404).body("Cliente/id não encontrado para edição.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Falha: " + e.getMessage()  );
        }

    }

    @DeleteMapping("{id}")
    private ResponseEntity<?> delete(@PathVariable Integer id) {

        try {
            if(clienteRepository.inativar(id)) {

                var result = Map.of(
                        "mensagem", "Cliente excluido com sucesso.",
                        "id", id,
                        "dataHoraExclusao", LocalDateTime.now()

                );

                return ResponseEntity.status(201).body(result);
            }
            else {
                return ResponseEntity.status(404).body("Cliente/id não encontrado para edição.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Falha: " + e.getMessage()  );
        }

    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam String nome) {

        try {
            var clientes = clienteRepository.obterPorNome(nome);
            var response = clientes.stream()
                    .map(cliente -> new ClienteResponse(
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getEmail(),
                            cliente.getCpf(),
                            cliente.getPlanoId()
                    )).toList();
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Falha: " + e.getMessage()  );
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {

        try {
            var clientes = clienteRepository.obterPorId(id);

            if(clientes != null) {
                var response = new ClienteResponse(
                        clientes.getId(), clientes.getNome(), clientes.getEmail(), clientes.getCpf(), clientes.getPlanoId()
                );
                return ResponseEntity.status(200).body(response);
            }
            else {
                return ResponseEntity.status(404).body("Cliente não encontrado.");
            }

        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Falha: " + e.getMessage()  );
        }

    }

}
