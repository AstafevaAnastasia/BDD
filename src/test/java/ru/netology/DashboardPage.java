package ru.netology;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.DataHelper;

import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String cardNumber = cardInfo.getCardNumber();
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);

        for (SelenideElement card : cards) {
            if (card.getText().contains(lastFourDigits)) {
                String text = card.getText();
                return extractBalance(text);
            }
        }
        throw new RuntimeException("Карта с номером " + cardNumber + " не найдена");
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        String cardNumber = cardInfo.getCardNumber();
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);

        for (SelenideElement card : cards) {
            if (card.getText().contains(lastFourDigits)) {
                card.$("button").click();
                return new TransferPage();
            }
        }
        throw new RuntimeException("Карта с номером " + cardNumber + " не найдена");
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(value);
    }
}