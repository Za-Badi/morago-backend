package com.habsida.morago.model.inputs;

import com.habsida.morago.model.entity.Language;
import com.habsida.morago.model.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslatorProfileInput {
    private String dateOfBirth;
    private String email;
    private Boolean isAvailable;
    private Boolean isOnline;
    private String levelOfKorean;
    private List<Long> languages;
}
