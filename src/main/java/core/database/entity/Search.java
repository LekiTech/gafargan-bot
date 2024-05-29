package core.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "search")
@EqualsAndHashCode(of = {"id"})
public class Search {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_chat_id", referencedColumnName = "chat_id")
    private UserChatId userChatId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}
