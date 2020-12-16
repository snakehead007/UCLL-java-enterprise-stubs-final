package be.ucll.java.ent.domain;

public class InschrijvingDTO {

    private String code;
    private String beschrijving;
    private boolean betaald;

    public InschrijvingDTO(String code, String beschrijving, boolean betaald) {
        this.code = code;
        this.beschrijving = beschrijving;
        this.betaald = betaald;
    }

    public String getCode() {
        return code;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public boolean isBetaald() {
        return betaald;
    }

    @Override
    public String toString() {
        return code + " - " + beschrijving + " (" + (betaald ? "Betaling OK" : "NOG NIET BETAALD") + ")";
    }
}
