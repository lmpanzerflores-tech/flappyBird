import java.awt.*;
import java.awt.event.*; // importa classe necessária para eventos
import java.util.ArrayList; // definir coordenadas
import javax.swing.*;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int larguraTela = 360;
    int alturaTela = 640;

    Image Fundo;
    Image TaffeBird;
    Image Cano1; // cano de cima
    Image Cano2; // cano de baixo

    // define posição inicial do taffe bird
    int passaroX = larguraTela / 8;
    int passaroY = alturaTela / 2;
    int passaroLargura = 34;
    int passaroAltura = 24;

    class Passaro {
        int x = passaroX;
        int y = passaroY;
        int largura = passaroLargura;
        int altura = passaroAltura;

        Image img;

        Passaro(Image img) {
            this.img = img; // criação do taffe bird
        }
    }

    // cano
    int canoX = larguraTela;
    int canoY = 0;
    int canoLargura = 64;
    int canoAltura = 512;

    class Cano {
        int X = canoX;
        int Y = canoY;
        int Largura = canoLargura;
        int Altura = canoAltura;
        Image img;
        boolean passou = false; // se o taffe bird já passou desse cano

        Cano(Image img) {
            this.img = img;
        }
    }

    // diminui a área de colisão, pois as imagens têm borda transparente
    int margemPassaroX = 5;
    int margemCanoX = 11;

    // lógica do jogo
    int gravidadeY = 1;
    Passaro passaro;
    int velocidadeY = 0; // velocidade que o taffe bird pula
    int velocidadeX = -4; // velocidade que os canos se movem para a esquerda

    ArrayList<Cano> Canos;
    Random random = new Random();

    Timer loopDoJogo;
    Timer timerCanos;
    boolean fimDeJogo = false;
    double pontuacao = 0;

    FlappyBird() {

        setPreferredSize(new Dimension(larguraTela, alturaTela)); // cria o jogo com base nas dimensões definidas
        // setBackground(Color.blue); // define cor do fundo
        setFocusable(true);
        addKeyListener(this);

        Fundo = new ImageIcon(getClass().getResource("./Background.jpeg")).getImage();
        Cano1 = new ImageIcon(getClass().getResource("./Cano1.png")).getImage();
        Cano2 = new ImageIcon(getClass().getResource("./Cano2.png")).getImage();
        TaffeBird = new ImageIcon(getClass().getResource("./TaffeBird.png")).getImage();

        passaro = new Passaro(TaffeBird);
        Canos = new ArrayList<>();

        // timer que coloca um novo par de canos a cada 1,5 segundo
        timerCanos = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColocarCanos();
            }
        });
        timerCanos.start();

        loopDoJogo = new Timer(1000 / 60, this); // atualiza o jogo 60 vezes por segundo
        loopDoJogo.start();

    }

    public void ColocarCanos() {
        // sorteia a altura do cano de cima
        int canoYAleatorio = (int) (canoY - canoAltura / 4 - Math.random() * (canoAltura / 2));
        int espacoAberto = alturaTela / 4; // buraco por onde o taffe bird passa

        Cano canoCima = new Cano(Cano1);
        canoCima.Y = canoYAleatorio;
        Canos.add(canoCima);

        Cano canoBaixo = new Cano(Cano2);
        canoBaixo.Y = canoCima.Y + canoAltura + espacoAberto; // começa depois do buraco
        Canos.add(canoBaixo);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhar(g);

    }

    public void desenhar(Graphics g) {
        g.drawImage(Fundo, 0, 0, larguraTela, alturaTela, null);

        g.drawImage(TaffeBird, passaro.x, passaro.y, passaro.largura, passaro.altura, null);

        // canos
        for (int i = 0; i < Canos.size(); i++) {
            Cano cano = Canos.get(i);

            g.drawImage(cano.img, cano.X, cano.Y,
                    cano.Largura, cano.Altura, null);
        }

        // pontuação
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

    public void mover() {
        passaro.y += velocidadeY; // configura a velocidade y
        passaro.y = Math.max(passaro.y, 0); // delimita que o taffe bird não saia da tela
        velocidadeY += gravidadeY; // cria a gravidade para o jogo (taffe bird caí)

        // canos
        for (int i = 0; i < Canos.size(); i++) {
            Cano cano = Canos.get(i);
            cano.X += velocidadeX;

            // 0.5 por cano, como são 2 canos por par, cada par vale 1 ponto
            if (!cano.passou && passaro.x > cano.X + cano.Largura) {
                pontuacao += 0.5;
                cano.passou = true;
            }

            if (colisao(passaro, cano)) {
                fimDeJogo = true;
            }
        }

        // remove os canos que já saíram da tela
        for (int i = Canos.size() - 1; i >= 0; i--) {
            if (Canos.get(i).X + Canos.get(i).Largura < 0) {
                Canos.remove(i);
            }
        }

        // taffe bird caiu no chão
        if (passaro.y > alturaTela) {
            fimDeJogo = true;
        }
    }

    // verifica se o taffe bird (a) encostou no cano (b)
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

    @Override
    public void actionPerformed(ActionEvent e) {
        mover();
        repaint();
        if (fimDeJogo) { // quando perde, para o jogo
            timerCanos.stop();
            loopDoJogo.stop();
        }
    }

    // importa teclas padrões
    @Override // desabilita shift, ctrl e teclas especiais
    public void keyTyped(KeyEvent e) {
    }

    @Override // quando você digita, a função é ativada na hora
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocidadeY = -9;

            if (fimDeJogo) { // reinicia o jogo zerando tudo
                passaro.y = passaroY;
                velocidadeY = 0;
                Canos.clear();
                fimDeJogo = false;
                pontuacao = 0;
                loopDoJogo.start();
                timerCanos.start();
            }
        }
    }

    @Override // dispara quando você tira o dedo da tecla
    public void keyReleased(KeyEvent e) {
    }

}
