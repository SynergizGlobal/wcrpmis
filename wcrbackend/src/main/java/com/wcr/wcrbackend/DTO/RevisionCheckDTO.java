package com.wcr.wcrbackend.DTO;
public class RevisionCheckDTO {
    private String designIdFk;
    private String revision;

    public RevisionCheckDTO(String designIdFk, String revision) {
        this.designIdFk = designIdFk;
        this.revision = revision;
    }

    public String getDesignIdFk() { return designIdFk; }
    public String getRevision() { return revision; }
}
