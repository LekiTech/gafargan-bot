package core.database.service;

import core.database.entity.SelectedDictionary;
import core.database.repository.SelectedDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectedDictionaryService {

    @Autowired
    private SelectedDictionaryRepository selectedDictionaryRepository;


    public void saveDictionary(SelectedDictionary dictionary) {
        if (findSelectedDictionary(dictionary.getUserChatId().getChatId()) != null) {
            selectedDictionaryRepository.updateSelectedDictionaryByUserChatId(
                    dictionary.getDictionary(), dictionary.getUserChatId().getChatId());
        }
        selectedDictionaryRepository.save(dictionary);
    }


    public String findSelectedDictionary(Long chatId) {
        return selectedDictionaryRepository.findSelectedDictionaryByUserChatId(chatId);
    }
}
