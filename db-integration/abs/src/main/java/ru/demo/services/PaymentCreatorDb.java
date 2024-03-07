package ru.demo.services;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.model.Payment;

public class PaymentCreatorDb implements PaymentCreator {
    private static final Logger logger = LoggerFactory.getLogger(PaymentCreatorDb.class);

    private final DataSource dataSource;

    public PaymentCreatorDb(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long create(Payment payment) {
        try (var connection = dataSource.getConnection();
                var pst = connection.prepareStatement("select create_payment(?, ?)")) {
            pst.setString(1, payment.cardAccount());
            pst.setBigDecimal(2, payment.amount());
            try (var rs = pst.executeQuery()) {
                rs.next();
                var paymentId = rs.getLong(1);
                logger.info("payment:{}, id:{}", payment, paymentId);
                return paymentId;
            }
        } catch (SQLException ex) {
            throw new PaymentException("payment creation error, payment:" + payment, ex);
        }
    }
}
