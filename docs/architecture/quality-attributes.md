# Características arquiteturais

Preencha no máximo **3–5** itens em prioridade alta. Deixe o restante em “bom o suficiente” ou fora de escopo.

Produto: CIOSP/CICC-MT — **CAD nosso** de atendimento, piloto em Cuiabá, Várzea Grande e Rondonópolis. Fonte: `docs/product/`. ADR 0002.

| Prioridade | Característica | Métrica / critério | Por quê (driver de negócio) | Trade-off aceito |
|---|---|---|---|---|
| 1 | Tempo de resposta da **sala (T1)** | Carimbos: início do atendimento (190/193) → visível ao despachador → despacho. Exibir T1; meta em minutos só depois da série do piloto | Cidadão e triagem: o PO exigiu o relógio desde o 190, não só o campo | Não otimizar matching “Uber” (Hungarian/OSRM) antes de T1 ser medido de verdade |
| 2 | Tempo de resposta de **campo (T2)** | Carimbos: despacho → status “no local” (AVL/tablet ou fallback voz). Exibir T2 à parte de T1 | Gargalo de logística e precisão do AVL; um relógio só esconde se a demora é na sala ou na rua | Viatura sem AVL fica fora da sugestão automática; T2 por voz é válido mas marcado como manual |
| 3 | Integração com legado | **PABX** alimenta o CAD nosso (uid, telefone, tronco, lat/long do celular). AVL e **LPR** no MVP. Ocorrência mora no nosso CAD | PO: criar o CAD do atendente; integrar o PABX a ele | Facial/CPF federado fora. PABX ou LPR atrasado: atendente abre o chamado à mão. Geocode é porta mock, não vendor |
| 4 | Rastreabilidade / LGPD | Toda mudança de T1/T2 e todo alerta LPR registra quem viu/agiu, quando, em qual CAD | Placa e localização de viatura/cidadão são dado pessoal; emergência não dispensa trilha | Sem Event Sourcing, hash chain ou WORM no piloto — log append-only basta |
| 5 | Disponibilidade da operação | Sala nas 3 cidades registra e despacha se PABX, LPR ou móvel cair | Emergência: o CAD nosso não pode depender da correlação automática | Sem 99,99%/K8s. Fallback: digitação manual + rádio |

## Implícitas levantadas

- Isolamento por unidade/força no que cada CAD já isola (sigilo entre órgãos).
- Rádio criptografado estadual: não substituir; o produto acrescenta o canal de dados (CIOSP Móvel).

## Fora de escopo (por enquanto)

- 99,99% / RPO 0 / Event Sourcing / Kafka-por-hype / microsserviço por BC
- Reconhecimento facial, dossiê CPF/RG federado
- Expansão além de CBA / VG / RDO; reusar o CAD legado como SoT
- Predição, heatmap H3, speech-to-text

## Histórico

| Data | Mudança | ADR |
|---|---|---|
| 2026-09-07 | Spec criada (template) | |
| 2026-09-07 | 5 *ilities* a partir das respostas do PO (T1+T2, 2-B LPR, 3 CAD) | |
| 2026-09-07 | Estilo: monólito modular em camadas | [ADR 0001](adr/0001-monolito-modular-camadas.md) |
| 2026-09-07 | CAD próprio; integração = PABX | [ADR 0002](adr/0002-cad-proprio-pabx.md) |
| 2026-09-07 | Persistência: Oracle + Oracle Spatial (não PostGIS) | [ADR 0003](adr/0003-oracle-persistencia-cad.md) |
| 2026-09-07 | Construção: Maven + Spring Boot + Angular (hexágono compilado) | [ADR 0004](adr/0004-maven-spring-angular-hexagonal.md) |
| 2026-09-08 | PABX entrega ponto do celular; geocode por porta mock | [ADR 0005](adr/0005-ponto-celular-pabx-geocode-mock.md) |
| 2026-09-07 | Versões: Java 26, Boot 4.1.x, Angular 21 | [contexto-tecnico.md](contexto-tecnico.md) |
