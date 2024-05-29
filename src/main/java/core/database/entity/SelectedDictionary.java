package core.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "selected_dictionary")
@EqualsAndHashCode(of = {"id"})
public class SelectedDictionary {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "dictionary")
    private String dictionary;

    @OneToOne
    @JoinColumn(name = "user_chat_id", referencedColumnName = "chat_id", unique = true)
    private UserChatId userChatId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
