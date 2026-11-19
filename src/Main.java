import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame janela = new JFrame("Flappy Bird"); // cria a janela com título
        janela.setResizable(false);                // não deixa mudar o tamanho
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // o X fecha o programa

        FlappyBird jogo = new FlappyBird();        // cria o jogo
        janela.add(jogo);                          // coloca o jogo dentro da janela
        janela.pack();                             // ajusta a janela ao tamanho do jogo (360x640)
        janela.setLocationRelativeTo(null);        // centraliza na tela (depois do pack)
        jogo.requestFocus();                       // faz o jogo receber o teclado
        janela.setVisible(true);                   // mostra a janela
    }
}