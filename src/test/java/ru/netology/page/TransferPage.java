package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorMessage = $("[data-test-id=error-notification]");

    public DashboardPage makeValidTransfer(String amount, DataHelper.CardInfo fromCard) {
        makeTransfer(amount, fromCard);
        return new DashboardPage();
    }

    public void makeTransfer(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getCardNumber());
        transferButton.click();
    }

    public void checkErrorMessage(String expectedErrorText) {
        errorMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text(expectedErrorText));
    }
}