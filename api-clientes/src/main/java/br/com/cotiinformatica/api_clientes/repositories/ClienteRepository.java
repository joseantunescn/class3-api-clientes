package br.com.cotiinformatica.api_clientes.repositories;

import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.factories.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Statement;

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

            var result = statement.getGeneratedKeys();
            if(result.next())
                return result.getInt(1);
            else
                throw new SQLException("Não foi possível obter o ID do cliente.");

        }





    }

}
