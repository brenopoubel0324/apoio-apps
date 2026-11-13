# Publicar uma versão

Atualizar VERSION_NAME em gradle.properties para uma nova versão semântica,
executar build/testes/consumidor do README, aplicar self-audit-loop e enviar:

```sh
git push origin main
git tag v0.1.0
git push origin v0.1.0
```

Publish testa antes de publicar e valida igualdade entre tag e VERSION_NAME.
Usa packages:write e contents:write; token não é gravado em arquivo. Actions estão
fixadas por SHA. Tags devem apontar para código revisado; não mover tags distribuídas.

Se Actions estiver bloqueado por cobrança/permissão, build local e consumo por
checkout Git continuam disponíveis. Isso não confirma publicação remota: conferir
a execução e os Packages. Falha após publicar apenas um módulo pode deixar versão
parcial. Não presumir atomicidade core/android nem repetir versão cegamente;
inspecionar e preparar nova versão completa quando necessário. Não excluir pacotes
automaticamente para contornar esse caso.

Mesmo pacotes públicos no GitHub Packages precisam de autenticação para consumo.
Consumo externo pode precisar de PAT classic com read:packages e
acesso ao repo/pacote, ou concessão explícita ao token do consumidor, conforme o
registro. [Documentação](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry).
