# Integrar em um app

Comece pelo [fluxo de revisão por app](revisao-apps.md), que reúne licença,
condições de distribuição, apoio e candidatos a recursos pagos. A ficha de cada
app fica local; `recipientReviewed` continua restrito à conferência do recebedor.

1. Clone o repositório ao lado do app e fixe uma tag, seguindo o README. Use
   `includeBuild("../apoio-apps")` e a dependência `android`; `core` chega
   transitivamente. Projetos JVM podem depender somente de `core`.
2. Configure uma conta diretamente no provedor e obtenha a URL pública do apoio.
   A biblioteca não precisa de senha, CPF, chave de API ou dados de cartão.
3. Confira o destino final e o recebedor, inclusive após redirecionamentos.
   Configure `Support.Method` com o domínio do provedor. `recipientReviewed`
   é uma declaração do responsável pela configuração, sem verificação automática.
4. Defina o canal na variante de build. Não aceite o canal de Intent, URL ou
   parâmetros enviados pelo pagador. `GOOGLE_PLAY`, `UNKNOWN` e nulo ficam sem links.
5. Use `catalog.available(channel)` para montar uma entrada em Sobre/Ajuda.
   Se a lista estiver vazia, esconda a entrada. Apoio é opcional e não libera recursos.
6. No clique, use `SupportLinks.open`. Trate `UNAVAILABLE`, `NO_HANDLER` e
   `BLOCKED` com uma mensagem simples. `LAUNCHED` significa apenas que o Android
   aceitou a Intent; retorno do navegador nunca confirma pagamento.
7. Se o app atua como navegador/roteador padrão, passe no quinto argumento o
   pacote de um navegador externo escolhido pelo usuário. O próprio pacote é
   bloqueado; o resolvedor padrão pode encaminhar o link de volta ao roteador.

O README contém um exemplo Kotlin desativado. Só habilite após substituir o
destino e conferir o recebedor. A UI (Views, Compose, textos, temas e idiomas)
pertence ao app consumidor. Não há tela pronta nem configuração remota.

## Verificar o app consumidor

Teste tema claro/escuro, idiomas, leitor de tela, fonte ampliada, ausência de
navegador, cancelamento e retorno sem pagamento. Execute os checks exigidos
pelo app. Os testes desta biblioteca usam JVM/mocks; não substituem testes do
fluxo real em Android. Não foi realizada transação financeira nesta entrega.

Um `.github/FUNDING.yml` pode complementar o botão dentro do app. Copie o
[exemplo](../examples/FUNDING.yml.example) e ative apenas os perfis configurados.
Em forks, preserve os créditos e o financiamento do projeto original.

## Limites

O núcleo é Java 8, sem rede, telemetria, valores, dados de doadores ou estado de
pagamento. O launcher é um JAR Android para celular (API 19+), sem recursos ou
manifest. Não é um plugin Gradle, npm, Flutter ou ChatGPT. Não há SDK JavaScript.

Wear OS exige encaminhamento ao telefone; o adaptador ainda não foi implementado.
Google Play exige avaliar o fluxo e as regras aplicáveis antes de habilitar apoio.
Premium, remoção de anúncios e assinaturas precisam de uma implementação própria.

Se conciliação financeira vier a ser necessária, implemente no servidor webhooks
autenticados, deduplicação, consulta ao provedor e tratamento de reembolso.
Cliques, URLs de sucesso e retorno do navegador não são prova de pagamento.
