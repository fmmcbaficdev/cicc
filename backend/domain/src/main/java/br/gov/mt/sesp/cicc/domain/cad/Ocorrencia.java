package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.time.Instant;
import java.util.Objects;

public class Ocorrencia {

    private final OcorrenciaId ocorrenciaId;
    private final Protocolo protocolo;
    private final String descricao;
    private Gravidade gravidade;
    private final String endereco;
    private final Ponto ponto;
    private final Instant inicioAtendimento;
    private final Telefone telefone;
    private final String pabxUid;
    private MesaRegiao mesa;
    private Instant encaminhadaEm;
    private String prefixoEmpenhado;
    private Instant inicioDeslocamento;
    private Instant noLocalEm;
    private boolean noLocalManual;

    private Ocorrencia(
            final OcorrenciaId ocorrenciaId,
            final Protocolo protocolo,
            final String descricao,
            final Gravidade gravidade,
            final String endereco,
            final Ponto ponto,
            final Instant inicioAtendimento,
            final Telefone telefone,
            final String pabxUid,
            final MesaRegiao mesa,
            final Instant encaminhadaEm,
            final String prefixoEmpenhado,
            final Instant inicioDeslocamento,
            final Instant noLocalEm,
            final boolean noLocalManual
    ) {
        if (ocorrenciaId == null) {
            throw new ValidationException("Identificador da ocorrência inválido");
        }
        if (protocolo == null) {
            throw new ValidationException("Protocolo inválido");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new ValidationException("Descrição da ocorrência é obrigatória");
        }
        if (gravidade == null) {
            throw new ValidationException("Gravidade inválida");
        }
        if (endereco == null || endereco.isBlank()) {
            throw new ValidationException("Endereço da ocorrência é obrigatório");
        }
        if (ponto == null) {
            throw new ValidationException("Ponto da ocorrência é obrigatório");
        }
        if (inicioAtendimento == null) {
            throw new ValidationException("Início do atendimento (T1) é obrigatório");
        }
        this.ocorrenciaId = ocorrenciaId;
        this.protocolo = protocolo;
        this.descricao = descricao.trim();
        this.gravidade = gravidade;
        this.endereco = endereco.trim();
        this.ponto = ponto;
        this.inicioAtendimento = inicioAtendimento;
        this.telefone = telefone;
        this.pabxUid = pabxUid == null || pabxUid.isBlank() ? null : pabxUid.trim();
        this.mesa = mesa;
        this.encaminhadaEm = encaminhadaEm;
        this.prefixoEmpenhado = prefixoEmpenhado == null || prefixoEmpenhado.isBlank() ? null : prefixoEmpenhado.trim();
        this.inicioDeslocamento = inicioDeslocamento;
        this.noLocalEm = noLocalEm;
        this.noLocalManual = noLocalManual;
    }

    public static Ocorrencia newOcorrencia(
            final String descricao,
            final String gravidade,
            final String endereco,
            final double latitude,
            final double longitude,
            final String protocolo,
            final Instant inicioAtendimento
    ) {
        return newOcorrencia(descricao, gravidade, endereco, latitude, longitude, protocolo, inicioAtendimento, null, null);
    }

    public static Ocorrencia newOcorrencia(
            final String descricao,
            final String gravidade,
            final String endereco,
            final double latitude,
            final double longitude,
            final String protocolo,
            final Instant inicioAtendimento,
            final Telefone telefone,
            final String pabxUid
    ) {
        return new Ocorrencia(
                OcorrenciaId.unique(),
                new Protocolo(protocolo),
                descricao,
                new Gravidade(gravidade),
                endereco,
                new Ponto(latitude, longitude),
                inicioAtendimento,
                telefone,
                pabxUid,
                null,
                null,
                null,
                null,
                null,
                false
        );
    }

    public static Ocorrencia restore(
            final OcorrenciaId ocorrenciaId,
            final Protocolo protocolo,
            final String descricao,
            final Gravidade gravidade,
            final String endereco,
            final Ponto ponto,
            final Instant inicioAtendimento
    ) {
        return restore(ocorrenciaId, protocolo, descricao, gravidade, endereco, ponto, inicioAtendimento, null, null);
    }

    public static Ocorrencia restore(
            final OcorrenciaId ocorrenciaId,
            final Protocolo protocolo,
            final String descricao,
            final Gravidade gravidade,
            final String endereco,
            final Ponto ponto,
            final Instant inicioAtendimento,
            final Telefone telefone,
            final String pabxUid
    ) {
        return restore(ocorrenciaId, protocolo, descricao, gravidade, endereco, ponto, inicioAtendimento, telefone, pabxUid, null, null, null, null);
    }

    public static Ocorrencia restore(
            final OcorrenciaId ocorrenciaId,
            final Protocolo protocolo,
            final String descricao,
            final Gravidade gravidade,
            final String endereco,
            final Ponto ponto,
            final Instant inicioAtendimento,
            final Telefone telefone,
            final String pabxUid,
            final MesaRegiao mesa,
            final Instant encaminhadaEm
    ) {
        return restore(ocorrenciaId, protocolo, descricao, gravidade, endereco, ponto, inicioAtendimento, telefone, pabxUid, mesa, encaminhadaEm, null, null, null, false);
    }

    public static Ocorrencia restore(
            final OcorrenciaId ocorrenciaId,
            final Protocolo protocolo,
            final String descricao,
            final Gravidade gravidade,
            final String endereco,
            final Ponto ponto,
            final Instant inicioAtendimento,
            final Telefone telefone,
            final String pabxUid,
            final MesaRegiao mesa,
            final Instant encaminhadaEm,
            final String prefixoEmpenhado,
            final Instant inicioDeslocamento
    ) {
        return restore(
                ocorrenciaId,
                protocolo,
                descricao,
                gravidade,
                endereco,
                ponto,
                inicioAtendimento,
                telefone,
                pabxUid,
                mesa,
                encaminhadaEm,
                prefixoEmpenhado,
                inicioDeslocamento,
                null,
                false
        );
    }

    public static Ocorrencia restore(
            final OcorrenciaId ocorrenciaId,
            final Protocolo protocolo,
            final String descricao,
            final Gravidade gravidade,
            final String endereco,
            final Ponto ponto,
            final Instant inicioAtendimento,
            final Telefone telefone,
            final String pabxUid,
            final MesaRegiao mesa,
            final Instant encaminhadaEm,
            final String prefixoEmpenhado,
            final Instant inicioDeslocamento,
            final Instant noLocalEm,
            final boolean noLocalManual
    ) {
        return new Ocorrencia(
                ocorrenciaId,
                protocolo,
                descricao,
                gravidade,
                endereco,
                ponto,
                inicioAtendimento,
                telefone,
                pabxUid,
                mesa,
                encaminhadaEm,
                prefixoEmpenhado,
                inicioDeslocamento,
                noLocalEm,
                noLocalManual
        );
    }

    public void classificar(final String gravidade) {
        this.gravidade = new Gravidade(gravidade);
    }

    public void encaminharAMesa(final String mesa, final Instant quando) {
        if (encaminhadaEm != null) {
            throw new ValidationException("Ocorrência já está na mesa do despachador");
        }
        if (quando == null) {
            throw new ValidationException("Instante do encaminhamento é obrigatório");
        }
        this.mesa = new MesaRegiao(mesa);
        this.encaminhadaEm = quando;
    }

    public void empenhar(final String prefixo, final Instant quando) {
        if (encaminhadaEm == null) {
            throw new ValidationException("Ocorrência ainda não está na mesa do despachador");
        }
        if (inicioDeslocamento != null) {
            throw new ValidationException("Ocorrência já tem viatura empenhada");
        }
        if (prefixo == null || prefixo.isBlank()) {
            throw new ValidationException("Prefixo da viatura é obrigatório");
        }
        if (quando == null) {
            throw new ValidationException("Início do deslocamento (T2) é obrigatório");
        }
        this.prefixoEmpenhado = prefixo.trim();
        this.inicioDeslocamento = quando;
    }

    public void registrarNoLocal(final Instant quando, final boolean manual) {
        if (inicioDeslocamento == null) {
            throw new ValidationException("Ocorrência ainda não tem viatura empenhada");
        }
        if (noLocalEm != null) {
            throw new ValidationException("Ocorrência já está no local");
        }
        if (quando == null) {
            throw new ValidationException("Instante do No local é obrigatório");
        }
        if (quando.isBefore(inicioDeslocamento)) {
            throw new ValidationException("No local não pode ser anterior ao início do T2");
        }
        this.noLocalEm = quando;
        this.noLocalManual = manual;
    }

    public String situacao() {
        if (noLocalEm != null) {
            return "NO_LOCAL";
        }
        if (inicioDeslocamento != null) {
            return "EMPENHADA";
        }
        return encaminhadaEm == null ? "EM_TRIAGEM" : "NA_MESA";
    }

    public MesaRegiao mesa() {
        return mesa;
    }

    public Instant encaminhadaEm() {
        return encaminhadaEm;
    }

    public String prefixoEmpenhado() {
        return prefixoEmpenhado;
    }

    public Instant inicioDeslocamento() {
        return inicioDeslocamento;
    }

    public Instant noLocalEm() {
        return noLocalEm;
    }

    public boolean noLocalManual() {
        return noLocalManual;
    }

    public OcorrenciaId ocorrenciaId() {
        return ocorrenciaId;
    }

    public Protocolo protocolo() {
        return protocolo;
    }

    public String descricao() {
        return descricao;
    }

    public Gravidade gravidade() {
        return gravidade;
    }

    public String endereco() {
        return endereco;
    }

    public Ponto ponto() {
        return ponto;
    }

    public Instant inicioAtendimento() {
        return inicioAtendimento;
    }

    public Telefone telefone() {
        return telefone;
    }

    public String pabxUid() {
        return pabxUid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Ocorrencia that = (Ocorrencia) o;
        return Objects.equals(ocorrenciaId, that.ocorrenciaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ocorrenciaId);
    }
}
