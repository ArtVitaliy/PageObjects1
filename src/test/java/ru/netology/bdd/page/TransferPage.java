package ru.netology.bdd.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.bdd.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement amountInput = $("[data-test-id='amount'] input");
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private final SelenideElement errorButton = $("[data-test-id=error-notification] button");


    public TransferPage() {
        transferHead.shouldBe(Condition.visible);
    }

    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        makeTransfer(amountToTransfer, cardInfo);
        return new DashboardPage();
    }

    public void makeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountToTransfer);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void errorLimit() {
        $("[data-test-id=error-notification]").should(Condition.exactText("Ошибка"));
    }

    public void invalidCard() {
        $("[data-test-id=error-notification]").should(Condition.text("Ошибка! "));
    }

}
