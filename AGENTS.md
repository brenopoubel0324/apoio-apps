# Trabalhar neste repositório

Leia README, docs/integracao.md e docs/releases.md antes de editar.

- Nunca inferir pagamento de retorno do navegador, clique ou parâmetro de URL.
- Manter canais desconhecidos e Google Play sem links até revisão específica.
- Não inserir conta real ou credenciais nos exemplos.
- Preservar Java 8 na API/bytecode de produção. Dependências Android são compile-only.
- Não alterar automaticamente os repositórios consumidores.

Validação:

```sh
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
