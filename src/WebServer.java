
import java.io.IOException;
import java.net.*;

public final class WebServer {
    public static void main(String[] args) throws IOException {
        //escolher porta
        int porta = 8181;

        //definir ligação do socket com o servidor
        ServerSocket socketServ = new ServerSocket(porta);

        // definir ligação do socket com o cliente.
        Socket socketCLi;

        //loop infinito propositado para servidor funcionar sempre
        while(true){

            System.out.println("Servidor Ativo");

            //Ligar o socket a internet com protocolo TCP
            socketCLi = socketServ.accept();


        }
    }
}
