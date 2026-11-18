import javax.swing.*; // biblioteca para fazer janela

public class SMain {
    public static void main(String[] args) throws Exception {

        int larguraTela = 360;
        int alturaTela = 640;

        JFrame janela = new JFrame("Flappy Bird"); // cria a janela
        janela.setSize(larguraTela, alturaTela); // define o tamanho, altura e largura
        janela.setLocationRelativeTo(null); // fixa a janela no meio, sem possibilidade de mover
        janela.setResizable(false); // bloqueia o tamanho da janela para não alterar tamanho
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // faz o "x" para fechar a janela

        FlappyBird jogo = new FlappyBird();
        janela.add(jogo); // add painel do jogo na janela principal
        janela.pack(); // ajusta com tamanho total, ajusta automáticamente
        jogo.requestFocus();
        janela.setVisible(true); // deixar janela visivel

    }
}
