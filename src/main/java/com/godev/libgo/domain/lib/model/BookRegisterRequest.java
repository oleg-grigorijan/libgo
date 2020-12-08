package com.godev.libgo.domain.lib.model;

import com.godev.libgo.domain.commons.model.Language;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookRegisterRequest implements Serializable {

    private String title;
    private String originalTitle;
    private Language language;
    private List<UUID> authorIds;
    private String description;
}
