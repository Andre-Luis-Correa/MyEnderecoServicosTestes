import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import unioeste.geral.endereco.bo.bairro.Bairro;
import unioeste.geral.endereco.bo.cidade.Cidade;
import unioeste.geral.endereco.bo.endereco.Endereco;
import unioeste.geral.endereco.bo.logradouro.Logradouro;
import unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import unioeste.geral.endereco.dao.EnderecoDAO;
import unioeste.geral.endereco.service.UCEnderecoGeralServicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UCEnderecoGeralServicosTeste {

    @InjectMocks
    private UCEnderecoGeralServicos ucEnderecoGeralServicos;

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa("PR", "Paraná");
        Cidade cidade = new Cidade(1L, "Foz do Iguaçu", unidadeFederativa);
        TipoLogradouro tipoLogradouro = new TipoLogradouro("AV", "Avenida");
        Logradouro logradouro = new Logradouro(1L, "Avenida Tancredo Neves", tipoLogradouro);
        Bairro bairro = new Bairro(1L, "Bairro Teste");

        endereco.setId(1L);
        endereco.setCep("00000000");
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setLogradouro(logradouro);
    }

    @Test
    public void deveCadastrarEnderecoComSucesso() throws Exception {
        when(EnderecoDAO.insertEndereco(endereco)).thenReturn(endereco);

        Endereco enderecoTeste = UCEnderecoGeralServicos.cadastrarEndereco(endereco);

        assertNotNull(enderecoTeste);
        assertEquals(enderecoTeste, endereco);
    }


}
