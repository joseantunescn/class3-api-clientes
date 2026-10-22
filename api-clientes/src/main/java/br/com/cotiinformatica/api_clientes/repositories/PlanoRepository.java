package br.com.cotiinformatica.api_clientes.repositories;

import br.com.cotiinformatica.api_clientes.entities.Plano;
import br.com.cotiinformatica.api_clientes.factories.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlanoRepository {

    @Autowired
    private ConnectionFactory connectionFactory;

    public List<Plano> obterTodos() throws Exception {

        try(var connectio = connectionFactory.createConnection()) {

            var statement = connectio.prepareStatement("SELECT * FROM PLANOS ORDER BY ID");
            var result = statement.executeQuery();
            var lista = new ArrayList<Plano>();
            while(result.next()) {
                var plano = new Plano();

                plano.setId(result.getInt("id"));
                plano.setNome(result.getString("nome"));
                plano.setValorMensal(result.getDouble("valor_mensal"));

                lista.add(plano);
            }

            return lista;

        }

    }

}
