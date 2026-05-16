package prime.video.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Représente l'abonnement d'un utilisateur à un créateur.
 */
@Entity
@Table(name = "creator_subscriptions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"subscriber_id", "creator_id"}))
public class CreatorSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorProfile creator;

    @CreationTimestamp
    private OffsetDateTime subscribedAt;

    public CreatorSubscription() {}

    public CreatorSubscription(User subscriber, CreatorProfile creator) {
        this.subscriber = subscriber;
        this.creator = creator;
    }

    public UUID getId() { return id; }
    public User getSubscriber() { return subscriber; }
    public void setSubscriber(User subscriber) { this.subscriber = subscriber; }
    public CreatorProfile getCreator() { return creator; }
    public void setCreator(CreatorProfile creator) { this.creator = creator; }
    public OffsetDateTime getSubscribedAt() { return subscribedAt; }
}
