Flappy Bird em Java 
Projeto de jogo inspirado no Flappy Bird, desenvolvido em Java com a biblioteca Swing. O objetivo é controlar um pássaro, desviar dos canos e marcar a maior quantidade possível de pontos.
Funcionalidades
- Controle do pássaro pela barra de espaço.
- Gravidade e movimentação vertical do pássaro.
- Geração de pares de canos em alturas aleatórias a cada 1,5 segundo.
- Movimento contínuo dos canos da direita para a esquerda.
- Detecção de colisões entre o pássaro e os canos.
- Pontuação de 1 ponto por par de canos ultrapassado.
- Tela de Game Over e reinício pela barra de espaço.
Tecnologias
- Java: linguagem utilizada na lógica do jogo.
- Swing (JFrame, JPanel, Timer): janela, área de desenho e atualizações periódicas.
- AWT (Graphics, Image, KeyEvent): desenho das imagens e captura de eventos do teclado.
- ArrayList: armazenamento e gerenciamento dos canos em movimento.
Estrutura do projeto
A estrutura abaixo representa uma organização simples para executar o código apresentado:
FlappyBird/
├── Main.java
├── FlappyBird.java
├── Background.jpeg
├── TaffeBird.png
├── Cano1.png
├── Cano2.png
└── README.md
Atenção: os nomes dos arquivos de imagem devem corresponder exatamente aos usados no código, incluindo letras maiúsculas, minúsculas e extensões. Os arquivos precisam estar acessíveis no classpath da aplicação.
Como executar
Pré-requisitos
- JDK 8 ou superior instalado.
- Terminal ou uma IDE com suporte a Java, como o VS Code.
Pelo terminal
Com os arquivos .java e as imagens na mesma pasta, abra o terminal nessa pasta e execute:
javac Main.java FlappyBird.java
java Main
O primeiro comando compila as classes. O segundo inicia o jogo.

Se estiver usando o VS Code, você também pode abrir a pasta do projeto e executar o método main da classe Main com a extensão de Java instalada.
Se as imagens não carregarem, confira se elas estão na pasta correspondente às classes compiladas e se seus nomes estão corretos. Se a barra de espaço não funcionar inicialmente, clique na janela do jogo para dar foco a ela.

Como jogar
1. Execute o projeto.
2. Pressione ESPAÇO para fazer o pássaro subir.
3. Solte a tecla e deixe a gravidade agir, ajustando os saltos para atravessar os espaços entre os canos.
4. Evite bater nos obstáculos ou cair para fora da tela.
5. Quando aparecer Game Over, pressione ESPAÇO para reiniciar.
Como o código funciona
Main.java
É o ponto de entrada da aplicação. Cria a janela com JFrame, configura sua aparência e adiciona o painel FlappyBird.
FlappyBird.java
Contém a lógica principal:
Elemento	Responsabilidade
Passaro	Guarda a posição, as dimensões e a imagem do pássaro.
Cano	Guarda a posição, as dimensões, a imagem e o controle de pontuação de cada cano.
ColocarCanos()	Cria os pares de obstáculos em posições verticais variáveis.
mover()	Aplica a gravidade, move os canos e atualiza a pontuação.
colisao()	Verifica a sobreposição das áreas do pássaro e dos canos.
paintComponent() e desenhar()	Atualizam os elementos visuais.
actionPerformed()	Executa o ciclo de atualização e interrompe os timers no fim da partida.
keyPressed()	Detecta a barra de espaço para saltar ou reiniciar.


Configurações do jogo
Parâmetro	Valor
Área do jogo	360 × 640 pixels
Pássaro	34 × 24 pixels
Largura dos canos	64 pixels
Espaço entre os canos	160 pixels
Intervalo de criação de canos	1.500 ms
Intervalo do loop	16 ms (configurado com 1000 / 60)
Velocidade horizontal dos canos	−4 pixels por atualização
Velocidade inicial do salto	−9
Gravidade	+1 por atualização


O jogo usa dois timers: um para criar os canos e outro para atualizar os movimentos e solicitar o redesenho da tela. A colisão é calculada comparando áreas retangulares, com margens horizontais para compensar as bordas transparentes das imagens.
Conceitos praticados
Este projeto exercita programação orientada a objetos, classes e construtores, herança, interfaces, listas dinâmicas, condicionais, laços de repetição, eventos de teclado, temporizadores e desenho de interfaces gráficas.
Observação
O projeto foi construído com recursos nativos do Java, sem bibliotecas externas. O código e os arquivos de imagem precisam estar disponíveis localmente para executar a aplicação.
