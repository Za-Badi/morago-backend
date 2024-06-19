//package com.habsida.morago.serviceImpl;
//
//import com.habsida.morago.exceptions.GlobalException;
//import com.habsida.morago.model.entity.TranslatorProfile;
//import com.habsida.morago.model.inputs.LanguageInput;
//import com.habsida.morago.model.entity.Language;
//import com.habsida.morago.repository.LanguageRepository;
//import com.habsida.morago.repository.TranslatorProfileRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class LanguageService implements com.habsida.morago.service.LanguageService {
//    private final LanguageRepository languageRepository;
//    private final TranslatorProfileRepository translatorProfileRepository;
//
//    public List<Language> getAllLanguages() {
//        return languageRepository.findAll();
//    }
//
//    public Language getLanguageById(Long id) throws GlobalException {
//        return languageRepository.findById(id)
//                .orElseThrow(() -> new GlobalException("Language not found with id: " + id));
//    }
//
//    public Language addLanguage(LanguageInput languageInput) {
//        Language language = new Language();
//        language.setName(languageInput.getName());
//        return languageRepository.save(language);
//    }
//
//    public Language updateLanguage(Long id, LanguageInput languageInput) throws GlobalException {
//        Language language = languageRepository.findById(id)
//                .orElseThrow(() -> new GlobalException("Language not found with id: " + id));
//        if (languageInput.getName() != null && !languageInput.getName().trim().isEmpty()) {
//            language.setName(languageInput.getName());
//        }
//        return languageRepository.save(language);
//    }
//
//    public void deleteLanguage(Long id) throws GlobalException {
//        Language language = languageRepository.findById(id)
//                .orElseThrow(() -> new GlobalException("Language not found with id: " + id));
//        if (language.getTranslatorProfiles() != null) {
//            for (TranslatorProfile translatorProfile : language.getTranslatorProfiles()) {
//                translatorProfile.getLanguages().remove(language);
//                translatorProfileRepository.save(translatorProfile);
//            }
//            language.getTranslatorProfiles().clear();
//            languageRepository.save(language);
//        }
//        languageRepository.deleteById(id);
//    }
//}