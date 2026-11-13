# Apoio voluntário nos apps do GitHub

Pesquisa consultada em **07/11/2026**. Objetivo: decidir como oferecer apoio
voluntário em vários apps do mesmo desenvolvedor, usando uma base comum.

## Recomendação para o portfólio

Começar com apoio opcional em **Sobre/Ajuda/Configurações**, checkout hospedado
e uma conta recebedora por provedor, com links identificáveis por app quando o
provedor permitir. Complementar com o botão Sponsor nos repositórios públicos.
Evitar um backend financeiro próprio antes de comprovar demanda.

Para público brasileiro, avaliar primeiro Pix por uma solução local ou página
própria de apoio; para público internacional, comparar Ko-fi e Buy Me a Coffee.
GitHub Sponsors é um canal adicional para pessoas que já acompanham o código.
A escolha final depende do país do recebedor, público, titularidade e contas
disponíveis — informações que precisam ser confirmadas em cada implantação.

Doação é uma hipótese de receita complementar. Esta pesquisa não fornece evidência
de que conseguirá sustentar os apps sozinha. Apoio voluntário não deve prometer
recursos, remoção de anúncios, badges ou prioridade de suporte como contrapartida;
esses benefícios pertencem a outro modelo comercial e exigem outro desenho.

## Método e limites

Foram consultados três projetos públicos e documentos oficiais dos provedores.
Esta versão pública apresenta somente essas referências. É uma amostra qualitativa,
sem estimativa de participação de mercado, faturamento, conversão ou prevalência.
Código público não comprova aprovação por lojas nem desempenho comercial.
As tarifas são um retrato da consulta de 07/11/2026; conferir as fontes antes de contratar.

## O que projetos semelhantes implementam

| Projeto | Evidência observada | Aplicação ao portfólio |
|---|---|---|
| LocalSend | Tela de doações, fluxo de compras conforme plataforma e alternativa externa com GitHub/Ko-fi; modelo FOSS sem provedor de compras | Uma mesma entrada de apoio pode variar por distribuição |
| Canta | `FUNDING.yml` com GitHub Sponsors e link de doação PayPal | Captação no repositório é simples e pode coexistir com o app |
| SD Maid SE | `FUNDING.yml` com Sponsors/Buy Me a Coffee e implementações diferentes de upgrade em `foss` e `gplay` | Separar apoio livre de compra/desbloqueio e separar canais desde o início |

Fontes primárias: [tela LocalSend](https://github.com/localsend/localsend/blob/main/app/lib/pages/donation/donation_page.dart),
[modelo LocalSend](https://github.com/localsend/localsend/blob/main/app/lib/pages/donation/donation_page_vm.dart),
[apoio LocalSend](https://localsend.org/donate),
[FUNDING Canta](https://github.com/samolego/Canta/blob/master/.github/FUNDING.yml),
[FUNDING SD Maid](https://github.com/d4rken-org/sdmaid-se/blob/main/.github/FUNDING.yml),
[código por variante do SD Maid](https://github.com/d4rken-org/sdmaid-se/tree/main/app/src).

A conclusão derivada dessa amostra é que vale compartilhar configuração e regras,
preservando a experiência nativa de cada app. Não encontramos fundamento para
construir um processador de pagamentos próprio para esta primeira etapa.

## Comparação dos caminhos

| Opção | Custos publicados e limitações | Adequação |
|---|---|---|
| GitHub Sponsors | Sem tarifa de patrocínio vindo de conta pessoal; até 6% quando parte de organização. Recebedor precisa configurar perfil elegível | Código aberto e audiência técnica |
| Buy Me a Coffee | 5% de plataforma, mais processamento e possíveis custos de repasse, internacionalização e recorrência. Brasil consta da lista de países de repasse, sujeito ao cadastro | Experiência pronta e marca reconhecível |
| Ko-fi | Free: 0% de plataforma para gorjeta avulsa; 5% em mensalidades e outros produtos. Standard aplica 5% também a avulsas; novas contas começam nesse modo. Processamento é adicional | Bom candidato para comparar custo do apoio avulso |
| Stripe Payment Links | Cliente pode escolher o valor, mas esse modo não aceita recorrência. No Brasil, tabela consultada de cartão nacional: 3,99% + R$ 0,39. Pix anunciado por convite | Mais controle sem desenvolver checkout |
| Pix por PSP local | Mercado Pago oferece Pix e checkout hospedado. Taxas, enquadramento do apoio e disponibilidade precisam ser confirmados na conta | Público brasileiro e valores pequenos |
| Open Collective | Custos dependem de modalidade e fiscal host; pode haver processamento e taxa do host. Modelo para organizações mudou em março de 2026 | Comunidade com orçamento e despesas transparentes; mais operação que uma gorjeta pessoal |

Fontes: [tarifas GitHub](https://docs.github.com/en/sponsors/sponsoring-open-source-contributors/about-sponsorships-fees-and-taxes),
[tarifas BMAC](https://help.buymeacoffee.com/en/articles/8105744-how-to-calculate-charges-on-your-payment),
[países BMAC](https://help.buymeacoffee.com/en/articles/6258038-supported-countries-for-payouts-on-buy-me-a-coffee),
[tarifas Ko-fi](https://help.ko-fi.com/hc/en-us/articles/360002506494-Does-Ko-fi-take-a-fee),
[Payment Links](https://docs.stripe.com/payment-links/create),
[preços Stripe Brasil](https://stripe.com/br/pricing),
[Checkout Pro Mercado Pago](https://www.mercadopago.com.br/developers/pt/docs/checkout-pro-preferences/overview),
[preços Open Collective](https://documentation.opencollective.com/why-open-collective/pricing).

“0% de plataforma” não significa custo total zero. As tarifas dos processadores,
moeda, país e forma de recebimento podem mudar o resultado. Não aplique a tarifa
americana da documentação BMAC automaticamente a uma integração Stripe brasileira.
Também não presuma que Ko-fi em BRL ou BMAC aceitam Pix apenas por atenderem o Brasil.

Como ilustração da parcela fixa, usando só a tarifa publicada de cartão nacional
Stripe: R$ 10 deixariam aproximadamente R$ 9,21; R$ 30 deixariam R$ 28,41, antes
de outros custos ou tributos. Esses cálculos não representam proposta comercial
nem estimativa de líquido de Ko-fi/BMAC.

Na Stripe, gorjetas e doações têm definições próprias: apoio pelo software deve
ser apresentado de acordo com o enquadramento aceito para o serviço prestado.
Não basta nomear qualquer transferência como doação beneficente.
Fonte: [requisitos oficiais](https://support.stripe.com/questions/requirements-for-accepting-tips-or-donations).

## APK, Google Play e Wear OS

Na distribuição direta por APK, links externos são o caminho mais simples.
Na Play Store, a FAQ distingue apoio direto sem contrapartida digital de compras.
Ela condiciona a dispensa do billing a 100% da contribuição ir ao criador e à
ausência de conteúdo ou serviços digitais, incluindo badges. Não é possível
concluir daí que qualquer provedor com comissão se enquadra automaticamente.

Por isso, o protótipo deixa links desligados no canal Play. Uma implementação
posterior pode avaliar apoio direto que atenda aos critérios ou compras via
billing, conforme o caso real. Chamar o botão de “café” não decide o enquadramento.
Fonte: [FAQ de pagamentos da Play](https://support.google.com/googleplay/android-developer/answer/10281818?hl=en).

No Wear OS, a proposta é apresentar uma ação curta de apoio e continuar no celular,
com fallback claro quando não houver telefone/encaminhamento disponível. Esse
fluxo é requisito futuro do módulo, não funcionalidade já entregue.

## Como deve ser o módulo comum

O núcleo concentra catálogo, destinos e canal. Cada app controla texto, tema,
idioma e navegação. Os valores e o pagamento ficam no checkout do provedor.
Uma conta central reduz operação; links separados por app ajudam a reconciliar
origem quando suportados. No início, mudanças de configuração entram por release;
não foi criado um painel remoto.

Fluxo proposto: pessoa abre Sobre → toca Apoiar → escolhe meio disponível → vê
recebedor no checkout externo → decide apoiar. Retornar ao app não confirma
pagamento. Se não houver navegador, mostrar falha e permitir tentar depois.

Texto sugerido: “Este app te ajuda? Você pode apoiar o desenvolvimento com um
café. É opcional e não altera as funcionalidades.” Botão: “Apoiar o desenvolvimento”.
Começar sem popup no primeiro uso, sem cobrança recorrente preselecionada e sem
pedido durante ações de bateria, desinstalação ou automação.

Uma segunda fase pode usar backend para conciliação por app, apenas se houver
necessidade real: webhook autenticado, deduplicação por evento, consulta ao
provedor e tratamento de reembolso. Não marcar apoio como pago por um parâmetro
`success=true`, clique, retorno de navegador ou cópia de Pix. O núcleo atual não
registra estado de pagamento nem concede acesso.

## Validar se vale a pena

Pilotar em um app de celular com fluxo simples e usuários reais. Usar links/exports
distintos por app quando disponíveis e acompanhar apoios confirmados, valor médio,
receita líquida e tempo de operação. Cliques só entram se houver coleta apropriada;
não foi adicionada telemetria aos apps.

| Cenário puramente hipotético mensal | Conta | Receita bruta |
|---|---|---|
| 1.000 usuários ativos; 0,5% apoiam R$ 10 | 1.000 × 0,005 × 10 | R$ 50 |
| 10.000 usuários ativos; 0,5% apoiam R$ 10 | 10.000 × 0,005 × 10 | R$ 500 |
| 10.000 usuários ativos; 1% apoia R$ 15 | 10.000 × 0,01 × 15 | R$ 1.500 |

As taxas de conversão acima são premissas para sensibilidade, não benchmarks
medidos. Avaliar aproximadamente 30 dias e o número real de pessoas expostas;
tráfego baixo pode exigir observação mais longa. Comparar líquido com hospedagem,
tarifas e tempo gasto antes de expandir para todo o portfólio.

## Entrega e decisões pendentes

Esta biblioteca inclui núcleo Java e launcher Android testados na JVM.
O estado de repositório/pacotes está no [README](../README.md). Continuam
pendentes escolher provedor e país, configurar recebedor, integrar UI em piloto,
validar acessibilidade/retorno/ausência de navegador, testar pagamento/recebimento
e decidir a política de loja de cada build. A pesquisa não comprova esses fluxos.
