package com.syngenta.portal.data.model;

import java.io.File;

public class FileParseResults {
    private File script;
    private File flatObjectScript;
    private File errorFile;
    private boolean parsingSucceed =false;
    private boolean emailSentSuccessfully=false;
    private boolean scriptApplied=false;

    public boolean isEmailSentSuccessfully() {
        return emailSentSuccessfully;
    }

    public void setEmailSentSuccessfully(boolean emailSentSuccessfully) {
        this.emailSentSuccessfully = emailSentSuccessfully;
    }

    public boolean isScriptApplied() {
        return scriptApplied;
    }

    public void setScriptApplied(boolean scriptApplied) {
        this.scriptApplied = scriptApplied;
    }

    public File getErrorFile() {
        return errorFile;
    }

    public void setErrorFile(File errorFile) {
        this.errorFile = errorFile;
    }

    public boolean isParsingSucceed() {
        return parsingSucceed;
    }

    public void setParsingSucceed(boolean parsingSucceed) {
        this.parsingSucceed = parsingSucceed;
    }


    public File getScript() {
        return script;
    }

    public void setScript(File script) {
        this.script = script;
    }

    public File getFlatObjectScript() {
        return flatObjectScript;
    }

    public void setFlatObjectScript(File flatObjectScript) {
        this.flatObjectScript = flatObjectScript;
    }

}
