package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import static com.codeborne.selenide.Selenide.open;

public class TransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 15000; // Увеличиваем таймаут
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecond() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        var maskedFirstCard = DataHelper.getMaskedNumber(firstCard.getCardNumber());
        var maskedSecondCard = DataHelper.getMaskedNumber(secondCard.getCardNumber());

        var firstCardBalance = dashboardPage.getCardBalance(maskedFirstCard);
        var secondCardBalance = dashboardPage.getCardBalance(maskedSecondCard);
        var amount = DataHelper.generateValidAmount(firstCardBalance);

        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;

        var transferPage = dashboardPage.selectCardToTransfer(maskedSecondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);

        dashboardPage.checkCardBalance(maskedFirstCard, expectedFirstCardBalance);
        dashboardPage.checkCardBalance(maskedSecondCard, expectedSecondCardBalance);
    }

    @Test
    void shouldNotTransferWhenAmountExceedsBalance() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        var maskedFirstCard = DataHelper.getMaskedNumber(firstCard.getCardNumber());
        var maskedSecondCard = DataHelper.getMaskedNumber(secondCard.getCardNumber());

        var firstCardBalance = dashboardPage.getCardBalance(maskedFirstCard);
        var secondCardBalance = dashboardPage.getCardBalance(maskedSecondCard);
        var amount = DataHelper.generateInvalidAmount(firstCardBalance);

        var transferPage = dashboardPage.selectCardToTransfer(maskedSecondCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard);

        transferPage.checkErrorMessage("Ошибка");

        dashboardPage.checkCardBalance(maskedFirstCard, firstCardBalance);
        dashboardPage.checkCardBalance(maskedSecondCard, secondCardBalance);
    }
}