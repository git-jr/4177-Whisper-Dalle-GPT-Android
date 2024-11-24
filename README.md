![androidAI-banner](https://github.com/user-attachments/assets/cde0207e-b0e1-43bf-b7ed-c5f05c543502)



# AnotaAI
A segunda versão [do aplicativo Android para criação de notas](https://github.com/git-jr/4176-Github-Copilot-Android-Studio) que permite anexas fotos, imagens, gravar áudios e claro escrever textos.
Essa nova versão conta com funções que utilizam [inteligência artificial generativa da OpenAI](https://platform.openai.com/docs/api-reference), como DALL-E, GPT e Whisper, para gerar áudios, imagens, resumir textos e realizar transcrições.

## 🔨 Funcionalidades com uso de inteligência artificial generativa


https://github.com/user-attachments/assets/bff9f2c2-aae2-4dca-b2af-0cd17871a717



## 🔨 Funcionalidades base do projeto


https://github.com/user-attachments/assets/ac3a7585-6ceb-41b4-af09-f18f72efd712






### 📱 Telas
- **Início:** Lista as notas criadas com miniaturas de cada tipo, como imagens, áudios ou textos.  
- **Criação de notas:** Permite inserir textos, imagens da galeria, tirar fotos novas para anexar à nota e gravar áudios.  
- **Configurações:** Gerência das notas inseridas
- **Extras**: Ícones e botões que disparam as ações de IA estão localizados na tela de criação/edição de notas.


## ✔️ Técnicas e tecnologias utilizadas


As técnicas e tecnologias utilizadas pra isso são:

- `Jetpack Compose`: kit de ferramentas moderno para criar IUs em dispositivos móveis.
- `Kotlin`: linguagem de programação.
- `Gradle Version Catalogs`: nova forma de gerenciar plugins e dependências em projetos Android.
- `Room`: biblioteca de persistência do Android que simplifica o uso do SQLite para armazenar e consultar dados.
- `Hilt`: Framework para injeção de dependências que facilita a criação e manutenção de projetos.
- `Material Design 3`: padrão de design recomendado pela Google para criação de UI modernas.
- `Navigating with Compose`: navegação entre composables e telas.
- `Viewmodel, states e flow`: gerenciamento de estados e controle dos eventos disparados pelas detecções do modelo da Google.
- `CameraX`: biblioteca do Jetpack que facilita a integração de funcionalidades de câmera em aplicativos Android, abstraindo a complexidade da API de câmera do Android e oferecendo uma interface simples para captura de fotos e vídeo.
- `Camera Permissions`: gerencia o acesso à câmera do dispositivo, solicitando permissão ao usuário para utilizá-la nas detecções e interações dentro do aplicativo.
- `Photo Picker`: ferramenta que facilita a seleção de imagens diretamente da galeria do dispositivo Android.
- `Secrets Gradle Plugin`: Gerencia de forma segura a chave de API do projeto, evitando que informações sensíveis sejam expostas no código-fonte.  
- `DALL-E`: API da OpenAI para geração de imagens a partir de descrições em texto.  
- `Whisper`: Modelo da OpenAI para transcrição e reconhecimento de fala, usado para converter áudio em texto.  
- `GPT`: Modelo da OpenAI utilizado para processamento de linguagem natural, incluindo geração de texto e entendimento contextual.

## 📁 Acesso ao projeto

- Versão inicial: Veja o [código fonte][codigo-inicial] ou [baixe o projeto][download-inicial]
- Versão final: Veja o [código fonte][codigo-final] ou [baixe o projeto][download-final]

## 🛠️ Abrir e rodar o projeto  

Após baixar o projeto, você pode abri-lo com o Android Studio. Para isso, na tela de launcher clique em:  

1. **"Open"** (ou alguma opção similar).  
2. Procure o local onde o projeto está e o selecione (caso o projeto seja baixado via zip, é necessário extraí-lo antes de procurá-lo).  
3. Por fim, clique em **"OK"**.  

O Android Studio executará algumas *tasks* do Gradle para configurar o projeto. Aguarde até finalizar. Após isso, você pode executar o App 🏆  

### Configurando chaves de API com o Secrets Gradle Plugin  

Este projeto utiliza o **Secrets Gradle Plugin** para gerenciar de forma segura as chaves de API. Antes de executar o aplicativo, é necessário adicionar a chave da OpenAI ao arquivo `local.properties` no seguinte formato:  

```properties
OPENAI-KEY=sua-chave-aqui
```  

Caso você ainda não tenha uma chave, acesse o [painel da OpenAI](https://platform.openai.com/settings/api-keys) para gerar uma nova chave de API.  

Feito isso, você estará pronto para executar o app com todas as funcionalidades habilitadas! 🚀  


## 📚 Mais informações do curso

Gostou do projeto e quer conhecer mais? Você pode [acessar a formação com esse e muitos outros cursos](https://www.alura.com.br/cursos-online-inteligencia-artificial/ia-para-mobile) relacioandos ao tema de Inteligência Artificial e Machine Learning no Android.

[codigo-inicial]: https://github.com/git-jr/4177-Whisper-Dalle-GPT-Android/commits/projeto-inicial
[download-inicial]: https://github.com/git-jr/4177-Whisper-Dalle-GPT-Android/archive/refs/heads/projeto-inicial.zip

[codigo-final]: https://github.com/git-jr/4177-Whisper-Dalle-GPT-Android/commits/aula-5/
[download-final]: https://github.com/git-jr/4177-Whisper-Dalle-GPT-Android/archive/refs/heads/aula-5.zip

