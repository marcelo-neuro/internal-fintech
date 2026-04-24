package com.marceloneuro.internalfintech.service.eventlistner;

import com.marceloneuro.internalfintech.dto.DepositCreatedEvent;
import com.marceloneuro.internalfintech.dto.TransferCreatedEvent;
import com.marceloneuro.internalfintech.dto.WithdrawCreatedEvent;
import com.marceloneuro.internalfintech.model.EmailDetails;
import com.marceloneuro.internalfintech.service.CustomEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class TransactionNotificationListener {

    private final CustomEmailService emailService;

    private final String TRANSFER_SEND_BODY = """
            Hello {0},
            
            We have detected a transfer of {1} from one of your wallets to {2}'s wallet.
            """;

    private final String TRANSFER_RECEIVE_BODY = """
            Hello {0},
            
            We have detected a transfer of {1} to one of your wallets from {2}'s wallets.
            """;

    private final String DEPOSIT_BODY = """
            Hello {0},
            
            We have detected a deposit of {1} to one of your wallets.
            """;

    private final String WITHDRAW_BODY = """
            Hello {0},
            
            We have detected a withdraw of {1} from one of your wallets.
            """;

    @TransactionalEventListener(value = TransferCreatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendTransferNotificationEmail(TransferCreatedEvent event) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

        String senderMessageBody = MessageFormat.format(
                TRANSFER_SEND_BODY,
                event.senderFullName(),
                numberFormat.format(event.amount()),
                event.receiverFullName()
        );
        EmailDetails senderEmail = new EmailDetails();
        senderEmail.setRecipient(event.senderEmail());
        senderEmail.setSubject("Transfer Detected");
        senderEmail.setMessageBody(senderMessageBody);

        String receiverMessageBody = MessageFormat.format(
                TRANSFER_RECEIVE_BODY,
                event.receiverFullName(),
                numberFormat.format(event.amount()),
                event.senderFullName()
        );
        EmailDetails receiverEmail = new EmailDetails();
        receiverEmail.setRecipient(event.receiverEmail());
        receiverEmail.setSubject("Transfer Detected");
        receiverEmail.setMessageBody(receiverMessageBody);

        emailService.sendSimpleMail(senderEmail);
        emailService.sendSimpleMail(receiverEmail);
    }

    @TransactionalEventListener(value = DepositCreatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendDepositNotificationEmail(DepositCreatedEvent event) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

        String messageBody = MessageFormat.format(
                DEPOSIT_BODY,
                event.receiverFullName(),
                numberFormat.format(event.amount())
        );
        EmailDetails email = new EmailDetails();
        email.setRecipient(event.receiverEmail());
        email.setSubject("Deposit Detected");
        email.setMessageBody(messageBody);

        emailService.sendSimpleMail(email);
    }

    @TransactionalEventListener(value = WithdrawCreatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendWithdrawNotificationEmail(WithdrawCreatedEvent event) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
        
        String messageBody = MessageFormat.format(
                WITHDRAW_BODY,
                event.senderFullName(),
                numberFormat.format(event.amount())
        );
        EmailDetails email = new EmailDetails();
        email.setRecipient(event.senderEmail());
        email.setSubject("Withdraw Detected");
        email.setMessageBody(messageBody);

        emailService.sendSimpleMail(email);
    }
}
