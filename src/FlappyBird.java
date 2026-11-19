import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    //  DADOS DO JOGO

    // Tamanho da tela
    int larguraTela = 360;
    int alturaTela = 640;

    // Imagens
    Image Fundo;
    Image TaffeBird;
    Image Cano1;   // cano de cima
    Image Cano2;   // cano de baixo

    // Pássaro: posição e tamanho inicial
    int passaroX = larguraTela / 8;   // perto da esquerda
    int passaroY = alturaTela / 2;    // meio da tela
    int passaroLargura = 34;
    int passaroAltura = 24;

    // Cano: posição e tamanho inicial
    int canoX = larguraTela;          // nasce fora da tela, na direita
    int canoY = 0;
    int canoLargura = 64;
    int canoAltura = 512;

    // Margens de colisão
    int margemPassaroX = 5;
    int margemCanoX = 11;

    // Física
    int gravidadeY = 1;     // a cada frame, a queda acelera 1
    int velocidadeY = 0;    // velocidade vertical do pássaro
    int velocidadeX = -4;   // canos andam 4 pixels para a esquerda por frame

    // Controle do jogo
    Passaro passaro;
    ArrayList<Cano> Canos;  // lista com todos os canos da tela
    Timer loopDoJogo;       // atualiza o jogo 60 vezes por segundo
    Timer timerCanos;       // cria um par de canos a cada 1,5 segundo
    boolean fimDeJogo = false;
    double pontuacao = 0;

    //  CLASSES DOS OBJETOS

    class Passaro {
        int x = passaroX;
        int y = passaroY;
        int largura = passaroLargura;
        int altura = passaroAltura;
        Image img;

        Passaro(Image img) {
            this.img = img;
        }
    }

    class Cano {
        int X = canoX;
        int Y = canoY;
        int Largura = canoLargura;
        int Altura = canoAltura;
        Image img;
        boolean passou = false;   // evita dar o mesmo ponto duas vezes

        Cano(Image img) {
            this.img = img;
        }
    }

    //  PREPARAÇÃO

    FlappyBird() {
        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setFocusable(true);      // permite receber teclas
        addKeyListener(this);    // este painel escuta o teclado

        // Carrega as imagens
        Fundo = new ImageIcon(getClass().getResource("./Background.jpeg")).getImage();
        Cano1 = new ImageIcon(getClass().getResource("./Cano1.png")).getImage();
        Cano2 = new ImageIcon(getClass().getResource("./Cano2.png")).getImage();
        TaffeBird = new ImageIcon(getClass().getResource("./TaffeBird.png")).getImage();

        passaro = new Passaro(TaffeBird);
        Canos = new ArrayList<>();

        // Relógio 1: a cada 1,5 s cria um par de canos
        timerCanos = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColocarCanos();
            }
        });
        timerCanos.start();

        // Relógio 2: 60 vezes por segundo roda o jogo (chama actionPerformed)
        loopDoJogo = new Timer(1000 / 60, this);
        loopDoJogo.start();
    }

    //  CRIAR CANOS

    public void ColocarCanos() {
        // Sorteia a posição do cano de cima (parte dele fica fora da tela)
        int canoYAleatorio = (int) (canoY - canoAltura / 4 - Math.random() * (canoAltura / 2));
        int espacoAberto = alturaTela / 4;   // tamanho do buraco (160 px)

        Cano canoCima = new Cano(Cano1);
        canoCima.Y = canoYAleatorio;
        Canos.add(canoCima);

        Cano canoBaixo = new Cano(Cano2);
        canoBaixo.Y = canoCima.Y + canoAltura + espacoAberto; // começa depois do buraco
        Canos.add(canoBaixo);
    }

    //  DESENHAR

    public void paintComponent(Graphics g) {
        super.paintComponent(g);   // limpa a tela
        desenhar(g);
    }

    public void desenhar(Graphics g) {
        // O que é desenhado depois fica por cima
        g.drawImage(Fundo, 0, 0, larguraTela, alturaTela, null);
        g.drawImage(passaro.img, passaro.x, passaro.y, passaro.largura, passaro.altura, null);

        for (int i = 0; i < Canos.size(); i++) {
            Cano cano = Canos.get(i);
            g.drawImage(cano.img, cano.X, cano.Y, cano.Largura, cano.Altura, null);
        }

        // Texto da pontuação
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        if (fimDeJogo) {
            g.drawString("Game Over: " + (int) pontuacao, 10, 35);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Aperte ESPAÇO para reiniciar", 50, alturaTela / 2);
        } else {
            g.drawString(String.valueOf((int) pontuacao), 10, 35);
        }
    }

    //  LÓGICA

    public void mover() {
        // --- Pássaro ---
        passaro.y += velocidadeY;            // aplica a velocidade
        passaro.y = Math.max(passaro.y, 0);  // não sai pelo topo
        velocidadeY += gravidadeY;           // gravidade: cai cada vez mais rápido

        // --- Canos ---
        for (int i = 0; i < Canos.size(); i++) {
            Cano cano = Canos.get(i);
            cano.X += velocidadeX;           // anda para a esquerda

            // Passou do cano? Ganha 0,5 (2 canos por par = 1 ponto)
            if (!cano.passou && passaro.x > cano.X + cano.Largura) {
                pontuacao += 0.5;
                cano.passou = true;
            }

            // Bateu no cano?
            if (colisao(passaro, cano)) {
                fimDeJogo = true;
            }
        }

        // Remove canos que saíram da tela (de trás para frente, para não pular nenhum)
        for (int i = Canos.size() - 1; i >= 0; i--) {
            if (Canos.get(i).X + Canos.get(i).Largura < 0) {
                Canos.remove(i);
            }
        }

        // Caiu no chão?
        if (passaro.y > alturaTela) {
            fimDeJogo = true;
        }
    }

    // Os retângulos se sobrepõem na horizontal E na vertical?
    boolean colisao(Passaro a, Cano b) {
        int passaroEsquerda = a.x + margemPassaroX;
        int passaroDireita = a.x + a.largura - margemPassaroX;
        int canoEsquerda = b.X + margemCanoX;
        int canoDireita = b.X + b.Largura - margemCanoX;

        return passaroEsquerda < canoDireita &&
                passaroDireita > canoEsquerda &&
                a.y < b.Y + b.Altura &&
                a.y + a.altura > b.Y;
    }

    // LOOP DO JOGO 60x por segundo

    @Override
    public void actionPerformed(ActionEvent e) {
        mover();      // atualiza tudo
        repaint();    // redesenha a tela
        if (fimDeJogo) {          // se perdeu, para os relógios
            timerCanos.stop();
            loopDoJogo.stop();
        }
    }

    // TECLADO

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (fimDeJogo) {
                // Reinicia: volta tudo ao começo
                passaro.y = passaroY;
                velocidadeY = 0;
                Canos.clear();
                pontuacao = 0;
                fimDeJogo = false;
                loopDoJogo.start();
                timerCanos.start();
            } else {
                velocidadeY = -9;   // pulo (negativo = para cima)
            }
        }
    }

    // Obrigatórios pela interface KeyListener, mas não usados
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}