package br.com.github.puti.engine.socket.client;

import java.util.Scanner;

import br.com.github.puti.engine.socket.logger.Console;

public class Main {

    public static void main(String[] args) throws Exception {

        Client client = new Client("localhost", "ClientName");
        client.on("read", (ConnectedPacket) -> {
            Console.log("Cliente conectado.");
        });

        @SuppressWarnings("resource")
        Scanner s = new Scanner(System.in);
        boolean loop = true;

        while (loop) {
            System.out.println("");
            System.out.println("Cliente OK!");

            int opcao = s.nextInt();

            switch (opcao) {
                case 1:
                    s.nextLine();
                    System.out.println("Escreva sua mensagem");
                    String message = s.nextLine();
                    System.out.printf(client.getName()+": %s%n", message);
                    client.writeMessage("data", message);
                    break;
                default:
                    System.out.println("Esse comando não existe");
            }
        }

    }

}
