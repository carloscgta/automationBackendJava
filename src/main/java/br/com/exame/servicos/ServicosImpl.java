package br.com.exame.servicos;


public interface ServicosImpl {

	Resposta getEndPoint(String endPoint);
    Resposta postEndPoint(String endPoint, Object mensagem);
    Resposta putEndPoint(String endPoint, Object mensagem);
    Resposta deleteEndpoint(String endPoint);

}
