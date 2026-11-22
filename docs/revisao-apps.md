# Revisar licença, apoio e monetização de um app

O `apoio-apps` concentra a biblioteca de links e este fluxo de revisão para cada
consumidor. A ferramenta local reúne evidências; a ficha orienta a análise e a
decisão. O app mantém sua UI, seu canal e seus termos de distribuição.

## Gerar a ficha

Use Python 3.9+ e Git, a partir de um checkout do `apoio-apps` que contenha a ferramenta:

```sh
python3 tools/review_app.py --repo ../meu-app
```

No Windows, use `py -3` no lugar de `python3`. O comando cria um novo Markdown em
`reviews/local/`, pasta ignorada pelo Git. Para escolher outro destino:

```sh
python3 tools/review_app.py --repo ../meu-app --output /caminho/local/revisao.md
```

Um arquivo existente é recusado, preservando as decisões já preenchidas. O
destino explícito fica sob responsabilidade de quem executa. Relatórios podem
revelar nomes de projetos e caminhos internos; publique somente uma versão
revisada para esse fim. Nenhum relatório é enviado automaticamente.

O comando usa arquivos rastreados do checkout presente. Lista textos/avisos de
licença, declarações SPDX em cabeçalhos, manifests/lockfiles e FUNDING próprio.
Não consulta GitHub ou provedores, não resolve dependências, não executa o app e
não altera sua licença ou cobrança. Cabeçalhos têm leitura limitada a 32 KiB e
80 linhas, com extensões reconhecidas; binários, licenças embutidas, arquivos não
rastreados e links simbólicos exigem inspeção própria. Um commit indicado no
relatório é referência, não prova de correspondência com o APK.

## Concluir a análise

1. Leia os textos completos e confira autoria, contribuições e material adaptado.
   Um fork ou repo privado não concede direitos exclusivos sobre terceiros.
2. Confira versões resolvidas, componentes embarcados, assets e dados. Separe
   dependência de teste/build/compile-only do conteúdo realmente distribuído.
3. Registre a licença exata, inclusive `-only`/`-or-later`, e as obrigações de avisos,
   modificações e fonte correspondente ao artefato/canal analisados.
4. Distinga exibir código publicamente, conceder reuso e vender. Código próprio
   público sem licença aberta não concede o reuso amplo de open source. No GitHub,
   os termos permitem visualizar/fazer fork. Material de terceiros conserva seus termos.
5. Para GPL, cobrança é possível e os destinatários mantêm os direitos previstos.
   A forma de disponibilizar fonte deve atender à licença e à distribuição real;
   isso não exige automaticamente abrir todos os repos ou um serviço independente.
6. Conclua por finalidade: LIBERÁVEL, CONDICIONADO, NÃO LIBERAR NESSES TERMOS ou
   NÃO CONCLUSIVO. Informe evidência, pendência e alternativa concreta. A ferramenta
   sempre inicia como PENDENTE, mesmo encontrando MIT, Apache ou GPL.

O próprio `apoio-apps` ainda tem licença aberta de seu código próprio a definir.
Estes guias não concedem licença aos consumidores nem mudam termos já existentes.

## Escolher apoio e recursos pagos

Use o [modelo de monetização](monetizacao.md) para definir compra única, apoio
voluntário, licença e trial completo. Registre na ficha as decisões e os casos
validados por produto e canal.

Preencha a matriz da ficha considerando primeiro a promessa do app para novos
usuários. Para público geek, controle, operação local e experimentação podem ser
o valor principal; valide a hipótese com usuários reais. “Avançado” não basta
para classificar um recurso como premium.

Preserve recuperação, segurança, portabilidade e uma experiência gratuita útil.
Considere conveniências novas ou serviços opcionais com custo contínuo quando
houver demanda. Não retire recursos prometidos como livres sem avaliar o impacto.
Uma assinatura pede valor contínuo; uma compra única pode servir a valor local.
O piloto deve observar fricção/retorno de usuários e custos, além de pagamentos
confirmados. Não há benchmark de conversão embutido ou coleta automática de dados.

Para apoio voluntário, confira recebedor, URL e canal e siga a [integração](integracao.md).
Pagamento que libera feature, badge, tema ou serviço precisa de análise de oferta
paga e política de distribuição, mesmo chamado “café”. Regras regionais de billing
e links externos devem ser consultadas para o caso real. O catálogo atual mantém
Google Play e canais desconhecidos sem links.

## Fontes para conferir

Registre na ficha a data da consulta e os textos pertinentes ao caso.

- [GitHub: licenciar repositório](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/licensing-a-repository)
- [Ausência de licença](https://choosealicense.com/no-permission/)
- [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html) e [cobrança por software livre](https://www.gnu.org/philosophy/selling.html)
- [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0)
- [FAQ de pagamentos da Play](https://support.google.com/googleplay/android-developer/answer/10281818?hl=en)
- [Pesquisa de modelos de apoio](pesquisa-de-mercado.md)
