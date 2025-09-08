package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public int getCardBalance(String maskedCardNumber) {
        return extractBalance(findCardElement(maskedCardNumber).getText());
    }

    public void checkCardBalance(String maskedCardNumber, int expectedBalance) {
        int actualBalance = getCardBalance(maskedCardNumber);
        if (actualBalance != expectedBalance) {
            throw new AssertionError("Expected balance: " + expectedBalance +
                    ", but was: " + actualBalance);
        }
    }

    public TransferPage selectCardToTransfer(String maskedCardNumber) {
        findCardElement(maskedCardNumber).$("button").click();
        return new TransferPage();
    }

    private SelenideElement findCardElement(String maskedNumber) {
        return cards.findBy(text(maskedNumber)).shouldBe(visible);
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(value);
    }
}