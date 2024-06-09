package core.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_chat_id")
@EqualsAndHashCode(of = {"chatId"})
public class UserChatId {

    @Id
    @Column(name = "chat_id", unique = true)
    private Long chatId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne(mappedBy = "userChatId")
    private SelectedDictionary selectedDictionary;

    @OneToMany(mappedBy = "userChatId", cascade = CascadeType.ALL)
    private Set<Search> searches;

    public UserChatId(Long chatId, Timestamp createdAt) {
        this.chatId = chatId;
        this.createdAt = createdAt;
    }
}
