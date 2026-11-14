# Apoio Apps

Biblioteca compartilhada de apoio voluntário para os apps Android/JVM de Breno
Poubel. O núcleo mantém destinos e regras; cada app mantém sua interface.
Pagamentos ocorrem no checkout do provedor. Não há conta recebedora ativa,
processamento financeiro ou confirmação de pagamento na biblioteca.

## Revisar nossos apps

Este repositório também reúne a revisão de licença, distribuição e monetização
por app. A partir deste checkout, gere uma ficha local:

```sh
python3 tools/review_app.py --repo ../meu-app
```

A coleta identifica evidências de licença, manifests e configuração de apoio.
A ficha inclui análise de autoria/obrigações, conferência do recebedor/canal e
matriz de features gratuitas, premium ou serviços pagos, considerando a adoção
do público geek. A conclusão jurídica/comercial exige revisão das evidências.

Relatórios ficam em `reviews/local/`, ignorados pelo Git. Nenhum relatório privado
é publicado automaticamente. Veja o [fluxo de revisão](docs/revisao-apps.md) e a
[ficha](reviews/modelo-app.md). A ferramenta é usada pelo desenvolvedor no checkout;
os JARs `v0.1.0` contêm o catálogo e o launcher. A tag `v0.1.0` é anterior a esta
ferramenta; os comandos de instalação abaixo fixam a versão da biblioteca.

[Autoauditoria da ferramenta](docs/auditoria-revisao-apps.md).

## Instalar pelo checkout Git

Clone este repo ao lado do app e fixe uma tag/revisão validada:

```sh
git clone https://github.com/brenopoubel0324/apoio-apps.git
cd apoio-apps
git checkout v0.1.0
```

Em settings.gradle.kts do app, com os diretórios lado a lado:

```kotlin
includeBuild("../apoio-apps")
```

Em app/build.gradle.kts:

```kotlin
dependencies {
    implementation("dev.brenopoubel.apoio:android:0.1.0")
}
```

Gradle substitui a dependência pelos projetos do checkout incluído. android inclui
core transitivamente. Para JVM sem Android, use o artefato core. O clone público
dispensa autenticação. Esse caminho independe da publicação de pacotes.

## Instalar pelo GitHub Packages

Após publicação da versão, a mesma dependência pode vir do registro Maven.
Adicione em dependencyResolutionManagement.repositories de settings.gradle.kts:

```kotlin
maven {
    url = uri("https://maven.pkg.github.com/brenopoubel0324/apoio-apps")
    credentials {
        username = providers.gradleProperty("gpr.user").orNull
        password = providers.gradleProperty("gpr.key").orNull
    }
    content { includeGroup("dev.brenopoubel.apoio") }
}
```

Configure gpr.user/gpr.key no arquivo **local** ~/.gradle/gradle.properties.
GitHub orienta PAT classic com read:packages para consumo externo. Nunca versione
o token. O workflow deste repo publica com seu próprio GITHUB_TOKEN.
[Autenticação oficial](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry).

## Configurar o apoio

```kotlin
import dev.brenopoubel.apoio.Support
import dev.brenopoubel.apoio.android.SupportLinks

val catalog = Support.Catalog("meu-app", listOf(Support.Method(
    "coffee", "Apoiar o desenvolvimento", "CONFERIR RECEBEDOR",
    Support.Provider.KO_FI, "https://ko-fi.com/EXAMPLE", null,
    false, // habilitar somente após configurar destino real
    false  // declarar somente após conferir titular e checkout
)))
val channel = Support.Channel.GITHUB_APK // definido pela variante de build
val methods = catalog.available(channel)
// Lista vazia: esconder a entrada. No clique de uma opção visível:
val result = SupportLinks.open(context, catalog, channel, "coffee")
```

Não ativar o exemplo como está. recipientReviewed é declaração de quem configura,
sem verificação automática de titularidade. LAUNCHED significa abertura aceita
pelo Android, não pagamento confirmado. Tratar UNAVAILABLE, NO_HANDLER e BLOCKED.

Apps que atuam como roteadores de links devem passar o pacote do navegador externo
escolhido como quinto argumento de open. Informar o próprio pacote é bloqueado.
A opção sem pacote usa o resolvedor padrão e não deve ser usada pelo app que
ocupa o papel de navegador padrão.

## Estrutura

| Diretório | Entrega |
|---|---|
| core | Catálogo/regras Java 8, sem Android ou rede |
| android | Launcher para celular, JAR sem manifest/recursos |
| wear | Requisitos documentados; sem adaptador/pacote publicado |
| examples/consumer | Consumidor independente da publicação Maven local |
| tools/review_app.py | Coleta local para revisão de licença e monetização por app |
| reviews | Ficha reutilizável; relatórios locais ignorados pelo Git |
| docs | Pesquisa pública, integração, publicação e auditoria |

Links apenas em GITHUB_APK e WEB. GOOGLE_PLAY, UNKNOWN e canal nulo continuam
desativados. Não há SDK JavaScript nem telas Compose/Views. Wear requer fluxo ao
telefone. A licença de código aberto ainda será definida pelo titular; a visibilidade pública
não concede automaticamente uma licença de redistribuição a terceiros.

## Desenvolver

JDK 21, Python 3 e Maven Central. Runtime Android API 19+. Não exige Android SDK/AGP: usamos assinaturas
antigas da API para compilar o launcher; elas não entram na distribuição. Testes
Android rodam na JVM com mocks, sem validar a experiência em aparelho/emulador.

```sh
python3 -m unittest discover -s tools/tests -v
./gradlew check assemble :core:sourcesJar :android:sourcesJar publishAllPublicationsToLocalTestRepository
./gradlew -p examples/consumer run
./gradlew -p examples/consumer -PuseCheckout=true run
python3 tools/release.py v0.1.0
git diff --check
```

No Windows, use gradlew.bat. Wrapper fixa Gradle 9.1.0 e verifica SHA da distribuição.
core:test executa a suíte standalone; android:test usa JUnit. Artefatos ficam em
core/build/libs e android/build/libs.

## Versões

VERSION_NAME em gradle.properties define a versão. CI testa commits/PRs. Publish
valida tag vX.Y.Z, testa pacotes/consumidor, publica no GitHub Packages e cria uma
Release com JARs/checksums. Não reutilizar versões publicadas. O caminho Git
permanece disponível se Actions/Packages forem bloqueados pela conta.

[Publicação](docs/releases.md) · [Pesquisa](docs/pesquisa-de-mercado.md) ·
[Integração](docs/integracao.md) · [Autoauditoria](docs/auditoria-publicacao.md)
