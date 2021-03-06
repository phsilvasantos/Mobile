import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class SmartMobile {
	private double versaoWS = 4.0;
	// private static Connection conexao;
	private static String conexao_erro = "";

	private static String c_linhas = "#l#"; // SEPARADOR DE REGISTROS = "#l#"
	private static String c_colunas = "#c#"; // SEPARADOR DE COLUNAS = "#c#"
	private static String c_select = "#separadorConsulta#"; // SEPARADOR DE
															// COLUNAS = "#c#"

	// private static final String URL =
	// "jdbc:jtds:sqlserver://10.0.0.200:1433/Smart";
	// private static final String URL =
	// "jdbc:jtds:sqlserver://vargasmobile.no-ip.org:1433/Vargas";
	// private static final String URL =
	// "jdbc:jtds:sqlserver://localhost:1433/";
	// private static final String URLteste =
	// "jdbc:jtds:sqlserver://10.0.0.200:1433/Smart";

	// private static final String DRIVER = "net.sourceforge.jtds.jdbc.Driver";

	private static final String DRIVER2 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String USUARIO = "sa";
	private static final String SENHA = "menuauto";

	public static Connection getConnection(String BancoSql) {

		Connection conexao = null;
		try {
			// Class.forName(DRIVER);
			Class.forName(DRIVER2);

			// if (BancoSql.toUpperCase().trim().equals("SMART"))
			// {conexao = DriverManager.getConnection(URLteste, USUARIO,
			// SENHA);}
			// else
			// {

			// conexao = DriverManager.getConnection(URL + BancoSql, USUARIO,
			// SENHA);
			// conexao =
			// DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/Moreira;integratedSecurity=true");

			String servidor = "localhost:1433";
			// String servidor = "127.0.0.1:1433";

			if (BancoSql.toUpperCase().trim().equals("SMART") || BancoSql.toUpperCase().trim().equals("GROUP")) {
				String hostname = "";
				try {
					InetAddress addr;
					addr = InetAddress.getLocalHost();
					hostname = addr.getHostName();
				} catch (UnknownHostException ex) {
				}

				// verifica se est� conectado na rede interna da amsoft
				// [servidor apache padr�o na m�quina do jakson]
				if (hostname.toUpperCase().equalsIgnoreCase("AMSOFT-JAKSON")) {
					servidor = "10.0.0.200:1433";
				} else { // conectando de fora da rede da amsoft
					servidor = "update.amsoft.com.br:1450";
				}

			} else if (BancoSql.toUpperCase().trim().equals("DEMO")) {
				servidor = "192.168.1.50:1433";
			} else if (BancoSql.toUpperCase().trim().equals("JOCIELI")) {
				servidor = "10.0.0.14:1433";
			} else if (BancoSql.toUpperCase().trim().equals("SANDRA")) {
				servidor = "10.0.0.35:1433";
			} else if (BancoSql.toUpperCase().trim().equals("ALESSANDRA")) {
				servidor = "10.0.0.116:1433";
			} else if (BancoSql.toUpperCase().trim().equals("CRIS")) {
				servidor = "10.0.0.15:1433";
			} else if (BancoSql.toUpperCase().trim().equals("GUILHERME")) {
				servidor = "10.0.0.15:1433";
			} else if (BancoSql.toUpperCase().trim().equals("JAKSON")) {
				servidor = "10.0.0.10:1433";
			} else if (BancoSql.toUpperCase().trim().equals("JENIFER")) {
				servidor = "10.0.0.20:1433";
			} else if (BancoSql.toUpperCase().trim().equals("DAIANE")) {
				servidor = "10.0.0.30:1433";
			} else if (BancoSql.toUpperCase().trim().equals("ANA")) {
				servidor = "10.0.0.17:1433";
			}

			// else if
			// (BancoSql.toUpperCase().trim().equals("UNILIMA")){servidor="unilima.no-ip.org:1433";}
			// else if
			// (BancoSql.toUpperCase().trim().equals("ZENIR")){servidor="10.0.0.55:1433";}
			// else if
			// (BancoSql.toUpperCase().trim().equals("DNA")){servidor="192.168.1.4:1433";}

			try {
				String connectionUrl = "jdbc:sqlserver://" + servidor + ";" + "databaseName=" + BancoSql + ";user=" + USUARIO + ";password=" + SENHA + ";ApplicationName=SmartMobile;";

				try {
					conexao = DriverManager.getConnection(connectionUrl);
				} catch (Exception e) {
					servidor = "127.0.0.1:1433";
					connectionUrl = "jdbc:sqlserver://" + servidor + ";" + "databaseName=" + BancoSql + ";user=" + USUARIO + ";password=" + SENHA + ";ApplicationName=SmartMobile;";
					conexao = DriverManager.getConnection(connectionUrl);
				}
			} catch (Exception ex) {
				// 18/08/2014 - JAKSON GAVA - caso n�o conseguir conectar pelo
				// 'localhost' tenta pelo '127.0.0.1'
				servidor = "127.0.0.1:1433";
				String connectionUrl = "jdbc:sqlserver://" + servidor + ";" + "databaseName=" + BancoSql + ";user=" + USUARIO + ";password=" + SENHA + ";ApplicationName=SmartMobile;";
				conexao = DriverManager.getConnection(connectionUrl);
			}

			// };

		} catch (Exception ex) {

			// try{
			// 18/08/2014 - JAKSON GAVA - caso n�o conseguir conectar pelo
			// 'localhost' tenta pelo '127.0.0.1'
			// servidor = "127.0.0.1:1433";
			// String connectionUrl = "jdbc:sqlserver://" + servidor + ";" +
			// "databaseName=" + BancoSql + ";user=" + USUARIO + ";password=" +
			// SENHA + ";ApplicationName=SmartMobile;";
			// conexao = DriverManager.getConnection(connectionUrl);
			// }catch (Exception ex) {
			conexao_erro = ex.getMessage();
			// }
		}

		return conexao;
	}

	private String RetornoErro() {

		// if (conexao_erro=="")
		// {return
		// "java.sql.SQLException: N�o foi poss�vel conectar ao Banco de Dados.";}
		// else
		// {
		return "java.sql.SQLException: N�o foi poss�vel conectar ao Banco de Dados. Mensagem:" + conexao_erro;
		// }

	}

	public String SqlConsulta(String banco, String xml) {

		String retorno = "";
		String ValorColuna = "";

		String[] selects = xml.split(c_select);

		Connection conServer = getConnection(banco);
		for (int i = 0; i < selects.length; i++) {

			if (conServer != null) {

				try {

					// return "SqlConsultaOn : " + xml;
					Statement stmt = conServer.createStatement();

					// Criando a instru��o a partir do SELECT que est� dentro da
					// vari�vel query
					ResultSet rs = stmt.executeQuery(selects[i]);

					// System.out.println("Lista de linhas da tabela sysobjects:");

					ResultSetMetaData rsMetaData = rs.getMetaData();

					// Fazendo um loop para mostrar tudo o que foi retornado do
					// banco
					while (rs.next()) {
						// coluna come�a sempre com 1

						for (int j = 1; j <= rsMetaData.getColumnCount(); j += 1) {

							//mantem o valor padrao por causa das mensagens de sugest�o com acentos etc
							if(banco.toUpperCase().equalsIgnoreCase("GROUP")) {
								ValorColuna =rs.getString(j);
							}	else{
								// remove acentos
								ValorColuna = removeAcentos(rs.getString(j));

								// removendo outros caracteres especiais
								ValorColuna = removeNonUtf8CompliantCharacters(ValorColuna);
							}

							if (j == 1) {
								retorno += ValorColuna; // SEPARADOR DE
														// COLUNAS
														// NA
														// PRIMEIRA
														// NAO
														// VAI
							} else {
								retorno += c_colunas + ValorColuna; // SEPARADOR
																	// DE
																	// COLUNAS
							}
						}

						// Obtendo o campo id em um inteiro

						// int i = rs.getInt("id");
						// System.out.println(s + " " + i);

						if (retorno != "") {
							retorno += c_linhas; // SEPARADOR DE
													// REGISTROS
						}

					}

					// Fechamdno a instru��o e a conex�o
					stmt.close();

				} catch (Exception ex) {
					retorno = "java.sql.SQLException: " + ex.getMessage();
				}

			} else {
				return RetornoErro();
			}

			if ((i + 1) != selects.length) {
				retorno += c_select;
			}
		}

		try {
			conServer.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retorno;

	}

	private String removeAcentos(String string) {
		if (string != null) {
			string = string.replaceAll("[�����]", "A");
			string = string.replaceAll("[�����]", "a");
			string = string.replaceAll("[����]", "E");
			string = string.replaceAll("[����]", "e");
			string = string.replaceAll("����", "I");
			string = string.replaceAll("����", "i");
			string = string.replaceAll("[�����]", "O");
			string = string.replaceAll("[�����]", "o");
			string = string.replaceAll("[����]", "U");
			string = string.replaceAll("[����]", "u");
			string = string.replaceAll("�", "C");
			string = string.replaceAll("�", "c");
			string = string.replaceAll("[��]", "y");
			string = string.replaceAll("�", "Y");
			string = string.replaceAll("�", "n");
			string = string.replaceAll("�", "N");
		}
		return string;
	}

	private static String removeNonUtf8CompliantCharacters(final String inString) {
		if (null == inString)
			return null;
		byte[] byteArr = inString.getBytes();
		for (int i = 0; i < byteArr.length; i++) {
			byte ch = byteArr[i];
			// remove any characters outside the valid UTF-8 range as well as
			// all control characters
			// except tabs and new lines
			if (!((ch > 31 && ch < 253) || ch == '\t' || ch == '\n' || ch == '\r')) {
				byteArr[i] = ' ';
			}
		}
		return new String(byteArr);
	}

	public String SqlExecuta(String banco, String xml) throws SQLException {

		String retorno = "";
		Connection conServer = getConnection(banco);

		if (conServer != null) {
			try {
				// Statement stmt = conexao.createStatement();
				// stmt.executeUpdate(xml);
				// stmt.close();

				/*
				 * ANTIGO CallableStatement cs =
				 * conServer.prepareCall("{? = call " + xml + "}");
				 * cs.registerOutParameter(1,java.sql.Types.VARCHAR);
				 * cs.execute(); retorno = cs.getString(1); //busca o id de
				 * retorno da procedure
				 */

				/* 2013 QUEBRA EM LINHAS DE EXECU��O */
				/* 2013 QUEBRA EM LINHAS DE EXECU��O */
				String ExecLinhas[] = xml.split(c_linhas);
				for (int i = 0; i < ExecLinhas.length; i++) {

					if (!ExecLinhas[i].trim().equals("")) {
						conServer.setAutoCommit(false);
						CallableStatement cs = conServer.prepareCall("exec " + ExecLinhas[i]);
						cs.registerOutParameter(1, java.sql.Types.INTEGER);
						cs.execute();
						if (cs.getInt(1) > 0) {
							retorno = cs.getString(1);
						} else {
							retorno = "";
						}

					}

				}
				conServer.commit();
				conServer.setAutoCommit(true);
				conServer.close();

			} catch (Exception ex) {
				conServer.rollback();
				conServer.setAutoCommit(true);
				retorno = "java.sql.SQLException: " + ex.getMessage();
			}

			return retorno;

		} else {
			return RetornoErro();
		}

	}

	/*
	 * public static void main(String[] args) throws SQLException {
	 * 
	 * System.out.println("Eu sou o seu primeiro programa.");
	 * System.out.println("Testando execu��o da procedure montar fucker");
	 * 
	 * 
	 * Connection conServer = getConnection("Smart"); if (conServer != null) {
	 * 
	 * 
	 * CallableStatement cs = conServer.prepareCall("exec " +
	 * "SP_MOBILE_VENDA @retornoid = ?, @empresaid = 1, @representanteid = 1, @data = '09/04/2012', @cpf_cnpj = '066.244.029-38', @forma_pgtoid = 2,  @listaid = 3,  @obs = 'senhor jesus'"
	 * + ""); cs.registerOutParameter(1,java.sql.Types.INTEGER); cs.execute();
	 * 
	 * System.out.println("retornou inteiro : " + cs.getInt(1)); String vendaid
	 * =cs.getString(1);
	 * 
	 * 
	 * String exec_item =
	 * "SP_MOBILE_VENDA_PRODUTO @retornoid = ?, @empresaid = 1 , @vendaid = " +
	 * vendaid + ", @produtoid = 25, @qtde =  1, @valor = 1,  @operacao = 0";
	 * CallableStatement cs2 = conServer.prepareCall("exec " + exec_item);
	 * cs2.registerOutParameter(1,java.sql.Types.INTEGER); cs2.execute();
	 * System.out.println("retornou_item inteiro : " + cs2.getInt(1));
	 * 
	 * 
	 * //Fechamdno a instru��o e a conex�o conServer.close(); } else
	 * {System.out.println("Problema:"+conexao_erro);}
	 * 
	 * }
	 */

	public String montarImagensZip(String nomeBanco, int idEmpresa) {
		String retorno = idEmpresa + "";
		try {

			Connection conServer = getConnection(nomeBanco);
			int tempId = 0;

			if (conServer != null) {

				// return "SqlConsultaOn : " + xml;
				Statement stmt = conServer.createStatement();

				// Criando a instru��o a partir do SELECT que est� dentro da
				// vari�vel query

				StringBuilder builderSql = new StringBuilder();
				builderSql.append("SELECT PRODUTOID, CODIGO, IMAGEM FROM VW_MOBILE_PRODUTOS_IMAGENS WHERE EMPRESAID = ");
				builderSql.append(idEmpresa);
				builderSql.append(" ORDER BY PRODUTOID");

				ResultSet rs = stmt.executeQuery(builderSql.toString());

				// Fazendo um loop para mostrar tudo o que foi retornado do
				// banco
				int valorTemp = 0;

				// CRIA A PASTA IMAGENS
				File files = new File("webapps/axis/imagens/");

				if (files != null) {
					String[] entries = files.list();
					if (entries != null) {
						if (entries.length > 0) {
							for (String s : entries) {
								File currentFile = new File(files.getPath(), s);
								currentFile.delete();
							}
						}
					}
				}

				files.mkdir();

				while (rs.next()) {
					String campoCodigoID = "PRODUTOID";
					if (nomeBanco.toUpperCase().trim().equals("INCAS") || nomeBanco.toUpperCase().trim().equals("SIAL")) {
						campoCodigoID = "CODIGO";
					}
					int produtoId = rs.getInt(campoCodigoID);

					byte[] fileBytes = rs.getBytes("IMAGEM");

					// SALVA A IMAGEM EM UM PASTA
					FileOutputStream targetFile = null;

					if (tempId != produtoId) {
						valorTemp = 0;
					}

					File fileCreate = new File("webapps/axis/imagens/" + produtoId + "_" + ++valorTemp + ".jpg");
					if (!fileCreate.exists()) {
						fileCreate.createNewFile();
					}

					targetFile = new FileOutputStream(fileCreate);
					BufferedOutputStream bs = new BufferedOutputStream(targetFile);
					bs.write(fileBytes);
					bs.close();

					// VALIDACAO PARA PROX IMAGEM
					tempId = rs.getInt(campoCodigoID);

				}

				// Fechamdno a instru��o e a conex�o
				stmt.close();
				conServer.close();

			}

			File entrada = new File("webapps/axis/imagens");
			File saida = new File("webapps/axis/teste.zip");
			if (saida.isFile()) {
				saida.delete();
			}

			saida = new File("webapps/axis/teste.zip");
			saida.createNewFile();

			try {
				this.compress(entrada, saida);
			} catch (IOException e) {
				retorno += e.getMessage();
			}

		} catch (Exception ex) {
			retorno = "java.sql.SQLException: " + ex.getMessage();
		}

		return retorno;
	}

	public String sincronizarInsereBanco(String xml) {
		boolean erro = false;
		Connection connection = null;
		String banco = "";
		int id = 0;

		Document resposta = new Document();
		Element eResp = new Element("resp");
		resposta.setRootElement(eResp);

		Element eClientes = new Element("clientes");
		Element ePedidos = new Element("pedidos");
		Element eSaldos = new Element("saldos");
		Element eEstoques = new Element("estoques");

		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new StringReader(xml));
			Element mobileRoot = (Element) doc.getRootElement();

			banco = mobileRoot.getAttributeValue("banco");

			connection = getConnection(banco);

			Element mobileClientes = mobileRoot.getChild("clientes");

			List mobileListClientes = mobileClientes.getChildren();
			Iterator mobileIClientes = mobileListClientes.iterator();

			while (mobileIClientes.hasNext()) {
				Element mobileCliente = (Element) mobileIClientes.next();
				String retorno = "";

				Element eCliente = new Element("cliente");
				Attribute aIdCliente = new Attribute("id", mobileCliente.getAttributeValue("id"));
				try {
					if (!erro) {
						SqlExecutaSemCommit(connection, mobileCliente.getAttributeValue("exec"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					erro = true;
					throw new Exception("Cliente: " + mobileCliente.getAttributeValue("id") + " - ERROR: " + e.getMessage());
				}

				eCliente.setAttribute(aIdCliente);

				eClientes.addContent(eCliente);
			}

			Element mobilePedidos = mobileRoot.getChild("pedidos");
			List mobileListVendas = mobilePedidos.getChildren();
			Iterator mobileIVendas = mobileListVendas.iterator();
			if (!erro) {
				while (mobileIVendas.hasNext()) {
					String retorno = "";
					Element mobileVenda = (Element) mobileIVendas.next();

					Element eVenda = new Element("venda");
					Attribute aIdVenda = new Attribute("id", mobileVenda.getAttributeValue("id"));

					try {
						if (!erro) {
							retorno = SqlExecutaSemCommit(connection, mobileVenda.getAttributeValue("exec"));
							id = Integer.parseInt(retorno);
						}
					} catch (Exception e) {
						e.printStackTrace();
						erro = true;
						throw new Exception("Venda Inserir: " + mobileVenda.getAttributeValue("id") + " - ERROR: " + e.getMessage());
					}

					eVenda.setAttribute(aIdVenda);

					Element mobileProdutos = mobileVenda.getChild("produtos");
					List mobileListProdutos = mobileProdutos.getChildren();
					Iterator mobileIProdutos = mobileListProdutos.iterator();

					if (!erro) {
						while (mobileIProdutos.hasNext()) {
							Element mobileProduto = (Element) mobileIProdutos.next();

							try {
								if (!erro) {
									if (id > 0) {
										SqlExecutaSemCommit(connection, mobileProduto.getAttributeValue("exec") + id);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								erro = true;
								throw new Exception("Venda-Produto: " + mobileVenda.getAttributeValue("id") + " - ERROR: " + e.getMessage());
							}
						}

						Element mobileFinaliza = mobileVenda.getChild("finaliza");
						if (!erro) {
							try {
								if (id > 0) {
									SqlExecutaSemCommit(connection, mobileFinaliza.getAttributeValue("exec") + id);
								}
							} catch (Exception e) {
								retorno += "Error: finaliza: " + e.getMessage();
								erro = true;
								throw new Exception("Venda-finaliza: " + mobileVenda.getAttributeValue("id") + " - ERROR: " + e.getMessage());
							}
						}

						ePedidos.addContent(eVenda);
					}
				}

				Element mobileSaldos = mobileRoot.getChild("saldos");
				List mobileListSaldos = mobileSaldos.getChildren();
				Iterator mobileISaldos = mobileListSaldos.iterator();
				String retorno = "";
				if (!erro) {
					while (mobileISaldos.hasNext()) {
						Element mobileSaldo = (Element) mobileISaldos.next();

						Element eSaldo = new Element("saldo");
						Attribute aIdSaldo = new Attribute("id", mobileSaldo.getAttributeValue("id"));
						eSaldo.setAttribute(aIdSaldo);
						try {
							if (!erro) {
								SqlExecutaSemCommit(connection, mobileSaldo.getAttributeValue("exec"));
							}
						} catch (Exception e) {
							retorno += "Error: saldo: " + e.getMessage();
							erro = true;
							throw new Exception("Saldo: " + mobileSaldo.getAttributeValue("id") + " - ERROR: " + e.getMessage());
						}

						eSaldos.addContent(eSaldo);
					}
				}

				try {
					Element mobileEstoques = mobileRoot.getChild("estoques");
					List mobileListEstoque = mobileEstoques.getChildren();
					Iterator mobileIEstoque = mobileListEstoque.iterator();
					if (!erro) {
						while (mobileIEstoque.hasNext()) {
							Element mobileEstoque = (Element) mobileIEstoque.next();

							Element eEstoque = new Element("estoque");
							Attribute aIdEstoque = new Attribute("id", mobileEstoque.getAttributeValue("id"));
							eEstoque.setAttribute(aIdEstoque);
							try {
								if (!erro) {
									SqlExecutaSemCommit(connection, mobileEstoque.getAttributeValue("exec"));
								}
							} catch (Exception e) {
								retorno += "Error: estoque: " + e.getMessage();
								erro = true;
								e.printStackTrace();
								throw new Exception("Estoque: " + mobileEstoque.getAttributeValue("id") + " - ERROR: " + e.getMessage());

							}

							eEstoques.addContent(eEstoque);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			connection.commit();
			connection.setAutoCommit(true);

			eResp.addContent(eClientes);
			eResp.addContent(ePedidos);
			eResp.addContent(eSaldos);

		} catch (Exception e) {
			e.printStackTrace();

			Element error = new Element("error");
			Attribute aError = new Attribute("mensagem", e.getMessage());
			error.setAttribute(aError);

			eResp.addContent(error);

			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return new XMLOutputter().outputString(resposta);
	}

	private String SqlExecutaSemCommit(Connection conServer, String execute) throws Exception {
		CallableStatement cs = null;
		try {
			String retorno = "";

			if (conServer != null) {
				conServer.setAutoCommit(false);
				cs = conServer.prepareCall("exec " + execute);
				cs.registerOutParameter(1, java.sql.Types.INTEGER);
				cs.execute();
				if (cs.getInt(1) > 0) {
					retorno = cs.getString(1);
				} else {
					retorno = "";
				}

				return retorno;

			} else {
				return RetornoErro();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (cs != null) {
				cs.close();
			}
		}

	}

	public String sincronizarBanco(String xml) {
		Connection connection = null;
		String banco = "";
		String vendedor = "";
		String empresa = "";
		String erro = "";
		Connection conServer = null;
		String tabelaString = "";
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new StringReader(xml));
			Element root = (Element) doc.getRootElement();

			banco = root.getAttributeValue("banco");
			vendedor = root.getAttributeValue("vendedor");
			empresa = root.getAttributeValue("empresa");

			// criar Banco de dados
			connection = criarBanco(vendedor, empresa);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			conServer = getConnection(banco);

			List listCreate = root.getChildren();

			Iterator i = listCreate.iterator();

			while (i.hasNext()) {
				StringBuilder insert = new StringBuilder();

				Element create = (Element) i.next();
				// sqlite
				statement.executeUpdate(create.getAttributeValue("exec"));
				System.out.println("Criou TABLE: " + create.getAttributeValue("exec"));

				if (create.getChild("select") != null) {
					// server
					Statement stmt = conServer.createStatement();
					ResultSet sql = stmt.executeQuery(create.getChild("select").getAttributeValue("exec"));
					ResultSetMetaData rsMetaData = sql.getMetaData();

					List itens = create.getChild("select").getChild("campo").getChildren();
					Iterator e = itens.iterator();

					StringBuilder valores = new StringBuilder();
					int inicial = 0;
					while (e.hasNext()) {

						Element item = (Element) e.next();
						if (inicial != 0) {
							valores.append(", ");
						}
						valores.append(item.getAttributeValue("value"));
						inicial++;
					}
					insert.append("BEGIN; ");

					int inicialValue = 0;

					while (sql.next()) {
						tabelaString = create.getChild("select").getChild("campo").getAttributeValue("tabela");
						insert.append("insert into ");
						insert.append(create.getChild("select").getChild("campo").getAttributeValue("tabela"));
						insert.append("(");
						insert.append(valores.toString());
						insert.append(") values");

						insert.append("(");

						for (int j = 0; j < itens.size(); j++) {
							if (inicialValue != 0) {
								insert.append(", ");
							}
							inicialValue++;

							Element item = (Element) itens.get(j);
							if (item.getAttributeValue("type").equals("text")) {
								String novo = sql.getString(item.getAttributeValue("value"));
								try {
									novo = novo.replace("'", "''");
								} catch (NullPointerException ex) {
									novo = "";
								}
								insert.append("'");
								insert.append(novo);
								insert.append("'");
							} else if (item.getAttributeValue("type").equals("INTEGER")) {
								insert.append(sql.getInt(item.getAttributeValue("value")));
							} else if (item.getAttributeValue("type").equals("DECIMAL") || item.getAttributeValue("type").equals("REAL")) {
								insert.append(sql.getDouble(item.getAttributeValue("value")));
							} else if (item.getAttributeValue("type").equals("DATE")) {
								String valor = sql.getString(item.getAttributeValue("value"));
								try {
									valor = valor.substring(6, 10) + "-" + valor.substring(3, 5) + "-" + valor.substring(0, 2);
								} catch (Exception ee) {
									valor = "";
								}
								insert.append("DATE(");
								insert.append("'");
								insert.append(valor);
								insert.append("'");
								insert.append(")");
							} else if (item.getAttributeValue("type").equals("FPClienteCliID")) {
								ResultSet rs = statement.executeQuery("select _id from clientes where cpf_cnpj = '" + sql.getString("cpf_cnpj") + "'");
								System.out.println(sql.getString("cpf_cnpj"));
								while (rs.next()) {
									insert.append(rs.getLong(1));
								}
							} else if (item.getAttributeValue("type").equals("CODIGO")) {
								String valor = sql.getString(item.getAttributeValue("type"));
								insert.append(valor);
							} else {
								try {
									double test = Double.parseDouble(item.getAttributeValue("type"));
									insert.append(item.getAttributeValue("type"));
								} catch (Exception ee) {
									insert.append("'");
									insert.append(item.getAttributeValue("type"));
									insert.append("'");
								}
							}

						}
						insert.append("); ");
						inicialValue = 0;
					}
					insert.append(" COMMIT;");

					try {
						System.out.println("Inserir TABLE: " + insert.toString().substring(0, 50));
					} catch (Exception ee) {
						System.out.println("Inserir TABLE: " + insert.toString());
					}

					statement.executeUpdate(insert.toString());

					stmt.close();
				}
				statement.close();
			}

		} catch (Exception e) {
			if (conexao_erro.isEmpty()) {
				erro = "java.sql.SQLException - " + tabelaString + ": " + e.getMessage();
			} else {
				erro = "java.sql.SQLException: " + conexao_erro;
			}
			e.printStackTrace();
		} finally {
			try {
				if (conServer != null) {
					conServer.close();
				}
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
		if (erro.isEmpty()) {
			return empresa + vendedor + ".db";
		} else {
			return erro;
		}
	}

	private Connection criarBanco(String vendedor, String empresa) {

		Connection connection = null;
		try {

			Class.forName("org.sqlite.JDBC");
			// create a database connection
			try {
				File file = new File("webapps/axis/" + empresa + vendedor + ".db");
				if (file.exists()) {
					file.delete();
				}
				connection = DriverManager.getConnection("jdbc:sqlite:webapps/axis/" + empresa + vendedor + ".db");
			} catch (Exception e) {
				System.out.println("Banco SQlite3 Criado em Memoria APENAS!");
				connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return connection;
	}

	public static void main(String[] args) {
		String aa = "";
		SmartMobile a = new SmartMobile();
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sinc banco=\"fer\"><create exec=\"CREATE TABLE CLIENTES (_id INTEGER PRIMARY KEY, NOME     TEXT,FANTASIA TEXT,CPF_CNPJ TEXT,INSC_EST TEXT,RESPONSAVEL TEXT,CIDADE   TEXT,ENDERECO TEXT,NUMERO   TEXT,BAIRRO   TEXT,COMPLEMENTO TEXT,CEP      TEXT,TELEFONE TEXT,CELULAR  TEXT,EMAIL    TEXT,OBS      TEXT,LIMITE   TEXT,ULT_DATA TEXT,ULT_TOTAL DECIMAL, LISTAID   INTEGER,FORMA_PGTOID INTEGER,SINCRONIZADO INTEGER)\">" + "<select exec=\"SELECT TOP 3000 NOME,FANTASIA,CPF_CNPJ,INSC_EST,RESPONSAVEL,CIDADE,LOGRADOURO as ENDERECO,NUMERO,BAIRRO,COMPLEMENTO,CEP,TELEFONE,CELULAR,EMAIL,OBS,LIMITE_CREDITO  as LIMITE,FORMA_PGTOID,LISTA_PRECOID as LISTAID,ATIVO,ULT_DATA,ULT_TOTAL from vw_mobile_clientes where empresaid = 1 order by nome desc\"><campo tabela=\"CLIENTES\"><item value=\"NOME\" type=\"text\" /><item value=\"FANTASIA\" type=\"text\" /><item value=\"CPF_CNPJ\" type=\"text\" /><item value=\"INSC_EST\" type=\"text\" /><item value=\"RESPONSAVEL\" type=\"text\" /><item value=\"CIDADE\" type=\"text\" /><item value=\"ENDERECO\" type=\"text\" /><item value=\"NUMERO\" type=\"text\" /><item value=\"BAIRRO\" type=\"text\" /><item value=\"COMPLEMENTO\" type=\"text\" /><item value=\"CEP\" type=\"text\" /><item value=\"TELEFONE\" type=\"text\" /><item value=\"CELULAR\" type=\"text\" /><item value=\"EMAIL\" type=\"text\" /><item value=\"OBS\" type=\"text\" /><item value=\"LIMITE\" type=\"text\" /><item value=\"ULT_DATA\" type=\"DATE\" /><item value=\"ULT_TOTAL\" type=\"DECIMAL\" /><item value=\"LISTAID\" type=\"INTEGER\" /><item value=\"FORMA_PGTOID\" type=\"INTEGER\" /><item value=\"SINCRONIZADO\" type=\"1\" /></campo></select></create>" + "<create exec=\"CREATE TABLE FORMAS_PGTO(_id INTEGER PRIMARY KEY, FORMA_PGTOID  INTEGER,DESCRICAO     TEXT)\"><select exec=\"select forma_pgtoid,descricao from vw_mobile_formas_pgto\"><campo tabela=\"FORMAS_PGTO\"><item value=\"FORMA_PGTOID\" type=\"INTEGER\" /><item value=\"DESCRICAO\" type=\"text\" /></campo></select></create>" + "<create exec=\"CREATE TABLE LISTAS_PRECOS(_id INTEGER PRIMARY KEY, LISTAID       INTEGER,DESCRICAO     TEXT,TIPO_LISTA    TEXT,PERCENTUAL    DECIMAL)\"><select exec=\"select LISTAID, DESCRICAO, TIPO_LISTA, PERCENTUAL from vw_mobile_listas_precos\"><campo tabela=\"LISTAS_PRECOS\"><item value=\"LISTAID\" type=\"INTEGER\" /><item value=\"DESCRICAO\" type=\"text\" /><item value=\"TIPO_LISTA\" type=\"text\" /><item value=\"PERCENTUAL\" type=\"DECIMAL\" /></campo></select></create>" + "<create exec=\"CREATE TABLE LISTAS_PRECOS_PRODUTOS(_id INTEGER PRIMARY KEY, LISTAID       INTEGER,PRODUTOID     INTEGER,TIPO          TEXT,PERCENTUAL    DECIMAL)\"><select exec=\"select LISTAID, PRODUTOID, TIPO, PERCENTUAL from vw_mobile_listas_precos_produtos where empresaid = 1\"><campo tabela=\"LISTAS_PRECOS_PRODUTOS\"><item value=\"LISTAID\" type=\"INTEGER\" /><item value=\"PRODUTOID\" type=\"INTEGER\" /><item value=\"TIPO\" type=\"text\" /><item value=\"PERCENTUAL\" type=\"DECIMAL\" /></campo></select></create>" + "<create exec=\"CREATE TABLE CLIENTES_FORMAS_PGTO (_id INTEGER PRIMARY KEY, CLIENTEID  INTEGER,FORMA_PGTOID INTEGER)\"><select exec=\"select cpf_cnpj, padrao, forma_pgtoid from vw_mobile_clientes_formas_pgto where empresaid = 1\"><campo tabela=\"CLIENTES_FORMAS_PGTO\"><item value=\"CLIENTEID\" type=\"FPClienteCliID\" /><item value=\"FORMA_PGTOID\" type=\"INTEGER\" /></campo></select></create>" + "<create exec=\"CREATE TABLE PRODUTOS (_id INTEGER PRIMARY KEY, PRODUTOID     INTEGER,UNIDADEID     INTEGER,UND           TEXT,LINHAID       INTEGER,COLUNAID      INTEGER,DESCRICAO     TEXT,LINHA         TEXT,COLUNA        TEXT,GRUPO         TEXT,DESC_MAX      DECIMAL,VALOR         DECIMAL,ESTOQUE       DECIMAL,IMPOSTOID     INTEGER,ALIQUOTA_IPI  DECIMAL)\"><select exec=\"select produtoid,descricao,grupo,unidadeid,un as UND,desconto_max as DESC_MAX,valor,estoque,linhaid,colunaid,linha,coluna,impostoid,aliquota_ipi from vw_mobile_produtos2 where empresaid = 1\"><campo tabela=\"PRODUTOS\"><item value=\"PRODUTOID\" type=\"INTEGER\" /><item value=\"UNIDADEID\" type=\"INTEGER\" /><item value=\"UND\" type=\"text\" /><item value=\"LINHAID\" type=\"INTEGER\" /><item value=\"COLUNAID\" type=\"INTEGER\" /><item value=\"DESCRICAO\" type=\"text\" /><item value=\"LINHA\" type=\"text\" /><item value=\"COLUNA\" type=\"text\" /><item value=\"GRUPO\" type=\"text\" /><item value=\"DESC_MAX\" type=\"DECIMAL\" /><item value=\"VALOR\" type=\"DECIMAL\" /><item value=\"ESTOQUE\" type=\"DECIMAL\" /><item value=\"IMPOSTOID\" type=\"INTEGER\" /><item value=\"ALIQUOTA_IPI\" type=\"DECIMAL\" /></campo></select></create>" + "<create exec=\"CREATE TABLE IMPOSTOS (_id INTEGER PRIMARY KEY, IMPOSTOID    INTEGER,UF           TEXT,ALIQUOTA_UF  DECIMAL,SUBS_ALIQ    DECIMAL,SUBS_IVA     DECIMAL)\"><select exec=\"select impostoid,uf,aliquota_uf,subs_aliq,subs_iva from vw_mobile_impostos\"><campo tabela=\"IMPOSTOS\"><item value=\"IMPOSTOID\" type=\"INTEGER\" /><item value=\"UF\" type=\"text\" /><item value=\"ALIQUOTA_UF\" type=\"DECIMAL\" /><item value=\"SUBS_ALIQ\" type=\"DECIMAL\" /><item value=\"SUBS_IVA\" type=\"DECIMAL\" /></campo></select></create>" + "<create exec=\"CREATE TABLE TITULOS (_id INTEGER PRIMARY KEY, TIPO       INTEGER,NOME       TEXT,CODIGO     INTEGER,DOCUMENTO  TEXT,EMISSAO    TEXT,VENCIMENTO TEXT,VALOR      REAL,HISTORICO  TEXT)\"><select exec=\"SELECT TIPO,CPF_CNPJ as NOME,CODIGO,DOCUMENTO,EMISSAO,VENCIMENTO,VALOR,HISTORICO from vw_mobile_titulos where empresaid = 1 order by VENCIMENTO_ORDER asc\"><campo tabela=\"TITULOS\"><item value=\"TIPO\" type=\"INTEGER\" /><item value=\"NOME\" type=\"text\" /><item value=\"CODIGO\" type=\"INTEGER\" /><item value=\"DOCUMENTO\" type=\"text\" /><item value=\"EMISSAO\" type=\"text\" /><item value=\"VENCIMENTO\" type=\"text\" /><item value=\"VALOR\" type=\"REAL\" /><item value=\"HISTORICO\" type=\"text\" /></campo></select></create>" + "<create exec=\"CREATE TABLE FLEX(_id INTEGER PRIMARY KEY, DATA       TEXT,REFERENCIA TEXT,ACRESCIMO  DECIMAL,DESCONTO   DECIMAL,SALDO      DECIMAL,SINCRONIZADO INTEGER)\"><select exec=\"SELECT DATA,REFERENCIA,ACRESCIMO,DESCONTO,SALDO FROM VW_MOBILE_SALDOS where vendedorid = 2 and empresaid = 1\"><campo tabela=\"FLEX\"><item value=\"DATA\" type=\"text\" /><item value=\"REFERENCIA\" type=\"text\" /><item value=\"ACRESCIMO\" type=\"DECIMAL\" /><item value=\"DESCONTO\" type=\"DECIMAL\" /><item value=\"SALDO\" type=\"DECIMAL\" /><item value=\"SINCRONIZADO\" type=\"INTEGER\" /></campo></select></create><create exec=\"CREATE TABLE CONFIG  (_id INTEGER PRIMARY KEY, SERVIDOR     TEXT,BANCO        TEXT,EMPRESAID    INTEGER,EMPRESA      TEXT,VENDEDORID   INTEGER,VENDEDOR     TEXT)\" /><create exec=\"CREATE TABLE VENDEDORES(_id INTEGER PRIMARY KEY, VENDEDORID   INTEGER,VENDEDOR     TEXT,GERENTE      INTEGER)\" /><create exec=\"CREATE TABLE EMPRESAS  (_id INTEGER PRIMARY KEY, EMPRESA      TEXT)\" /><create exec=\"CREATE TABLE METAS(_id INTEGER PRIMARY KEY, MES   INTEGER,META  DECIMAL,TOTAL DECIMAL)\" /><create exec=\"CREATE TABLE VENDAS&#x9;(_id INTEGER PRIMARY KEY, OPERACAO      INTEGER,CLIENTEID     INTEGER,DATA          TEXT,FORMA_PGTOID  INTEGER,LISTAID       INTEGER,OBS           TEXT,TOTAL         DECIMAL,TOTAL_ST      DECIMAL,SINCRONIZADO  INTEGER)\" /><create exec=\"CREATE TABLE VENDAS_ITENS&#x9;(_id INTEGER PRIMARY KEY, VENDAID      INTEGER,PRODUTOID    INTEGER,UNIDADEID    INTEGER,LINHAID      INTEGER,COLUNAID     INTEGER,QTDE         DECIMAL,ACRESCIMO    DECIMAL,DESCONTO     DECIMAL,VALOR        DECIMAL,FLEX_ACRESCIMO DECIMAL,FLEX_DESCONTO  DECIMAL,VALOR_ST       DECIMAL)\" /></sinc>";

		String xmlInserir = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ins banco=\"fer\"><clientes /><pedidos><venda id=\"17\" exec=\"SP_MOBILE_VENDA @retornoid = ?, @empresaid = 1, @representanteid = 1, @data = '24/04/2015', @cpf_cnpj = '07.902.764/0001-11', @forma_pgtoid = 1,  @listaid = 0,  @obs = ''\"><produtos><produto exec=\"SP_MOBILE_VENDA_PRODUTO3 @retornoid = ?, @empresaid = 1, @produtoid = 20, @unidadeid = 1, @linhaid = 0,@colunaid = 0, @qtde = 1, @valor = 10, @acrescimo = 0,@desconto = 2,  @operacao = 0, @vendaid = \" /></produtos><finaliza exec=\"SP_MOBILE_VENDA_FINALIZA @retornoid = ?, @empresaid = 1, @vendaid = \" /></venda><venda id=\"18\" exec=\"SP_MOBILE_VENDA @retornoid = ?, @empresaid = 1, @representanteid = 1, @data = '24/04/2015', @cpf_cnpj = '029.025.480-95', @forma_pgtoid = 1,  @listaid = 0,  @obs = ''\"><produtos><produto exec=\"SP_MOBILE_VENDA_PRODUTO3 @retornoid = ?, @empresaid = 1, @produtoid = 20, @unidadeid = 1, @linhaid = 0,@colunaid = 0, @qtde = 1, @valor = 10, @acrescimo = 0,@desconto = 5,  @operacao = 0, @vendaid = \" /></produtos><finaliza exec=\"SP_MOBILE_VENDA_FINALIZA @retornoid = ?, @empresaid = 1, @vendaid = \" /></venda></pedidos><saldos><saldo exec=\"SP_MOBILE_SALDO @retornoid = ?, @empresaid = 1, @vendedorid = 1, @referencia = 'Pedido N�17, [ ADD ] Produto 20 - Produto Teste', @acrescimo = 0, @desconto = 1\" id=\"42\" /><saldo exec=\"SP_MOBILE_SALDO @retornoid = ?, @empresaid = 1, @vendedorid = 1, @referencia = 'Pedido N�18, [ ADD ] Produto 20 - Produto Teste', @acrescimo = 0, @desconto = 5\" id=\"43\" /><saldo exec=\"SP_MOBILE_SALDO @retornoid = ?, @empresaid = 1, @vendedorid = 1, @referencia = 'Pedido N�17, [ DEL ]Produto 20', @acrescimo = 1, @desconto = 0\" id=\"44\" /><saldo exec=\"SP_MOBILE_SALDO @retornoid = ?, @empresaid = 1, @vendedorid = 1, @referencia = 'Pedido N�17, [ ADD ] Produto 20 - Produto Teste', @acrescimo = 0, @desconto = 2\" id=\"45\" /></saldos></ins>";

		try {
			// aa = a.SqlExecuta("GUILHERME", "");
			// aa = a.SqlConsulta("GUILHERME",
			// "select cpf_cnpj, padrao, forma_pgtoid from vw_mobile_clientes_formas_pgto where empresaid = 1#separadorConsulta#select produtoid,descricao,grupo,unidadeid,un,desconto_max,valor,estoque,linhaid,colunaid,linha,coluna,impostoid,aliquota_ipi from vw_mobile_produtos2 where empresaid = 1");
			// aa = a.SqlConsulta("GUILHERME",
			// "SELECT NOME,FANTASIA,CPF_CNPJ,INSC_EST,RESPONSAVEL,CIDADE,LOGRADOURO,NUMERO,BAIRRO,COMPLEMENTO,CEP,TELEFONE,CELULAR,EMAIL,OBS,LIMITE_CREDITO,FORMA_PGTOID,LISTA_PRECOID,ATIVO,ULT_DATA,ULT_TOTAL from vw_mobile_clientes where vendedorid = 1 and empresaid = 1 order by nome desc");

			// String as = a.sincronizarBanco(xml);
			// System.out.println(as);

			String retorno = a.sincronizarInsereBanco(xmlInserir);
			System.out.println(retorno);

			// aa = a.montarImagensZip("GUILHERME", 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(aa);
	}

	private void compress(final File input, final File output) throws IOException {
		if (!input.exists()) {
			throw new IOException(input.getName() + " n�o existe!");
		}
		if (output.exists()) {
			if (output.isDirectory()) {
				throw new IllegalArgumentException("\"" + output.getAbsolutePath() + "\" n�o � um arquivo!");
			}
		} else {
			final File parent = output.getParentFile();
			if (parent != null) {
				parent.mkdirs();
			}
			output.createNewFile();
		}
		final ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(output));
		zip.setLevel(Deflater.BEST_COMPRESSION);
		compress("", input, zip);
		zip.finish();
		zip.flush();
		zip.close();
	}

	private void compress(final String caminho, final File arquivo, final ZipOutputStream saida) throws IOException {
		final boolean dir = arquivo.isDirectory();
		final String nome = arquivo.getName();
		final ZipEntry elemento = new ZipEntry(caminho + '/' + nome + (dir ? "/" : ""));
		elemento.setSize(arquivo.length());
		elemento.setTime(arquivo.lastModified());
		saida.putNextEntry(elemento);
		if (dir) {
			final File[] arquivos = arquivo.listFiles();
			for (int i = 0; i < arquivos.length; i++) {
				// recursivamente adiciona outro arquivo ao ZIP
				compress(caminho + '/' + nome, arquivos[i], saida);
			}
		} else {
			final FileInputStream entrada = new FileInputStream(arquivo);
			copy(entrada, saida);
			entrada.close();
		}
	}

	private void copy(final InputStream in, final OutputStream out) throws IOException {
		final int n = 4096;
		final byte[] b = new byte[n];
		for (int r = -1; (r = in.read(b, 0, n)) != -1; out.write(b, 0, r)) {
		}
		out.flush();
	}

	public String getVersao() {
		return versaoWS + "";
	}

}
