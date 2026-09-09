# ADR 0002 — CAD próprio do atendente; o que se integra é o PABX

- Status: aceita
- Data: 2026-09-07
- Decisores: PO
- Corrige premissa do [ADR 0001](0001-monolito-modular-camadas.md) (“não cria CAD”). O **estilo** (monólito modular) permanece.

## Contexto

Houve um mal-entendido: “somente o CAD de Cuiabá, Várzea Grande e Rondonópolis” foi lido como *reusar o sistema de ocorrência atual*. O PO esclareceu:

- O piloto **implementa um CAD** para o atendimento do atendente (abrir chamado, gravidade, endereço, ciclo, T1).
- O que se **integra** a esse CAD é o **PABX** (ligação 190/193 chega e vincula à ocorrência).
- As três cidades continuam o **onde o piloto roda**, não o “usar o software velho como dono da ocorrência”.

*Ilities:* T1 começa no atendimento; sem ocorrência nossa não há T1. PABX é legado crítico, mas a sala precisa registrar mesmo se a correlação automática falhar (digitação manual).

## Opções consideradas

1. **Só integrar o CAD legado (Intelbras/SIOSP-GEO)** — o que o ADR 0001 assumiu
   - Prós: menos software nosso; operação já conhece a tela.
   - Contras: o PO quer o CAD do atendente *nosso*; PABX continua isolado; T1 depende de um sistema que não controlamos.

2. **CAD nosso + integrar o PABX nele**
   - Prós: ocorrência e relógio T1 no mesmo artefato; PABX vira adaptador (uid, telefone, tronco, unidade); atendente trabalha numa tela só.
   - Contras: construímos o núcleo que antes seria “de graça”; persistência nossa deixa de ser adiada; migração/convívio com o CAD velho nas 3 cidades precisa de plano operacional.

3. **CAD nosso sem PABX no MVP**
   - Prós: fatia menor.
   - Contras: o PO nomeou o PABX como *a* integração; T1 “desde o 190” fica cego do instante da ligação.

## Decisão

**Opção 2.** O monólito **é** o CAD de atendimento (módulo `cad`). O PABX entra por porta (`pabx`), com timeout: se cair, o atendente abre o chamado à mão e T1 começa no clique — não na correlação.

Trade-off aceito: **pagamos o custo de um CAD novo** em troca de dono da ocorrência e do relógio. O CAD antigo, se continuar no prédio, não é SoT deste produto.

AVL, LPR, rádio e CIOSP Móvel continuam satélites (ADR 0001). O estilo não muda.

## Consequências

- Positivas: T1 instrumentável de ponta a ponta; PABX deixa de ser “release 2”; mapa de componentes alinha com o que o PO descreveu.
- Negativas / dívida: precisamos de persistência nossa (ADR de banco em aberto); plano de corte/convívio com o CAD atual nas 3 cidades; volume de regra de ocorrência passa a ser nosso.
- *Ilities* afetadas: 1 e 3 (integração = PABX + LPR + AVL, não “CAD legado”); 5 (sala registra sem PABX).
- Specs: `visao-geral.md`, `jornada.md`, `levantamento-funcional.md`, `quality-attributes.md`, `component-map.md`, `risk-register.md`.
- Contrato da ligação ganhou lat/long do celular em [ADR 0005](0005-ponto-celular-pabx-geocode-mock.md) (não substitui este ADR).
