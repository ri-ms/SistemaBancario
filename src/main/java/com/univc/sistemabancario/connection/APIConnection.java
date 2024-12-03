package com.univc.sistemabancario.connection;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.univc.sistemabancario.Conta;
import com.univc.sistemabancario.connection.DTO.ContaDTO;
import com.univc.sistemabancario.connection.DTO.TipoDTO;
import okhttp3.*;

import java.net.URI;
import java.net.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class APIConnection {
    private String HTTPAddress;
    private String port;

    public APIConnection(String HTTPAddress, String port){
        this.HTTPAddress = HTTPAddress;
        this.port = port;
    }
    public APIConnection(){
        this.HTTPAddress = "http://localhost";
        this.port = "8080";
    }

    public String getUrl(String endpoint){
        return this.HTTPAddress + ":" + this.port + endpoint;
    }

    TipoDTO tipoDTO = new TipoDTO();
    ContaDTO contaDTO = new ContaDTO();

    public ResponseEntity<Void> sendAPIBody(String endpoint, String JsonBody) throws RuntimeException{
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JsonBody, JSON);
        String url = this.HTTPAddress + ":" + port + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
    }
    public Response getAPIResponse(String endpoint) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String url = this.HTTPAddress + ":" + port + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }


    public ContaDTO postConta(ContaDTO conta){
        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(conta);

            // Criando a requisição HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/banco/cadastro")))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            contaDTO = mapper.readValue(response.body(), ContaDTO.class);
        } catch (Exception e) {
            System.out.println("Erro ao realizar a requisição POST: " + e.getMessage());
        }
        return contaDTO;
    }

    public List<ContaDTO> getContas() throws Exception{
        List<ContaDTO> listaContas = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/banco/listar"))).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            listaContas = mapper.readValue(response.body(), new TypeReference<List<ContaDTO>>(){});
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return listaContas;
    }

    public ContaDTO getConta(int id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/banco/login/" + id))).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            contaDTO = mapper.readValue(response.body(), ContaDTO.class);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return contaDTO;
    }

    public TipoDTO getTipo(int id) throws Exception{
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/tipo/" + id))).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            tipoDTO = mapper.readValue(response.body(), TipoDTO.class);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return tipoDTO;
    }

    public void deleteConta(int id) throws Exception{
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/banco/delete/" + id)))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public HttpResponse<String> realizarSaque(int id, double quantidade) throws Exception{
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Criando a requisição HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/banco/saque/"+id+"/"+quantidade)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (Exception e) {
            throw new Exception("Erro ao realizar a requisição POST: " + e.getMessage());
        }
    }

    public String realizarDeposito(int id, double quantidade) throws Exception{
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Criando a requisição HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/banco/deposito/"+id+"/"+quantidade)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new Exception("Erro ao realizar a requisição POST: " + e.getMessage());
        }
    }

    public double realizarRendimento(int id) throws Exception{
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Criando a requisição HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.getUrl("/banco/rend/"+id)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            double valor = mapper.readValue(response.body(), Double.class);
            return valor;
        } catch (Exception e) {
            throw new Exception("Erro ao realizar a requisição POST: " + e.getMessage());
        }
    }
}
