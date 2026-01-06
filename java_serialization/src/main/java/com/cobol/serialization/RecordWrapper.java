package com.cobol.serialization;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class to match COBOL JSON output structure.
 * 
 * COBOL JSON GENERATE produces output wrapped in the record name:
 * {"ws-record":{"name":"...","value":"...","ws-record-blank":"...","enabled":"..."}}
 * 
 * This wrapper class provides that structure for JSON serialization.
 */
public class RecordWrapper {

    @JsonProperty("ws-record")
    private Record record;

    public RecordWrapper() {
    }

    public RecordWrapper(Record record) {
        this.record = record;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
