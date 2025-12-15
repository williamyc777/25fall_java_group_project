package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.domain.DefinedFormEntry;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefinedFormDto {
    private long id;
    private String name;
    private String type;
    private boolean required;
    private List<String> options;

    public DefinedFormDto(DefinedFormEntry definedFormEntry) {
        this.id = definedFormEntry.getEntryId();
        this.name = definedFormEntry.getName();
        this.type = definedFormEntry.getType();
        this.required = definedFormEntry.isRequired();
        // 立即拷贝 options，避免在序列化时触发懒加载异常
        List<String> srcOptions = definedFormEntry.getOptions();
        this.options = (srcOptions != null) ? new ArrayList<>(srcOptions) : null;
    }

    public DefinedFormEntry toDefinedFormEntry() {
        DefinedFormEntry definedFormEntry = new DefinedFormEntry();
        definedFormEntry.setEntryId(this.id);
        definedFormEntry.setName(this.name);
        definedFormEntry.setType(this.type);
        definedFormEntry.setRequired(this.required);
        definedFormEntry.setOptions(this.options);
        return definedFormEntry;
    }

}
