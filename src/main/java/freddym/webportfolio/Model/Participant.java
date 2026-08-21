package freddym.webportfolio.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(name= "sms_consent", nullable=false)
    private boolean smsConsent = false;

    @Column(name="sms_consent_timestamp")
    private LocalDateTime smsConsentTimestamp;

    public boolean isSmsConsent() {
        return smsConsent;
    }

    public void setSmsConsent(boolean smsConsent) {
        this.smsConsent = smsConsent;
    }

    public LocalDateTime getSmsConsentTimestamp() {
        return smsConsentTimestamp;
    }

    public void setSmsConsentTimestamp(LocalDateTime smsConsentTimestamp) {
        this.smsConsentTimestamp = smsConsentTimestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
