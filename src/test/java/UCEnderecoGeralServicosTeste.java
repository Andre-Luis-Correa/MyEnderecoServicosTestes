import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import unioeste.geral.endereco.bo.bairro.Bairro;
import unioeste.geral.endereco.bo.cidade.Cidade;
import unioeste.geral.endereco.bo.endereco.Endereco;
import unioeste.geral.endereco.bo.logradouro.Logradouro;
import unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import unioeste.geral.endereco.col.CidadeCOL;
import unioeste.geral.endereco.col.EnderecoCOL;
import unioeste.geral.endereco.dao.CidadeDAO;
import unioeste.geral.endereco.dao.EnderecoDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.infra.CepAPI;
import unioeste.geral.endereco.service.UCEnderecoGeralServicos;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mockStatic;

public class UCEnderecoGeralServicosTeste {

    private Endereco endereco = new Endereco();
    private List<Endereco> enderecoList = new ArrayList<>();

    @Before
    public void setUp() {
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

        enderecoList = List.of(endereco);
    }

    @Test
    public void deveCadastrarEnderecoComSucesso() throws Exception {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class);
             MockedStatic<EnderecoDAO> enderecoDAOMock = mockStatic(EnderecoDAO.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.enderecoValido(endereco)).thenReturn(true);
            enderecoDAOMock.when(() -> EnderecoDAO.insertEndereco(endereco)).thenReturn(endereco);

            // Act
            Endereco enderecoTeste = UCEnderecoGeralServicos.cadastrarEndereco(endereco);

            // Assert
            assertNotNull(enderecoTeste);
            assertEquals(endereco, enderecoTeste);
            enderecoCOLMock.verify(() -> EnderecoCOL.enderecoValido(endereco));
            enderecoDAOMock.verify(() -> EnderecoDAO.insertEndereco(endereco));
        }
    }

    @Test
    public void deveRetornarEnderecoExceptionQuandoTentarCadastrarEnderecoInvalido() {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.enderecoValido(endereco)).thenReturn(false);

            // Act and Assert
            assertThrows(EnderecoException.class, () -> UCEnderecoGeralServicos.cadastrarEndereco(endereco));
        }
    }

    @Test
    public void deveRetornarListaEnderecosObtidaPorCep() throws Exception {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class);
             MockedStatic<EnderecoDAO> enderecoDAOMock = mockStatic(EnderecoDAO.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.cepValido(endereco.getCep())).thenReturn(true);
            enderecoDAOMock.when(() -> EnderecoDAO.selectEnderecoPorCep(endereco.getCep())).thenReturn(enderecoList);

            // Act
            List<Endereco> enderecoListTeste = UCEnderecoGeralServicos.obterEnderecoPorCep(endereco.getCep());

            // Assert
            assertNotNull(enderecoListTeste);
            assertEquals(enderecoList, enderecoListTeste);
            enderecoCOLMock.verify(() -> EnderecoCOL.cepValido(endereco.getCep()));
            enderecoDAOMock.verify(() -> EnderecoDAO.selectEnderecoPorCep(endereco.getCep()));
        }
    }

    @Test
    public void deveRetornarEnderecoExcpetionQuandoNaoEncontrarEnderecoPorCep() {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class);
             MockedStatic<EnderecoDAO> enderecoDAOMock = mockStatic(EnderecoDAO.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.cepValido(endereco.getCep())).thenReturn(true);
            enderecoDAOMock.when(() -> EnderecoDAO.selectEnderecoPorCep(endereco.getCep())).thenReturn(new ArrayList<>());

            // Act and Assert
            assertThrows(EnderecoException.class, () -> UCEnderecoGeralServicos.obterEnderecoPorCep(endereco.getCep()));
        }
    }

    @Test
    public void deveRetornarEnderecoObtidoPorId() throws Exception {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class);
             MockedStatic<EnderecoDAO> enderecoDAOMock = mockStatic(EnderecoDAO.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.idValido(endereco.getId())).thenReturn(true);
            enderecoDAOMock.when(() -> EnderecoDAO.selectEnderecoPorId(endereco.getId())).thenReturn(endereco);

            // Act
            Endereco enderecoTeste = UCEnderecoGeralServicos.obterEnderecoPorId(endereco.getId());

            // Assert
            assertNotNull(enderecoTeste);
            assertEquals(endereco, enderecoTeste);
            enderecoCOLMock.verify(() -> EnderecoCOL.idValido(endereco.getId()));
            enderecoDAOMock.verify(() -> EnderecoDAO.selectEnderecoPorId(endereco.getId()));
        }
    }

    @Test
    public void deveRetornarEnderecoExcpetionQuandoNaoEncontrarEnderecoPorId() {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class);
             MockedStatic<EnderecoDAO> enderecoDAOMock = mockStatic(EnderecoDAO.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.idValido(endereco.getId())).thenReturn(true);
            enderecoDAOMock.when(() -> EnderecoDAO.selectEnderecoPorId(endereco.getId())).thenReturn(null);

            // Act and Assert
            assertThrows(EnderecoException.class, () -> UCEnderecoGeralServicos.obterEnderecoPorId(endereco.getId()));
        }
    }

    @Test
    public void deveRetornarEnderecoExternoObtidoPorCep() throws Exception {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class);
             MockedStatic<CepAPI> cepAPIMock = mockStatic(CepAPI.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.cepValido(endereco.getCep())).thenReturn(true);
            cepAPIMock.when(() -> CepAPI.getCep(endereco.getCep())).thenReturn(endereco);

            // Act
            Endereco enderecoTeste = UCEnderecoGeralServicos.obterEnderecoExterno(endereco.getCep());

            // Assert
            assertNotNull(enderecoTeste);
            assertEquals(endereco, enderecoTeste);
            enderecoCOLMock.verify(() -> EnderecoCOL.cepValido(endereco.getCep()));
            cepAPIMock.verify(() -> CepAPI.getCep(endereco.getCep()));
        }
    }

    @Test
    public void deveRetornarEnderecoExcpetionQuandoNaoEncontrarEnderecoExternoPorCep() {
        try (MockedStatic<EnderecoCOL> enderecoCOLMock = mockStatic(EnderecoCOL.class);
             MockedStatic<CepAPI> cepAPIMock = mockStatic(CepAPI.class)) {

            // Arrange
            enderecoCOLMock.when(() -> EnderecoCOL.cepValido(endereco.getCep())).thenReturn(true);
            cepAPIMock.when(() -> CepAPI.getCep(endereco.getCep())).thenReturn(null);

            // Act and Assert
            assertThrows(EnderecoException.class, () -> UCEnderecoGeralServicos.obterEnderecoExterno(endereco.getCep()));
        }
    }

    @Test
    public void deveRetornarCidadeObtidaPorId() throws Exception {
        try (MockedStatic<CidadeCOL> cidadeCOLMock = mockStatic(CidadeCOL.class);
             MockedStatic<CidadeDAO> cidadeDAOMock = mockStatic(CidadeDAO.class)) {

            // Arrange
            cidadeCOLMock.when(() -> CidadeCOL.idValido(endereco.getCidade().getId())).thenReturn(true);
            cidadeDAOMock.when(() -> CidadeDAO.selectCidadePorId(endereco.getCidade().getId())).thenReturn(endereco.getCidade());

            // Act
            Cidade cidadeTeste = UCEnderecoGeralServicos.obterCidade(endereco.getCidade().getId());

            // Assert
            assertNotNull(cidadeTeste);
            assertEquals(endereco.getCidade(), cidadeTeste);
            cidadeCOLMock.verify(() -> CidadeCOL.idValido(endereco.getCidade().getId()));
            cidadeDAOMock.verify(() -> CidadeDAO.selectCidadePorId(endereco.getCidade().getId()));
        }
    }

    @Test
    public void deveRetornarEnderecoExcpetionQuandoNaoEncontrarCidadePorId() {
        try (MockedStatic<CidadeCOL> cidadeCOLMock = mockStatic(CidadeCOL.class);
             MockedStatic<CidadeDAO> cidadeDAOMock = mockStatic(CidadeDAO.class)) {

            // Arrange
            cidadeCOLMock.when(() -> CidadeCOL.idValido(endereco.getCidade().getId())).thenReturn(true);
            cidadeDAOMock.when(() -> CidadeDAO.selectCidadePorId(endereco.getCidade().getId())).thenReturn(null);

            // Act and Assert
            assertThrows(EnderecoException.class, () -> UCEnderecoGeralServicos.obterCidade(endereco.getCidade().getId()));
        }
    }

}
