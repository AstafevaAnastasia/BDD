package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        return extractBalance(findCardElement(cardInfo).getText());
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        findCardElement(cardInfo).$("button").click();
        return new TransferPage();
    }

    private SelenideElement findCardElement(DataHelper.CardInfo cardInfo) {
        String maskedNumber = DataHelper.getMaskedNumber(cardInfo.getCardNumber());
        return cards.findBy(visible).shouldHave(com.codeborne.selenide.Condition.text(maskedNumber));
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(value);
    }
}