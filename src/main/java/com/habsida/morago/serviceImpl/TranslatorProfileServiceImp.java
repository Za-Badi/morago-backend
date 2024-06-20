package com.habsida.morago.serviceImpl;

import com.habsida.morago.exceptions.ExceptionGraphql;
import com.habsida.morago.model.entity.*;
import com.habsida.morago.model.inputs.TranslatorPage;
import com.habsida.morago.model.inputs.TranslatorProfileInput;
import com.habsida.morago.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TranslatorProfileServiceImp {
    private final TranslatorProfileRepository translatorProfileRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final TranslatorProfileRepositoryPaged translatorProfileRepositoryPaged;
    @Transactional(readOnly = true)
    public List<TranslatorProfile> getAllTranslatorProfiles() {
        return translatorProfileRepository.findAll();
    }
    @Transactional(readOnly = true)
    public TranslatorPage getAllTranslatorProfilesPaged(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        Page<TranslatorProfile> translatorProfilePage = translatorProfileRepositoryPaged.findAll(PageRequest.of(page, size));
        return new TranslatorPage(
                translatorProfilePage.getContent(),
                translatorProfilePage.getTotalPages(),
                (int) translatorProfilePage.getTotalElements(),
                translatorProfilePage.getSize(),
                translatorProfilePage.getNumber()
        );
    }
    @Transactional(readOnly = true)
    public TranslatorProfile getTranslatorProfileById(Long id) throws ExceptionGraphql {
        return translatorProfileRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("TranslatorProfile not found with id: " + id));
    }
    @Transactional(readOnly = true)
    public List<TranslatorProfile> getTranslatorProfilesByIsOnlineAndThemeId(Boolean isOnline, Long id) {
        return translatorProfileRepository.findByIsOnlineAndThemesId(isOnline, id);
    }
    @Transactional(readOnly = true)
    public TranslatorPage getTranslatorProfilesByIsOnlineAndThemeIdPaged(Integer page, Integer size, Long id, Boolean isOnline) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        Page<TranslatorProfile> translatorProfilePage = translatorProfileRepositoryPaged.findByIsOnlineAndThemesId(isOnline, id ,PageRequest.of(page, size));
        return new TranslatorPage(
                translatorProfilePage.getContent(),
                translatorProfilePage.getTotalPages(),
                (int) translatorProfilePage.getTotalElements(),
                translatorProfilePage.getSize(),
                translatorProfilePage.getNumber()
        );
    }
    @Transactional(readOnly = true)
    public List<TranslatorProfile> getTranslatorProfilesByThemeId(Long id) {
        return translatorProfileRepository.findByThemesId(id);
    }
    @Transactional(readOnly = true)
    public TranslatorPage getTranslatorProfilesByThemeIdPaged(Integer page, Integer size, Long id) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        Page<TranslatorProfile> translatorProfilePage = translatorProfileRepositoryPaged.findByThemesId(id, PageRequest.of(page, size));
        return new TranslatorPage(
                translatorProfilePage.getContent(),
                translatorProfilePage.getTotalPages(),
                (int) translatorProfilePage.getTotalElements(),
                translatorProfilePage.getSize(),
                translatorProfilePage.getNumber()
        );
    }
    @Transactional(readOnly = true)
    public List<TranslatorProfile> getTranslatorProfilesByThemeName(String name)  {
        return translatorProfileRepository.findByThemesName(name);
    }
    @Transactional(readOnly = true)
    public TranslatorPage getTranslatorProfilesByThemeNamePaged(Integer page, Integer size, String name) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        Page<TranslatorProfile> translatorProfilePage = translatorProfileRepositoryPaged.findByThemesName(name, PageRequest.of(page, size));
        return new TranslatorPage(
                translatorProfilePage.getContent(),
                translatorProfilePage.getTotalPages(),
                (int) translatorProfilePage.getTotalElements(),
                translatorProfilePage.getSize(),
                translatorProfilePage.getNumber()
        );
    }
    @Transactional(readOnly = true)
    public TranslatorPage searchTranslators(String searchInput, Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        Page<TranslatorProfile> translatorProfilePage = translatorProfileRepositoryPaged.searchTranslatorProfileByEmailOrLevelOfKoreanOrDateOfBirthOrLanguages(searchInput, PageRequest.of(page, size));
        return new TranslatorPage(
                translatorProfilePage.getContent(),
                translatorProfilePage.getTotalPages(),
                (int) translatorProfilePage.getTotalElements(),
                translatorProfilePage.getSize(),
                translatorProfilePage.getNumber()
        );
    }
    @Transactional
    public TranslatorProfile addTranslatorProfile(Long id, TranslatorProfileInput translatorProfileInput) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = new TranslatorProfile();
        translatorProfile.setDateOfBirth(translatorProfileInput.getDateOfBirth());
        translatorProfile.setEmail(translatorProfileInput.getEmail());
        translatorProfile.setIsAvailable(translatorProfileInput.getIsAvailable());
        translatorProfile.setIsOnline(translatorProfileInput.getIsOnline());
        translatorProfile.setLevelOfKorean(translatorProfileInput.getLevelOfKorean());
        List<Language> languages = new ArrayList<>();
        if (translatorProfileInput.getLanguages() != null) {
            for (Long languageId : translatorProfileInput.getLanguages()) {
                Language language = languageRepository.findById(languageId)
                        .orElseThrow(() -> new ExceptionGraphql("Language not found with id: " + languageId));
                languages.add(language);
            }
        }
        translatorProfile.setLanguages(languages);
        List<Theme> themes = new ArrayList<>();
        translatorProfile.setThemes(themes);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("User not found with id: " + id));
        if (user.getTranslatorProfile() == null) {
            user.setTranslatorProfile(translatorProfile);
            userRepository.save(user);
        } else {
            throw new ExceptionGraphql("User already has a Profile attached");
        }
        return user.getTranslatorProfile();
    }
    @Transactional
    public TranslatorProfile updateTranslatorProfile(Long id, TranslatorProfileInput translatorProfileInput) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("TranslatorProfile not found with id: " + id));
        if (translatorProfileInput.getDateOfBirth() != null && !translatorProfileInput.getDateOfBirth().isBlank()) {
            translatorProfile.setDateOfBirth(translatorProfileInput.getDateOfBirth());
        }
        if (translatorProfileInput.getEmail() != null && !translatorProfileInput.getEmail().isBlank()) {
            translatorProfile.setEmail(translatorProfileInput.getEmail());
        }
        if (translatorProfileInput.getIsAvailable() != null) {
            translatorProfile.setIsAvailable(translatorProfileInput.getIsAvailable());
        }
        if (translatorProfileInput.getIsOnline() != null) {
            translatorProfile.setIsOnline(translatorProfileInput.getIsOnline());
        }
        if (translatorProfileInput.getLevelOfKorean() != null && !translatorProfileInput.getLevelOfKorean().isBlank()) {
            translatorProfile.setLevelOfKorean(translatorProfileInput.getLevelOfKorean());
        }
        if (translatorProfileInput.getLanguages() != null) {
            translatorProfile.getLanguages().clear();
            List<Language> languages = new ArrayList<>();
            for (Long languageId : translatorProfileInput.getLanguages()) {
                Language language = languageRepository.findById(languageId)
                        .orElseThrow(() -> new ExceptionGraphql("Language not found with id: " + languageId));
                languages.add(language);
            }
            translatorProfile.setLanguages(languages);
        }
        return translatorProfileRepository.save(translatorProfile);
    }
    @Transactional
    public void deleteTranslatorProfile(Long id) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("TranslatorProfile not found with id: " + id));
        if (translatorProfile.getUser() != null){
            User user = translatorProfile.getUser();
            user.setTranslatorProfile(null);
            translatorProfile.setUser(null);
            userRepository.save(user);
        }
        translatorProfile.getLanguages().clear();
        translatorProfile.getThemes().clear();
        translatorProfileRepository.save(translatorProfile);
        translatorProfileRepository.deleteById(id);
    }
    @Transactional
    public TranslatorProfile updateTranslatorProfileByUserId(Long id, TranslatorProfileInput translatorProfileInput) throws ExceptionGraphql {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("User not found with id: " + id));
        Long newId = user.getTranslatorProfile().getId();
        if (newId==null) {
            throw new ExceptionGraphql("User doesn't have Translator Profile");
        }
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(newId)
                .orElseThrow(() -> new ExceptionGraphql("TranslatorProfile not found with id: " + newId));
        if (translatorProfileInput.getDateOfBirth() != null && !translatorProfileInput.getDateOfBirth().isBlank()) {
            translatorProfile.setDateOfBirth(translatorProfileInput.getDateOfBirth());
        }
        if (translatorProfileInput.getEmail() != null && !translatorProfileInput.getEmail().isBlank()) {
            translatorProfile.setEmail(translatorProfileInput.getEmail());
        }
        if (translatorProfileInput.getIsAvailable() != null) {
            translatorProfile.setIsAvailable(translatorProfileInput.getIsAvailable());
        }
        if (translatorProfileInput.getIsOnline() != null) {
            translatorProfile.setIsOnline(translatorProfileInput.getIsOnline());
        }
        if (translatorProfileInput.getLevelOfKorean() != null && !translatorProfileInput.getLevelOfKorean().isBlank()) {
            translatorProfile.setLevelOfKorean(translatorProfileInput.getLevelOfKorean());
        }
        if (translatorProfileInput.getLanguages() != null) {
            translatorProfile.getLanguages().clear();
            List<Language> languages = new ArrayList<>();
            for (Long languageId : translatorProfileInput.getLanguages()) {
                Language language = languageRepository.findById(languageId)
                        .orElseThrow(() -> new ExceptionGraphql("Language not found with id: " + languageId));
                languages.add(language);
            }
            translatorProfile.setLanguages(languages);
        }
        return translatorProfileRepository.save(translatorProfile);
    }
    @Transactional
    public Boolean changeIsAvailable(Long id, Boolean isAvailable) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("Translator Profile not found with id: " + id));
        translatorProfile.setIsAvailable(isAvailable);
        translatorProfileRepository.save(translatorProfile);
        return true;
    }
    @Transactional
    public Boolean changeIsOnline(Long id, Boolean isOnline) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("Translator Profile not found with id: " + id));
        translatorProfile.setIsOnline(isOnline);
        translatorProfileRepository.save(translatorProfile);
        return true;
    }
    @Transactional
    public TranslatorProfile addLanguageToTranslatorProfile(Long languageId, Long translatorProfileId) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(translatorProfileId)
                .orElseThrow(() -> new ExceptionGraphql("Translator Profile not found with id: " + translatorProfileId));
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new ExceptionGraphql("Language not found with id: " + languageId));
        if(!translatorProfile.getLanguages().contains(language)) {
            translatorProfile.getLanguages().add(language);
        } else {
            throw new ExceptionGraphql("User already has this language");
        }
        translatorProfileRepository.save(translatorProfile);
        return translatorProfile;
    }
    @Transactional
    public void deleteLanguageFromTranslatorProfile(Long languageId, Long translatorProfileId) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(translatorProfileId)
                .orElseThrow(() -> new ExceptionGraphql("Translator Profile not found with id: " + translatorProfileId));
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new ExceptionGraphql("Language not found with id: " + languageId));
        translatorProfile.getLanguages().remove(language);
        translatorProfileRepository.save(translatorProfile);
    }
    @Transactional
    public User changeBalanceForTranslatorProfileId(Long id, Double balance) throws ExceptionGraphql {
        TranslatorProfile translatorProfile = translatorProfileRepository.findById(id)
                .orElseThrow(() -> new ExceptionGraphql("Translator Profile not found with id: " + id));
        User user = userRepository.findById(translatorProfile.getUser().getId())
                .orElseThrow(() -> new ExceptionGraphql("User not found with id: " + id));
        user.setBalance(balance);
        return userRepository.save(user);
    }
}