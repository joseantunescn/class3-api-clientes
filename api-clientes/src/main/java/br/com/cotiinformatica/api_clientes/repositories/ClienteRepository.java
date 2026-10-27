package br.com.cotiinformatica.api_clientes.repositories;

import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.factories.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteRepository {

    @Autowired
    private ConnectionFactory connectionFactory;

    public Integer createCliente(Cliente cliente) throws Exception {

        try(var connection = connectionFactory.createConnection()) {

            var statement = connection.prepareStatement("INSERT INTO CLIENTES(NOME, EMAIL, CPF, PLANO_ID) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getEmail());
            statement.setString(3, cliente.getCpf());
            statement.setInt(4, cliente.getPlanoId());
            statement.execute();

            var result = statement.getGeneratedKeys();
            if(result.next())
                return result.getInt(1);
            else
                throw new SQLException("Não foi possível obter o ID do cliente.");

        }

    }

    public boolean alterar(Cliente cliente) throws Exception {

        try (var connection = connectionFactory.createConnection()) {

            var statement = connection.prepareStatement("""
                UPDATE CLIENTES SET NOME=?, EMAIL=?, CPF=?, PLANO_ID=?
                WHERE ID=? AND ATIVO = 1
                """);

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getEmail());
            statement.setString(3, cliente.getCpf());
            statement.setInt(4, cliente.getPlanoId());
            statement.setInt(5, cliente.getId());
            return statement.executeUpdate() > 0;

        }

    }

    public boolean inativar(Integer id) throws Exception {

        try (var connection = connectionFactory.createConnection()) {

            var statement = connection.prepareStatement("""
                UPDATE CLIENTES SET 
                ATIVO = 0
                WHERE ID=? AND ATIVO = 1
                """);

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        }

    }

    public List<Cliente> obterPorNome(String nome) throws Exception {
        try (var connection = connectionFactory.createConnection()) {

            var statement = connection.prepareStatement("""
                SELECT * FROM CLIENTES
                WHERE ATIVO = 1
                AND NOME LIKE ?
                
                """);

            statement.setString(1, "%" + nome + "%");
            var result =  statement.executeQuery();

            var lista = new ArrayList<Cliente>();
            while (result.next()) {
                var cliente =  new Cliente();

                cliente.setId(result.getInt("id"));
                cliente.setNome(result.getString("nome"));
                cliente.setCpf(result.getString("cpf"));
                cliente.setEmail(result.getString("email"));
                cliente.setPlanoId(result.getInt("plano_id"));
                lista.add(cliente);

            }
            return lista;

        }
    }

    public Cliente obterPorId(Integer id) throws Exception {
        try (var connection = connectionFactory.createConnection()) {

            var statement = connection.prepareStatement("""
                
                    SELECT * FROM CLIENTES
                WHERE ATIVO = 1
                AND ID = ?
                
                """);

            statement.setInt(1, id);
            var result =  statement.executeQuery() ;

            if(result.next()) {
                var cliente =  new Cliente();

                cliente.setId(result.getInt("id"));
                cliente.setNome(result.getString("nome"));
                cliente.setCpf(result.getString("cpf"));
                cliente.setEmail(result.getString("email"));
                cliente.setPlanoId(result.getInt("plano_id"));

                return cliente;

            }
            else {
                return null;
            }

        }
    }



}
