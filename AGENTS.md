# Trabalhar neste repositório

Leia README, docs/integracao.md e docs/releases.md antes de editar.

Ao revisar um consumidor para distribuição, apoio ou monetização, seguir
docs/revisao-apps.md. Usar tools/review_app.py para iniciar a ficha local e
concluir manualmente as evidências; encontrar uma licença não aprova a entrega.
Manter relatórios privados em reviews/local/ e fora dos commits públicos.

- Nunca inferir pagamento de retorno do navegador, clique ou parâmetro de URL.
- Manter canais desconhecidos e Google Play sem links até revisão específica.
- Não inserir conta real ou credenciais nos exemplos.
- Preservar Java 8 na API/bytecode de produção. Dependências Android são compile-only.
- Não alterar automaticamente os repositórios consumidores.

Validação:

```sh
python3 -m unittest discover -s tools/tests -v
./gradlew check assemble :core:sourcesJar :android:sourcesJar publishAllPublicationsToLocalTestRepository
./gradlew -p examples/consumer run
git diff --check
```

Após implementar, aplicar `breno-review-audit:self-audit-loop` quando disponível,
registrando achados antes das correções e validando novamente. Não substituir
essa revisão apenas pela contagem de testes. Se indisponível, informar o limite.

Tags precisam corresponder a VERSION_NAME em gradle.properties e passar em
tools/release.py. Não reutilizar versão já publicada. Credenciais de consumo ficam
nas configurações locais/Actions; publicação usa GitHub Packages.
