# Modelo de monetização por app

Use a mesma estrutura de oferta, as mesmas garantias de recuperação e a mesma
ficha de validação em cada app. Defina preço, recursos Pro, duração do trial e
canais por produto. O acesso é concedido por app. Uma oferta que inclua vários
apps exige uma decisão própria.

## Gratuito, Pro e apoio

- **Gratuito.** Entrega a função central do app. Mantém segurança, reversão de
  mudanças, recuperação e acesso aos dados que o usuário já criou.
- **Pro.** Compra única para liberar o mesmo conjunto de recursos Pro a todos os
  compradores. Cada app documenta os benefícios concretos e o preço antes da compra.
- **Apoio voluntário.** Contribuição opcional e repetível, inclusive durante o trial
  e depois da compra. Pode oferecer valores sugeridos. Todos representam a mesma
  forma de apoio, sem níveis de acesso ou benefícios digitais. Confira as regras e
  o recebedor de cada canal na [integração](integracao.md).

Use ações distintas para **Experimentar Pro**, **Comprar Pro**, **Restaurar compra**
e **Apoiar o projeto**. Identifique o preço e quando ocorre a cobrança. Uma
compra pendente continua pendente até a confirmação pelo canal. Abrir ou fechar o
checkout só informa que o usuário navegou.

## A quem pertence a licença

O padrão recomendado para o piloto é a compra restaurável. A Play usa a conta
que comprou. No APK direto, um comprovante verificável ou código de recuperação
pode dar direito à restauração sem exigir cadastro no app.

| Modelo | O que acontece ao usar outro aparelho | Custo para usuário e projeto |
| --- | --- | --- |
| Compra restaurável | O comprador restaura no próprio celular e tablet. | Exige uma forma de recuperar o acesso e lidar com compartilhamento indevido. |
| Número limitado de aparelhos ativos | O comprador libera uma vaga e transfere a ativação. | Exige troca sem o aparelho antigo, regras de uso offline e atendimento para exceções. |
| Licença vendida por aparelho | Uma instalação em outro aparelho pode exigir nova compra, conforme a oferta. | Aumenta a fricção de troca e exige um fluxo de compra independente por licença. |

Se um produto precisar de limite, registre a justificativa, o número de vagas,
a troca após perda ou defeito e o efeito de reinstalação ou mudança de ROM. Mostre
essas condições antes da compra. Use somente os identificadores necessários,
restritos ao app. Evite IMEI, número de série e impressão digital do aparelho.
[Identificadores recomendados pelo Android](https://developer.android.com/identity/user-data-ids)

O Galaxy Max Hz informa na loja que a licença premium é por aparelho. Esse modelo
precisa ser avaliado junto ao tipo de compra. Um produto não consumível da Play é
comprado uma vez por conta e dá direito permanente. Uma nova cobrança por aparelho
exige outro desenho comercial e validação do catálogo e das regras da loja.
[Galaxy Max Hz](https://play.google.com/store/apps/details?id=com.tribalfs.gmh_donate) ·
[Produtos de compra única na Play](https://developer.android.com/google/play/billing/one-time-products?hl=en) ·
[Catálogo da Play](https://developer.android.com/google/play/billing/manage-catalog?hl=en)

## Trial completo

1. Ofereça a ativação voluntária com prazo visível, sem cartão, renovação ou
   cobrança automática. Comece a contar ao confirmar **Experimentar Pro**. Se o
   usuário ainda precisar concluir uma configuração para testar a oferta,
   mostre essa etapa antes da ativação. Sete dias é uma hipótese inicial para
   o piloto. Registre a duração por produto.
2. Libere todos os recursos da oferta Pro, respeitando os mesmos requisitos de
   hardware, ROM e consentimento da compra. Exiba a data de término e permita
   comprar durante o teste. A liberação pela compra vale desde a confirmação
   e continua após o fim do trial. Um checkout cancelado ou pendente conserva
   o trial vigente.
3. Ao vencer, volte ao plano gratuito e preserve dados, perfis e as ações de
   segurança. Se uma automação Pro modificou o sistema, encerre-a pelo fluxo
   de restauração do app e mantenha disponível a recuperação de uma falha.
4. Defina a elegibilidade e o que acontece offline, após reinstalação, perda do
   aparelho e mudança de relógio. Habilitar um trial por instalação pode permitir
   repetições. Qualquer promessa de elegibilidade por comprador requer uma
   maneira de recuperar essa identidade. Use o mínimo de dados necessário.

## Compra e restauração por canal

- **APK direto.** Confirme o pagamento no provedor e só então emita o direito de
  uso. Defina como o comprador restaura ao reinstalar ou trocar de aparelho e
  como recupera um código perdido com comprovante válido. Trate confirmação
  repetida e reembolso confirmado.
- **Google Play.** Para uma compra única restaurável, valide a compra e sua
  titularidade e trate o reconhecimento e a restauração conforme as APIs da
  Play. Se houver companion, valide também a entrega e a restauração do direito
  no app principal. O trial completo é um acesso temporário do produto e pode
  funcionar independentemente do checkout. Confira apoio voluntário por canal e
  região antes de habilitar. [Integração da Play Billing](https://developer.android.com/google/play/billing/integrate) ·
  [FAQ de pagamentos](https://support.google.com/googleplay/android-developer/answer/10281818?hl=en)

Em cada canal, preserve o acesso de uma compra já confirmada segundo a política
offline definida. Registre como o app distingue uma consulta indisponível de um
reembolso confirmado e como renova sua evidência de titularidade.

## Validar antes de cobrar

Preencha a ficha do app e teste a variante que o usuário receberá em ambiente de
teste do canal. Registre versão, resultado e evidência para os casos pertinentes.

| Fluxo | Critério de aceite |
| --- | --- |
| Gratuito e apoio | O app entrega sua função central e permite restaurar mudanças. Abrir, cancelar ou repetir o apoio deixa o acesso aos recursos igual. |
| Trial e vencimento | Todos os recursos Pro disponíveis no aparelho funcionam no trial. O vencimento preserva dados e recuperação após reabrir, ficar offline ou alterar o relógio. |
| Compra | Pendente, cancelada, confirmada e reembolso têm efeitos distintos. Confirmação duplicada não concede uma segunda licença. Uma compra confirmada continua vigente quando o trial expira. |
| Restauração | Reinstalação, troca, perda do aparelho e perda do código seguem a promessa comercial. Uma compra ou credencial de outro produto não libera este app. |

O `apoio-apps` mantém este guia, a ficha e a biblioteca de links de apoio. A
validação de compras, o trial e o direito de acesso pertencem ao app e à sua
integração comercial. Use o piloto para validar a oferta em um app e confirme
quais contratos se repetem em um segundo antes de extrair código comum.
