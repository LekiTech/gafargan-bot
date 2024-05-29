package core.database.service;

import core.database.entity.SelectedDictionary;
import core.database.repository.SelectedDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectedDictionaryService {

    @Autowired
    private SelectedDictionaryRepository selectedDictionaryRepository;

    public void saveSelectedDictionary(Long chatId, SelectedDictionary dictionary) {
        if (selectedDictionaryRepository.findSelectedDictionaryByUserChatId(chatId) != null) {
//             selectedDictionaryRepository.updateSelectedDictionaryByUserChatId(dictionary, chatId);
        }
        selectedDictionaryRepository.save(dictionary);
    }
}
