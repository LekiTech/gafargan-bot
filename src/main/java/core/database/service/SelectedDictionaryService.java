package core.database.service;

import core.database.entity.SelectedDictionary;
import core.database.repository.SelectedDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelectedDictionaryService {

    @Autowired
    private SelectedDictionaryRepository selectedDictionaryRepository;

    @Transactional
    public void saveDictionary(SelectedDictionary dictionary) {
        Long chatId = dictionary.getUserChatId().getChatId();
        String existingDictionary = findSelectedDictionary(chatId);
        if (existingDictionary != null) {
            selectedDictionaryRepository.updateSelectedDictionaryByUserChatId(dictionary.getDictionary(), chatId);
        } else {
            selectedDictionaryRepository.save(dictionary);
        }
    }

    public String findSelectedDictionary(Long chatId) {
        return selectedDictionaryRepository.findSelectedDictionaryByUserChatId(chatId);
    }
}
