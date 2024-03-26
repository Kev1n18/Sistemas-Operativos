
import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest implements Runnable {

    final static String CRLF = "\r\n";

    Socket socket;

    private String senhaParaAutenticacao = "redes:computadores";

    public HttpRequest (Socket socket) throws Exception{
        this.socket = socket;
    }

    public void run(){

        try{

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processRequest() throws Exception{

        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        BufferedReader br = new BufferedReader(isr);
        String requestLine = br.readLine();

        System.out.println();
        System.out.println(requestLine);

        String headerLine = null;
        StringTokenizer senhaAutorizada = null;
        boolean ehAutenticada = false;
        boolean ehRestrito = false;


        String log = requestLine + System.getProperty("line.separator");

        while((headerLine = br.readLine()).length() != 0){

            log = log + (headerLine + System.getProperty("line.separator"));

            if (headerLine.contains(("Authorization: basic"))) {

                senhaAutorizada = new StringTokenizer(headerLine);
                senhaAutorizada.nextToken();
                senhaAutorizada.nextToken();

                String senha = senhaAutorizada.nextToken();

                if (Base64Coder.decodeString(senha).equals(this.senhaParaAutenticacao)){
                    ehAutenticada = true;
                }
            }
            System.out.println(headerLine);
        }
        StringTokenizer requisicao = new StringTokenizer(requestLine);
        String metodo = requisicao.nextToken();
        String arquivo = requisicao.nextToken();
        arquivo = "." + arquivo;
        FileInputStream fis = null;
        boolean existeArq = true;
        String linhaStatus = null;
        String linhaContentType = null;
        String msgHtml = null;

        if (metodo.equals("GET")){

            try {
                fis = new FileInputStream(arquivo);
            } catch (FileNotFoundException e){
                existeArq = false;
            }

            if (arquivo.contains("RESTRITO")|| arquivo.contains("restrito")){
                ehRestrito = true;
            }


        }
    }

}
