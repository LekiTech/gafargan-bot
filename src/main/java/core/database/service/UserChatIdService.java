package core.database.service;

import core.database.entity.UserChatId;
import core.database.repository.UserChatIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserChatIdService {

    @Autowired
    private UserChatIdRepository userChatIdRepository;

    public void saveUser(UserChatId userChatId) {
        if (checkUser(userChatId.getChatId()).isPresent()) {
            return;
        }
        userChatIdRepository.save(userChatId);
    }

    public Optional<UserChatId> checkUser(Long chatId) {
        return userChatIdRepository.findById(chatId);
    }
}
